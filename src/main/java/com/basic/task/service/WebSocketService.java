package com.basic.task.service;

import com.basic.task.model.JobStatus;

public interface WebSocketService {
    void task(Object obj);

    void jobStatus(JobStatus jobStatus);
}
