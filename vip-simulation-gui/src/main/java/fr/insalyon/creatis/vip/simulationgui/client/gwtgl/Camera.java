
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

/**
 *
 * @author moulin
 */
public class Camera {
                
                private int angleX=0;
                private int angleY=0;
                private float translateZ =-3;
                private float normalView =-3;
                private float translateX=0;
                private float translateY=0;
                
                private float stepOfViewScroll=1f;
                private float stepOfViewTranslation=1f;
                private float ladderOfView=1f;
                
                private static Camera instance;
                
                public static Camera getInstance() {
                if (instance == null) {
                     instance = new Camera();
                }
                    return instance;
               }
               private Camera()
               {
                  
               }
               public int getAngleX()
               {
                   return angleX;
               }
               public int getAngleY()
               {
                   return angleY;
               }
               public float getTranslateX()
               {
                   return translateX;
               }
               public float getTranslateY()
               {
                   return translateY;
               }
               public float getTranslateZ()
               {
                   return translateZ;
               }
                public void setAngleX(int x)
               {
                   angleX=x;
                 
               }
               public void setAngleY(int y)
               {
                   angleY=y;
               }
              
               public void setTranslateX(float x)
               {
                   translateX=translateX+x*ladderOfView*stepOfViewTranslation/250;       
               }
               public void setTranslateY(float y)
               {                  
                   translateY=translateY+y*ladderOfView*stepOfViewTranslation/250;
               }
               public void setTranslateZ(float z)
               {
                   translateZ=translateZ-z*ladderOfView*stepOfViewScroll/25;         
               }
               public void setNormalZ(float z)
               {
                   ladderOfView=Math.abs(z/5);
                   normalView=z*2;
                   translateZ=z*2;
               }
               public void setViewToNormalZ()
               {
                   translateZ=normalView;
               }
               public void setStepOfViewTranslation(int t)
               {
                   stepOfViewTranslation=t;  
               }
               public void setStepOfViewScroll(int s)
               {
                   stepOfViewScroll=s;
               }
}
