package com.basic.task.logger.impl;

import com.basic.common.utils.SuperLogger;
import com.basic.task.logger.TaskLogger;

/**
 * @author Gersy
 * @date 2020/8/4
 */
public class PrintTaskLogger implements TaskLogger {

    @Override
    public void d(String jobKey,String obj) {
        SuperLogger.e(jobKey+"："+obj);
    }

    @Override
    public void i(String jobKey,String obj) {
        SuperLogger.e(jobKey+"："+obj);
    }

    @Override
    public void w(String jobKey,String obj) {
        SuperLogger.warn(jobKey+"："+obj);
    }

    @Override
    public void e(String jobKey,String obj) {
        SuperLogger.e(jobKey+"："+obj);
    }
}
