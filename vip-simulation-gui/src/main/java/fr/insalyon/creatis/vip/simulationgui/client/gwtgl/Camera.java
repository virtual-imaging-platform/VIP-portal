
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
                   translateX=x;
                   
               }
               public void setTranslateY(float y)
               {
                   translateY=y;
                   
               }
               public void setTranslateZ(float z)
               {
                   translateZ=z;
                  
               }
               public void setNormalZ(float z)
               {
                   normalView=z;
                   translateZ=z;
               }
               public void setViewToNormalZ()
               {
                   translateZ=normalView;
               }
               
}
