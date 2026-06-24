package com.arkeomag.pro;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;

public class Utils {
    public static int bg = Color.rgb(9, 14, 22);
    public static int panel = Color.rgb(13, 22, 35);
    public static int card = Color.rgb(18, 28, 44);
    public static int line = Color.rgb(50, 67, 88);
    public static int txt = Color.rgb(240, 246, 252);
    public static int muted = Color.rgb(142, 154, 171);
    public static int cyan = Color.rgb(26, 207, 236);
    public static int green = Color.rgb(48, 222, 133);
    public static int yellow = Color.rgb(255, 206, 75);
    public static int red = Color.rgb(255, 85, 85);

    public static int dp(Context c, int v) {
        return (int) (v * c.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static TextView tv(Context c, String s, int sp, int col) {
        TextView v = new TextView(c);
        v.setText(s);
        v.setTextSize(sp);
        v.setTextColor(col);
        v.setPadding(dp(c, 10), dp(c, 4), dp(c, 10), dp(c, 4));
        return v;
    }

    public static TextView title(Context c, String s) {
        TextView v = tv(c, s, 25, txt);
        v.setTypeface(Typeface.DEFAULT_BOLD);
        return v;
    }

    public static TextView section(Context c, String s) {
        TextView v = tv(c, s, 16, cyan);
        v.setTypeface(Typeface.DEFAULT_BOLD);
        return v;
    }

    public static GradientDrawable bgRound(int color, int strokeColor, float radius, int stroke) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        if (stroke > 0) g.setStroke(stroke, strokeColor);
        return g;
    }

    public static LinearLayout card(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(dp(c, 14), dp(c, 12), dp(c, 14), dp(c, 12));
        l.setBackground(bgRound(card, line, dp(c, 18), 1));
        return l;
    }

    public static Button btn(Context c, String s) {
        Button b = new Button(c);
        b.setText(s);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setPadding(dp(c, 6), 0, dp(c, 6), 0);
        b.setBackground(bgRound(Color.rgb(32, 107, 137), Color.rgb(75, 174, 201), dp(c, 12), 1));
        return b;
    }

    public static TextView chip(Context c, String s) {
        TextView v = tv(c, s, 12, txt);
        v.setGravity(Gravity.CENTER);
        v.setBackground(bgRound(Color.rgb(25, 40, 61), Color.rgb(58, 86, 113), dp(c, 20), 1));
        return v;
    }
}
