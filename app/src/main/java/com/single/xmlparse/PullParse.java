package com.single.xmlparse;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 用pull的方式解析
 */
public class PullParse implements Parse {
    @Override
    public List<Paragraph> parse(InputStream is) throws Exception {
        List<Paragraph> paragraphs = null;
        Paragraph paragraph = null;
        Text tag = null;
        //创建xmlPull解析器
        XmlPullParser parser = Xml.newPullParser();
        ///初始化xmlPull解析器
        parser.setInput(is, "utf-8");
        //读取文件的类型
        int type = parser.getEventType();
        //对应当前节点的深度
        int startDepth = parser.getDepth();
        Log.d("PullParse", "startDepth:" + startDepth);
        //无限判断文件类型进行读取
        //pull解析是按照节点的start_tag和end_tag来解析，在遍历的时候通过判断是不是到了节点的结尾
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //开始标签
                case XmlPullParser.START_TAG:
                    if ("simple".equals(parser.getName())) {
                        paragraphs = new ArrayList<>();
                        int simpleDepth = parser.getDepth();
                        Log.d("PullParse", "simpleDepth:" + simpleDepth);
                    } else if ("paragraph".equals(parser.getName())) {
                        paragraph = new Paragraph();
                        int paragraphDepth = parser.getDepth();
                        Log.d("PullParse", "paragraphDepth:" + paragraphDepth);
                    } else if ("text".equals(parser.getName())) {
                        tag = new Text();
                        int textDepth = parser.getDepth();
                        Log.d("PullParse", "textDepth:" + textDepth);
                        String link = parser.getAttributeValue(null, "link");
                        String content = parser.getAttributeValue(null, "content");
                        String strong = parser.getAttributeValue(null, "strong");
                        tag.link = link;
                        tag.content = content;
                        tag.strong = strong;
                    }
                    break;
                //结束标签
                case XmlPullParser.END_TAG:
                    if ("text".equals(parser.getName())) {
                        paragraph.tags.add(tag);
                    } else if ("paragraph".equals(parser.getName())) {
                        paragraphs.add(paragraph);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return paragraphs;
    }
}
