package com.basic.task.task.impl;

import com.basic.task.annotation.TaskAnnotation;
import com.basic.task.annotation.TaskParam;
import com.basic.task.constants.Constants;
import com.basic.task.constants.DataType;
import com.basic.task.filter.FileFilter;
import com.basic.task.model.FilterInfo;
import com.basic.task.model.FilterType;
import com.basic.task.model.TaskInfo;
import com.basic.task.model.TaskResult;
import com.basic.task.task.impl.base.BaseFlatTask;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件扫描任务
 *
 * @author Gersy
 */
@TaskAnnotation(name = "文件扫描", group = Constants.PUBLIC, hasFilter = true)
public class FileScanFlatTask extends BaseFlatTask<String> {

    private FileFilter filter;
    @TaskParam(alias = "待扫描的目录路径",type = DataType.STRING)
    private String sourceDirectory;
    @TaskParam(alias = "是否扫描子目录", type = DataType.BOOLEAN,defaultValue = "true", options = {"true", "false"})
    private boolean needScanChildren;
    @TaskParam(alias = "文件过滤器", type = DataType.OBJECT)
    private String fileFilter;
    @TaskParam(alias = "文件夹过滤器", type = DataType.OBJECT)
    private String dirFilter;

    public FileScanFlatTask(TaskInfo taskInfo) {
        super(taskInfo);
//        fileFilter = FileFilter.createFromFilterInfo(taskInfo.getFilterInfos());
        filter=new FileFilter();
        filter.createFromFilterInfo(new FilterInfo(FilterType.FILE.getType(),fileFilter));
        filter.createFromFilterInfo(new FilterInfo(FilterType.DIRECTORY.getType(),dirFilter));
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.filter = fileFilter;
    }

    @Override
    public TaskResult<?> execute() {
//        TaskLogger taskLogger = TaskLoggerFactory.getTaskLogger();
        sourceDirectory = propertyInfo == null ? sourceDirectory : propertyInfo.getData();
        File file = new File(sourceDirectory);
        if (!file.exists()) {
            String obj = file.getAbsolutePath() + " 目录不存在，请检查！";
//            taskLogger.d(obj);
            return TaskResult.error(obj);
        }
        try {
            List<File> files = scanFiles(file);
            String msg = "共扫描到符合要求的文件：" + files.size() + "个";
//            taskLogger.d(msg);
            return TaskResult.success(msg, files);
        } catch (Exception e) {
            e.printStackTrace();
//            taskLogger.e(e.getMessage());
            return TaskResult.error(e.getMessage());
        }
    }

    private LinkedList<File> scanFiles(File directory) {
        if (!directory.exists()) {
            throw new RuntimeException("目录不存在：" + directory.getAbsolutePath());
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException("不是合法的目录：" + directory.getAbsolutePath());
        }
        LinkedList<File> fileList = new LinkedList<>();
        LinkedList<File> directories = new LinkedList<>();
        listFile(directory, directories, fileList);
        if (needScanChildren) {
            while (!directories.isEmpty()) {
                File directory1 = directories.removeFirst();
                listFile(directory1, directories, fileList);
            }
        }
        return fileList;
    }

    private void listFile(File directory, LinkedList<File> directories, LinkedList<File> fileList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (needScanChildren && filter.checkDirectory(file)) {
                        directories.add(file);
                    }
                } else {
                    if (filter.checkFile(file)) {
                        fileList.add(file);
                        increaseCount();
                    }
                }
            }
        }
    }

//    public List<File> scanFiles(File file, List<File> fileList) {
//        if (fileList == null) {
//            fileList = new ArrayList<>();
//        }
//        if (file.isDirectory()) {
//            File[] files = file.listFiles();
//            if (files != null) {
//                for (File dir : files) {
//                    if (dir.isDirectory()) {
//                        if (needScanChildren && fileFilter.checkDirectory(file)) {
//                            scanFiles(dir, fileList);
//                        }
//                    } else {
//                        if (fileFilter.checkFile(file)) {
//                            fileList.add(dir);
//                            increaseCount();
//                        }
//                    }
//                }
//            }
//        } else {
//            fileList.add(file);
//            increaseCount();
//        }
//        return fileList;
//    }

}
