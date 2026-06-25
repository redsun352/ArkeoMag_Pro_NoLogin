package com.arkeomag.pro;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

public class FourDView extends View {
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    float rot = 0, zoom = 1f, phase = 0;
    ArrayList<Float> frames = new ArrayList<>();

    public FourDView(Context c){ super(c); setBackgroundColor(Color.rgb(4,8,14)); }

    public void add(float v){
        frames.add(v);
        while(frames.size()>240) frames.remove(0);
        phase += 0.045f;
        invalidate();
    }

    int heat(float v){
        v=Math.max(0,Math.min(1,v));
        if(v<.20f) return Color.rgb(0,20,180);
        if(v<.40f) return Color.rgb(0,180,255);
        if(v<.60f) return Color.rgb(0,220,30);
        if(v<.80f) return Color.rgb(255,230,0);
        return Color.rgb(255,40,0);
    }

    float sx(float x,float y,float z,int w,int h){
        float cx=w*.50f;
        float a=(float)Math.cos(rot), b=(float)Math.sin(rot);
        float xr=(x-50)*a-(y-50)*b;
        float yr=(x-50)*b+(y-50)*a;
        return cx + zoom*(xr*4.2f + yr*1.6f);
    }

    float sy(float x,float y,float z,int w,int h){
        float cy=h*.60f;
        float a=(float)Math.cos(rot), b=(float)Math.sin(rot);
        float yr=(x-50)*b+(y-50)*a;
        return cy + zoom*(yr*.95f - z*2.3f);
    }

    @Override
    protected void onDraw(Canvas c){
        int w=getWidth(),h=getHeight();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(4,8,14));
        c.drawRect(0,0,w,h,p);

        drawHeader(c,w);
        drawBox(c,w,h);
        drawSurface(c,w,h);
        drawTargetVolume(c,w,h);
        drawAxis(c,w,h);
        drawFooter(c,w,h);

        rot += .0025f;
        phase += .01f;
        invalidate();
    }

    void drawHeader(Canvas c,int w){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(0,125,230));
        c.drawRect(0,0,w,58,p);

        p.setColor(Color.WHITE);
        p.setTextSize(34);
        c.drawText("‹",20,40,p);

        p.setTextSize(20);
        c.drawText("4D",70,36,p);

        int left=w/2-155;
        for(int i=0;i<310;i++){
            p.setColor(heat(i/310f));
            c.drawLine(left+i,15,left+i,43,p);
        }

        p.setColor(Color.WHITE);
        p.setTextSize(17);
        c.drawText("-120",left-55,36,p);
        c.drawText("120",left+320,36,p);

        p.setTextSize(30);
        c.drawText("↻",w-95,39,p);
        c.drawText("⋮",w-38,39,p);
    }

    void drawBox(Canvas c,int w,int h){
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);

        float[][] q={
            {0,0,-30},{100,0,-30},{100,100,-30},{0,100,-30},
            {0,0,90},{100,0,90},{100,100,90},{0,100,90}
        };

        int[][] e={
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7}
        };

        for(int[] a:e){
            c.drawLine(
                sx(q[a[0]][0],q[a[0]][1],q[a[0]][2],w,h),
                sy(q[a[0]][0],q[a[0]][1],q[a[0]][2],w,h),
                sx(q[a[1]][0],q[a[1]][1],q[a[1]][2],w,h),
                sy(q[a[1]][0],q[a[1]][1],q[a[1]][2],w,h),
                p
            );
        }
    }

    float zval(float x,float y){
        float bump=(float)(75*Math.exp(-Math.pow((x-80)/12.0,2)-Math.pow((y-65)/18.0,2)));
        float pit=(float)(-65*Math.exp(-Math.pow((x-48)/18.0,2)-Math.pow((y-48)/20.0,2)));
        float wave=(float)(22*Math.sin(x*.08+phase)+18*Math.cos(y*.11+phase*.7));
        float last=frames.size()>0?frames.get(frames.size()-1):0;
        return wave+bump+pit+(last*.25f);
    }

    void drawSurface(Canvas c,int w,int h){
        p.setStyle(Paint.Style.FILL);

        for(int y=0;y<95;y+=5){
            for(int x=0;x<100;x+=5){
                float z1=zval(x,y);
                float z2=zval(x+5,y);
                float z3=zval(x+5,y+5);
                float z4=zval(x,y+5);

                Path path=new Path();
                path.moveTo(sx(x,y,z1,w,h),sy(x,y,z1,w,h));
                path.lineTo(sx(x+5,y,z2,w,h),sy(x+5,y,z2,w,h));
                path.lineTo(sx(x+5,y+5,z3,w,h),sy(x+5,y+5,z3,w,h));
                path.lineTo(sx(x,y+5,z4,w,h),sy(x,y+5,z4,w,h));
                path.close();

                float avg=(z1+z2+z3+z4)/4f;
                p.setColor(heat((avg+120)/240f));
                c.drawPath(path,p);
            }
        }
    }

    void drawTargetVolume(Canvas c,int w,int h){
        float cx=sx(52,54,60,w,h);
        float cy=sy(52,54,60,w,h);

        RadialGradient g=new RadialGradient(
            cx-35,cy-45,130,
            Color.rgb(0,245,255),
            Color.rgb(0,70,180),
            Shader.TileMode.CLAMP
        );

        p.setShader(g);
        p.setStyle(Paint.Style.FILL);
        c.drawOval(new RectF(cx-90,cy-125,cx+95,cy+95),p);
        p.setShader(null);
    }

    void drawAxis(Canvas c,int w,int h){
        p.setColor(Color.WHITE);
        p.setTextSize(24);
        c.drawText("X",sx(8,0,-34,w,h),sy(8,0,-34,w,h)+34,p);
        c.drawText("Y",sx(100,90,-34,w,h),sy(100,90,-34,w,h)+28,p);
        c.drawText("Z",45,135,p);
    }

    void drawFooter(Canvas c,int w,int h){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.LTGRAY);
        p.setTextSize(18);
        c.drawText("Frame: "+frames.size()+" / 240",24,h-58,p);
        c.drawText("STAR/V3D görünüm: yüzey + izohacim + zaman",24,h-30,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction()==MotionEvent.ACTION_MOVE){
            rot += e.getX()/getWidth()*0.015f;
            zoom = Math.max(.65f,Math.min(1.45f,zoom + (e.getY()<getHeight()/2?-0.005f:0.005f)));
            invalidate();
        }
        return true;
    }
}
