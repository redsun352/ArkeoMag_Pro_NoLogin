package com.arkeomag.pro;

import android.content.*;import android.hardware.*;import android.os.*;import java.util.*;

public class SensorHub implements SensorEventListener{
 public interface Listener{void onSensor(float[] mag,float[] acc,float[] gyro,float total,float delta);} 
 private final SensorManager sm; private Sensor magS,accS,gyroS; private final ArrayList<Listener> ls=new ArrayList<>();
 public final float[] mag=new float[3],acc=new float[3],gyro=new float[3],base=new float[3]; public boolean hasBase=false; public float total=0,delta=0,threshold=8;
 private Vibrator vib; private long lastVibe=0;
 public SensorHub(Context c){sm=(SensorManager)c.getSystemService(Context.SENSOR_SERVICE);magS=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);accS=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);gyroS=sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);vib=(Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);} 
 public void add(Listener l){if(!ls.contains(l))ls.add(l);} public void remove(Listener l){ls.remove(l);} 
 public void start(){int d=SensorManager.SENSOR_DELAY_GAME;if(magS!=null)sm.registerListener(this,magS,d);if(accS!=null)sm.registerListener(this,accS,d);if(gyroS!=null)sm.registerListener(this,gyroS,d);} 
 public void stop(){sm.unregisterListener(this);} public void zero(){System.arraycopy(mag,0,base,0,3);hasBase=true;}
 public void onSensorChanged(SensorEvent e){ if(e.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){System.arraycopy(e.values,0,mag,0,3);total=(float)Math.sqrt(mag[0]*mag[0]+mag[1]*mag[1]+mag[2]*mag[2]); if(!hasBase)zero(); float dx=mag[0]-base[0],dy=mag[1]-base[1],dz=mag[2]-base[2]; delta=(float)Math.sqrt(dx*dx+dy*dy+dz*dz); if(delta>threshold&&System.currentTimeMillis()-lastVibe>350){lastVibe=System.currentTimeMillis(); if(vib!=null){ if(Build.VERSION.SDK_INT>=26)vib.vibrate(VibrationEffect.createOneShot(35,VibrationEffect.DEFAULT_AMPLITUDE)); else vib.vibrate(35);}} }
 else if(e.sensor.getType()==Sensor.TYPE_ACCELEROMETER)System.arraycopy(e.values,0,acc,0,3); else if(e.sensor.getType()==Sensor.TYPE_GYROSCOPE)System.arraycopy(e.values,0,gyro,0,3);
 for(Listener l:new ArrayList<>(ls))l.onSensor(mag,acc,gyro,total,delta);} public void onAccuracyChanged(Sensor s,int a){}
}
