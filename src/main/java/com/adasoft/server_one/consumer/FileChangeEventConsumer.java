package com.adasoft.server_one.consumer;

import com.adasoft.server_one.model.entity.FileChangeEvent;
import com.adasoft.server_one.service.FileChangeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FileChangeEventConsumer {

    @Autowired
    private FileChangeEventService fileChangeEventService;

    @KafkaListener(topics = "${server_one.kafka.topic}")
    public void consumeFileChangeEvent(FileChangeEvent fileChangeEvent) {
        // Process the FileChangeEvent
        fileChangeEventService.processFileChangeEvent(fileChangeEvent);
    }
}

