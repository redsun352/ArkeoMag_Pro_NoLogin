package com.lugatek.thubanlodestar.widgets;

import android.content.*;import android.graphics.*;import android.util.*;import android.view.*;

public class ColumnGraphView extends View {
    private final Paint p = new Paint(1), text = new Paint(1), bg = new Paint(1);
    private float x,y,z,delta;
    public ColumnGraphView(Context c){super(c);init();} public ColumnGraphView(Context c, AttributeSet a){super(c,a);init();}
    private void init(){ text.setColor(Color.WHITE); text.setTextSize(22); bg.setColor(Color.rgb(13,22,35)); }
    public void add(float d){ delta=d; invalidate(); }
    public void setAxes(float x,float y,float z,float d){this.x=x;this.y=y;this.z=z;this.delta=d;invalidate();}
    protected void onDraw(Canvas c){ super.onDraw(c); int w=getWidth(),h=getHeight(); c.drawColor(Color.rgb(13,22,35));
        float[] v={x,y,z}; String[] lab={"X","Y","Z"}; int[] col={Color.rgb(255,94,86),Color.rgb(56,222,133),Color.rgb(42,200,237)};
        for(int i=0;i<3;i++){ float cx=w*(i+.5f)/3f; float bh=Math.min(h*.72f, Math.abs(v[i])*3f); p.setColor(col[i]); c.drawRect(cx-28,h-32-bh,cx+28,h-32,p); c.drawText(lab[i]+" "+String.format("%.1f",v[i]),cx-46,h-8,text); }
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(3); p.setColor(Color.argb(140,255,255,255)); c.drawRoundRect(8,8,w-8,h-8,18,18,p); p.setStyle(Paint.Style.FILL);
    }
}
