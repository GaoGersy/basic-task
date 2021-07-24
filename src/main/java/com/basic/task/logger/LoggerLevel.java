package com.basic.task.logger;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public enum LoggerLevel {
    DEBUG(1, "调试"),
    INFO(2, "信息"),
    WARN(3, "警告"),
    ERROR(4, "错误");
    private int level;
    private String name;

    LoggerLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static int getLevel(String name) {
        LoggerLevel[] values = LoggerLevel.values();
        for (LoggerLevel value : values) {
            if (value.name.equals(name)) {
                return value.level;
            }
        }
        return -1;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
