package com.example.demo.consumer;

import com.example.demo.response.KafkaContoDTO;
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

    @KafkaListener(topics = "hello", groupId = "academy2024")
    public void consumeJsonMessage(KafkaContoDTO contoDTO) {
        log.info(String.format("messaggio arrivato a destinazione dal topic hello: %s", contoDTO.toString()));
        fileService.writeToFile("src/main/resources/conti.txt", contoDTO.toString());
    }

}
