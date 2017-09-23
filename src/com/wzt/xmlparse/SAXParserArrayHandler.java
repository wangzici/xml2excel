package com.wzt.xmlparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserArrayHandler extends DefaultHandler {
	String mName;
	ArrayList<String> arraylist;
	Map<String,ArrayList<String>> map;
	String mType;
	StringBuilder strBuilder;
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		arraylist = new ArrayList<String>();
		map = new HashMap<String,ArrayList<String>>();
		strBuilder = new StringBuilder();
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		mType = qName.trim();
		System.out.println("uri = " + uri + "; localName = " + localName + "; qName = " + qName);
		if("string-array".equals(qName)){
			int num = attributes.getLength();
			for(int i=0;i < num;i++){
				if("name".equals(attributes.getQName(i))){
					String value = attributes.getValue(i);
					mName = value;
					System.out.println("value = " + value);
				}
			}
		}else if("item".equals(qName)){
			strBuilder = new StringBuilder();
		}
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if("string-array".equals(qName)){
			ArrayList<String> temp = (ArrayList<String>) arraylist.clone();
			map.put(mName, temp);
			mName = null;
			arraylist.clear();
			System.out.println("endElement ; localName = " + localName + "; qName = " + qName);
		}else if("item".equals(qName)){
			arraylist.add(strBuilder.toString());
		}
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if("item".equals(mType)){
			strBuilder.append(new String(ch, start, length));
			System.out.println("[characters] strBuilder = " + strBuilder.toString());
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		System.out.println("endDocument ; map = " + map.toString());
	}
	
	public Map<String,ArrayList<String>> getXmlValue(){
		return map;
	}
}
