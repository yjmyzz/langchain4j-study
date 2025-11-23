package com.cnblogs.yjmyzz.langchain4j.study.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态配置服务
 * 支持运行时动态创建和管理多个API密钥的模型实例
 * 为多租户系统提供基础支持
 *
 * @author 菩提树下的杨过
 * @version 1.0.0
 */
@Service
public class DynamicConfigService {

    private static final Logger log = LoggerFactory.getLogger(DynamicConfigService.class);

    private static final String DEFAULT_BASE_URL = "https://api.deepseek.com";
    private static final String DEFAULT_MODEL = "deepseek-chat";
    private static final Integer DEFAULT_TIMEOUT = 60;
    private static final Double DEFAULT_TEMPERATURE = 0.7;
    private static final Integer DEFAULT_MAX_TOKENS = 2048;

    // 缓存不同API密钥对应的模型实例
    private final ConcurrentHashMap<String, ChatModel> chatModelCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, StreamingChatModel> streamingChatModelCache = new ConcurrentHashMap<>();

    /**
     * 获取或创建聊天模型实例
     *
     * @param apiKey API密钥
     * @return ChatModel实例
     */
    public ChatModel getChatModel(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API密钥不能为空");
        }

        return chatModelCache.computeIfAbsent(apiKey, key -> {
            log.info("为API密钥创建新的聊天模型实例: {}", maskApiKey(key));
            return createChatModel(key);
        });
    }

    /**
     * 获取或创建流式聊天模型实例
     *
     * @param apiKey API密钥
     * @return StreamingChatModel实例
     */
    public StreamingChatModel getStreamingChatModel(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API密钥不能为空");
        }

        return streamingChatModelCache.computeIfAbsent(apiKey, key -> {
            log.info("为API密钥创建新的流式聊天模型实例: {}", maskApiKey(key));
            return createStreamingChatModel(key);
        });
    }

    /**
     * 创建聊天模型实例
     */
    private ChatModel createChatModel(String apiKey) {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(DEFAULT_BASE_URL)
                .modelName(DEFAULT_MODEL)
                .temperature(DEFAULT_TEMPERATURE)
                .maxTokens(DEFAULT_MAX_TOKENS)
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * 创建流式聊天模型实例
     */
    private StreamingChatModel createStreamingChatModel(String apiKey) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(DEFAULT_BASE_URL)
                .modelName(DEFAULT_MODEL)
                .temperature(DEFAULT_TEMPERATURE)
                .maxTokens(DEFAULT_MAX_TOKENS)
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * 验证API密钥格式
     *
     * @param apiKey API密钥
     * @return 是否有效
     */
    public boolean isValidApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return false;
        }
        // 简单的格式验证，可以根据实际需要扩展
        return apiKey.length() >= 20 && apiKey.startsWith("sk-");
    }

    /**
     * 清除指定API密钥的缓存
     *
     * @param apiKey API密钥
     */
    public void clearCache(String apiKey) {
        if (apiKey != null) {
            chatModelCache.remove(apiKey);
            streamingChatModelCache.remove(apiKey);
            log.info("已清除API密钥的缓存: {}", maskApiKey(apiKey));
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        chatModelCache.clear();
        streamingChatModelCache.clear();
        log.info("已清除所有模型缓存");
    }

    /**
     * 获取缓存统计信息
     */
    public String getCacheStats() {
        return String.format("聊天模型缓存: %d, 流式模型缓存: %d", 
                chatModelCache.size(), streamingChatModelCache.size());
    }

    /**
     * 掩码API密钥用于日志记录
     */
    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() <= 8) {
            return "****";
        }
        return apiKey.substring(0, 4) + "****" + apiKey.substring(apiKey.length() - 4);
    }
}
