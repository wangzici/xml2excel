package com.wzt.xmlparse.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringFile {
    private Map<String, String> values = new LinkedHashMap<>();
    private String dirName = "";

    public String getValue(String name) {
        return values.get(name);
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void put(String name, String value) {
        values.put(name, value);
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }
}
