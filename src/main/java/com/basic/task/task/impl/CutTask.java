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
@TaskAnnotation(name = "文件剪切", group = Constants.PUBLIC)
public class CutTask extends BaseTask<File> {

    @TaskParam(alias = "目标文件夹路径")
    private String targetDirectory;

    public CutTask(TaskInfo taskInfo) {
        super(taskInfo);
        targetDirectory = taskInfo.getParamInfoMap().get("targetDirectory").getValue();
    }

    @Override
    public TaskResult<?> execute() {
        File file = propertyInfo.getData();
//        String newFileName = fileInfo.getNewFileName();
//        boolean b = newFileName == null ? copyFile(file, targetDirectory) : copyFile(file, newFileName, targetDirectory);
        boolean b = transferFile(file, targetDirectory);
        if (b) {
            return TaskResult.success(file.getName() + "剪切成功", file);
        }
        return TaskResult.error(file.getName() + "剪切失败", file);
    }

    protected boolean transferTo(String sourcePath, String distPath) {
        try {
            File file = new File(sourcePath);
            FileUtils.copyFile(file, new File(distPath));
            return file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean transferTo(File sourceFile, String distPath) {
        return transferTo(sourceFile, sourceFile.getName(), distPath);
    }

    private boolean transferTo(File sourceFile, String fileName, String distPath) {
        try {
            String distFile = distPath + File.separator + fileName;
            File destFile = new File(distFile);
//            if (destFile.exists()){
//                renameOldFile(distPath,fileName,destFile);
//            }
            FileUtils.copyFile(sourceFile, destFile);
            return sourceFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean renameTo(String sourcePath, String distPath) {
        File file = new File(sourcePath);
        return transferFile(file, distPath);
    }

    protected boolean transferFile(File file, String distPath) {
        return transferFile(file, file.getName(), distPath);
    }

    private boolean transferFile(File file, String fileName, String distPath) {
        String str = distPath.substring(0, 1);
        boolean success = false;
        String first = file.getAbsolutePath().substring(0, 1);
        if (str.equalsIgnoreCase(first)) {
            success = renameTo(file, fileName, distPath);
        } else {
            success = transferTo(file, fileName, distPath);
        }
        return success;
    }

    protected boolean renameTo(File file, String distPath) {
        return renameTo(file, file.getName(), distPath);
    }

    protected boolean renameTo(File file, String fileName, String distPath) {
        File folder = new File(distPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File destFile = new File(folder, fileName);
        if (destFile.exists()) {
//            renameOldFile(distPath,fileName,destFile);
            destFile.delete();
        }
        return file.renameTo(destFile);
    }

    private void renameOldFile(String distPath, String fileName, File sourceFile) {
        int maxIndex = 0;
        File dir = new File(distPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            String name = file.getName();
            int firstIndex = name.lastIndexOf("_");
            int lastIndex = name.lastIndexOf(".");
            String index;
            if (lastIndex == -1) {
                index = name.substring(firstIndex + 1);
            } else {
                index = name.substring(firstIndex + 1, lastIndex);
            }
            if (firstIndex != -1) {
                int i = Integer.parseInt(index);
                maxIndex = maxIndex < i ? i : maxIndex;
            }
        }
        int endIndex = fileName.lastIndexOf(".");
        String reFileName;
        String fileType = "";
        if (endIndex != -1) {
            reFileName = fileName.substring(0, endIndex);
            fileType = fileName.substring(endIndex);
        } else {
            reFileName = fileName;
        }
        reFileName = reFileName + "_bak_" + (maxIndex + 1) + fileType;
        sourceFile.renameTo(new File(dir, reFileName));
    }

    protected boolean rename(String sourcePath, String newName) {
        File file = new File(sourcePath);
        return rename(file, newName);
    }

    protected boolean rename(File file, String newName) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        String lastName = name.substring(index);
        String distPath = file.getParent() + File.separator + newName + lastName;
        return file.renameTo(new File(distPath));
    }


}
