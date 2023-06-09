package com.adasoft.server_one.consumer;

import com.adasoft.server_one.model.entity.FileChangeEvent;
import com.adasoft.server_one.service.FileChangeEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class FileChangeEventConsumer {

    @Autowired
    private FileChangeEventService fileChangeEventService;

    @KafkaListener(topics = "${server_one.kafka.topic}", groupId = "${server_one.kafka.groupId}")
    public void consumeFileChangeEvent(@Payload String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileChangeEvent fileChangeEvent = objectMapper.readValue(payload, FileChangeEvent.class);
            System.out.println("Received file change event: " + fileChangeEvent);
            fileChangeEventService.processFileChangeEvent(fileChangeEvent);
        } catch (JsonProcessingException e) {
            // Handle the exception if needed
            e.printStackTrace();
        }
    }
}


