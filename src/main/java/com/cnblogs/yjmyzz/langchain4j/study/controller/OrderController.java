package com.cnblogs.yjmyzz.langchain4j.study.controller;

import com.cnblogs.yjmyzz.langchain4j.study.tools.OrderTools;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    @Qualifier("ollamaChatModel")
    private ChatModel ollamaChatModel;


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
            String prompt = "请分析以下订单状态信息并给出用户友好的回复(不要做过多额外解释)：\n" + toolResult;
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
     * 健康检查端点
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OrderController服务运行正常");
    }
}