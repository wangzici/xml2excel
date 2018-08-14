package com.wzt.xmlparse;

import com.wzt.xmlparse.models.ArrayFile;
import com.wzt.xmlparse.models.StringFile;
import com.wzt.xmlparse.models.ValuesDir;
import com.wzt.xmlparse.utils.CommonUtils;
import com.wzt.xmlparse.utils.ExcelUtil;
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

    public static boolean xml2xls(String resFileDirPath, String xlsFileDirPath) {
        File xlsFileDir = new File(xlsFileDirPath);
        File resFileDir = new File(resFileDirPath);
        if (!xlsFileDir.exists() || !resFileDir.exists()) {
            return false;
        }
        File translateXLS = new File(xlsFileDir.getAbsolutePath(), Constants.FILE_NAME_TRANSLATE);
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
        for (File valuesDir : fileList) {
            String valuesPath = valuesDir.getAbsolutePath();

            File stringFile = new File(valuesPath, Constants.FILE_NAME_STRINGS);
            if (stringFile.exists()) {
                stringFileList.add(stringFile);
            }

            File arrayFile = new File(valuesPath, Constants.FILE_NAME_ARRAYS);
            if (arrayFile.exists()) {
                arrayFileList.add(arrayFile);
            }
        }

        //遍历所有strings.xml
        for (File stringFile : stringFileList) {
            StringFile myStringFile = parseStringXML(stringFile);
            String parentName = stringFile.getParentFile().getName();
            try {
                ExcelUtil.writeStringExcel(translateXLS, myStringFile, parentName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        //遍历所有arrays.xml
        for (File arrayFile : arrayFileList) {
            ArrayFile myArrayFile = xmlArrayParse(arrayFile);
            String parentName = arrayFile.getParentFile().getName();
            try {
                ExcelUtil.writeArrayExcel(translateXLS, myArrayFile, parentName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean xls2xml(String xlsFilePath, String xmlFileDirPath) {
        File xlsFile = new File(xlsFilePath);
        File xmlFileDir = new File(xmlFileDirPath);
        if (!xlsFile.exists() || !xmlFileDir.exists())
            return false;
        try {
            excel2xml(xlsFile, xmlFileDir);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static ArrayFile xmlArrayParse(File file) {
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

    private static StringFile parseStringXML(File file) {
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

    private static void excel2xml(File excelFile, File FileDir) {
        createStringXMLs(ExcelUtil.getStringExcel(excelFile), FileDir);
        createArrayXMLs(ExcelUtil.getArrayExcel(excelFile), FileDir);
    }

    private static void createStringXMLs(Map<String, Map<String, String>> maps, File FileDir) {
        for (Map.Entry<String, Map<String, String>> mapEntry : maps.entrySet()) {
            String fileDirName = mapEntry.getKey();
            Map<String, String> map = mapEntry.getValue();
            File resFile = new File(FileDir.getAbsolutePath() + "/res_translate");
            FileUtils.createDir(resFile);
            File valueFile = new File(resFile.getAbsolutePath() + "/" + fileDirName);
            FileUtils.createDir(valueFile);
            File xmlFile = new File(valueFile.getAbsolutePath(), Constants.FILE_NAME_STRINGS);
            FileUtils.deleteFile(xmlFile);


            try {
                SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
                TransformerHandler handler = factory.newTransformerHandler();
                Transformer transformer = handler.getTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
                Result result = new StreamResult(new FileOutputStream(xmlFile));
                handler.setResult(result);

                handler.startDocument();
                AttributesImpl attrs = new AttributesImpl();
                attrs.clear();
                attrs.addAttribute("", "xmlns:xliff", "xmlns:xliff", "string", "urn:oasis:names:tc:xliff:document:1.2");
                handler.startElement("", "resources", "resources", attrs);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    attrs.clear();
                    attrs.addAttribute("", "name", "name", "string", entry.getKey());
                    handler.startElement("", "string", "string", attrs);
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

    private static void createArrayXMLs(ArrayList<ValuesDir> valuesarrayList, File FileDir) {
        for (ValuesDir valuesDir : valuesarrayList) {
            String fileDirName = valuesDir.getTitle();
            Map<String, ArrayList<String>> map = valuesDir.getArraysMap();
            System.out.println(map.toString());

            File resFile = new File(FileDir.getAbsolutePath() + "/res_translate");
            FileUtils.createDir(resFile);
            File valueFile = new File(resFile.getAbsolutePath() + "/" + fileDirName);
            FileUtils.createDir(valueFile);
            File xmlFile = new File(valueFile.getAbsolutePath() , Constants.FILE_NAME_ARRAYS);
            FileUtils.deleteFile(xmlFile);

            try {
                SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
                TransformerHandler handler = factory.newTransformerHandler();
                Transformer transformer = handler.getTransformer();
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

                for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
                    String name = entry.getKey();
                    ArrayList<String> itemArrayList = entry.getValue();
                    atts.clear();
                    atts.addAttribute("", "name", "name", "string", name);
                    handler.startElement("", "string-array", "string-array", atts);
                    for (String item : itemArrayList) {
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


