package com.adasoft.server_one.service;

import com.adasoft.server_one.model.entity.FileChangeEvent;

public interface FileChangeEventService {
    void processFileChangeEvent(FileChangeEvent fileChangeEvent);
}

