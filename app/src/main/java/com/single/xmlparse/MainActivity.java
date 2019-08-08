package com.single.xmlparse;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.show_list);
        XmlFactory xmlFactory = new XmlFactory(XmlFactory.PULL);
        Parse parse = xmlFactory.getParse();
        try {
            final List<Paragraph> paragraphList = parse.parse(getResources().getAssets().open("simple.xml"));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    TextView textView = new TextView(MainActivity.this);
                    textView.setLineSpacing(0.5f, 1.2f);
                    textView.setTextColor(Color.BLACK);
                    textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new MyHolder(textView);
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    ((TextView) ((MyHolder) viewHolder).itemView).setText(showParagraph(paragraphList.get(i)));
                    ((TextView) ((MyHolder) viewHolder).itemView).setMovementMethod(LinkMovementMethod.getInstance());
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                            ((TextView) ((MyHolder) viewHolder).itemView).getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 40);
                }

                @Override
                public int getItemCount() {
                    return paragraphList.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SpannableString showParagraph(Paragraph paragraph) {
        StringBuilder stringBuilder = new StringBuilder();

        List<LinkSpanController> linkSpans = new ArrayList<>();
        List<StrongSpanController> strongSpans = new ArrayList<>();
        for (Text tag : paragraph.tags) {
            if (!TextUtils.isEmpty(tag.link)) {
                int start = stringBuilder.toString().length();
                LinkSpanController linkSpanController = new LinkSpanController();
                linkSpanController.start = start;
                linkSpanController.end = start + tag.content.length();
                linkSpanController.context = this;
                linkSpanController.link = tag.link;
                linkSpans.add(linkSpanController);
                stringBuilder.append(tag.content);
            } else if (!TextUtils.isEmpty(tag.strong)) {
                int start = stringBuilder.toString().length();
                StrongSpanController strongSpanController = new StrongSpanController();
                strongSpanController.start = start;
                strongSpanController.end = start + tag.strong.length();
                strongSpans.add(strongSpanController);
                stringBuilder.append(tag.strong);
            } else {
                stringBuilder.append(tag.content);
            }

        }
        SpannableString spannableString = new SpannableString(stringBuilder.toString());
        //首行缩进
        spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        for (int i = 0; i < linkSpans.size(); i++) {
            LinkSpanController linkSpanController = linkSpans.get(i);
            spannableString.setSpan(linkSpanController.linkSpan, linkSpanController.start, linkSpanController.end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(linkSpanController.clickableSpanTell, linkSpanController.start, linkSpanController.end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        for (int i = 0; i < strongSpans.size(); i++) {
            StrongSpanController strongSpanController = strongSpans.get(i);
            spannableString.setSpan(strongSpanController.boldSpan, strongSpanController.start, strongSpanController.end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
