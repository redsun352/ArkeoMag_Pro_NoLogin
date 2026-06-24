package com.arkeomag.pro;

import android.content.*;import android.hardware.*;import java.util.*;

public class SensorEngine implements SensorEventListener{
 public interface Listener{void onSensor(float x,float y,float z,float total,float anomaly,float tilt);}
 private final SensorManager sm; private final List<Listener> ls=new ArrayList<>();
 public float x,y,z,total,baseline=0,anomaly=0,tilt=0; private float ax,ay,az; public boolean calibrated=false;
 public SensorEngine(Context c){sm=(SensorManager)c.getSystemService(Context.SENSOR_SERVICE);} public void add(Listener l){ls.add(l);} 
 public void start(){Sensor m=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);Sensor a=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); if(m!=null)sm.registerListener(this,m,SensorManager.SENSOR_DELAY_GAME); if(a!=null)sm.registerListener(this,a,SensorManager.SENSOR_DELAY_GAME);} 
 public void stop(){sm.unregisterListener(this);} public void calibrate(){baseline=total; calibrated=true;}
 public void onSensorChanged(SensorEvent e){if(e.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){x=e.values[0];y=e.values[1];z=e.values[2];total=(float)Math.sqrt(x*x+y*y+z*z); if(!calibrated&&baseline==0)baseline=total; anomaly=Math.abs(total-baseline);} if(e.sensor.getType()==Sensor.TYPE_ACCELEROMETER){ax=e.values[0];ay=e.values[1];az=e.values[2];tilt=(float)Math.toDegrees(Math.atan2(Math.sqrt(ax*ax+ay*ay),az));} for(Listener l:ls)l.onSensor(x,y,z,total,anomaly,tilt);} public void onAccuracyChanged(Sensor s,int a){}
}
