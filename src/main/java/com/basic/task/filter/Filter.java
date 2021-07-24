package com.basic.task.filter;

/**
 * 扫描过滤器
 * 考虑到文件扫描时会出现过滤掉某种文件或只接收某类型文件，所以过滤和接受两种方式
 * 默认以{$accept()}方法为优先判断，如果返回accept返回false再判断filter，如果filter返回true才会真正过滤掉
 * 这样可以处理比较复杂的业务逻辑【"过滤某些类型文件","接受某些类型文件"】
 *
 * @author Gersy
 */
public interface Filter<T> {
    /**
     * 如果没有接收的条件，默认返回false,交由filter去判断过滤
     *
     * @return 接受就返回true
     */
    boolean accept(T t);
}
