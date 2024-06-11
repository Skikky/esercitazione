package com.example.demo.producer;

import com.example.demo.entities.Conto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonProducer {

    @Autowired
    private KafkaTemplate<String, Conto> kafkaTemplate;

    public void sendMessage(Conto conto) {
        Message<Conto> message = MessageBuilder
                .withPayload(conto)
                .setHeader(KafkaHeaders.TOPIC, "resoconto")
                .build();
        kafkaTemplate.send(message);
    }

}
