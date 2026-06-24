package com.lugatek.thubanlodestar.widgets;

import android.content.*;import android.graphics.*;import android.util.*;import android.view.*;import java.util.*;

public class HeatMapView extends View {
    private final Paint p=new Paint(1), grid=new Paint(1), txt=new Paint(1);
    private final ArrayList<Float> samples=new ArrayList<>();
    private int cols=12, rows=18;
    public HeatMapView(Context c){super(c);init();} public HeatMapView(Context c,AttributeSet a){super(c,a);init();}
    private void init(){grid.setColor(Color.rgb(55,70,92));grid.setStrokeWidth(1);txt.setColor(Color.WHITE);txt.setTextSize(24);}
    public void add(float v){samples.add(v);while(samples.size()>cols*rows)samples.remove(0);invalidate();}
    public void clear(){samples.clear();invalidate();}
    private int color(float v){float t=Math.min(1f,v/35f); if(t<.33f) return Color.rgb(25,(int)(120+250*t),220); if(t<.66f) return Color.rgb((int)(250*t),220,70); return Color.rgb(255,(int)(210-180*t),45);}
    protected void onDraw(Canvas c){int w=getWidth(),h=getHeight();c.drawColor(Color.rgb(9,14,22));float cw=w/(float)cols,ch=h/(float)rows; for(int r=0;r<rows;r++){for(int col=0;col<cols;col++){int idx=r*cols+col;if(idx<samples.size()){p.setColor(color(samples.get(idx)));c.drawRect(col*cw+2,r*ch+2,(col+1)*cw-2,(r+1)*ch-2,p);}}}
        for(int i=0;i<=cols;i++)c.drawLine(i*cw,0,i*cw,h,grid);for(int i=0;i<=rows;i++)c.drawLine(0,i*ch,w,i*ch,grid);c.drawText("AREA GRID / ISI HARİTASI",16,32,txt);}
}
