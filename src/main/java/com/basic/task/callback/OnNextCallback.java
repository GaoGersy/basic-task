package com.basic.task.callback;

/**
 * 当FlatTask任务分解时，将元素逐个发给后续任务时通过此回调传递数据
 */
public interface OnNextCallback<T> {
    void onNext(T t);
}
