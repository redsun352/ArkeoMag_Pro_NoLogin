package com.arkeomag.pro;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import java.util.*;

public class GraphView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ArrayList<Float> mag = new ArrayList<>();
    private final ArrayList<Float> delta = new ArrayList<>();
    private final int maxPoints = 220;

    public GraphView(Context c) {
        super(c);
        setBackgroundColor(Color.rgb(7, 15, 28));
    }

    public void add(float magValue, float deltaValue) {
        mag.add(magValue);
        delta.add(deltaValue);
        while (mag.size() > maxPoints) mag.remove(0);
        while (delta.size() > maxPoints) delta.remove(0);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int w = getWidth();
        int h = getHeight();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(10, 24, 42));
        c.drawRoundRect(0, 0, w, h, 22, 22, p);

        drawGrid(c, w, h);
        drawLine(c, mag, Color.rgb(70, 190, 255), w, h, "MAG");
        drawLine(c, delta, Color.rgb(255, 196, 64), w, h, "DELTA");

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(26);
        p.setColor(Color.WHITE);
        c.drawText("Canlı Manyetik Grafik", 22, 34, p);

        p.setTextSize(20);
        p.setColor(Color.rgb(70, 190, 255));
        c.drawText("MAG", w - 150, 34, p);
        p.setColor(Color.rgb(255, 196, 64));
        c.drawText("DELTA", w - 90, 34, p);
    }

    private void drawGrid(Canvas c, int w, int h) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        p.setColor(Color.argb(55, 255, 255, 255));

        for (int i = 1; i < 5; i++) {
            float y = h * i / 5f;
            c.drawLine(0, y, w, y, p);
        }

        for (int i = 1; i < 8; i++) {
            float x = w * i / 8f;
            c.drawLine(x, 0, x, h, p);
        }
    }

    private void drawLine(Canvas c, ArrayList<Float> data, int color, int w, int h, String label) {
        if (data.size() < 2) return;

        float min = Collections.min(data);
        float max = Collections.max(data);
        if (Math.abs(max - min) < 1f) {
            max += 1f;
            min -= 1f;
        }

        Path path = new Path();

        for (int i = 0; i < data.size(); i++) {
            float x = i * (w - 20f) / (maxPoints - 1f) + 10f;
            float norm = (data.get(i) - min) / (max - min);
            float y = h - 18f - norm * (h - 58f);

            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        p.setColor(color);
        c.drawPath(path, p);

        float last = data.get(data.size() - 1);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(19);
        p.setColor(color);
        c.drawText(label + ": " + String.format(Locale.US, "%.1f", last), 20, h - ("MAG".equals(label) ? 34 : 10), p);
    }
}
