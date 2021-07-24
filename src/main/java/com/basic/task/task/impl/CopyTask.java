package com.basic.task.task.impl;

import com.basic.task.task.impl.base.BaseTask;
import com.basic.task.annotation.TaskAnnotation;
import com.basic.task.annotation.TaskParam;
import com.basic.task.constants.Constants;
import com.basic.task.model.TaskInfo;
import com.basic.task.model.TaskResult;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Gersy
 */
@TaskAnnotation(name = "文件拷贝", group = Constants.PUBLIC)
public class CopyTask extends BaseTask<File> {

    @TaskParam(alias = "目标文件夹路径")
    private String targetDirectory;

    public CopyTask(TaskInfo taskInfo) {
        super(taskInfo);
        targetDirectory = taskInfo.getParamInfoMap().get("targetDirectory").getValue();
    }

    @Override
    public TaskResult<?> execute() {
        File file = propertyInfo.getData();
//        String newFileName = fileInfo.getNewFileName();
//        boolean b = newFileName == null ? copyFile(file, targetDirectory) : copyFile(file, newFileName, targetDirectory);
        boolean b = copyFile(file, targetDirectory);
        if (b) {
            return TaskResult.success(file.getName() + "拷贝成功", file);
        }
        return TaskResult.error(file.getName() + "拷贝失败", file);
    }

    public static boolean copyFile(File file, String targetDirectory) {
        return copyFile(file, file.getName(), targetDirectory);
    }

    public static boolean copyFile(File file, String fileName, String targetDirectory) {
        String path = targetDirectory + File.separator + fileName;
        try {
            FileUtils.copyFile(file, new File(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}