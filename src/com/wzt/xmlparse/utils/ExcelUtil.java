package com.wzt.xmlparse.utils;

import com.wzt.xmlparse.models.ValuesDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	public static void createExcel(File file){
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
	
	public static void writeArrayExcel(File file,Map<String, ArrayList<String>> map,String parentName) throws IOException,RowsExceededException,WriteException, JXLException{
		if(!file.exists()){
			createExcel(file);
		}
		
		Workbook wd = Workbook.getWorkbook(file);
		WritableWorkbook workbook = Workbook.createWorkbook(file, wd);
		WritableSheet sheet = workbook.getSheet("arrays");
		
		int colume = -1;
		for(int i=1;i<sheet.getColumns();i++){
			String title = sheet.getCell(i,0).getContents();
			if(title.equals(parentName)){
				colume = i;
			}
		}
		if(colume == -1){
			colume = sheet.getColumns();
			Label labelTitle = new Label(colume, 0, parentName);
			sheet.addCell(labelTitle);
		}
		String[] rows = new String[sheet.getRows()];
		for(int i=1;i<rows.length;i++){
			rows[i] = sheet.getCell(0, i).getContents();
		}
		
		int row = 1;
		for(Map.Entry<String, ArrayList<String>> entry : map.entrySet()){
			String name = entry.getKey();
			ArrayList<String> arraylist = entry.getValue();
			for(int i=1;i<rows.length;i++){
				if(name.equals(rows[i])){
					row = i;
					break;
				}
				if(i == rows.length-1){
					row = rows.length;
				}
			}
			Label labelName = new Label(0, row, name);
			sheet.addCell(labelName);
			for(String str : arraylist){
				Label labelValue = new Label(colume, row, str);
				sheet.addCell(labelValue);
				row++;
			}
		}
		
		workbook.write();
		workbook.close();
	}
	
	public static void writeStringExcel(File file,Map<String, String> map , String parentName) throws IOException,RowsExceededException,WriteException, JXLException{
		if(!file.exists()){
			createExcel(file);
		}
		
		Workbook wd = Workbook.getWorkbook(file);
		WritableWorkbook workbook = Workbook.createWorkbook(file, wd);
		WritableSheet sheet = workbook.getSheet("strings");
		
		int colume = -1;
		for(int i=1;i<sheet.getColumns();i++){
			String title = sheet.getCell(i,0).getContents();
			if(title.equals(parentName)){
				colume = i;
			}
		}
		if(colume == -1){
			colume = sheet.getColumns();
			Label labelTitle = new Label(colume, 0, parentName);
			sheet.addCell(labelTitle);
		}
		
		String[] rows = new String[sheet.getRows()];
		for(int i=1;i<rows.length;i++){
			rows[i] = sheet.getCell(0, i).getContents();
		}
		
		int row = 1;
		for(Map.Entry<String, String> entry : map.entrySet()){
			String name = entry.getKey();
			String value = entry.getValue();
			for(int i=1;i<rows.length;i++){
				if(name.equals(rows[i])){
					row = i;
					break;
				}
				if(i == rows.length-1){
					row = rows.length;
				}
			}
			Label labelName = new Label(0, row, name);
			sheet.addCell(labelName);
			Label labelValue = new Label(colume, row, value);
			sheet.addCell(labelValue);
			row++;
		}
		
		workbook.write();
		workbook.close();
	}
	
	public static Map<String,Map<String, String>> getStringExcel(File file){
		if(!file.exists())
			return null;
		try {
			Workbook wd = Workbook.getWorkbook(file);
			Sheet sheet = wd.getSheet("strings");
			String[] names = new String[sheet.getRows()];
			for(int i = 1;i<names.length;i++){
				names[i] = sheet.getCell(0, i).getContents();
			}
			
			Map<String,Map<String, String>> maps = new HashMap<String, Map<String,String>>(); 
			String[] titles = new String[sheet.getColumns()];
			for(int i =1;i<titles.length;i++){
				titles[i] = sheet.getCell(i, 0).getContents();
				Map<String,String> map = new HashMap<String, String>();
				for(int j=1;j<names.length;j++){
					String content = sheet.getCell(i,j).getContents();
					if(!"".equals(content))
						map.put(names[j], sheet.getCell(i,j).getContents());
				}
				maps.put(titles[i], map);
			}
			return maps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<ValuesDir> getArrayExcel(File file){
		if(!file.exists())
			return null;
		try {
			Workbook wd = Workbook.getWorkbook(file);
			Sheet sheet = wd.getSheet("arrays");
			String[] names = new String[sheet.getRows()];
			for(int i = 1;i<names.length;i++){
				names[i] = sheet.getCell(0, i).getContents();
			}

			ArrayList<ValuesDir> result = new ArrayList<ValuesDir>();
			String[] titles = new String[sheet.getColumns()];
			for(int i =1;i<titles.length;i++){
				titles[i] = sheet.getCell(i, 0).getContents();
				Map<String,ArrayList<String>> arrayListMap = new TreeMap<String,ArrayList<String>>();
				ArrayList<String> arraylist = new ArrayList<String>();
				String lastName = "";
				String name = "";
				for(int j=1;j<names.length;j++){
					if(!"".equals(names[j])){
						lastName = name;
						name = names[j];
					}
					String content = sheet.getCell(i,j).getContents();
					if("".equals(names[j]) || j == 1){
						if(!"".equals(content))
							arraylist.add(content);
					}else{
						System.out.println("j = " + j);
						System.out.println("lastName = " + lastName + ";arraylist = " + arraylist);
						arrayListMap.put(lastName,(ArrayList<String>) arraylist.clone());
						arraylist.clear();
						if(!"".equals(content))
							arraylist.add(content);
					}
					if(j == names.length -1 && arraylist.size() != 0){
						System.out.println("name = " + name + ";arraylist = " + arraylist);
						arrayListMap.put(name , (ArrayList<String>) arraylist.clone());
					}
				}
				result.add(new ValuesDir(titles[i],null,arrayListMap));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
