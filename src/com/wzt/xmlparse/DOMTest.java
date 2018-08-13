package com.wzt.xmlparse;
import com.wzt.xmlparse.utils.CommonUtils;
import com.wzt.xmlparse.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.helpers.AttributesImpl;


public class DOMTest {
	
	public static boolean xml2xls(String resFileDirPath,String xlsFileDirPath){
		File xlsFileDir = new File(xlsFileDirPath);
		File resFileDir = new File(resFileDirPath);
		if (!xlsFileDir.exists() || !resFileDir.exists()) {
			return false;
		}
		File translateXLS = new File(xlsFileDir.getAbsolutePath() , Constants.FILE_NAME_TRANSLATE);
		FileUtils.deleteFile(translateXLS);
		ExcelUtil.createExcel(translateXLS);
		File[] fileList = resFileDir.listFiles();
		if (CommonUtils.isEmpty(fileList)) {
			return false;
		}
		System.out.println("fileList.size = " + fileList.length);

		ArrayList<File> stringFileList = new ArrayList<>();
		ArrayList<File> arrayFileList = new ArrayList<>();
		//过滤出所有strings.xml与arrays.xml
		for(File valuesDir : fileList){
			String valuesPath = valuesDir.getAbsolutePath();
			System.out.println("valuesDir = " + valuesPath);

			File stringFile = new File(valuesPath, Constants.FILE_NAME_STRINGS);
			if(stringFile.exists()) {
				stringFileList.add(stringFile);
			}

			File arrayFile = new File(valuesPath, Constants.FILE_NAME_ARRAYS);
			if(arrayFile.exists()) {
				arrayFileList.add(arrayFile);
			}
		}

		//遍历所有strings.xml
		for(File stringFile:stringFileList){
			Map<String, String> map = xmlStringParse(stringFile);
			String parentName = stringFile.getParentFile().getName();
			try {
				ExcelUtil.writeStringExcel(translateXLS, map , parentName);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		//遍历所有arrays.xml
		for(File arrayFile:arrayFileList){
			Map<String, ArrayList<String>> map = xmlArrayParse(arrayFile);
			String parentName = arrayFile.getParentFile().getName();
			try {
				ExcelUtil.writeArrayExcel(translateXLS, map , parentName);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public static boolean xls2xml(String xlsFilePath,String xmlFileDirPath){
		File xlsFile = new File(xlsFilePath);
		File xmlFileDir = new File(xmlFileDirPath);
		if(!xlsFile.exists() || !xmlFileDir.exists())
			return false;
		try {
			excel2xml(xlsFile,xmlFileDir);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	private static Map<String, ArrayList<String>> xmlArrayParse(File file){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			SAXParserArrayHandler handler = new SAXParserArrayHandler();
			parser.parse(file, handler);
			return handler.getXmlValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<String, String> xmlStringParse(File file){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			SAXParserStringHandler handler = new SAXParserStringHandler();
			parser.parse(file, handler);
			return handler.getXmlValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void excel2xml(File excelFile , File FileDir){
		createStringXmls(ExcelUtil.getStringExcel(excelFile) , FileDir);
		createArrayXmls(ExcelUtil.getArrayExcel(excelFile),FileDir);
	}
	
	private static void createStringXmls(Map<String, Map<String, String>> maps,File FileDir){
		for(Map.Entry<String, Map<String, String>> entrys : maps.entrySet()){
			String fileDirName = entrys.getKey();
			Map<String, String> map = entrys.getValue();
			File resFile = new File(FileDir.getAbsolutePath() + "/res_translate");
			if(!resFile.exists())
				resFile.mkdir();
			File valueFile = new File(resFile.getAbsolutePath()+"/"+fileDirName);
			if(!valueFile.exists())
				valueFile.mkdir();
			File xmlFile = new File(valueFile.getAbsolutePath()+"/strings.xml");
			if(xmlFile.exists()){
				xmlFile.delete();
			}
			
			
			try {
				SAXTransformerFactory  factory= (SAXTransformerFactory) SAXTransformerFactory.newInstance();
				TransformerHandler handler=factory.newTransformerHandler();
				Transformer transformer=handler.getTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
				Result result = new StreamResult(new FileOutputStream(xmlFile));
				handler.setResult(result);
				
				handler.startDocument();
				AttributesImpl atts = new AttributesImpl();
				atts.clear();
				atts.addAttribute("", "xmlns:xliff", "xmlns:xliff", "string", "urn:oasis:names:tc:xliff:document:1.2");
				handler.startElement("", "resources", "resources", atts);
				
				for(Map.Entry<String, String> entry : map.entrySet()){
					atts.clear();
					atts.addAttribute("", "name", "name", "string", entry.getKey());
					handler.startElement("", "string", "string", atts);
					handler.characters(entry.getValue().toCharArray(), 0, entry.getValue().length());
					handler.endElement("", "string", "string");
				}
				handler.endElement("", "resources", "resources");
				handler.endDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void createArrayXmls(ArrayList<ValuesDir> valuesarrayList,File FileDir){
		for(ValuesDir valuesDir : valuesarrayList){
			String fileDirName = valuesDir.getTitle();
			Map<String, ArrayList<String>> map = valuesDir.getmArraysmap();
			System.out.println(map.toString());
			/*for(Map.Entry<String,ArrayList<String>> entry : map.entrySet()){

			}*/
			File resFile = new File(FileDir.getAbsolutePath() + "/res_translate");
			if(!resFile.exists())
				resFile.mkdir();
			File valueFile = new File(resFile.getAbsolutePath()+"/"+fileDirName);
			if(!valueFile.exists())
				valueFile.mkdir();
			File xmlFile = new File(valueFile.getAbsolutePath()+"/arrays.xml");
			if(xmlFile.exists()){
				xmlFile.delete();
			}


			try {
				SAXTransformerFactory  factory= (SAXTransformerFactory) SAXTransformerFactory.newInstance();
				TransformerHandler handler=factory.newTransformerHandler();
				Transformer transformer=handler.getTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
				Result result = new StreamResult(new FileOutputStream(xmlFile));
				handler.setResult(result);

				handler.startDocument();
				AttributesImpl atts = new AttributesImpl();
				atts.clear();
				atts.addAttribute("", "xmlns:xliff", "xmlns:xliff", "string", "urn:oasis:names:tc:xliff:document:1.2");
				handler.startElement("", "resources", "resources", atts);

				for(Map.Entry<String, ArrayList<String>> entry : map.entrySet()){
					String name = entry.getKey();
					ArrayList<String> itemArrayList = entry.getValue();
					atts.clear();
					atts.addAttribute("", "name", "name", "string", name);
					handler.startElement("", "string-array", "string-array", atts);
					for(String item : itemArrayList){
						handler.startElement("", "item", "item", null);
						handler.characters(item.toCharArray(), 0, item.length());
						handler.endElement("", "item", "item");
					}
					handler.endElement("", "string-array", "string-array");
				}
				handler.endElement("", "resources", "resources");
				handler.endDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}


