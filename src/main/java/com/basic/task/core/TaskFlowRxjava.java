//package com.gersy.task.core;
//
//
//import com.piesat.project.common.utils.SuperLogger;
//import com.gersy.task.exector.Executor;
//import com.gersy.task.model.PropertyInfo;
//import com.gersy.task.model.TaskResult;
//import com.gersy.task.task.Task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Function;
//
//public class TaskFlowRxjava {
//    private List<Task<?>> mTasks;
//    //忽略异常
//    private boolean ignoreError = true;
//    private String projectName;
//    private PropertyInfo propertyInfo;
//    private Observable<PropertyInfo<?>> observable;
//
//    private TaskFlowRxjava() {
//    }
//
//    private TaskFlowRxjava(String projectName) {
//        this.projectName = projectName;
//        observable = Observable.just(new PropertyInfo<>(null));
//    }
//
//    public TaskFlowRxjava(String projectName, PropertyInfo propertyInfo) {
//        this.projectName = projectName;
//        this.propertyInfo = propertyInfo;
//    }
//
//    public static TaskFlowRxjava create(String projectName) {
//        return new TaskFlowRxjava(projectName);
//    }
//
//    public TaskFlowRxjava tasks(List<Task<?>> tasks) {
//        this.mTasks = tasks;
//        for (Task<?> task : tasks) {
//            doOnNext(task);
//        }
//        return this;
//    }
//
//    public String getProjectName() {
//        return projectName;
//    }
//
//    public TaskFlowRxjava ignoreError(boolean ignoreError) {
//        this.ignoreError = ignoreError;
//        return this;
//    }
//
//    public TaskFlowRxjava doOnNext(Task<?> task) {
//        if (task.isFlat()) {
//            observable = observable.flatMap((Function<PropertyInfo<?>, ObservableSource<PropertyInfo<?>>>) propertyInfo -> {
//                TaskResult<?> execute = task.execute();
//                List<?> data = (List<?>) execute.getData();
//                List<PropertyInfo<?>> propertyInfos = new ArrayList<>(data.size());
//                for (Object datum : data) {
//                    propertyInfos.add(new PropertyInfo<>(datum));
//                }
//                return Observable.fromIterable(propertyInfos);
//            });
//        } else {
//            observable = observable.map((Function<PropertyInfo, PropertyInfo<?>>) propertyInfo -> {
//                task.setPropertyInfo(propertyInfo);
//                TaskResult<?> taskResult = task.execute();
//                return new PropertyInfo<>(taskResult.getData());
//            });
//        }
//        return this;
//    }
//
//    public void start(Executor executor) {
//        observable.subscribe(new Observer<PropertyInfo<?>>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//
//            }
//
//            @Override
//            public void onNext(PropertyInfo<?> propertyInfo) {
//                SuperLogger.e(propertyInfo);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                SuperLogger.e(throwable);
//            }
//
//            @Override
//            public void onComplete() {
//                SuperLogger.d("完成");
//            }
//        });
//
//    }
//}
