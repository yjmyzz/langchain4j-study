package com.cnblogs.yjmyzz.langchain4j.study.controller;

import com.cnblogs.yjmyzz.langchain4j.study.service.DynamicConfigService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 聊天控制器
 * 提供与DeepSeek大模型交互的REST API
 * 支持多租户动态API密钥配置
 *
 * @author 菩提树下的杨过
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private DynamicConfigService dynamicConfigService;

    /**
     * 发送聊天消息（GET方式）
     *
     * @param prompt 用户输入的消息
     * @param apiKey API密钥（可选，如果不提供则使用默认配置）
     * @return 聊天响应
     */
    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat(@RequestParam String prompt, 
                                     @RequestParam(required = false) String apiKey) {
        log.info("收到聊天请求: {}", prompt);

        try {
            // 验证API密钥
            if (apiKey != null && !dynamicConfigService.isValidApiKey(apiKey)) {
                return ResponseEntity.badRequest()
                    .body("API密钥格式无效，请检查密钥是否正确");
            }

            // 获取聊天模型
            ChatModel chatModel = (apiKey != null) ? 
                dynamicConfigService.getChatModel(apiKey) : 
                getDefaultChatModel();

            String aiResponse = chatModel.chat(prompt);
            return ResponseEntity.ok(aiResponse);

        } catch (IllegalArgumentException e) {
            log.error("API密钥验证失败", e);
            return ResponseEntity.badRequest().body("API密钥验证失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("与DeepSeek通信时发生错误", e);
            String errorResponse = "抱歉，处理您的请求时发生了错误: " + e.getMessage();
            return ResponseEntity.ok(errorResponse);
        }
    }

    /**
     * 流式聊天消息（GET方式）
     *
     * @param prompt 用户输入的消息
     * @param apiKey API密钥（可选，如果不提供则使用默认配置）
     * @return 流式聊天响应
     */
    @GetMapping(value = "/chat/stream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(@RequestParam String prompt, 
                                  @RequestParam(required = false) String apiKey) {
        log.info("收到流式聊天请求: {}", prompt);

        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        try {
            // 验证API密钥
            if (apiKey != null && !dynamicConfigService.isValidApiKey(apiKey)) {
                sink.tryEmitNext(escapeToHtml("API密钥格式无效，请检查密钥是否正确"));
                sink.tryEmitComplete();
                return sink.asFlux();
            }

            // 获取流式聊天模型
            StreamingChatModel streamingChatModel = (apiKey != null) ? 
                dynamicConfigService.getStreamingChatModel(apiKey) : 
                getDefaultStreamingChatModel();

            streamingChatModel.chat(prompt, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String s) {
                    log.info("收到部分响应: {}",s);
                    // 发送SSE格式的数据
                    sink.tryEmitNext(escapeToHtml(s));
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    log.info("流式响应完成");
                    sink.tryEmitComplete();
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("流式响应发生错误", throwable);
                    sink.tryEmitError(throwable);
                }
            });

        } catch (Exception e) {
            log.error("流式聊天初始化失败", e);
            sink.tryEmitNext(escapeToHtml("抱歉，初始化聊天时发生错误: " + e.getMessage()));
            sink.tryEmitComplete();
        }

        return sink.asFlux();
    }


    /**
     * 健康检查端点
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("langchain4j Study服务运行正常");
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计
     */
    @GetMapping("/cache/stats")
    public ResponseEntity<String> getCacheStats() {
        return ResponseEntity.ok(dynamicConfigService.getCacheStats());
    }

    /**
     * 清除指定API密钥的缓存
     *
     * @param apiKey API密钥
     * @return 操作结果
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<String> clearCache(@RequestParam String apiKey) {
        try {
            dynamicConfigService.clearCache(apiKey);
            return ResponseEntity.ok("缓存清除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("缓存清除失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有缓存
     *
     * @return 操作结果
     */
    @PostMapping("/cache/clear-all")
    public ResponseEntity<String> clearAllCache() {
        try {
            dynamicConfigService.clearAllCache();
            return ResponseEntity.ok("所有缓存清除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("缓存清除失败: " + e.getMessage());
        }
    }

    /**
     * 验证API密钥
     *
     * @param apiKey API密钥
     * @return 验证结果
     */
    @GetMapping("/validate-api-key")
    public ResponseEntity<String> validateApiKey(@RequestParam String apiKey) {
        boolean isValid = dynamicConfigService.isValidApiKey(apiKey);
        if (isValid) {
            return ResponseEntity.ok("API密钥格式有效");
        } else {
            return ResponseEntity.badRequest().body("API密钥格式无效");
        }
    }


    /**
     * 获取默认聊天模型（用于向后兼容）
     */
    private ChatModel getDefaultChatModel() {
        // 这里可以返回一个默认的模型实例，或者抛出异常提示需要提供API密钥
        throw new IllegalArgumentException("请提供有效的API密钥");
    }

    /**
     * 获取默认流式聊天模型（用于向后兼容）
     */
    private StreamingChatModel getDefaultStreamingChatModel() {
        // 这里可以返回一个默认的模型实例，或者抛出异常提示需要提供API密钥
        throw new IllegalArgumentException("请提供有效的API密钥");
    }

    public String escapeToHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\n", "<br/>")
                .replace("<think>", "&lt;think&gt;")
                .replace("</think>", "&lt;/think&gt;");
    }
}