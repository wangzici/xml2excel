package com.wzt.xmlparse;

import com.wzt.xmlparse.models.StringFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserStringHandler extends DefaultHandler {
    private String mName;
    private String mType;
    private StringFile stringFile;
    private StringBuilder strBuilder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        stringFile = new StringFile();
        strBuilder = new StringBuilder();
    }

    /**
     * @param uri xml文档的命名空间
     * @param localName 标签的名字
     * @param qName 带命名空间的标签的名字
     * @param attributes 标签的属性集
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        mType = qName;
        super.startElement(uri, localName, qName, attributes);
        if ("string".equals(qName)) {
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                if ("name".equals(attributes.getQName(i))) {
                    mName = attributes.getValue(i);
                    break;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if ("string".equals(qName)) {
            System.out.println("name = " + mName);
            System.out.println("value = " + strBuilder.toString());
            stringFile.put(mName, strBuilder.toString());

            mName = null;
            mType = null;
            strBuilder = new StringBuilder();
        }
    }

    /**
     * @param ch 当前读取到的TextNode(文本节点)的字节数组
     * @param start 字节开始的位置，为0则读取全部
     * @param length 当前TextNode的长度
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        if ("string".equals(mType)) {
            strBuilder.append(new String(ch, start, length));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public StringFile getXmlValue() {
        return stringFile;
    }
}
