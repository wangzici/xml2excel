package com.wzt.xmlparse.utils;

import com.sun.istack.internal.NotNull;

import java.io.File;

public class FileUtils {
    public static void deleteFile(@NotNull File file) {
        if (file.exists()) {
            if (file.delete()) {
                //TODO: print delete file success
            } else {
                //TODO: print delete file fail
            }
        }
    }

    public static void createDir(@NotNull File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                //TODO: print create dir success
            } else {
                //TODO: print create dir fail
            }
        }
    }
}
