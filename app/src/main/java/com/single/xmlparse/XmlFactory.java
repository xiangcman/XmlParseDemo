package com.single.xmlparse;

public class XmlFactory {
    private int type;
    public static final int PULL = 1;
    public static final int DOM = 2;
    public static final int SAX = 3;
    public XmlFactory(int type) {
        this.type = type;
    }

    public Parse getParse() {
        if (type == PULL) {
            return new PullParse();
        } else if (type == DOM) {
            return new DomParse();
        } else if (type == SAX) {
            return new SaxParse();
        }
        return new PullParse();
    }
}
