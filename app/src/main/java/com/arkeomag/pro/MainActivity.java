package com.arkeomag.pro;

import android.Manifest;
import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.bluetooth.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

import com.lugatek.thubanlodestar.widgets.*;
import com.lugatek.thubanlodestar.widgets.opengl.*;

public class MainActivity extends Activity implements SensorHub.Listener {
    private SensorHub hub;
    private LinearLayout root, content, bottomNav;
    private TextView title, sensorBar, pageTitle, values, gpsText;
    private LineGraphView line;
    private ColumnGraphView columns;
    private ModelSurfaceView model;
    private GaugeCalibration gauge;
    private HeatMapView heat;
    private SonarRadarView sonar;
    private final ArrayList<String> csv = new ArrayList<>();
    private int screen = 0;

    private final String[] tabs = {"Menü", "Dedektör", "Kalibrasyon", "Live", "Area", "Sonar", "BT"};

    public void onCreate(Bundle b) {
        super.onCreate(b);
        hub = new SensorHub(this);
        hub.add(this);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            }, 5);
        }
        buildShell();
        showMenu();
    }

    public void onResume() { super.onResume(); hub.start(); }
    public void onPause() { super.onPause(); hub.stop(); }

    private int statusPad() {
        int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return id > 0 ? getResources().getDimensionPixelSize(id) : Utils.dp(this, 24);
    }

    private void buildShell() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Utils.bg);
        root.setPadding(0, statusPad(), 0, 0);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(Utils.dp(this, 8), 0, Utils.dp(this, 8), Utils.dp(this, 6));
        header.setBackgroundColor(Color.rgb(8, 13, 20));
        title = Utils.title(this, "ArkeoMag Pro Classic");
        sensorBar = Utils.tv(this, "Sensör bekleniyor...", 13, Utils.muted);
        header.addView(title);
        header.addView(sensorBar);
        root.addView(header);

        ScrollView sc = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(Utils.dp(this, 10), Utils.dp(this, 8), Utils.dp(this, 10), Utils.dp(this, 10));
        sc.addView(content);
        root.addView(sc, new LinearLayout.LayoutParams(-1, 0, 1));

        bottomNav = new LinearLayout(this);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setPadding(Utils.dp(this, 4), Utils.dp(this, 4), Utils.dp(this, 4), Utils.dp(this, 4));
        bottomNav.setBackgroundColor(Color.rgb(8, 13, 20));
        root.addView(bottomNav, new LinearLayout.LayoutParams(-1, Utils.dp(this, 64)));
        for (int i = 0; i < tabs.length; i++) {
            final int k = i;
            TextView b = Utils.chip(this, tabs[i]);
            b.setOnClickListener(v -> navigate(k));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1, 1);
            lp.setMargins(Utils.dp(this, 2), 0, Utils.dp(this, 2), 0);
            bottomNav.addView(b, lp);
        }
        setContentView(root);
    }

    private void navigate(int k) {
        screen = k;
        if (k == 0) showMenu();
        else if (k == 1) showDetector();
        else if (k == 2) showCalibration();
        else if (k == 3) showLive();
        else if (k == 4) showArea();
        else if (k == 5) showSonar();
        else showBluetooth();
    }

    private void clear(String t) {
        content.removeAllViews();
        pageTitle = Utils.title(this, t);
        content.addView(pageTitle);
    }

    private void showMenu() {
        clear("Ana Menü");
        LinearLayout hero = Utils.card(this);
        TextView h = Utils.tv(this, "No-Login Classic Port", 18, Utils.cyan);
        h.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        hero.addView(h);
        hero.addView(Utils.tv(this, "Eski APK düzenine yakın menü, canlı sensör ve tarama ekranları.", 14, Utils.muted));
        content.addView(hero, cardLp());

        String[] names = {"Dedektör", "Kalibrasyon", "Live Scan", "Area Scan", "Sonar / 3D", "Bluetooth", "CSV Kaydet", "Taban Sıfırla"};
        String[] desc = {"Canlı manyetik anomali", "XY / YZ / XZ sensör kalibrasyonu", "Hareketli çizgi ve yüzey", "Grid / ısı haritası", "Radar halkaları ve hedefler", "Eşleşmiş cihazlar", "Ölçüm dosyası oluştur", "Anlık alanı referans al"};
        for (int i = 0; i < names.length; i += 2) {
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
            row.addView(menuCard(names[i], desc[i], i), new LinearLayout.LayoutParams(0, Utils.dp(this, 120), 1));
            if (i + 1 < names.length) row.addView(menuCard(names[i+1], desc[i+1], i+1), new LinearLayout.LayoutParams(0, Utils.dp(this, 120), 1));
            content.addView(row);
        }
    }

    private LinearLayout.LayoutParams cardLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, Utils.dp(this, 6), 0, Utils.dp(this, 8));
        return lp;
    }

    private View menuCard(String name, String desc, int index) {
        LinearLayout c = Utils.card(this);
        LinearLayout.LayoutParams mlp = (LinearLayout.LayoutParams)c.getLayoutParams();
        TextView n = Utils.tv(this, name, 18, Utils.txt); n.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        TextView d = Utils.tv(this, desc, 12, Utils.muted);
        c.addView(n); c.addView(d);
        c.setOnClickListener(v -> {
            if (index == 0) showDetector(); else if (index == 1) showCalibration(); else if (index == 2) showLive();
            else if (index == 3) showArea(); else if (index == 4) showSonar(); else if (index == 5) showBluetooth();
            else if (index == 6) savePoint(); else { hub.zero(); Toast.makeText(this,"Taban sıfırlandı",Toast.LENGTH_SHORT).show(); }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, Utils.dp(this, 120), 1);
        lp.setMargins(Utils.dp(this, 4), Utils.dp(this, 5), Utils.dp(this, 4), Utils.dp(this, 5));
        c.setLayoutParams(lp);
        return c;
    }

    private void showDetector() {
        clear("Dedektör Modu");
        values = Utils.tv(this, "Bekleniyor...", 16, Utils.txt);
        columns = new ColumnGraphView(this);
        line = new LineGraphView(this);
        model = new ModelSurfaceView(this);
        SeekBar th = new SeekBar(this); th.setMax(70); th.setProgress((int) hub.threshold);
        th.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean f) { hub.threshold = Math.max(1, p); }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button zero = Utils.btn(this, "Taban Sıfırla"); zero.setOnClickListener(v -> hub.zero());
        Button save = Utils.btn(this, "CSV Nokta"); save.setOnClickListener(v -> savePoint());
        actions.addView(zero, new LinearLayout.LayoutParams(0, Utils.dp(this, 58), 1));
        actions.addView(save, new LinearLayout.LayoutParams(0, Utils.dp(this, 58), 1));
        content.addView(values);
        content.addView(columns, new LinearLayout.LayoutParams(-1, Utils.dp(this, 150)));
        content.addView(line, new LinearLayout.LayoutParams(-1, Utils.dp(this, 150)));
        content.addView(model, new LinearLayout.LayoutParams(-1, Utils.dp(this, 320)));
        content.addView(Utils.section(this, "Hassasiyet / alarm eşiği"));
        content.addView(th, new LinearLayout.LayoutParams(-1, Utils.dp(this, 52)));
        content.addView(actions);
    }

    private void showCalibration() {
        clear("Kalibrasyon");
        gauge = new GaugeCalibration(this);
        content.addView(gauge, new LinearLayout.LayoutParams(-1, Utils.dp(this, 330)));
        content.addView(Utils.tv(this, "Telefonu 8 çizerek ve XY / YZ / XZ eksenlerinde yavaşça çevir. Çember düzgün oldukça kalite artar.", 15, Utils.txt));
        Button zero = Utils.btn(this, "Kalibrasyonu Uygula / Tabanı Sıfırla");
        zero.setOnClickListener(v -> hub.zero());
        content.addView(zero, new LinearLayout.LayoutParams(-1, Utils.dp(this, 65)));
    }

    private void showLive() {
        clear("Live Scan");
        line = new LineGraphView(this); model = new ModelSurfaceView(this); values = Utils.tv(this, "Canlı izleniyor...", 15, Utils.txt);
        content.addView(line, new LinearLayout.LayoutParams(-1, Utils.dp(this, 160)));
        content.addView(model, new LinearLayout.LayoutParams(-1, Utils.dp(this, 500)));
        content.addView(values);
    }

    private void showArea() {
        clear("Area Scan");
        heat = new HeatMapView(this); values = Utils.tv(this, "Grid noktaları otomatik doluyor. Nokta kaydet ile CSV’ye aktar.", 15, Utils.txt);
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button clear = Utils.btn(this, "Temizle"); clear.setOnClickListener(v -> heat.clear());
        Button save = Utils.btn(this, "CSV Nokta"); save.setOnClickListener(v -> savePoint());
        actions.addView(clear, new LinearLayout.LayoutParams(0, Utils.dp(this, 58), 1));
        actions.addView(save, new LinearLayout.LayoutParams(0, Utils.dp(this, 58), 1));
        content.addView(heat, new LinearLayout.LayoutParams(-1, Utils.dp(this, 620)));
        content.addView(values); content.addView(actions);
    }

    private void showSonar() {
        clear("Sonar / 3D");
        sonar = new SonarRadarView(this); model = new ModelSurfaceView(this);
        content.addView(sonar, new LinearLayout.LayoutParams(-1, Utils.dp(this, 500)));
        content.addView(model, new LinearLayout.LayoutParams(-1, Utils.dp(this, 360)));
        content.addView(Utils.tv(this, "Radar halkaları, hedef yankıları ve 3D yüzey görünümü. Eski APK'deki ModelSurfaceView mantığına yakın temiz port.", 14, Utils.muted));
    }

    private void showBluetooth() {
        clear("Bluetooth");
        TextView list = Utils.tv(this, "Cihazlar listeleniyor...", 15, Utils.txt);
        content.addView(list);
        try {
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            StringBuilder sb = new StringBuilder();
            if (ba == null) sb.append("Bluetooth yok");
            else {
                if (Build.VERSION.SDK_INT >= 31 && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    sb.append("BLUETOOTH_CONNECT izni gerekli");
                } else {
                    Set<BluetoothDevice> devs = ba.getBondedDevices();
                    if (devs == null || devs.isEmpty()) sb.append("Eşleşmiş cihaz yok");
                    else for (BluetoothDevice d : devs) sb.append(d.getName()).append("\n").append(d.getAddress()).append("\n\n");
                }
            }
            list.setText(sb.toString());
        } catch (Exception e) { list.setText(e.toString()); }
    }

    private void savePoint() {
        csv.add(System.currentTimeMillis()+","+hub.mag[0]+","+hub.mag[1]+","+hub.mag[2]+","+hub.total+","+hub.delta+","+hub.acc[0]+","+hub.acc[1]+","+hub.acc[2]);
        try {
            File f = new File(getExternalFilesDir(null), "arkeomag_scan.csv");
            FileWriter fw = new FileWriter(f);
            fw.write("t,x,y,z,total,delta,acc_x,acc_y,acc_z\n");
            for (String s : csv) fw.write(s+"\n");
            fw.close();
            Toast.makeText(this, "CSV kaydedildi: "+csv.size()+" nokta", Toast.LENGTH_LONG).show();
        } catch(Exception e) { Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show(); }
    }

    public void onSensor(float[] m, float[] a, float[] g, float total, float delta) {
        String s = String.format(Locale.US, "X %.2f  Y %.2f  Z %.2f  |B| %.2f µT  Sapma %.2f µT", m[0], m[1], m[2], total, delta);
        String detail = String.format(Locale.US, "X: %.2f   Y: %.2f   Z: %.2f µT\n|B|: %.2f µT   Sapma: %.2f µT\nAccel: %.1f %.1f %.1f   Gyro: %.2f %.2f %.2f", m[0],m[1],m[2],total,delta,a[0],a[1],a[2],g[0],g[1],g[2]);
        runOnUiThread(() -> {
            sensorBar.setText(s);
            if (values != null) values.setText(detail);
            if (line != null) line.add(delta);
            if (columns != null) columns.setAxes(m[0], m[1], m[2], delta);
            if (model != null) model.setLevel(delta);
            if (gauge != null) gauge.setQuality(Math.min(1f, delta / 35f));
            if (heat != null) heat.add(delta);
            if (sonar != null) sonar.add(delta);
        });
    }
}
