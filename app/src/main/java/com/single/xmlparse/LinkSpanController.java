package com.single.xmlparse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

public class LinkSpanController {
    public Context context;
    public ForegroundColorSpan linkSpan = new ForegroundColorSpan(Color.BLUE);

    public int start;
    public int end;
    public String link;
    public ClickableSpan clickableSpanTell = new ClickableSpan() {
        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
        }

        @Override
        public void onClick(View widget) {
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    };
}
