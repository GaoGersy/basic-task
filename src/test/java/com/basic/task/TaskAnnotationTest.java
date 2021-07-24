//package com.gersy.task;
//
//import com.piesat.project.common.auto.ApplicationContextRegister;
//import com.gersy.task.core.CommonHandler;
//import com.gersy.task.model.ParamInfo;
//import com.piesat.project.common.utils.SuperLogger;
//import com.gersy.task.model.TaskInfo;
//import com.gersy.task.scanner.TaskScanner;
//
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Gersy
// * @date 2020/5/21
// */
//public class TaskAnnotationTest {
//    @Test
//    public void taskFlow() {
//        Map<String, ParamInfo> fileMap = new HashMap<>(1);
//        fileMap.put("sourceDirectory", new ParamInfo("sourceDirectory", "待扫描的目录路径", "D:\\taskflow\\source"));
//        Map<String, ParamInfo> copyMap = new HashMap<>(1);
//        copyMap.put("targetDirectory", new ParamInfo("targetDirectory", "目标文件夹路径", "D:\\taskflow\\copy"));
//        Map<String, ParamInfo> cutMap = new HashMap<>(1);
//        cutMap.put("targetDirectory", new ParamInfo("targetDirectory", "目标文件夹路径", "D:\\taskflow\\cut"));
////        TaskFlow.create("aaa")
////                .doOnNext(new FileScanFlatTask(fileMap))
////                .doOnNext(new CopyTask(copyMap))
////                .doOnNext(new CutTask(cutMap))
////                .start(new TaskFlow.Executor() {
////                    @Override
////                    public void info(TaskResult taskResult) {
////                        SuperLogger.i(taskResult.toString());
////                    }
////
////                    @Override
////                    public void onNext(PropertyInfo taskResult) {
////                        SuperLogger.d(taskResult);
////                    }
////
////                    @Override
////                    public void onError(TaskResult taskResult) {
////                        SuperLogger.e(taskResult);
////                    }
////
////                    @Override
////                    public void onComplete(String projectName) {
////                        SuperLogger.d(projectName);
////                    }
////                });
//    }
//
//    @Test
//    public void testTask() {
//        CommonHandler commonHandler = new CommonHandler();
//        SuperLogger.d("");
//    }
//
//    @Test
//    public void testTaskScan(){
//        CommonHandler bean = ApplicationContextRegister.getBean(CommonHandler.class);
//        TaskScanner taskScanner = ApplicationContextRegister.getBean(TaskScanner.class);
//        List<String> groups = taskScanner.getGroups();
//        List<TaskInfo> publicTasks = taskScanner.getPublicTasks();
//        List<TaskInfo> groupTasks = taskScanner.getGroupTasks(groups.get(0));
//    }
//}
