package com.lugatek.thubanlodestar.widgets;

import android.content.*;import android.graphics.*;import android.util.*;import android.view.*;import java.util.*;

public class SonarRadarView extends View {
    private final Paint p=new Paint(1), ring=new Paint(1), sweep=new Paint(1), txt=new Paint(1);
    private final ArrayList<Float> targets=new ArrayList<>(); private float angle=0, level=0;
    public SonarRadarView(Context c){super(c);init();} public SonarRadarView(Context c,AttributeSet a){super(c,a);init();}
    private void init(){ring.setColor(Color.rgb(58,80,102));ring.setStyle(Paint.Style.STROKE);ring.setStrokeWidth(2);sweep.setColor(Color.argb(90,35,220,180));p.setStyle(Paint.Style.FILL);txt.setColor(Color.WHITE);txt.setTextSize(25);}
    public void add(float v){level=v; targets.add(v); while(targets.size()>40)targets.remove(0); angle=(angle+7)%360; invalidate();}
    protected void onDraw(Canvas c){int w=getWidth(),h=getHeight();c.drawColor(Color.rgb(8,13,22));float cx=w/2f,cy=h/2f;float R=Math.min(w,h)*.43f; for(int i=1;i<=4;i++)c.drawCircle(cx,cy,R*i/4f,ring);c.drawLine(cx-R,cy,cx+R,cy,ring);c.drawLine(cx,cy-R,cx,cy+R,ring);c.drawArc(cx-R,cy-R,cx+R,cy+R,angle-26,26,true,sweep);
        for(int i=0;i<targets.size();i++){float a=(angle-i*11)*(float)Math.PI/180f;float r=R*(.2f+Math.min(1,targets.get(i)/38f)*.75f);int alpha=Math.max(45,220-i*5);p.setColor(Color.argb(alpha,255,200,70));c.drawCircle(cx+(float)Math.cos(a)*r,cy+(float)Math.sin(a)*r,7+Math.min(16,targets.get(i)/3),p);}c.drawText("SONAR SCAN",18,34,txt);c.drawText(String.format("%.1f µT",level),18,h-24,txt);} }
