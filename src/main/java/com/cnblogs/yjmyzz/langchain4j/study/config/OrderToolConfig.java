package com.cnblogs.yjmyzz.langchain4j.study.config;

import com.cnblogs.yjmyzz.langchain4j.study.tools.OrderTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderToolConfig {

    @Bean
    public OrderTools orderTools() {
        return new OrderTools();
    }
}