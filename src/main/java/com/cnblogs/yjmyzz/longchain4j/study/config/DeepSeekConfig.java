package com.cnblogs.yjmyzz.longchain4j.study.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * DeepSeek配置类
 * 用于配置LongChain4j与DeepSeek大模型的连接
 *
 * @author 菩提树下的杨过
 * @version 1.0.0
 */
@Configuration
public class DeepSeekConfig {

    @Value("${deepseek.api-key:your-deepseek-api-key-here}")
    private String apiKey;

    @Value("${deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    @Value("${deepseek.timeout:60}")
    private Integer timeoutSeconds;

    @Value("${deepseek.temperature:0.7}")
    private Double temperature;

    @Value("${deepseek.max-tokens:2048}")
    private Integer maxTokens;

    /**
     * 配置DeepSeek聊天模型
     *
     * @return ChatModel实例
     */
    @Bean("deepSeekChatModel")
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * 配置DeepSeek流式聊天模型
     *
     * @return StreamingChatModel实例
     */
    @Bean("deepSeekStreamingChatModel")
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}