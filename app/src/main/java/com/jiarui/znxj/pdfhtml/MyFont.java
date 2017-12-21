package com.jiarui.znxj.pdfhtml;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;


public class MyFont implements FontProvider {

    //自定义中文字体位置
    private static final String FONT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/znxj/DroidSansChinese.ttf";
    private static final String FONT_ALIAS = "my_font";
    public MyFont() throws IOException {
        FontFactory.register(FONT_PATH, FONT_ALIAS);
    }

    @Override
    public Font getFont(String fontname, String encoding, boolean embedded,
                        float size, int style, BaseColor color) {

        return FontFactory.getFont(FONT_ALIAS, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED, size, style, color);
    }

    @Override
    public boolean isRegistered(String name) {
        return name.equals(FONT_ALIAS);
    }

}