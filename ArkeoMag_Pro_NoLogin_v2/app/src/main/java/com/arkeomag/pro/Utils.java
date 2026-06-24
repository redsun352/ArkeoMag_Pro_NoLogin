package com.arkeomag.pro; import android.graphics.*; import android.graphics.drawable.*; import android.view.*; import android.widget.*; import android.content.*;
public class Utils{ static int bg=Color.rgb(16,21,28),card=Color.rgb(25,31,41),line=Color.rgb(62,76,93),txt=Color.rgb(235,241,248),muted=Color.rgb(143,154,169),cyan=Color.rgb(0,220,255),orange=Color.rgb(255,177,68); 
 static TextView tv(Context c,String s,int sp,int col){TextView v=new TextView(c);v.setText(s);v.setTextSize(sp);v.setTextColor(col);v.setPadding(14,8,14,8);return v;} 
 static TextView title(Context c,String s){TextView v=tv(c,s,22,txt);v.setTypeface(Typeface.DEFAULT_BOLD);return v;} 
 static Button btn(Context c,String s){Button b=new Button(c);b.setText(s);b.setTextColor(Color.WHITE);b.setAllCaps(false);b.setBackgroundColor(Color.rgb(41,91,117));return b;} 
 static LinearLayout card(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(12,12,12,12);GradientDrawable g=new GradientDrawable();g.setColor(card);g.setStroke(2,line);g.setCornerRadius(18);l.setBackground(g);return l;} }
