package com.wzt.xmlparse.models;

import java.util.ArrayList;
import java.util.List;

public class ArrayFile {
    private List<ArrayItem> map = new ArrayList<>();

    public void putArray(String name, List<String> items) {
        ArrayItem arrayItem = new ArrayItem(name, items);
        map.add(arrayItem);
    }

    public List<ArrayItem> getArrays() {
        return map;
    }

    public final class ArrayItem {
        private String mName;
        private List<String> mValues;

        public ArrayItem(String mName, List<String> mValues) {
            this.mName = mName;
            this.mValues = mValues;
        }

        public String getName() {
            return mName;
        }

        public List<String> getValues() {
            return mValues;
        }
    }
}
