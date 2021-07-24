package com.basic.task.task.impl.base;

import com.basic.task.model.TaskInfo;

/**
 * 一种能将集合铺平然后挨个发射给后面的任务的Task
 *
 * @author Gersy
 * @date 2020/5/21
 */
public abstract class BaseFlatTask<T> extends BaseTask<T> {
    /**
     * 用于统计扫描到的文件个数
     */
    private int count = 0;

    public BaseFlatTask(TaskInfo taskInfo) {
        super(taskInfo);
        isFlat = true;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        this.count++;
    }
}
