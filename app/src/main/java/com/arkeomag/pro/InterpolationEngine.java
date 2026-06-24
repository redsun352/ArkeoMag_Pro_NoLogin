package com.arkeomag.pro; import java.util.*;
public class InterpolationEngine{ public static class P{public float x,y,v; public P(float x,float y,float v){this.x=x;this.y=y;this.v=v;}}
 public static float[][] idw(List<P> pts,int n){float[][] g=new float[n][n]; for(int i=0;i<n;i++)for(int j=0;j<n;j++){float x=i/(float)(n-1),y=j/(float)(n-1),num=0,den=0; for(P p:pts){float dx=x-p.x,dy=y-p.y,d=(float)Math.sqrt(dx*dx+dy*dy)+.03f; float w=1/(d*d); num+=p.v*w; den+=w;} g[i][j]=den==0?0:num/den;} return g;}
}
