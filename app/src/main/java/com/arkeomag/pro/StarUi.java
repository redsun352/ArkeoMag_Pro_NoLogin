package com.arkeomag.pro;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;

public class StarUi {
    static int BG=Color.rgb(4,10,18);
    static int CARD=Color.rgb(12,22,36);
    static int BAR=Color.rgb(0,130,225);

    public static TextView text(Context c,String s,int sp,int color,int style){
        TextView v=new TextView(c);
        v.setText(s);
        v.setTextSize(sp);
        v.setTextColor(color);
        v.setTypeface(Typeface.DEFAULT,style);
        v.setGravity(Gravity.CENTER_VERTICAL);
        return v;
    }

    public static GradientDrawable bg(int color,int r){
        GradientDrawable d=new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(r);
        d.setStroke(1,Color.rgb(55,75,100));
        return d;
    }

    public static View toolbar(Context c,String title){
        LinearLayout bar=new LinearLayout(c);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(14,0,10,0);
        bar.setBackgroundColor(BAR);

        TextView back=text(c,"‹",38,Color.WHITE,Typeface.BOLD);
        TextView name=text(c,title,24,Color.WHITE,Typeface.BOLD);
        TextView refresh=text(c,"↻",30,Color.WHITE,Typeface.BOLD);
        TextView menu=text(c,"⋮",30,Color.WHITE,Typeface.BOLD);

        bar.addView(back,new LinearLayout.LayoutParams(52,-1));
        bar.addView(name,new LinearLayout.LayoutParams(0,-1,1));
        bar.addView(refresh,new LinearLayout.LayoutParams(52,-1));
        bar.addView(menu,new LinearLayout.LayoutParams(36,-1));
        return bar;
    }

    public static Button menuCard(Context c,int icon,String title){
        Button b=new Button(c);
        b.setAllCaps(false);
        b.setText("  "+title);
        b.setTextSize(25);
        b.setTextColor(Color.WHITE);
        b.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        b.setPadding(18,0,8,0);
        b.setBackground(bg(CARD,8));
        if(icon!=0){
            Drawable d=c.getResources().getDrawable(icon);
            d.setBounds(0,0,48,48);
            b.setCompoundDrawables(d,null,null,null);
            b.setCompoundDrawablePadding(14);
        }
        return b;
    }

    public static LinearLayout bottom(Context c){
        LinearLayout nav=new LinearLayout(c);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setBackgroundColor(Color.rgb(5,14,24));
        String[] items={"Ana","Ground","Magnet","V3D","Files","Ayar"};
        for(String s:items){
            TextView t=text(c,s,13,Color.LTGRAY,Typeface.NORMAL);
            t.setGravity(Gravity.CENTER);
            nav.addView(t,new LinearLayout.LayoutParams(0,-1,1));
        }
        return nav;
    }
}
