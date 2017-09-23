package com.wzt.xmlparse;

import java.util.ArrayList;
import java.util.Map;

public class ValuesDir{
    private String mTitle;
    private Map<String,String> mStringmap;
    private Map<String,ArrayList<String>> mArraysmap;

    public ValuesDir(String title,Map<String,String> stringmap,Map<String,ArrayList<String>> arraysmap){
        mTitle = title == null ? "": title;
        mStringmap = stringmap;
        mArraysmap = arraysmap;
    }

    public String getTitle() {
        return mTitle;
    }

    public Map<String, String> getmStringmap() {
        return mStringmap;
    }

    public Map<String, ArrayList<String>> getmArraysmap() {
        return mArraysmap;
    }
}
