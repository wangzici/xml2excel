package com.wzt.xmlparse.models;

import java.util.ArrayList;
import java.util.Map;

public class ValuesDir {
    private String mTitle;
    private Map<String, String> mStringMap;
    private Map<String, ArrayList<String>> mArraysMap;

    public ValuesDir(String title, Map<String, String> stringMap, Map<String, ArrayList<String>> arraysmap) {
        mTitle = title == null ? "" : title;
        mStringMap = stringMap;
        mArraysMap = arraysmap;
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
