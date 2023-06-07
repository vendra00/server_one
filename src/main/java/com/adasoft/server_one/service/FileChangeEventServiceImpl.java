package com.adasoft.server_one.service;

import com.adasoft.commons.model.EventType;
import com.adasoft.server_one.model.entity.FileChangeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileChangeEventServiceImpl implements FileChangeEventService {

    @Value("${server_one.folder.path}")
    private String folderPath;

    @Override
    public void processFileChangeEvent(FileChangeEvent fileChangeEvent) {
        // Perform actions based on the event type
        EventType eventType = fileChangeEvent.getEventType();
        if (eventType == EventType.CREATED) {
            // Handle file creation event
            handleFileCreation(fileChangeEvent);
        } else if (eventType == EventType.UPDATED) {
            // Handle file update event
            handleFileUpdate(fileChangeEvent);
        } else if (eventType == EventType.DELETED) {
            // Handle file deletion event
            handleFileDeletion(fileChangeEvent);
        }
    }


    private void handleFileCreation(FileChangeEvent fileChangeEvent) {
        String filePath = folderPath + File.separator + fileChangeEvent.getFilename();
        // Implement the logic to handle file creation event using the file path
        System.out.println("File created: " + filePath);
    }

    private void handleFileUpdate(FileChangeEvent fileChangeEvent) {
        String filePath = folderPath + File.separator + fileChangeEvent.getFilename();
        // Implement the logic to handle file update event using the file path
        System.out.println("File updated: " + filePath);
    }

    private void handleFileDeletion(FileChangeEvent fileChangeEvent) {
        String filePath = folderPath + File.separator + fileChangeEvent.getFilename();
        // Implement the logic to handle file deletion event using the file path
        System.out.println("File deleted: " + filePath);
    }
}

