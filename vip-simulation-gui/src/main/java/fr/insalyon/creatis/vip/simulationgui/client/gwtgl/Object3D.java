
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;



/**
 *
 * @author moulin
 */
public class Object3D{
               protected int angleX;
               protected int angleY;
               protected int angleZ;
               protected float translateZ;
               protected float translateX;
               protected float translateY;
               protected String id;
               protected Data3D model;
               protected Data3D box;
               protected Data3D axis;
               protected double boundingBox[]= new double[]{-1,1,-1,1,-1,1};
               private void initVertex()
               {
                   
               }
               private void initColors()
               {
                       
               }
               private void initIndices()
               {
                   
               }
            
               public Object3D() 
               {
               
               }
               public Object3D(String contents) 
               {
                id=contents;
               }
               public int getAngleX()
               {
                   return angleX;
               }
               public int getAngleY()
               {
                   return angleY;
               }
               public int getAngleZ()
               {
                   return angleZ;
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
               public float[] getVertices()
               {
                 return parsor(box.getVertices(),axis.getVertices(),model.getVertices());
               }
                public float[] getColors()
               {
                 return parsor(box.getColors(),axis.getColors(),model.getColors());
               }
               public int[] getIndices()
               {
                 return mapping(box.getIndices(),axis.getIndices(),model.getIndices());
               }
               public int getItemSizeColor()
               {
                   return 4;
                           //model.GetItemSizeColor();
               }
               public int getItemSizeVertex()
               {
                   return 3;
                           //model.GetItemSizeVertex();
               }
               public int getNumItemIndex()
               {
                   return box.getNumItemIndex()+axis.getNumItemIndex()+model.getNumItemIndex();
               }
               public double[] getBoundingBox()
               {
                        return boundingBox;
               }   
               public void setAngleX(int x)
               {
                   angleX=x;
               }
               public void setAngleY(int y)
               {
                   angleY=y;
               }
               public void setAngleZ(int z)
               {
                   angleZ=z;
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
               public void setVertices(float[] v, String ID)
               {
                   if("model".equals(ID))model.setVertices(v);
                   if("box".equals(ID))box.setVertices(v);
                   if("axis".equals(ID))axis.setVertices(v);
               }
               public void setColors(float[] c, String ID)
               {
                   if("model".equals(ID))model.setColors(c);
                   if("box".equals(ID))box.setColors(c);
                   if("axis".equals(ID))axis.setColors(c);
               }
               public void setIndices(int[] i, String ID)
               {
                   if("model".equals(ID))model.setIndices(i);
                   if("box".equals(ID))box.setIndices(i);
                   if("axis".equals(ID))axis.setIndices(i);
               }
               
               public void setItemSizeColor(int c, String ID)
               {
                   if("model".equals(ID))model.setItemSizeColor(c);
                   if("box".equals(ID))box.setItemSizeColor(c);
                   if("axis".equals(ID))axis.setItemSizeColor(c);
                          
               }
                 public void setItemSizeVertex(int v, String ID)
               {
                   if("model".equals(ID))model.setItemSizeVertex(v);
                   if("box".equals(ID))box.setItemSizeVertex(v);
                   if("axis".equals(ID))axis.setItemSizeVertex(v);
               }
                  public void setNumItemIndex(int i, String ID)
               {
                   if("model".equals(ID))model.setNumItemIndex(i);
                   if("box".equals(ID))box.setNumItemIndex(i);
                   if("axis".equals(ID))axis.setNumItemIndex(i);
               }
            
               public void reconstructor(Data3D DATA)
               {
                  
               }
                public void resizeVertex()
                {
                         float weight=weighting();
                         float[] vertices=new float[box.getSupVertices().length];
                         for(int i=0;i<box.getSupVertices().length;i++)vertices[i]=box.getSupVertices()[i]*weight;
                          box.setVertices(vertices);
                         
                          vertices = new float[axis.getSupVertices().length];
                          for(int i=0;i<axis.getSupVertices().length;i++)vertices[i]=axis.getSupVertices()[i]*weight;
                          axis.setVertices(vertices);
                          
                          vertices = new float[model.getSupVertices().length];
                          for(int i=0;i<model.getSupVertices().length;i++)vertices[i]=model.getSupVertices()[i]*weight;
                          model.setVertices(vertices);
                  
                }
               public void setBoundingBox(double[] b)
               {
                  boundingBox=b;
                  model.setBoundingBox(b);
                  box.setBoundingBox(b);
                  axis.setBoundingBox(b);
                  initVertex();
               }
               
               public void enable(String ID)
               {
                   if("model".equals(ID))model.enable();
                   if("box".equals(ID))box.enable();
                   if("axis".equals(ID))axis.enable();
               }
               public void disable(String ID)
               {
                   if("model".equals(ID))model.disable();
                   if("box".equals(ID))box.disable();
                   if("axis".equals(ID))axis.disable();
               }
                protected float[] parsor(float[]vert0, float[]vert1, float[]vert2)
                {
                    float[] tmp;
                    float[][]V= new float[3][1];
                    V[0]=vert0;
                    V[1]=vert1;
                    V[2]=vert2;
                    int taille=0;
                    if(V[0]!=null)taille=V[0].length;
                    if(V[1]!=null)taille=taille+V[1].length;
                    if(V[2]!=null)taille=taille+V[2].length;
                    if(V[0]==null&&V[1]==null&&V[1]==null)tmp=null;
                    else tmp= new float[taille];

                    int k=0;
                    for(int j=0 ; j<3;j++)
                    {
                        if(V[j]!=null)    
                        {
                            for(int i=0 ; i<V[j].length;i++)
                            {     
                              tmp[k]=V[j][i];
                              k++;
                            }
                        }
                    }
                    return tmp;
                }
                protected  int[] mapping(int indic0[],int indic1[],int indic2[])
                {
                      int[] tmp;
                      int[][]I= new int[3][1];
                     I[0]=indic0;
                     I[1]=indic1;
                     I[2]=indic2;
                    int taille=0;
                    if(I[0]!=null)taille=I[0].length;
                    if(I[1]!=null)taille=taille+I[1].length;
                    if(I[2]!=null)taille=taille+I[2].length;
                   if(I[0]==null&&I[1]==null&&I[1]==null)tmp=null;
                   else tmp= new int[taille];
                   int max=0;
                   int  k=0;
                    int ad=0;
                    // indicateur !
                    for(int j=0 ; j<3;j++)
                    {
                        if(I[j]!=null)
                        {
                            max=0;
                            for(int i=0 ; i<(I[j].length);i++)
                            {
                              tmp[k]=I[j][i]+ad;
                              if(tmp[k]>max)max=tmp[k];
                              k++;
                            }
                            ad=max+1;
                            
                        }
                    }
                    return tmp;
                }
                protected float weighting()
                {
                    float maxx=(float)boundingBox[1];
                    float minx=(float)boundingBox[0];
                    float maxy=(float)boundingBox[3];
                    float miny=(float)boundingBox[2];
                    float maxz=(float)boundingBox[5];
                    float minz=(float)boundingBox[4];
                    float tmp =maxx-minx;
                    float tmp2 = maxy-miny;
                    if(tmp>=tmp2)
                    {
                        tmp2=maxz-minz;
                        if(tmp>=tmp2)
                        {
                            if(tmp!=0)return tmp;
                            else return 1;
                        }
                        else 
                        {
                            if(tmp2!=0)return tmp2;
                            else return 1;
                        }
                    }
                    else
                    {
                        tmp=maxz-minz;
                        if(tmp>=tmp2)
                        {
                            if(tmp!=0)return tmp;
                            else return 1;
                        }
                        else 
                        {
                            if(tmp2!=0)return tmp2;
                            else return 1;
                        }
                    }
                }
                
}
