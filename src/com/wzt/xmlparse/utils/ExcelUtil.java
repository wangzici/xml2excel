package com.wzt.xmlparse.utils;

import com.wzt.xmlparse.models.ArrayFile;
import com.wzt.xmlparse.models.StringFile;
import com.wzt.xmlparse.models.ValuesDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {
    public static void createExcel(File file) {
        try {
            WritableWorkbook workBook = Workbook.createWorkbook(file);
            WritableSheet stringSheet = workBook.createSheet("strings", 0);
            WritableSheet arraySheet = workBook.createSheet("arrays", 1);

            Label labelName = new Label(0, 0, "name");
            stringSheet.addCell(labelName);
            Label labelName1 = new Label(0, 0, "name");
            arraySheet.addCell(labelName1);

            workBook.write();
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeArrayExcel(File file, ArrayFile arrayFile, String parentName) throws IOException, JXLException {
        if (!file.exists()) {
            createExcel(file);
        }

        Workbook wd = Workbook.getWorkbook(file);
        WritableWorkbook workbook = Workbook.createWorkbook(file, wd);
        WritableSheet sheet = workbook.getSheet("arrays");

        int column = -1;
        for (int i = 1; i < sheet.getColumns(); i++) {
            String title = sheet.getCell(i, 0).getContents();
            if (title.equals(parentName)) {
                column = i;
            }
        }
        if (column == -1) {
            column = sheet.getColumns();
            Label labelTitle = new Label(column, 0, parentName);
            sheet.addCell(labelTitle);
        }
        String[] rows = new String[sheet.getRows()];
        for (int i = 1; i < rows.length; i++) {
            rows[i] = sheet.getCell(0, i).getContents();
        }

        int row = -1;
        for (ArrayFile.ArrayItem arrayItem : arrayFile.getArrays()) {
            String name = arrayItem.getName();
            List<String> arrayList = arrayItem.getValues();
            for (int i = 1; i < rows.length; i++) {
                if (name.equals(rows[i])) {
                    row = i;
                    break;
                }
            }
            if (row == -1) {
                row = rows.length;
            }
            Label labelName = new Label(0, row, name);
            sheet.addCell(labelName);
            for (String str : arrayList) {
                Label labelValue = new Label(column, row, str);
                sheet.addCell(labelValue);
                row++;
            }
        }

        workbook.write();
        workbook.close();
    }

    public static void writeStringExcel(File file, StringFile stringFile, String parentName) throws IOException, JXLException {
        if (!file.exists()) {
            createExcel(file);
        }

        Workbook wd = Workbook.getWorkbook(file);
        WritableWorkbook workbook = Workbook.createWorkbook(file, wd);
        WritableSheet sheet = workbook.getSheet("strings");

        int column = -1;
        //首先查找是否有相同的value文件夹名字
        for (int i = 1; i < sheet.getColumns(); i++) {
            String title = sheet.getCell(i, 0).getContents();
            if (title.equals(parentName)) {
                column = i;
            }
        }
        //如果没有相同的value文件夹，则新创建一列
        if (column == -1) {
            column = sheet.getColumns();
            Label labelTitle = new Label(column, 0, parentName);
            sheet.addCell(labelTitle);
        }

        String[] rows = new String[sheet.getRows()];
        for (int i = 1; i < rows.length; i++) {
            rows[i] = sheet.getCell(0, i).getContents();
        }

        int row = -1;
        for (Map.Entry<String, String> entry : stringFile.getValues().entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            for (int i = 1; i < rows.length; i++) {
                if (name.equals(rows[i])) {
                    //如果有相同的name,则直接放到同一行显示
                    row = i;
                    break;
                }
            }
            if (row == -1) {
                row = rows.length;
            }
            Label labelName = new Label(0, row, name);
            sheet.addCell(labelName);
            Label labelValue = new Label(column, row, value);
            sheet.addCell(labelValue);
            row++;
        }

        workbook.write();
        workbook.close();
    }

    public static List<StringFile> getStringExcel(File file) {
        List<StringFile> stringFiles = new ArrayList<>();
        if (file.exists()) {
            try {
                Workbook wd = Workbook.getWorkbook(file);
                Sheet sheet = wd.getSheet("strings");
                String[] names = new String[sheet.getRows()];
                for (int i = 1; i < names.length; i++) {
                    names[i] = sheet.getCell(0, i).getContents();
                }

                String[] titles = new String[sheet.getColumns()];
                for (int i = 1; i < titles.length; i++) {
                    StringFile stringFile = new StringFile();
                    titles[i] = sheet.getCell(i, 0).getContents();
                    for (int j = 1; j < names.length; j++) {
                        String content = sheet.getCell(i, j).getContents();
                        if (!"".equals(content)) {
                            stringFile.put(names[j], sheet.getCell(i, j).getContents());
                        }
                    }
                    stringFile.setDirName(titles[i]);
                    stringFiles.add(stringFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringFiles;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<ValuesDir> getArrayExcel(File file) {
        ArrayList<ValuesDir> result = new ArrayList<>();
        if (file.exists()) {
            try {
                Workbook wd = Workbook.getWorkbook(file);
                Sheet sheet = wd.getSheet("arrays");
                String[] names = new String[sheet.getRows()];
                for (int i = 1; i < names.length; i++) {
                    names[i] = sheet.getCell(0, i).getContents();
                }

                String[] titles = new String[sheet.getColumns()];
                for (int i = 1; i < titles.length; i++) {
                    titles[i] = sheet.getCell(i, 0).getContents();
                    Map<String, ArrayList<String>> arrayListMap = new TreeMap<>();
                    ArrayList<String> arrayList = new ArrayList<>();
                    String lastName = "";
                    String name = "";
                    for (int j = 1; j < names.length; j++) {
                        if (!"".equals(names[j])) {
                            lastName = name;
                            name = names[j];
                        }
                        String content = sheet.getCell(i, j).getContents();
                        if ("".equals(names[j]) || j == 1) {
                            if (!"".equals(content))
                                arrayList.add(content);
                        } else {
                            System.out.println("j = " + j);
                            System.out.println("lastName = " + lastName + ";arrayList = " + arrayList);
                            arrayListMap.put(lastName, (ArrayList<String>) arrayList.clone());
                            arrayList.clear();
                            if (!"".equals(content))
                                arrayList.add(content);
                        }
                        if (j == names.length - 1 && arrayList.size() != 0) {
                            System.out.println("name = " + name + ";arrayList = " + arrayList);
                            arrayListMap.put(name, (ArrayList<String>) arrayList.clone());
                        }
                    }
                    result.add(new ValuesDir(titles[i], null, arrayListMap));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
