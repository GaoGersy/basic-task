package com.basic.task.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gersy
 * @date 2020/5/21
 */
public enum FilterType {
    /**
     * 用来区分过滤器中文件和文件夹有类型
     */
    FILE(1, "文件"),
    DIRECTORY(2, "文件夹");
//    TIME(3, "时间");
    private int type;
    private String name;

    FilterType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static FilterType getFileType(int type) {
        FilterType[] values = FilterType.values();
        for (FilterType value : values) {
            if (value.getType() == type) {
                return value;
            }
        }
        return FILE;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("type", type);
        map.put("name", name);
//        map.put("conditions", getCondition());
        return map;
    }

//    private List<Map<String, String>> getCondition() {
//        if (type == 3) {
//            return TimeRelation.toList();
//        }
//        return NameRelation.toList();
//    }

    public static List<Map<String, Object>> getFilterTypes() {
        FilterType[] values = FilterType.values();
        List<Map<String, Object>> list = new ArrayList<>(values.length);
        for (FilterType value : values) {
            list.add(value.toMap());
        }
        return list;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
