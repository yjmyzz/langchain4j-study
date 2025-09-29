package com.cnblogs.yjmyzz.longchain4j.study.controller;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/mcp")
@CrossOrigin(origins = "*")
public class McpController {

    private interface Assistant {
        String chat(String userMessage);
    }

    /**
     * 初始化SSE客户端
     *
     * @param sseUrl SSE服务器连接地址
     * @return McpClient实例
     */
    private static McpClient initSseClient(String sseUrl) {
        // 构建默认MCP客户端
        return new DefaultMcpClient.Builder()
                .clientName("yjmyzz.cnblogs.com")
                .protocolVersion("2024-11-05")
                .toolExecutionTimeout(Duration.ofSeconds(10))
                // 配置HTTP传输层参数
                .transport(new HttpMcpTransport.Builder()
                        // 设置SSE服务器连接URL
                        .sseUrl(sseUrl)
                        // 设置连接超时时间
                        .timeout(Duration.ofSeconds(10))
                        // 启用请求日志记录
                        .logRequests(true)
                        // 启用响应日志记录
                        .logResponses(true)
                        .build())
                .build();
    }


    @Autowired
    @Qualifier("deepSeekChatModel")
    private ChatModel deepSeekChatModel;

    /**
     * 直接获取订单状态信息
     *
     * @param orderId 订单ID，用于查询指定订单的状态
     * @return ResponseEntity<String> 包含订单状态信息的响应实体，成功时返回订单状态JSON字符串，
     * 失败时返回包含错误信息的JSON字符串
     */
    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderStatusDirect(@RequestParam String orderId) {
        McpClient mcpClient = null;
        try {
            // 初始化SSE客户端连接
            mcpClient = initSseClient("http://localhost:8070/sse");

            // 构建AI助手服务，配置聊天模型和工具提供者
            Assistant assistant = AiServices.builder(Assistant.class)
                    .chatModel(deepSeekChatModel)
                    .toolProvider(McpToolProvider.builder().mcpClients(initSseClient("http://localhost:8070/sse")).build())
                    .build();

            // 调用AI助手查询订单状态
            String response = assistant.chat("查询订单状态，订单号：" + orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询订单状态时发生错误", e);
            return ResponseEntity.ok("{\"error\":\"查询订单状态失败: " + e.getMessage() + "\"}");
        } finally {
            // 确保MCP客户端连接被正确关闭
            if (mcpClient != null) {
                try {
                    mcpClient.close();
                } catch (Exception e) {
                    log.error("关闭MCP客户端时发生错误", e);
                }
            }
        }
    }

}
