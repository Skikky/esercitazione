package com.example.demo.configurations;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {
    @Bean
    public NewTopic helloTopic() {
        return TopicBuilder
                .name("resoconto")
                .build();
    }
}
