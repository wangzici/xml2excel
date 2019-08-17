package com.wzt.xmlparse.utils;

import com.sun.istack.internal.NotNull;

import java.io.File;

public class FileUtils {
    public static void deleteFile(@NotNull File file) {
        System.out.println("[deleteFile] filePath = " + file.getAbsolutePath());

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("[deleteFile]delete success!");
            } else {
                System.out.println("[deleteFile]delete fail!");
            }
        }
    }

    public static void createDir(@NotNull File file) {
        System.out.println("[createDir] filePath = " + file.getAbsolutePath());
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("[createDir]create success!");
            } else {
                System.out.println("[createDir]create fail!");
            }
        }
    }
}
