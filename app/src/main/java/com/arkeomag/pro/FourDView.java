package com.arkeomag.pro;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

public class FourDView extends View {
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    float phase = 0;
    ArrayList<Float> frames = new ArrayList<>();

    public FourDView(Context c){ super(c); setBackgroundColor(Color.rgb(4,9,18)); }

    public void add(float v){
        frames.add(v);
        while(frames.size()>180) frames.remove(0);
        phase += 0.06f;
        invalidate();
    }

    int heat(float v){
        v=Math.max(0,Math.min(1,v));
        if(v<0.25f) return Color.rgb(0,40,(int)(160+v*360));
        if(v<0.50f) return Color.rgb(0,(int)(120+v*240),70);
        if(v<0.75f) return Color.rgb((int)(v*260),220,0);
        return Color.rgb(255,(int)(220-(v-0.75f)*500),0);
    }

    float sx(float x,float y,float z,int w,int h){
        return w*0.12f + x*3.9f + y*1.85f;
    }

    float sy(float x,float y,float z,int w,int h){
        return h*0.70f - y*1.15f - z*2.25f;
    }

    protected void onDraw(Canvas c){
        int w=getWidth(), h=getHeight();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(4,9,18));
        c.drawRect(0,0,w,h,p);

        drawTopBar(c,w);
        drawColorScale(c,w);
        drawBox(c,w,h);
        drawSurface(c,w,h);
        drawBlueBody(c,w,h);
        drawLabels(c,w,h);

        phase += 0.015f;
        invalidate();
    }

    void drawTopBar(Canvas c,int w){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(0,115,220));
        c.drawRoundRect(0,0,w,58,0,0,p);

        p.setColor(Color.WHITE);
        p.setTextSize(34);
        c.drawText("‹",22,40,p);

        p.setTextSize(20);
        c.drawText("4D",72,36,p);

        p.setTextSize(32);
        c.drawText("↻",w-92,39,p);
        c.drawText("⋮",w-38,39,p);
    }

    void drawColorScale(Canvas c,int w){
        int left=w/2-150, top=13, bw=300, bh=28;
        for(int i=0;i<bw;i++){
            float v=i/(float)bw;
            p.setColor(heat(v));
            c.drawLine(left+i,top,left+i,top+bh,p);
        }
        p.setColor(Color.WHITE);
        p.setTextSize(18);
        c.drawText("-120",left-62,34,p);
        c.drawText("120",left+bw+12,34,p);
    }

    void drawBox(Canvas c,int w,int h){
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);

        float[][] pts={
            {0,0,-25},{100,0,-25},{100,100,-25},{0,100,-25},
            {0,0,90},{100,0,90},{100,100,90},{0,100,90}
        };

        int[][] e={
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7}
        };

        for(int[] a:e){
            c.drawLine(
                sx(pts[a[0]][0],pts[a[0]][1],pts[a[0]][2],w,h),
                sy(pts[a[0]][0],pts[a[0]][1],pts[a[0]][2],w,h),
                sx(pts[a[1]][0],pts[a[1]][1],pts[a[1]][2],w,h),
                sy(pts[a[1]][0],pts[a[1]][1],pts[a[1]][2],w,h),
                p
            );
        }
    }

    void drawSurface(Canvas c,int w,int h){
        p.setStyle(Paint.Style.FILL);

        for(int y=0;y<90;y+=5){
            Path path=new Path();
            boolean first=true;

            for(int x=0;x<=100;x+=5){
                float z=(float)(
                    18*Math.sin((x*0.07)+phase)+
                    30*Math.cos((y*0.08)+phase*0.7)+
                    55*Math.exp(-Math.pow((x-78)/16.0,2)-Math.pow((y-65)/18.0,2))-
                    35*Math.exp(-Math.pow((x-48)/20.0,2)-Math.pow((y-48)/20.0,2))
                );

                float px=sx(x,y,z,w,h);
                float py=sy(x,y,z,w,h);

                if(first){path.moveTo(px,py);first=false;}
                else path.lineTo(px,py);
            }

            for(int x=100;x>=0;x-=5){
                float z=(float)(
                    18*Math.sin((x*0.07)+phase)+
                    30*Math.cos(((y+5)*0.08)+phase*0.7)+
                    55*Math.exp(-Math.pow((x-78)/16.0,2)-Math.pow(((y+5)-65)/18.0,2))-
                    35*Math.exp(-Math.pow((x-48)/20.0,2)-Math.pow(((y+5)-48)/20.0,2))
                );
                path.lineTo(sx(x,y+5,z,w,h), sy(x,y+5,z,w,h));
            }

            path.close();

            float v=y/90f;
            p.setColor(heat(0.35f+0.45f*(float)Math.sin(v*3.14f+phase*0.3f)));
            c.drawPath(path,p);
        }
    }

    void drawBlueBody(Canvas c,int w,int h){
        float cx=sx(50,55,65,w,h);
        float cy=sy(50,55,65,w,h);

        RadialGradient g=new RadialGradient(
            cx-30,cy-35,145,
            Color.rgb(0,235,255),
            Color.rgb(0,75,180),
            Shader.TileMode.CLAMP
        );

        p.setStyle(Paint.Style.FILL);
        p.setShader(g);

        RectF r=new RectF(cx-95,cy-125,cx+95,cy+105);
        c.drawOval(r,p);
        p.setShader(null);
    }

    void drawLabels(Canvas c,int w,int h){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setTextSize(22);
        c.drawText("X", sx(10,0,-30,w,h), sy(10,0,-30,w,h)+36, p);
        c.drawText("Y", sx(100,85,-30,w,h)+12, sy(100,85,-30,w,h)+20, p);
        c.drawText("Z", 52, 138, p);

        p.setTextSize(18);
        p.setColor(Color.LTGRAY);
        c.drawText("Frame: "+frames.size()+" / 180",24,h-34,p);
    }
}
