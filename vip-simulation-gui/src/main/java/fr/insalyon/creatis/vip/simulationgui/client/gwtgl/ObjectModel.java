
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.util.HashMap;

/**
 *
 * @author moulin
 */

public class ObjectModel extends Object3D  {
                
                static private ObjectModel instance;
                private Data3D multiModel[][];
                private  HashMap<String,Data3D> elementMap;
                public static ObjectModel getInstance() {
                if (instance == null) {
                     instance = new ObjectModel();
                }
                    return instance;
               }
                private ObjectModel()
                 {
                     boundingBox = new double[]{0,1,0,1,0,1};
                     model=new Data3D("model");
                     box=new Data3D("box");
                     axis=new Data3D("axis");
                     multiModel=new Data3D[1][2];
                     initIndices();
                     initVertex();
                     initColors();
                    
                     angleX = 0;
                     angleY=0;
                     angleZ=0;
                     translateZ = 0;
                     translateX=0;
                     translateY=0;
                   
                     box.setItemSizeVertex(3);//itemSizeVertex=3;
                     box.setItemSizeColor(4);//itemSizeColor=4;
                     box.setNumItemIndex(36); //numItemIndex=36;
                     
                     axis.setItemSizeVertex(3);//itemSizeVertex=3;
                     axis.setItemSizeColor(4);//itemSizeColor=4;
                     axis.setNumItemIndex(108);//numItemIndex=108;
                     
                     id="model";
                     
                     box.enable();
                     axis.enable();

                 }
                private void initVertex()
                {
                    float maxx=((float)boundingBox[1]-(float)boundingBox[0])/2;
                    float minx=((float)boundingBox[1]-(float)boundingBox[0])/2;
                    float maxy=((float)boundingBox[3]-(float)boundingBox[2])/2;
                    float miny=((float)boundingBox[3]-(float)boundingBox[2])/2;
                    float maxz=((float)boundingBox[5]-(float)boundingBox[4])/2;
                    float minz=((float)boundingBox[5]-(float)boundingBox[4])/2;
                    
                    
                    if(maxx<0)maxx=-maxx;
                    if(maxy<0)maxy=-maxy;
                    if(maxz<0)maxz=-maxz;
                    if(minx<0)minx=-minx;
                    if(miny<0)miny=-miny;
                    if(minz<0)minz=-minz;
                       float vertices[] = new float[] {                  
                    // X direction
                 // Front face             numéro de sommet du cube-> numéro de sommet figure
                 1.0f*maxx,  1.0f*maxy,  -1.0f*minz,     //0 ->0
                 1.0f*maxx,  1.0f*maxy,  1.0f*maxz,      //1 ->1
                 -1.0f*minx,  1.0f*maxy,  1.0f*maxz,     //2 ->2
                 -1.0f*minx, 1.0f*maxy,  -1.0f*minz,     //3 ->3
                 
                // Back face
                 1.0f*maxx,  -1.0f*miny,  -1.0f*minz,    //4 ->4
                 1.0f*maxx,  -1.0f*miny,  1.0f*maxz,     //5 ->5
                 -1.0f*minx,  -1.0f*miny,  1.0f*maxz,    //6 ->6
                 -1.0f*minx, -1.0f*miny,  -1.0f*minz,    //7 ->7
                
                // Top face
                 1.0f*maxx,  1.0f*maxy,  -1.0f*minz,     //0 ->8
                 1.0f*maxx,  1.0f*maxy,  1.0f*maxz,      //1 ->9
                 1.0f*maxx,  -1.0f*miny,  1.0f*maxz,     //5 ->10
                 1.0f*maxx,  -1.0f*miny,  -1.0f*minz,    //4 ->11
                
                // Bottom face
                -1.0f*minx, 1.0f*maxy,  -1.0f*minz,      //3 ->12
                -1.0f*minx,  1.0f*maxy,  1.0f*maxz,      //2 ->13
                -1.0f*minx,  -1.0f*miny,  1.0f*maxz,     //6 ->14
                -1.0f*minx, -1.0f*miny,  -1.0f*minz,     //7 ->15
                 
                 
                 // Right face
                  1.0f*maxx,  1.0f*maxy,  1.0f*maxz,     //1 ->16
                  -1.0f*minx,  1.0f*maxy,  1.0f*maxz,    //2 ->17
                  -1.0f*minx,  -1.0f*miny,  1.0f*maxz,   //6 ->18
                  1.0f*maxx,  -1.0f*miny,  1.0f*maxz,    //5 ->19
                  
                  // Left face
                  1.0f*maxx,  1.0f*maxy,  -1.0f*minz,    //0 ->20
                 -1.0f*minx, 1.0f*maxy,  -1.0f*minz,     //3 ->21
                 -1.0f*minx, -1.0f*miny,  -1.0f*minz,    //7 ->22
                 1.0f*maxx,  -1.0f*miny,  -1.0f*minz     //4 ->23
                 };
                 box.setVertices(vertices);
                 
                       vertices = new float[] {                  
                    // X direction
                 // Front face             numéro de sommet du cube-> numéro de sommet figure
                 0.0f,  0.0f,  0.0f,       //0 ->0
                 0.0f,  0.0f,  0.02f,      //1 ->1
                 0.0f,  -0.02f,  0.02f,    //2 ->2
                 0.0f,  -0.02f,  0.0f,     //3 ->3
                 
                // Back face
                 1.0f,  0.0f,  0.0f,       //4 ->4
                 1.0f,  0.0f,  0.02f,      //5 ->5
                 1.0f,  -0.02f,  0.02f,    //6 ->6
                 1.0f,  -0.02f,  0.0f,     //7 ->7
                
                // Top face
                 0.0f,  0.0f,  0.0f,       //0 ->8
                 0.0f,  0.0f,  0.02f,      //1 ->9
                  1.0f,  0.0f,  0.02f,     //5 ->10
                 1.0f,  0.0f,  0.0f,       //4 ->11
                
                // Bottom face
                 0.0f,  -0.02f,  0.0f,     //3 ->12
                 0.0f,  -0.02f,  0.02f,    //2 ->13
                 1.0f,  -0.02f,  0.02f,    //6 ->14
                 1.0f,  -0.02f,  0.0f,     //7 ->15
                 
                 
                 // Right face
                  0.0f,  0.0f,  0.02f,     //1 ->16
                  0.0f,  -0.02f,  0.02f,   //2 ->17
                  1.0f,  -0.02f,  0.02f,   //6 ->18
                  1.0f,  0.0f,  0.02f,     //5 ->19
                  
                  // Left face
                  0.0f,  0.0f,  0.0f,      //0 ->20
                  0.0f, -0.02f,  0.0f,     //3 ->21
                  1.0f,-0.02f, 0.0f,       //7 ->22
                  1.0f,  0.0f,  0.0f,      //4 ->23
                   
                      // Y direction
                 // Front face  
                 0.0f,  0.0f,  0.0f,       //0 ->24
                 -0.02f,  0.0f,  0.0f,     //1 ->25
                 -0.02f,  0.0f,  0.02f,    //2 ->26
                 0.0f,   0.0f,  0.02f,     //3 ->27
                 
                // Back face
                  0.0f,  1.0f,  0.0f,      //4 ->28
                 -0.02f,  1.0f,  0.0f,     //5 ->29
                 -0.02f,  1.0f,  0.02f,    //6 ->30
                  0.0f,   1.0f,  0.02f,    //7 ->31
                
                // Top face
                0.0f,  0.0f,  0.0f,        //0 ->32
                 -0.02f,  0.0f,  0.0f,     //1 ->33
                 -0.02f,  1.0f,  0.0f,     //5 ->34
                 0.0f,  1.0f,  0.0f,       //4 ->35
                
                // Bottom face
                  0.0f,   0.0f,  0.02f,    //3 ->36
                 -0.02f,  0.0f,  0.02f,    //2 ->37
                 -0.02f,  1.0f,  0.02f,    //6 ->38
                  0.0f,   1.0f,  0.02f,    //7 ->39
                 
                 
                 // Right face
                   -0.02f,  0.0f,  0.0f,   //1 ->40
                  -0.02f,  0.0f,  0.02f,   //2 ->41
                  -0.02f,  0.5f,  0.02f,   //6 ->42
                 -0.02f,  1.0f,  0.0f,     //5 ->43
                  
                  // Left face
                    0.0f,  0.0f,  0.0f,    //0 ->44
                   0.0f,   0.0f,  0.02f,   //3 ->45
                   0.0f,   1.0f,  0.02f,   //7 ->46
                   0.0f,  1.0f,  0.0f,     //4  >47
                        
                         // Z direction
                 // Front face  
                  0.0f,  0.0f,  0.0f,      //0 ->48
                 0.0f,  -0.02f,  0.0f,     //1 ->49
                 -0.02f,  -0.02f,  0.0f,   //2 ->50
                 -0.02f,   0.0f,  0.0f,    //3 ->51
                 
                // Back face
                  0.0f,  0.0f,  1.0f,      //4 ->52
                  0.0f,  -0.02f,  1.0f,    //5 ->53
                 -0.02f,  -0.02f,  1.0f,   //6 ->54
                  -0.02f,   0.0f,  1.0f,   //7 ->55
                
                // Top face
                 0.0f,  0.0f,  0.0f,       //0 ->56
                 0.0f,  -0.02f,  0.0f,     //1 ->57
                 0.0f,  -0.02f, 1.0f,      //5 ->58
                 0.0f,  0.0f,  1.0f,       //4 ->59
                
                // Bottom face
                 -0.02f,   0.0f,  0.0f,    //3 ->60
                 -0.02f,  -0.02f,  0.0f,   //2 ->61
                 -0.02f,  -0.02f,  1.0f,   //6 ->62
                 -0.02f,   0.0f,  1.0f,    //7 ->63
                 
                 
                 // Right face
                   0.0f,  -0.02f,  0.0f,    //1  ->64
                  -0.02f,  -0.02f,  0.0f,   //2 ->65
                  -0.02f,  -0.02f,  1.0f,   //6 ->66
                   0.0f,  -0.02f,  1.0f,    //5 ->67
                  
                  // Left face
                 0.0f,  0.0f,    0.0f,      //0  ->68
                 -0.02f,   0.0f,  0.0f,     //3 ->69
                 -0.02f,   0.0f, 1.0f,      //7 ->70
                  0.0f,  0.0f,  1.0f        //4  ->71
                 };  
                 axis.setVertices(vertices);
                 resizeVertex();
                }
                private void initColors()
                {
                     float colors[] = new float[] { 
                                // X direction
                      // Front face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        // Back face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        // Top face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        // Bottom face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        // Right face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        // Left face
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f,
                        0.0f, 0.0f, 1.0f, 0.3f, 
                };
                 box.setColors(colors);
                 
                   colors = new float[] { 
                                // X direction
                      // Front face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        // Back face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        // Top face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                       // Bottom face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        // Right face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        // Left face
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                           // Y direction
                      // Front face
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        // Back face
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        // Top face
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                       // Bottom face
                         0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        // Right face
                         0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        // Left face
                         0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                            // Z direction
                      // Front face
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        // Back face
                         1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                         // Top face
                         1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        // Bottom face
                          1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                         // Right face
                          1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                         // Left face
                          1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f
                };
                  axis.setColors(colors); 
                }
                private void initIndices()
                {
                        int indices[] = new int[] {
                        0, 1, 2,  0, 2, 3,    //x
                                           
                        4, 5, 6,  4, 6, 7,
                                           
                        8, 9, 10,   8, 10, 11,
                                          
                        12, 13, 14,  12, 14, 15,
                                           
                        16, 17, 18,  16, 18, 19,
                                           
                        20, 21, 22,   20, 22, 23,                     
                   };
                    box.setIndices(indices);
                    
                       indices = new int[] {
                        0, 1, 2,  0, 2, 3,    //x
                                           
                        4, 5, 6,  4, 6, 7,
                                           
                        8, 9, 10,   8, 10, 11,
                                          
                        12, 13, 14,  12, 14, 15,
                                           
                        16, 17, 18,  16, 18, 19,
                                           
                        20, 21, 22,   20, 22, 23, // ->y
                                           
                        24, 25, 26,  24, 26, 27,
                                           
                        28, 29, 30,  28, 30, 31,
                                           
                        32, 33, 34,  32, 34, 35,
                                          
                        36, 37, 38,  36, 38, 39,
                                           
                        40, 41, 42,  40, 42, 43,
                                           
                        44, 45, 46,   44, 46, 47, // ->z
                        
                        48, 49, 50,  48, 50, 51,
                                           
                        52, 53, 54,  52, 54, 55,
                                           
                        56, 57, 58,  56, 58, 59,
                                          
                        60, 61, 62,  60, 62, 63,
                                           
                        64, 65, 66,  64, 66, 67,
                                           
                         68, 69, 70,   68, 70, 71
                                           
                   };
                    axis.setIndices(indices);
                }
                
                public void reconstructor()
                {
      
                    float[]tmp=null;
                    float[]tmp2=null;
                    int[]tmp3=null;
                    int tmp4=0;
                    for(int j=0;j<multiModel.length;j++)
                    {
                        for(int i=0;i<multiModel[j].length;i++)
                        {
                            if(multiModel[j][i].isEnable()){
                            tmp=parsor(tmp,multiModel[j][i].getVertices(),null);
                            tmp2=parsor(tmp2,multiModel[j][i].getColors(),null);
                            tmp3=mapping(tmp3,multiModel[j][i].getIndices(),null);
                            tmp4+=multiModel[j][i].getNumItemIndex();
                            }
                        }
                   }       
                    model.setVertices(tmp);
                    model.setColors(tmp2);  
                    model.setIndices(tmp3);
                    model.setNumItemIndex(tmp4);
                }
                public void addModel(Data3D[][] DATA)
                {
                    elementMap = new HashMap<String,Data3D>();
                    multiModel=DATA;
                    enableAndMapAllElement();
                    reconstructor();
                    model.enable();
                    setBoundingBox(DATA[0][0].getBoundingBox());                   
                    Scene.getInstance().refreshBuffer();
                    Scene.getInstance().changeCameraView(DATA[0][0].getBoundingBox());
                    Scene.getInstance().refreshScreen();
                    SC.say("load succes, number of vertex : " +model.getNumItemIndex()+" [x min : "+model.getBoundingBox()[0]+"mm x max : "+model.getBoundingBox()[1]+"mm] [y min : "+model.getBoundingBox()[2]+"mm y max : "+model.getBoundingBox()[3]+"mm] [z min : "+model.getBoundingBox()[4]+"mm z max : "+model.getBoundingBox()[5]+"mm]");
     
                }
                private void enableAndMapAllElement()
                {
                     for(int j=0;j<multiModel.length;j++)
                    {
                        for(int i=0;i<multiModel[j].length;i++)
                        {
                            multiModel[j][i].enable();
                            elementMap.put(multiModel[j][i].getID(), multiModel[j][i]);
                        }
                    }
                }
                 public void setBoundingBox(double[] b)
               {
                  boundingBox=b;
                  model.setBoundingBox(b);
                  box.setBoundingBox(b);
                  axis.setBoundingBox(b);
                  initVertex();
               }
               public void resizeVertex()
                {
                         float weight=weighting();          
                         float[] vertices = new float[axis.getSupVertices().length];
                          for(int i=0;i<axis.getSupVertices().length;i++)vertices[i]=axis.getSupVertices()[i]*weight;
                          axis.setVertices(vertices); 
                }
                public int getNumItemIndex()
               {
                   return box.getNumItemIndex()+axis.getNumItemIndex()+model.getNumItemIndex();
               }
               public double[] getBoundingBox()
               {
                        return boundingBox;
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
               public void unsetElement(ListGridRecord []e)
               {    
                       for(int j=0;j<multiModel.length;j++)
                       {
                            for(int i=0;i<multiModel[j].length;i++)
                            {
                               multiModel[j][i].disable();
                            }
                       }
                       
                        for(ListGridRecord e1:e)
                        {                                    
                           if(elementMap.get(e1.getAttribute("ElementId"))!=null)elementMap.get(e1.getAttribute("ElementId")).enable();                                    
                        }                         
                       reconstructor();
                       Scene.getInstance().refreshBuffer();
                       Scene.getInstance().refreshScreen();
               }
}
