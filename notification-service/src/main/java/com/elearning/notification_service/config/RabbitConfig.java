package com.elearning.notification_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String NEW_MODULE_QUEUE = "new-module";

    @Bean
    public Queue newModuleQueue() {
        return new Queue(NEW_MODULE_QUEUE, true);
    }
}
