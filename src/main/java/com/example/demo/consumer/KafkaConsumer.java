package com.example.demo.consumer;

import com.example.demo.entities.Conto;
import com.example.demo.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @Autowired
    private FileService fileService;

    @KafkaListener(topics = "resoconto", groupId = "BuonApetit")
    public void consumeJsonMessage(Conto conto) {
        log.info(String.format("messaggio arrivato a destinazione dal topic resoconto: %s", conto.toString()));
        fileService.writeToFile("src/main/resources/conti.txt", conto.toString());
    }
}
