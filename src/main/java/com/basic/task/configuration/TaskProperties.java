package com.basic.task.configuration;

import com.basic.task.model.ParamInfo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gersy
 * @date 2020/6/5
 */
@ConfigurationProperties(prefix = "task", ignoreInvalidFields = true)
public class TaskProperties {
    private ParamInfo[] paramInfos;
    private String[] taskPackages;

    public ParamInfo[] getParamInfos() {
        return paramInfos;
    }

    public void setParamInfos(ParamInfo[] paramInfos) {
        this.paramInfos = paramInfos;
    }

    public String[] getTaskPackages() {
        return taskPackages;
    }

    public void setTaskPackages(String[] taskPackages) {
        this.taskPackages = taskPackages;
    }

    public String getAlias(String name){
        if (paramInfos==null){
            return null;
        }
        for (ParamInfo paramInfo : paramInfos) {
            if (paramInfo.getName().equals(name)){
                return paramInfo.getAlias();
            }
        }
        return null;
    }

//    @Override
//    public void afterPropertiesSet() {
//
//    }
}
