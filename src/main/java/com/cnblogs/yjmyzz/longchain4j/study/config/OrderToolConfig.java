package com.cnblogs.yjmyzz.longchain4j.study.config;

import com.cnblogs.tools.OrderTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderToolConfig {

    @Bean
    public OrderTools orderTools() {
        return new OrderTools();
    }
}
