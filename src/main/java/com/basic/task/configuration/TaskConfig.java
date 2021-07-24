package com.basic.task.configuration;

import com.basic.task.annotation.TaskScan;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gersy
 * @date 2020/6/5
 */
@Configuration
@TaskScan()
@EnableConfigurationProperties(TaskProperties.class)
public class TaskConfig {
}
