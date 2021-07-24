package com.basic.task.service.impl;


import com.basic.common.utils.SuperLogger;
import com.basic.task.logger.TaskLogger;
import com.basic.task.model.JobStatus;
import com.basic.task.service.WebSocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private TaskLogger taskLogger;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    void init(){
//        taskLogger = TaskLoggerFactory.getTaskLogger();
    }

    @Override
    public void task(Object obj) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/job/task", obj);
        } else {
            SuperLogger.e(obj);
        }
    }

    @Override
    public void jobStatus(JobStatus jobStatus){
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/job/status", jobStatus);
        } else {
            SuperLogger.e(jobStatus);
        }
    }

}
