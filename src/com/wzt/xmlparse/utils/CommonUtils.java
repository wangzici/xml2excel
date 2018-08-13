package com.wzt.xmlparse.utils;

import java.util.Collection;

public class CommonUtils {
    public static <V> boolean isEmpty(Collection<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    public static <V> boolean isEmpty(V[] sourceList) {
        return (sourceList == null || sourceList.length == 0);
    }
}
