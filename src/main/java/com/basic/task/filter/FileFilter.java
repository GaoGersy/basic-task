package com.basic.task.filter;

import com.basic.task.model.FilterInfo;
import com.basic.task.model.FilterType;

import java.io.File;
import java.util.List;

/**
 * @author Gersy
 */
public class FileFilter {

    private Filter<File> fileFilter;
    private Filter<File> directoryFilter;

    public FileFilter() {
    }

    public FileFilter(Filter<File> fileFilter, Filter<File> directoryFilter) {
        setFileFilter(fileFilter);
        setDirectoryFilter(directoryFilter);
    }

    public boolean checkFile(File file) {
        return fileFilter==null || fileFilter.accept(file);
    }

    public boolean checkDirectory(File file) {
        return directoryFilter==null || directoryFilter.accept(file);
    }

    public void setFileFilter(Filter<File> fileFilter) {
        this.fileFilter = fileFilter;
    }

    public void setDirectoryFilter(Filter<File> directoryFilter) {
        this.directoryFilter = directoryFilter;
    }

    public void createFromFilterInfo(FilterInfo filterInfo) {
        if (isEmpty(filterInfo.getCondition())) {
            return;
        }
        int fileType = filterInfo.getFileType();
        if (fileType == FilterType.FILE.getType()) {
            setFileFilter(new FileNameFilter(filterInfo.getCondition()));
        } else if (fileType == FilterType.DIRECTORY.getType()) {
            setDirectoryFilter(new FileNameFilter(filterInfo.getCondition()));
        }
    }

    private boolean isEmpty(String condition) {
        return condition == null || condition.isEmpty();
    }

    public static FileFilter createFromFilterInfo(List<FilterInfo> filterInfos) {
        if (filterInfos == null) {
            return new FileFilter(null, null);
        }
        Filter<File> fileFilter = null;
        Filter<File> directoryFilter = null;
        for (FilterInfo filterInfo : filterInfos) {
            int fileType = filterInfo.getFileType();
            if (fileType == FilterType.FILE.getType()) {
                fileFilter = new FileNameFilter(filterInfo.getCondition());
            } else if (fileType == FilterType.DIRECTORY.getType()) {
                directoryFilter = new FileNameFilter(filterInfo.getCondition());
            }
//            else if (fileType == FilterType.TIME.getType()) {
//                filterMap.computeIfAbsent(FilterType.TIME, k -> new TimeFilter(filterInfo.getConditions()));
//            }
        }
        return new FileFilter(fileFilter, directoryFilter);
    }
}
