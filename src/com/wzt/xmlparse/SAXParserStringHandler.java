package com.wzt.xmlparse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserStringHandler extends DefaultHandler {
    private String mName;
    private String mType;
    private Map<String, String> map;
    private StringBuilder strBuilder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        map = new LinkedHashMap<>();
        strBuilder = new StringBuilder();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        mType = qName;
        super.startElement(uri, localName, qName, attributes);
        System.out.println("uri = " + uri + "; localName = " + localName + "; qName = " + qName);
        if ("string".equals(qName)) {
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                if ("name".equals(attributes.getQName(i))) {
                    String value = attributes.getValue(i);
                    mName = value;
                    System.out.println("value = " + value);
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if ("string".equals(qName)) {
            map.put(mName, strBuilder.toString());
            mName = null;
            mType = null;
            strBuilder = new StringBuilder();
            System.out.println("endElement ; localName = " + localName + "; qName = " + qName);
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        if ("string".equals(mType)) {
            strBuilder.append(new String(ch, start, length));
            System.out.println("[characters] strBuilder = " + strBuilder.toString());
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("endDocument ; map = " + map.toString());
    }

    public Map<String, String> getXmlValue() {
        return map;
    }
}
