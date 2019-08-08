package com.single.xmlparse;

import android.text.TextUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DomParse implements Parse {
    @Override
    public List<Paragraph> parse(InputStream is) throws Exception {
        List<Paragraph> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        //dom解析首先是获取到当前有几个直接的子节点
        NodeList studentList = document.getElementsByTagName("paragraph");
        for (int i = 0; i < studentList.getLength(); i++) {
            Node node_student = studentList.item(i);
            NodeList childNodes = node_student.getChildNodes();
            Paragraph student = new Paragraph();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNode = childNodes.item(j);
                if ("text".equals(childNode.getNodeName())) {
                    Text text = new Text();
                    NamedNodeMap nnm = childNode.getAttributes();
                    Node linkNode = nnm.getNamedItem("link");
                    Node contentNode = nnm.getNamedItem("content");
                    Node strongNode = nnm.getNamedItem("strong");
                    if (linkNode != null && !TextUtils.isEmpty(linkNode.getTextContent())) {
                        text.link = linkNode.getTextContent();
                    }
                    if (contentNode != null && !TextUtils.isEmpty(contentNode.getTextContent())) {
                        text.content = contentNode.getTextContent();
                    }
                    if (strongNode != null && !TextUtils.isEmpty(strongNode.getTextContent())) {
                        text.strong = strongNode.getTextContent();
                    }
                    student.tags.add(text);
                }
            }
            //加到List中
            list.add(student);
        }
        return list;
    }
}
