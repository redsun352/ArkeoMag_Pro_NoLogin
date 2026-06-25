package com.arkeomag.pro;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

public class FourDView extends View {
    Paint p = new Paint(1);
    ArrayList<Float> frames = new ArrayList<>();
    float phase = 0;

    public FourDView(Context c){ super(c); setBackgroundColor(Color.rgb(5,10,20)); }

    public void add(float v){
        frames.add(v);
        while(frames.size()>160) frames.remove(0);
        phase += 0.08f;
        invalidate();
    }

    int heatColor(float v){
        v=Math.max(0,Math.min(1,v));
        if(v<0.25f) return Color.rgb(0,70,(int)(120+v*500));
        if(v<0.50f) return Color.rgb(0,(int)(120+v*220),80);
        if(v<0.75f) return Color.rgb((int)(v*255),200,0);
        return Color.rgb(255,(int)(180-(v-0.75f)*500),0);
    }

    protected void onDraw(Canvas c){
        int w=getWidth(), h=getHeight();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(6,14,26));
        c.drawRect(0,0,w,h,p);

        p.setTextSize(28);
        p.setColor(Color.WHITE);
        c.drawText("4D Zamanlı 3D Yüzey",24,42,p);

        int cols=18, rows=12;
        float cw=w/(float)cols;
        float ch=(h-90)/(float)rows;

        for(int y=0;y<rows;y++){
            for(int x=0;x<cols;x++){
                float t=(float)Math.sin((x*0.45f)+(y*0.35f)+phase);
                float history = frames.size()>0 ? frames.get(frames.size()-1)/35f : 0;
                float v=Math.max(0,Math.min(1,(t+1)/2f*0.45f+history));
                p.setColor(heatColor(v));
                float px=x*cw;
                float py=70+y*ch-(v*28);
                c.drawRoundRect(px+2,py+2,px+cw-2,py+ch-2,8,8,p);
            }
        }

        p.setTextSize(22);
        p.setColor(Color.LTGRAY);
        c.drawText("Zaman akışı: " + frames.size() + " frame",24,h-22,p);

        invalidate();
    }
}
