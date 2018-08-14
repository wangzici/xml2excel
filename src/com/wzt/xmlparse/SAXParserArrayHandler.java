package com.wzt.xmlparse;

import com.wzt.xmlparse.models.ArrayFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("unchecked")
public class SAXParserArrayHandler extends DefaultHandler {
    private String mName;
    private ArrayList<String> arrayList;
    private ArrayFile mArrayFile;
    private String mType;
    private StringBuilder strBuilder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        arrayList = new ArrayList<>();
        mArrayFile = new ArrayFile();
        strBuilder = new StringBuilder();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        mType = qName.trim();
        if ("string-array".equals(qName)) {
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                if ("name".equals(attributes.getQName(i))) {
                    String value = attributes.getValue(i);
                    mName = value;
                    System.out.println("name = " + value);
                }
            }
        } else if ("item".equals(qName)) {
            strBuilder = new StringBuilder();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if ("string-array".equals(qName)) {
            ArrayList<String> temp = (ArrayList<String>) arrayList.clone();
            mArrayFile.putArray(mName, temp);
            mName = null;
            arrayList.clear();
        } else if ("item".equals(qName)) {
            arrayList.add(strBuilder.toString());
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        if ("item".equals(mType)) {
            strBuilder.append(new String(ch, start, length));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public ArrayFile getXmlValue() {
        return mArrayFile;
    }
}
