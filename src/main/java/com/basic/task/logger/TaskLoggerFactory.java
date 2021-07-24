package com.basic.task.logger;

import com.basic.common.auto.ApplicationContextRegister;
import com.basic.task.config.TaskLoggerFactoryBean;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public class TaskLoggerFactory {

    private static TaskLogger taskLogger;

    public static TaskLogger getTaskLogger() {
        if (taskLogger == null) {
            synchronized (TaskLoggerFactory.class) {
                if (taskLogger == null) {
                    try {
                        TaskLoggerFactoryBean bean = ApplicationContextRegister.getBean(TaskLoggerFactoryBean.class);
                        taskLogger = bean.getLogger();
                    } catch (Exception e) {
                        taskLogger = new TaskLoggerFactoryBean().getLogger();
                    }
                }
            }
        }
        return taskLogger;
    }
}
