package com.basic.task.filter;

import com.basic.common.utils.filter.FilterHelper;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Gersy
 */
public class FileNameFilter implements Filter<File> {
    private final Pattern pattern;

    public FileNameFilter(String accept) {
        pattern = FilterHelper.parsePattern(accept);
    }

    @Override
    public boolean accept(File file) {
        return pattern.matcher(file.getName()).find();
    }
}