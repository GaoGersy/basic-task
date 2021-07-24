package com.basic.task.task.impl.base;

import com.basic.task.model.TaskInfo;
import com.basic.task.model.TaskResult;

import java.io.File;

/**
 * @author Gersy
 * @date 2020/5/21
 */
public abstract class BaseFileTask extends BaseTask<File> {
    public static final String PROPERTY_TARGET_DIRECTORY = "targetDirectory";
    public static final String PROPERTY_NEW_FILE_NAME = "newFileName";
    private String action;
    private final String targetDirectory;

    public BaseFileTask(String action, TaskInfo taskInfo) {
        super(taskInfo);
        this.action = action;
        targetDirectory = taskInfo.getParamInfoMap().get(PROPERTY_TARGET_DIRECTORY).getValue();
    }

    @Override
    public TaskResult<?> execute() {
        File file = propertyInfo.getData();
        String newFileName = (String) propertyInfo.getProperty(PROPERTY_NEW_FILE_NAME);
        boolean b = newFileName == null ? handleFile(file, targetDirectory) : handleFile(file, newFileName, targetDirectory);
        if (b) {
            return TaskResult.success(file.getName() + action+"成功", file);
        }
        return TaskResult.error(file.getName() + action+"失败", file);
    }

    protected abstract boolean handleFile(File file, String newFileName, String targetDirectory);

    protected boolean handleFile(File file, String targetDirectory){
        return handleFile(file,file.getName(),targetDirectory);
    }
}
