package com.basic.task.filter;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author Gersy
 * @date 2020/5/29
 */
public class TimeFilter implements Filter<File> {
    private int type = -1;
    private long testTime = -1;

    public TimeFilter(Map<String, String> acceptType) {
        this.init(acceptType);
    }

    private void init(Map<String, String> acceptType) {
        if (acceptType == null) {
            return;
        }
        Set<Map.Entry<String, String>> entries = acceptType.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            type = Integer.parseInt(entry.getKey());
            String value = entry.getValue();
            if (value == null || value.equals("")) {
                continue;
            }
            testTime = Long.parseLong(entry.getValue());
        }
    }

    @Override
    public boolean accept(File file) {
        long modified = file.lastModified();
        return check(modified);
    }

    private boolean check(long time) {
//        if (type == TimeRelation.BEFORE.getType()) {
//            return time < testTime;
//        } else if (type == TimeRelation.AFTER.getType()) {
//            return time > testTime;
//        }
        return false;
    }
}
