package com.adasoft.server_one.producer;

import com.adasoft.commons.model.EventType;
import com.adasoft.server_one.model.entity.FileChangeEvent;
import com.adasoft.server_one.service.FileChangeEventService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class FileChangeEventProducer {

    @Autowired
    private KafkaTemplate<String, FileChangeEvent> kafkaTemplate;

    @Value("${server_one.folder.path}")
    private String folderPath;

    @Autowired
    private FileChangeEventService fileChangeEventService;

    public void sendFileChangeEvent(FileChangeEvent fileChangeEvent) {
        kafkaTemplate.send("file-change-topic", fileChangeEvent);
    }

    @PostConstruct
    public void startMonitoringFolder() {
        try {
            // Create a WatchService for monitoring file changes
            WatchService watchService = FileSystems.getDefault().newWatchService();

            // Register the folder_a directory for file change events
            Path directory = Paths.get(folderPath);
            directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            // Start a separate thread to handle file change events
            Thread watchThread = new Thread(() -> {
                try {
                    while (true) {
                        WatchKey key = watchService.take();

                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                                // Handle overflow event if needed
                                continue;
                            }

                            // Get the filename and event type
                            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                            Path filePath = directory.resolve(pathEvent.context());
                            String filename = filePath.toString();
                            EventType eventType = mapWatchEventToEventType(event.kind());

                            // Check if the file is within the monitored folder
                            if (filePath.startsWith(Paths.get(folderPath))) {
                                // Create a FileChangeEvent object
                                FileChangeEvent fileChangeEvent = new FileChangeEvent();
                                fileChangeEvent.setFilename(filename);
                                fileChangeEvent.setEventType(eventType);

                                // Send the FileChangeEvent to Kafka
                                kafkaTemplate.send("file-change-topic", fileChangeEvent);

                                // Process the FileChangeEvent
                                fileChangeEventService.processFileChangeEvent(fileChangeEvent);
                            }
                        }

                        key.reset();
                    }
                } catch (InterruptedException e) {
                    // Handle the interruption if needed
                } finally {
                    // Close the WatchService
                    try {
                        watchService.close();
                    } catch (IOException e) {
                        // Handle the IOException if needed
                    }
                }
            });

            // Start the watch thread
            watchThread.start();
        } catch (IOException e) {
            // Handle the IOException if needed
        }
    }

    private EventType mapWatchEventToEventType(WatchEvent.Kind<?> eventKind) {
        if (eventKind == StandardWatchEventKinds.ENTRY_CREATE) {
            return EventType.CREATED;
        } else if (eventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return EventType.UPDATED;
        } else if (eventKind == StandardWatchEventKinds.ENTRY_DELETE) {
            return EventType.DELETED;
        } else {
            // Handle unknown event types if needed
            return null;
        }
    }
}
