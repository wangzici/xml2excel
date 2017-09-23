package com.wzt.xmlparse;
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
		if(!xlsFileDir.exists())
			return false;
		if(!resFileDir.exists())
			return false;
		File translateXLS = new File(xlsFileDir.getAbsolutePath() + "/translate.xls");
		if(translateXLS.exists()){
			translateXLS.delete();
		}
		try {
			ExcelUtil.createEcel(translateXLS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		File[] filelist = resFileDir.listFiles();
		System.out.println("filelist.size = " + filelist.length);
		
		ArrayList<File> stringFileList = new ArrayList<File>();
		ArrayList<File> arrayFileList = new ArrayList<File>();
		for(int i=0;i < filelist.length;i++){
			File valuesDir = filelist[i];
			System.out.println("valuesDir = " + valuesDir.getAbsolutePath());
			File stringFile = new File(valuesDir.getAbsolutePath() + "/strings.xml");
			File arrayFile = new File(valuesDir.getAbsolutePath() + "/arrays.xml");
			if(stringFile.exists())
				stringFileList.add(stringFile);
			if(arrayFile.exists())
				arrayFileList.add(arrayFile);
		}
		System.out.println("stringFileList = " + stringFileList.toString());
		System.out.println("arrayFileList = " + arrayFileList.toString());
		for(File stringFile:stringFileList){
			Map<String, String> map = xmlStringParse(stringFile);
			String parentName = stringFile.getParentFile().getName();
			try {
				ExcelUtil.writeStringExcel(translateXLS, map , parentName);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
		}
		for(File arrayFile:arrayFileList){
			Map<String, ArrayList<String>> map = xmlArrayParse(arrayFile);
			String parentName = arrayFile.getParentFile().getName();
			try {
				ExcelUtil.writeArrayExcel(translateXLS, map , parentName);
			} catch (Exception e) {
				// TODO: handle exception
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
			// TODO: handle exception
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
			// TODO: handle exception
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
			// TODO: handle exception
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
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private static void createArrayXmls(ArrayList<ValuesDir> valuesarrayList,File FileDir){
		for(ValuesDir valuesDir : valuesarrayList){
			String fileDirName = valuesDir.getTitle();
			Map<String, ArrayList<String>> map = valuesDir.getArrayMap();
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


