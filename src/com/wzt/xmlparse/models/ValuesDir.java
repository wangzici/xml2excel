package com.wzt.xmlparse.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * values文件夹实体类
 */
public class ValuesDir {
    private String mTitle;
    private Map<String, String> mStringMap;
    private Map<String, ArrayList<String>> mArraysMap;

    public ValuesDir(String title, Map<String, String> stringMap, Map<String, ArrayList<String>> arraysMap) {
        mTitle = title == null ? "" : title;
        mStringMap = stringMap;
        mArraysMap = arraysMap;
    }

    public String getTitle() {
        return mTitle;
    }

    public Map<String, String> getStringMap() {
        return mStringMap;
    }

    public Map<String, ArrayList<String>> getArraysMap() {
        return mArraysMap;
    }
}
