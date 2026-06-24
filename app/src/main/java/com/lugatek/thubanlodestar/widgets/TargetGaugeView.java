package com.lugatek.thubanlodestar.widgets;

import android.content.*;import android.graphics.*;import android.util.*;import android.view.*;

public class TargetGaugeView extends View{
    private final Paint p=new Paint(1), arc=new Paint(1), bg=new Paint(1), txt=new Paint(1), small=new Paint(1);
    private float level=0,total=0;
    public TargetGaugeView(Context c){super(c);init();} public TargetGaugeView(Context c,AttributeSet a){super(c,a);init();}
    private void init(){
        arc.setStyle(Paint.Style.STROKE); arc.setStrokeCap(Paint.Cap.ROUND); arc.setStrokeWidth(28);
        bg.setStyle(Paint.Style.STROKE); bg.setStrokeCap(Paint.Cap.ROUND); bg.setStrokeWidth(28); bg.setColor(Color.rgb(41,54,74));
        txt.setColor(Color.WHITE); txt.setTextAlign(Paint.Align.CENTER); txt.setTypeface(Typeface.DEFAULT_BOLD); txt.setTextSize(38);
        small.setColor(Color.rgb(150,162,180)); small.setTextAlign(Paint.Align.CENTER); small.setTextSize(18);
    }
    public void setValues(float total, float delta){this.total=total;this.level=delta;invalidate();}
    private int color(){float t=Math.min(1,level/36f); if(t<.35f)return Color.rgb(44,210,138); if(t<.70f)return Color.rgb(255,205,70); return Color.rgb(255,90,75);} 
    protected void onDraw(Canvas c){
        int w=getWidth(),h=getHeight(); c.drawColor(Color.rgb(13,22,35)); float cx=w/2f, cy=h*.78f; float r=Math.min(w*.42f,h*.68f);
        RectF rr=new RectF(cx-r,cy-r,cx+r,cy+r); c.drawArc(rr,200,140,false,bg); arc.setColor(color()); c.drawArc(rr,200,Math.min(140,level/45f*140f),false,arc);
        p.setColor(color()); p.setStyle(Paint.Style.FILL); float a=(float)Math.toRadians(200+Math.min(140,level/45f*140f)); c.drawCircle(cx+(float)Math.cos(a)*r,cy+(float)Math.sin(a)*r,12,p);
        c.drawText(String.format("%.1f µT",level),cx,h*.54f,txt); c.drawText("ANOMALİ SAPMASI",cx,h*.68f,small); c.drawText(String.format("|B| %.1f µT",total),cx,h*.86f,small);
    }
}
