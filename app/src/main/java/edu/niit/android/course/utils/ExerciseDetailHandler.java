package edu.niit.android.course.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.entity.ExerciseDetail;

public class ExerciseDetailHandler extends DefaultHandler {
    private List<ExerciseDetail> details = null;
    private ExerciseDetail detail = null;
    private String nodeName;

    /**
     * xml文档的开始位置触发
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        details = new ArrayList<>();
    }

    /**
     * 每个开始标签触发
     * @param qName: 表示当前读到的开始标签名称
     * @param attributes : 属性列表
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = qName;

        if ("exercises".equals(qName)) {
            detail = new ExerciseDetail();
            if(attributes != null) {
                String value = attributes.getValue("id");
                detail.setSubjectId(Integer.parseInt(value));
            }
        }
    }

    /**
     * 文本内容触发
     *   char[]: 表示到目前为止读到的文本内容
     *   start: 表示当前内容的起始位置
     *   length: 表示当前内容的长度
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(nodeName == null) {
            return;
        }

        if ("subject".equals(nodeName)) {
            detail.setSubject(new String(ch, start, length));
        } else if ("a".equals(nodeName)) {
            detail.setA(new String(ch, start, length));
        } else if ("b".equals(nodeName)) {
            detail.setB(new String(ch, start, length));
        } else if ("c".equals(nodeName)) {
            detail.setC(new String(ch, start, length));
        } else if ("d".equals(nodeName)) {
            detail.setD(new String(ch, start, length));
        } else if ("answer".equals(nodeName)) {
            detail.setAnswer(Integer.parseInt(new String(ch, start, length)));
        }
    }

    /**
     * 每个结束标签时触发
     * @param qName: 当前读到的结束标签名称
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("exercises".equals(qName)) {
            details.add(detail);
            detail = null;
        }
        nodeName = "";
    }

    /**
     * xml文档的结尾触发
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * 返回结果
     * @return 习题集合
     */
    public List<ExerciseDetail> getDetails() {
        return details;
    }

}
