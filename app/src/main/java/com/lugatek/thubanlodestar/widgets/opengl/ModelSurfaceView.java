package com.lugatek.thubanlodestar.widgets.opengl;
import android.content.*;import android.graphics.*;import android.util.*;import android.view.*;import java.util.*;
public class ModelSurfaceView extends View{
    Paint p=new Paint(1),grid=new Paint(1),txt=new Paint(1),axis=new Paint(1); float level=0; ArrayList<Float> hist=new ArrayList<>();
    public ModelSurfaceView(Context c){super(c);init();} public ModelSurfaceView(Context c,AttributeSet a){super(c,a);init();}
    void init(){p.setStyle(Paint.Style.FILL);grid.setStyle(Paint.Style.STROKE);grid.setStrokeWidth(1.3f);grid.setColor(Color.rgb(58,75,98));txt.setColor(Color.WHITE);txt.setTypeface(Typeface.DEFAULT_BOLD);txt.setTextSize(25);axis.setColor(Color.rgb(28,205,235));axis.setStrokeWidth(3);} 
    public void setLevel(float l){level=l;hist.add(l);while(hist.size()>90)hist.remove(0);invalidate();}
    int color(float v){float t=Math.min(1f,v/40f); if(t<.35)return Color.rgb(38,205,160); if(t<.7)return Color.rgb(255,205,70); return Color.rgb(255,88,80);} 
    protected void onDraw(Canvas c){
        c.drawColor(Color.rgb(8,13,22));int w=getWidth(),h=getHeight();float ox=w*.12f, oy=h*.76f; float sx=w*.74f, sy=h*.50f;
        // pseudo 3D grid
        for(int i=0;i<=8;i++){float x=ox+sx*i/8f; c.drawLine(x,oy,x+sx*.20f,oy-sy,grid);}
        for(int j=0;j<=7;j++){float y=oy-sy*j/7f; c.drawLine(ox,y,ox+sx,y,grid); c.drawLine(ox+sx,y,ox+sx+sx*.20f,y-sy,grid);} 
        c.drawLine(ox,oy,ox+sx,oy,axis); c.drawLine(ox,oy,ox+sx*.20f,oy-sy,axis);
        // bars / voxel columns from history
        int n=Math.min(hist.size(),18); float bw=sx/22f;
        for(int i=0;i<n;i++){float v=hist.get(hist.size()-n+i); float bh=Math.min(sy*.72f, v/42f*sy*.72f); float x=ox+sx*(i+2)/22f; float y=oy-bh; p.setColor(color(v)); c.drawRoundRect(x,y,x+bw,oy,8,8,p); p.setColor(Color.argb(85,255,255,255)); c.drawRoundRect(x,y,x+bw,y+5,8,8,p);} 
        // target sphere
        float r=Math.min(w,h)*(0.045f+Math.min(1,level/40f)*0.085f); p.setColor(color(level)); c.drawCircle(ox+sx*.63f,oy-sy*.53f,r,p); p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);p.setColor(Color.argb(135,255,255,255));c.drawCircle(ox+sx*.63f,oy-sy*.53f,r*1.55f,p);p.setStyle(Paint.Style.FILL);
        c.drawText("3D ANOMALİ YÜZEYİ",20,34,txt); txt.setTypeface(Typeface.DEFAULT); txt.setTextSize(21); c.drawText(String.format("Sapma %.1f µT",level),20,h-22,txt); txt.setTypeface(Typeface.DEFAULT_BOLD); txt.setTextSize(25);
    }
}
