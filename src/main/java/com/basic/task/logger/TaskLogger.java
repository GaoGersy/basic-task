package com.basic.task.logger;

/**
 * 任务执行日志类
 *
 * @author Gersy
 * @date 2020/8/4
 */
public interface TaskLogger {
    /**
     * 调试日志
     *
     * @param msg 打印的对象
     */
    void d(String jobKey, String msg);

    /**
     * 信息日志
     *
     * @param msg 打印的对象
     */
    void i(String jobKey, String msg);

    /**
     * 警告日志
     *
     * @param msg 打印的对象
     */
    void w(String jobKey, String msg);

    /**
     * 错误日志
     *
     * @param msg 打印的对象
     */
    void e(String jobKey, String msg);
}
