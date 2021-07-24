package com.basic.task.logger.impl;

import com.basic.common.auto.ApplicationContextRegister;
import com.basic.task.logger.TaskLogger;
import com.basic.task.model.TaskResult;
import com.basic.task.service.WebSocketService;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public class WebSocketTaskLogger implements TaskLogger {

    private final WebSocketService webSocketService;

    public WebSocketTaskLogger() {
        webSocketService = ApplicationContextRegister.getBean(WebSocketService.class);
    }

    @Override
    public void d(String jobKey, String obj) {
        TaskResult<Object> success = TaskResult.success(obj, null);
        success.setJobKey(jobKey);
        webSocketService.task(success);
    }

    @Override
    public void i(String jobKey, String obj) {
        TaskResult<Object> info = TaskResult.info(obj, null);
        info.setJobKey(jobKey);
        webSocketService.task(info);
    }

    @Override
    public void w(String jobKey, String obj) {
        TaskResult<Object> warn = TaskResult.warn(obj, null);
        warn.setJobKey(jobKey);
        webSocketService.task(warn);
    }

    @Override
    public void e(String jobKey, String obj) {
        TaskResult<Object> error = TaskResult.error(obj);
        error.setJobKey(jobKey);
        webSocketService.task(error);
    }
}
