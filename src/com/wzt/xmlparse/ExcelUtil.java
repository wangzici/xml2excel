package com.wzt.xmlparse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {
	public static void createEcel(File file) throws IOException,RowsExceededException,WriteException{
		WritableWorkbook workbood = Workbook.createWorkbook(file);
		WritableSheet stringSheet = workbood.createSheet("strings", 0);
		WritableSheet arraySheet = workbood.createSheet("arrays", 1);
		
		Label labelName = new Label(0,0,"name");
		stringSheet.addCell(labelName);
		Label labelName1 = new Label(0,0,"name");
		arraySheet.addCell(labelName1);
		
		workbood.write();
		workbood.close();
	}
	
	public static void writeArrayExcel(File file,Map<String, ArrayList<String>> map,String parentName) throws IOException,RowsExceededException,WriteException, JXLException{
		if(!file.exists()){
			createEcel(file);
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
			createEcel(file);
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
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
