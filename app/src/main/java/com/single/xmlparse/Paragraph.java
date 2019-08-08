package com.single.xmlparse;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {
    public List<Text> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "Paragraph{" +
                "tags=" + tags +
                '}';
    }
}
