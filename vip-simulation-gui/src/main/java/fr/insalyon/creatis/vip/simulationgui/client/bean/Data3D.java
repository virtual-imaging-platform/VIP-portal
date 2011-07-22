
package fr.insalyon.creatis.vip.simulationgui.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author moulin
 */

public class Data3D implements IsSerializable{
               
               protected double [] boundingBox;
               protected int itemSizeVertex;
               protected int itemSizeColor;
               protected int numItemIndex;
               protected String id;
               protected String type;
               protected float[] vertices;
               protected float[] colors;
               protected int[] indices;
               protected boolean enable;
               public Data3D() 
               {
                 
               }
               public Data3D(String ID) 
               {
                   boundingBox = new double[]{-1,1,-1,1,-1,1};
                   itemSizeVertex=0;
                   itemSizeColor=0;
                   numItemIndex=0;
                   vertices=null;
                   colors=null;
                   indices=null;
                   id=ID;
                   enable=false;
               }
               public float[] getSupVertices()
               {
                   return vertices;
               }
                public float[] getSupColors()
               {
                 return colors;
                 
               }
               public int[] getSupIndices()
               {
                    return indices;
                    
               }
               public float[] getVertices()
               {
                  if(enable)return vertices;
                  else return null;
               }
                public float[] getColors()
               {
                  if(enable)return colors;
                  else return null;
               }
               public int[] getIndices()
               {
                    if(enable)return indices;
                    else return null;
               }
               public int getItemSizeColor()
               {
                    return itemSizeColor;
               }
                 public int getItemSizeVertex()
               {
                    return itemSizeVertex;
               }
               public int getSupNumItemIndex()
               {       
                   return numItemIndex;
               }
                  public int getNumItemIndex()
               {       
                   if(enable)return numItemIndex;
                   else return 0;
               }
               public double[] getBoundingBox()
               {
                        return boundingBox;
               }   
               public String getID()
               {
                   return id;
               }
               public String getType()
               {
                   return type;
               }
               public void setVertices(float[] v)
               {
                   vertices=v;
               }
               public void setColors(float[] c)
               {
                   colors=c;
               }
               public void setIndices(int[] i)
               {
                   indices=i;
               }
               
               public void setItemSizeColor(int c)
               {
                       itemSizeColor=c;   
               }
                 public void setItemSizeVertex(int v)
               {
                      itemSizeVertex=v;
               }
                  public void setNumItemIndex(int i)
               {
                     numItemIndex=i;
               }
            
              public void reconstructor(Data3D DATA)
               {
                  
               }
              public void setBoundingBox(double[] box)
               {
                   boundingBox=box;                   
               }
              public void setType(String typ)
               {
                   type=typ;                   
               }
              public void enable()
              {
                  enable=true;
              }
              public void disable()
              {
                  enable=false;
              }
              public boolean isEnable()
              {
                  return enable;
              }
              public boolean isDisable()
              {
                  return !enable;
              }
             
}
