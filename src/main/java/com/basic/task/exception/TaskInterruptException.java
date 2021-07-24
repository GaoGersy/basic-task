package com.basic.task.exception;

public class TaskInterruptException extends RuntimeException {
    public TaskInterruptException() {
        super("任务中断");
    }
}
