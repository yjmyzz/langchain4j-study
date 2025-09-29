package com.cnblogs.yjmyzz.longchain4j.study.controller;

import com.cnblogs.tools.OrderTools;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 订单控制器
 * 用于测试Ollama调用工具功能
 *
 * @author 菩提树下的杨过
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    @Qualifier("ollamaChatModel")
    private ChatModel ollamaChatModel;

    @Autowired
    @Qualifier("deepSeekChatModel")
    private ChatModel deepSeekChatModel;

    @Autowired
    private OrderTools orderTools;

    /**
     * 直接调用OrderTools的getOrderStatus方法
     *
     * @param orderId 订单ID
     * @return 订单状态
     */
    @GetMapping(value = "/status/direct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderStatusDirect(@RequestParam String orderId) {
        log.info("直接调用OrderTools.getOrderStatus，订单ID: {}", orderId);

        try {
            String status = orderTools.getOrderStatus(orderId);
            log.info("订单状态查询结果: {}", status);
            return ResponseEntity.ok("{\"orderId\":\"" + orderId + "\",\"status\":\"" + status + "\"}");
        } catch (Exception e) {
            log.error("查询订单状态时发生错误", e);
            return ResponseEntity.ok("{\"error\":\"查询订单状态失败: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 通过Ollama模型调用OrderTools工具
     *
     * @param orderId 订单ID
     * @return AI响应结果
     */
    @GetMapping(value = "/status/ollama", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getOrderStatusWithOllama(@RequestParam String orderId) {
        log.info("通过AI调用OrderTools.getOrderStatus，订单ID: {}", orderId);

        try {
            // 先直接调用工具获取结果
            String toolResult = orderTools.getOrderStatus(orderId);
            log.info("工具调用结果: {}", toolResult);

            // 构建消息，让AI分析工具结果
            String prompt = "请分析以下订单状态信息并给出用户友好的回复：\n" + toolResult;
            UserMessage userMessage = UserMessage.from(prompt);

            // 创建包含工具的聊天消息列表
            List<ChatMessage> messages = Arrays.asList(userMessage);

            // 调用模型
            ChatResponse response = ollamaChatModel.chat(messages);

            // 获取响应内容
            String aiAnalysis = response.toString();
            log.info("AI分析结果: {}", aiAnalysis);

            // 组合工具结果和AI分析
            String result = "工具调用结果: " + toolResult + "\n\nAI分析: " + aiAnalysis;

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("通过AI调用工具时发生错误", e);
            return ResponseEntity.ok("通过AI调用工具失败: " + e.getMessage());
        }
    }
    /**
 * 通过DeepSeek模型查询订单状态。
 * 该方法会构造一个包含订单状态查询意图的用户消息，并利用工具调用机制让AI决定是否需要调用订单状态查询工具。
 * 如果AI请求调用工具，则执行工具并再次与AI交互以生成最终响应。
 *
 * @param orderId 订单ID，用于查询对应订单的状态
 * @return ResponseEntity<String> 返回订单状态的文本描述或错误信息
 */
@GetMapping(value = "/status/deepseek", produces = MediaType.TEXT_PLAIN_VALUE)
public ResponseEntity<String> getOrderStatusWithDeepseek(@RequestParam String orderId) {

    try {
        // 注册可用的工具规范，供AI在对话中调用
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(OrderTools.class);

        // 构造用户提问消息
        UserMessage userMessage = UserMessage.from("订单" + orderId + "的状态是什么，请用友好的方式回答");

        // 构建第一次请求，包含用户消息和工具定义
        ChatRequest request = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolSpecifications)
                .build();

        // 第一次调用LLM，获取AI响应
        ChatResponse response = deepSeekChatModel.chat(request);
        AiMessage aiMessage = response.aiMessage();

        // 检查AI是否希望调用工具，并确认是调用订单状态查询工具
        if (aiMessage.hasToolExecutionRequests() && "getOrderStatus".equalsIgnoreCase(aiMessage.toolExecutionRequests().getFirst().name())) {
            // 执行订单状态查询工具
            String toolResult = orderTools.getOrderStatus(orderId);
            // 构造工具执行结果消息
            ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(aiMessage.toolExecutionRequests().getFirst(), toolResult);

            // 构建第二次请求，将用户消息、AI原始响应和工具执行结果一并发送给AI进行总结
            ChatRequest request2 = ChatRequest.builder()
                    .messages(List.of(userMessage, aiMessage, toolExecutionResultMessage))
                    .toolSpecifications(toolSpecifications)
                    .build();

            // 第二次调用LLM，生成最终自然语言响应
            ChatResponse response2 = deepSeekChatModel.chat(request2);
            return ResponseEntity.ok(response2.aiMessage().text());
        } else {
            log.warn("AI没有请求调用任何工具，响应内容: {}", aiMessage);
            return ResponseEntity.ok("AI没有请求调用任何工具，响应内容: " + aiMessage);
        }
    } catch (Exception e) {
        log.error("通过AI调用工具时发生错误", e);
        return ResponseEntity.ok("通过AI调用工具失败: " + e.getMessage());
    }
}



    /**
     * 健康检查端点
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OrderController服务运行正常");
    }
}