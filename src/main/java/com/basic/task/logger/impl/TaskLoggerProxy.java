package com.basic.task.logger.impl;

import com.basic.task.logger.LoggerLevel;
import com.basic.task.logger.TaskLogger;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public class TaskLoggerProxy implements TaskLogger {
    protected int level = LoggerLevel.DEBUG.getLevel();
    private TaskLogger realTaskLogger;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TaskLoggerProxy() {
    }

    public TaskLoggerProxy(TaskLogger realTaskLogger) {
        this.realTaskLogger = realTaskLogger;
        if (realTaskLogger == null) {
            throw new RuntimeException("realLogger不能为null");
        }
    }

    public void setRealTaskLogger(TaskLogger realTaskLogger) {
        this.realTaskLogger = realTaskLogger;
    }

    @Override
    public void d(String jobKey, String obj) {
        if (level == LoggerLevel.DEBUG.getLevel()) {
            realTaskLogger.d(jobKey, obj);
        }
    }

    @Override
    public void i(String jobKey, String obj) {
        if (level <= LoggerLevel.INFO.getLevel()) {
            realTaskLogger.i(jobKey, obj);
        }
    }

    @Override
    public void w(String jobKey, String obj) {
        if (level <= LoggerLevel.WARN.getLevel()) {
            realTaskLogger.w(jobKey, obj);
        }
    }

    @Override
    public void e(String jobKey, String obj) {
        realTaskLogger.e(jobKey, obj);
    }
}
