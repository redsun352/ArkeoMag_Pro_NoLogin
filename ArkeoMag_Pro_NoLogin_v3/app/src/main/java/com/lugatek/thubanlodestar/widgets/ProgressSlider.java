package com.lugatek.thubanlodestar.widgets; import android.content.*;import android.util.*;import android.widget.*;
public class ProgressSlider extends SeekBar{ public ProgressSlider(Context c){super(c);} public ProgressSlider(Context c,AttributeSet a){super(c,a);setMax(100);setProgress(30);} }
