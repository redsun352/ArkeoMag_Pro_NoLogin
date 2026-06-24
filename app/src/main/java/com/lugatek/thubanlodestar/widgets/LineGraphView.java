package com.lugatek.thubanlodestar.widgets;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class LineGraphView extends View {
    private final Paint line = new Paint(1), glow = new Paint(1), grid = new Paint(1), text = new Paint(1);
    private final ArrayList<Float> vals = new ArrayList<>();
    public LineGraphView(Context c) { super(c); init(); }
    public LineGraphView(Context c, AttributeSet a) { super(c, a); init(); }
    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        line.setColor(Color.rgb(37, 232, 197)); line.setStrokeWidth(4); line.setStyle(Paint.Style.STROKE);
        glow.setColor(Color.argb(90, 37, 232, 197)); glow.setStrokeWidth(12); glow.setStyle(Paint.Style.STROKE);
        grid.setColor(Color.rgb(45, 60, 79)); grid.setStrokeWidth(1);
        text.setColor(Color.rgb(220,230,240)); text.setTextSize(22);
    }
    public void add(float v) { vals.add(v); while (vals.size() > 180) vals.remove(0); invalidate(); }
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int w = getWidth(), h = getHeight();
        c.drawColor(Color.rgb(13, 22, 35));
        for (int i=1;i<4;i++) c.drawLine(0, h*i/4f, w, h*i/4f, grid);
        for (int i=1;i<8;i++) c.drawLine(w*i/8f, 0, w*i/8f, h, grid);
        c.drawText("Canlı Sinyal", 16, 28, text);
        if (vals.size() < 2) return;
        float max = 8f;
        for (float v: vals) max = Math.max(max, Math.abs(v));
        Path p = new Path();
        for (int i=0;i<vals.size();i++) {
            float x = w * (i / (float)(vals.size()-1));
            float y = h * .62f - (vals.get(i) / max) * (h * .42f);
            if (i==0) p.moveTo(x,y); else p.lineTo(x,y);
        }
        c.drawPath(p, glow);
        c.drawPath(p, line);
    }
}
