package com.basic.task.config;

import com.basic.task.logger.LoggerLevel;
import com.basic.task.logger.TaskLogger;
import com.basic.task.logger.impl.TaskLoggerProxy;
import com.basic.task.logger.impl.WebSocketTaskLogger;

import java.util.Map;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public class TaskLoggerFactoryBean {
    private TaskLogger taskLogger;
    private LoggerLevel loggerLevel;
    private Map<String, TaskLogger> map;
    private final TaskLoggerProxy taskLoggerProxy;

    public TaskLoggerFactoryBean() {
        taskLogger=new WebSocketTaskLogger();
        taskLoggerProxy = new TaskLoggerProxy(taskLogger);
    }

    public TaskLogger getLogger() {
        return taskLoggerProxy;
    }

    public void setTaskLogger(TaskLogger taskLogger) {
        this.taskLogger = taskLogger;
        taskLoggerProxy.setRealTaskLogger(taskLogger);
    }

    public LoggerLevel getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(LoggerLevel loggerLevel) {
        this.loggerLevel = loggerLevel;
        taskLoggerProxy.setLevel(loggerLevel.getLevel());
    }
}
