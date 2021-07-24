package com.basic.task.task.impl;

import com.basic.common.utils.filter.FilterHelper;
import com.basic.task.task.impl.base.BaseFlatTask;
import com.basic.common.utils.ftp.EasyFtp;
import com.basic.task.annotation.TaskAnnotation;
import com.basic.task.annotation.TaskParam;
import com.basic.task.constants.Constants;
import com.basic.task.logger.TaskLoggerFactory;
import com.basic.task.model.TaskInfo;
import com.basic.task.model.TaskResult;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Gersy
 */
@TaskAnnotation(name = "ftp下载", group = Constants.PUBLIC, hasFilter = true)
public class FtpFlatTask extends BaseFlatTask<EasyFtp.FtpConfig> {
    private FTPFileFilter ftpFileFilter;
    @TaskParam(alias = "ip地址")
    private String ip;
    @TaskParam(alias = "端口号", defaultValue = "21")
    private String port;
    @TaskParam(alias = "用户名")
    private String username;
    @TaskParam(alias = "密码")
    private String password;
    @TaskParam(alias = "ftp工作目录")
    private String workDir;
    @TaskParam(alias = "下载目录")
    private String downloadDir;
    @TaskParam(alias = "过滤条件")
    private String condition;
    private EasyFtp.FtpConfig ftpConfig;

    public FtpFlatTask(TaskInfo taskInfo) {
        super(taskInfo);
        ftpConfig = new EasyFtp.FtpConfig(ip, Integer.parseInt(port), username, password, workDir, downloadDir);
        createFilter();
    }

    private void createFilter() {
        Pattern pattern = FilterHelper.parsePattern(condition);
        if (pattern != null) {
            ftpFileFilter = new FTPFileFilter() {
                @Override
                public boolean accept(FTPFile file) {
                    return pattern.matcher(file.getName()).find();
                }
            };
        }
    }

    public FtpFlatTask(TaskInfo taskInfo, FTPFileFilter ftpFileFilter) {
        this(taskInfo);
        this.ftpFileFilter = ftpFileFilter;
    }

    public void setFtpFileFilter(FTPFileFilter ftpFileFilter) {
        this.ftpFileFilter = ftpFileFilter;
    }

    @Override
    public TaskResult<?> execute() {
        EasyFtp easyFtp = new EasyFtp(ftpConfig);
        String downloadDir = ftpConfig.getDownloadDir();
        if (downloadDir == null) {
            return TaskResult.error("没有设置downloadDir，无法下载ftp文件");
        }
        TaskLoggerFactory.getTaskLogger().d(taskInfo.getJobKey(), "ftp连接成功，开始扫描文件...");
        try {
            List<String> listNames = easyFtp.listAllNames(ftpConfig.getWorkDir(), ftpFileFilter);
            TaskLoggerFactory.getTaskLogger().d(taskInfo.getJobKey(), "共扫描到文件：" + listNames.size());
            List<File> files = new ArrayList<>(listNames.size());
            for (String fileName : listNames) {
                File distFile = new File(downloadDir + fileName);
                if (distFile.exists()) {
                    continue;
                }
                TaskLoggerFactory.getTaskLogger().d(taskInfo.getJobKey(), distFile.getAbsolutePath());
                files.add(easyFtp.downloadFile(fileName, downloadDir));
            }
            easyFtp.destory();
            listNames.clear();
            return TaskResult.success("从ftp共下载到符合要求的文件：" + files.size() + "个", files);
        } catch (IOException e) {
            e.printStackTrace();
            easyFtp.destory();
            return TaskResult.error("从ftp：" + ftpConfig.getIp() + "下载文件出错，错误内容：" + e.getMessage());
        } finally {
            easyFtp.destory();
        }
    }
}
