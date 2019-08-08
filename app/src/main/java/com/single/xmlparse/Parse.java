package com.single.xmlparse;

import java.io.InputStream;
import java.util.List;

public interface Parse {
    List<Paragraph> parse(InputStream is) throws Exception;
}
