
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;



/**
 *
 * @author moulin
 */
public class ObjectSimulateur extends Object3D {
               
                public ObjectSimulateur(String contents)
                 {
                   boundingBox=new double[]{-1,1,-1,1,-1,1};
                   model=new Data3D("model");
                   box=new Data3D("aoe");
                   axis=new Data3D("axis");  
                   id=contents;
                   
                   angleX=0;
                   angleY=0;
                   angleZ=0;
                   translateZ = 0;
                   translateX=0;
                   translateY=0;
                   axis.setItemSizeVertex(3);//itemSizeVertex=3;
                   axis.setItemSizeColor(4);//itemSizeColor=4;
                   axis.setNumItemIndex(108);//numItemIndex=108;
                   if(id.equals("US"))
                   {
                    box.setItemSizeVertex(3);//itemSizeVertex=3;
                    box.setItemSizeColor(4);//itemSizeColor=4;
                    box.setNumItemIndex(6);//numItemIndex=108;
                    
                   }
                   else 
                   {
                       box=new CircleFactory("aoe");
                       model.setItemSizeVertex(3);
                       model.setItemSizeColor(4);
                       model.setNumItemIndex(108);
                   }
                   initIndices();
                   initVertex();
                   initColors();
                   axis.enable();
                 }
                 private void initIndices()
                {
                   int indices[] = new int[] {
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
                   
                   if(id.equals("US"))
                  {
                  indices = new int[] {
                        0, 1, 2,  0, 2, 3   
                      
                   };
                   box.setIndices(indices);
                  }
                  if(id.equals("US"))
                  {
                 
                 }
                 else
                 {
                      indices = new int[] {
                        // Front face
                        0, 8, 9,  0, 1, 8,
                        1, 2, 8,  7, 2, 8,
                        2, 3, 7,  3, 4, 7,
                        4, 5, 7,  5, 7, 6,
                        
                        // Bottom face
                        0, 10, 11,  0, 11, 1,
                        1, 11, 12,  1, 12, 2,
                        2, 12, 13,  2, 13, 3,
                        3, 13, 14,  3, 14, 4,
                        4, 14, 15,  4, 15, 5,

                        9, 19, 10,  9, 10, 0,
                        5, 15, 16,  5, 16, 6,
                       
                        // Left face
                        6, 16, 17,  6, 17, 7,
                        
                        //Top face
                        7, 17, 18,  7, 18, 8,
                        
                        //Rigth face
                        8, 18, 19,  8, 19, 9,
                       
                        //Back face
                        10, 18, 19,  10, 11, 18,
                        11, 12, 18,  17, 12, 18,
                        12, 13, 17,  13, 14, 17,
                        14, 15, 17,  15, 17, 16,  
                   };   
                      model.setIndices(indices);
                 }
                 
                  
                   
                }
                private void initColors()
                {
                    
                     float colors[] = new float[] { 
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
                  if(id.equals("US"))
                  {
                      colors = new float[] { 
                                // X direction
                      // Front face
                        0.5f, 0.5f, 1.0f, 1.0f,
                        0.5f, 0.5f, 1.0f, 1.0f,
                        0.5f, 0.5f, 1.0f, 1.0f,
                        0.5f, 0.5f, 1.0f, 1.0f,
                     };
                     box.setColors(colors);
                  }
                  if(id.equals("US"))
                  {
                      
                     
                  }
                  else
                  {
                      colors = new float[] { 
                                // X direction
                      // Front face
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                      
                        //Bottom face  
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                                             
                        //Left face
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        // Top face
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        // Right face
                        
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                       
                        // Back face
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        0.62f, 0.7f, 0.8f, 1.0f,
                        
                        
                     };
                     model.setColors(colors);
                  }
                  
                }
                private void initVertex()
                {
                    
                    
                  float vertices[] = new float[] {                  
                    // X direction
                 // Front face             numéro de sommet du cube-> numéro de sommet figure
                 0.0f,  0.0f,  0.0f,       //0 ->0
                 0.0f,  0.0f,  0.02f,      //1 ->1
                 0.0f,  -0.02f,  0.02f,    //2 ->2
                 0.0f,  -0.02f,  0.0f,     //3 ->3
                 
                // Back face
                 0.5f,  0.0f,  0.0f,       //4 ->4
                 0.5f,  0.0f,  0.02f,      //5 ->5
                 0.5f,  -0.02f,  0.02f,    //6 ->6
                 0.5f,  -0.02f,  0.0f,     //7 ->7
                
                // Top face
                 0.0f,  0.0f,  0.0f,       //0 ->8
                 0.0f,  0.0f,  0.02f,      //1 ->9
                  0.5f,  0.0f,  0.02f,     //5 ->10
                 0.5f,  0.0f,  0.0f,       //4 ->11
                
                // Bottom face
                 0.0f,  -0.02f,  0.0f,     //3 ->12
                 0.0f,  -0.02f,  0.02f,    //2 ->13
                 0.5f,  -0.02f,  0.02f,    //6 ->14
                 0.5f,  -0.02f,  0.0f,     //7 ->15
                 
                 
                 // Right face
                  0.0f,  0.0f,  0.02f,     //1 ->16
                  0.0f,  -0.02f,  0.02f,   //2 ->17
                  0.5f,  -0.02f,  0.02f,   //6 ->18
                  0.5f,  0.0f,  0.02f,     //5 ->19
                  
                  // Left face
                  0.0f,  0.0f,  0.0f,      //0 ->20
                  0.0f, -0.02f,  0.0f,     //3 ->21
                  0.5f,-0.02f, 0.0f,       //7 ->22
                   0.5f,  0.0f,  0.0f,     //4 ->23
                   
                      // Y direction
                 // Front face  
                 0.0f,  0.0f,  0.0f,       //0 ->24
                 -0.02f,  0.0f,  0.0f,     //1 ->25
                 -0.02f,  0.0f,  0.02f,    //2 ->26
                 0.0f,   0.0f,  0.02f,     //3 ->27
                 
                // Back face
                  0.0f,  0.5f,  0.0f,      //4 ->28
                 -0.02f,  0.5f,  0.0f,     //5 ->29
                 -0.02f,  0.5f,  0.02f,    //6 ->30
                  0.0f,   0.5f,  0.02f,    //7 ->31
                
                // Top face
                0.0f,  0.0f,  0.0f,        //0 ->32
                 -0.02f,  0.0f,  0.0f,     //1 ->33
                 -0.02f,  0.5f,  0.0f,     //5 ->34
                 0.0f,  0.5f,  0.0f,       //4 ->35
                
                // Bottom face
                  0.0f,   0.0f,  0.02f,    //3 ->36
                 -0.02f,  0.0f,  0.02f,    //2 ->37
                 -0.02f,  0.5f,  0.02f,    //6 ->38
                  0.0f,   0.5f,  0.02f,    //7 ->39
                 
                 
                 // Right face
                   -0.02f,  0.0f,  0.0f,   //1 ->40
                  -0.02f,  0.0f,  0.02f,   //2 ->41
                  -0.02f,  0.5f,  0.02f,   //6 ->42
                 -0.02f,  0.5f,  0.0f,     //5 ->43
                  
                  // Left face
                    0.0f,  0.0f,  0.0f,    //0 ->44
                   0.0f,   0.0f,  0.02f,   //3 ->45
                   0.0f,   0.5f,  0.02f,   //7 ->46
                   0.0f,  0.5f,  0.0f,     //4  >47
                        
                         // Z direction
                 // Front face  
                  0.0f,  0.0f,  0.0f,      //0 ->48
                 0.0f,  -0.02f,  0.0f,     //1 ->49
                 -0.02f,  -0.02f,  0.0f,   //2 ->50
                 -0.02f,   0.0f,  0.0f,    //3 ->51
                 
                // Back face
                  0.0f,  0.0f,  0.5f,      //4 ->52
                  0.0f,  -0.02f,  0.5f,    //5 ->53
                 -0.02f,  -0.02f,  0.5f,   //6 ->54
                  -0.02f,   0.0f,  0.5f,   //7 ->55
                
                // Top face
                 0.0f,  0.0f,  0.0f,       //0 ->56
                 0.0f,  -0.02f,  0.0f,     //1 ->57
                 0.0f,  -0.02f, 0.5f,      //5 ->58
                 0.0f,  0.0f,  0.5f,       //4 ->59
                
                // Bottom face
                 -0.02f,   0.0f,  0.0f,    //3 ->60
                 -0.02f,  -0.02f,  0.0f,   //2 ->61
                 -0.02f,  -0.02f,  0.5f,   //6 ->62
                 -0.02f,   0.0f,  0.5f,    //7 ->63
                 
                 
                 // Right face
                   0.0f,  -0.02f,  0.0f,   //1  ->64
                  -0.02f,  -0.02f,  0.0f,  //2 ->65
                  -0.02f,  -0.02f,  0.5f,  //6 ->66
                   0.0f,  -0.02f,  0.5f,   //5 ->67
                  
                  // Left face
                 0.0f,  0.0f,    0.0f,    //0  ->68
                 -0.02f,   0.0f,  0.0f,   //3 ->69
                 -0.02f,   0.0f, 0.5f,    //7 ->70
                  0.0f,  0.0f,  0.5f      //4  ->71
                 };                  
                  
                  axis.setVertices(vertices);
                  
                  if(id.equals("US"))
                  {
                          vertices = new float[] {                  
                            // X direction
                         // Front face             numéro de sommet du cube-> numéro de sommet figure
                         0.0f,  0.0f,  0.0f,      //0 ->0
                         0.5f,  0.0f,  0.0f,      //1 ->1
                         0.5f,  0.5f,  0.0f,      //2 ->2
                         0.0f,  0.5f,  0.0f,       //3 ->3
                         };                                  
                         box.setVertices(vertices);
                  }
                  else
                   {
                       boolean tmp=box.isEnable();
                       box= new CircleFactory("aoe");
                       if(tmp)box.enable();
                       else box.disable();
                   }
       
                  if(id.equals("US"))
                  {
                          vertices = new float[] {                  
                            // X direction
                         // Front face             numéro de sommet du cube-> numéro de sommet figure
                         0.0f,  0.0f,  0.0f,      //0 ->0
                         0.5f,  0.0f,  0.0f,      //1 ->1
                         0.5f,  0.5f,  0.0f,      //2 ->2
                         0.0f,  0.5f,  0.0f,      //3 ->3
                         };                                  
                         model.setVertices(vertices);
                  }
                  else
                   {
                       double pi=Math.PI;
                        vertices = new float[] {                  
                            // X direction
                         // Front face             numéro de sommet du cube-> numéro de sommet figure
                         (float)Math.cos((0*(2*pi)/10)),(float)Math.sin((0*(2*pi)/10)),0.5f,  //0 ->0
                         (float)Math.cos((1*(2*pi)/10)),(float)Math.sin((1*(2*pi)/10)),0.5f,  //1 ->1
                         (float)Math.cos((2*(2*pi)/10)),(float)Math.sin((2*(2*pi)/10)),0.5f,  //2 ->2
                         (float)Math.cos((3*(2*pi)/10)),(float)Math.sin((3*(2*pi)/10)),0.5f,  //3 ->3
                         (float)Math.cos((4*(2*pi)/10)),(float)Math.sin((4*(2*pi)/10)),0.5f,  //4 ->4
                         (float)Math.cos((5*(2*pi)/10)),(float)Math.sin((5*(2*pi)/10)),0.5f,  //5 ->5
                         -2f, 0.0f, 0.5f,                                                   //6 ->6
                         -2f, 1.5f, 0.5f,                                                   //7 ->7
                          2f, 1.5f, 0.5f,                                                   //8 ->8
                          2f, 0.0f, 0.5f,                                                   //9 ->9
                         (float)Math.cos((0*(2*pi)/10)),(float)Math.sin((0*(2*pi)/10)),-0.5f, //0 ->10
                         (float)Math.cos((1*(2*pi)/10)),(float)Math.sin((1*(2*pi)/10)),-0.5f, //1 ->11
                         (float)Math.cos((2*(2*pi)/10)),(float)Math.sin((2*(2*pi)/10)),-0.5f, //2 ->12
                         (float)Math.cos((3*(2*pi)/10)),(float)Math.sin((3*(2*pi)/10)),-0.5f, //3 ->13
                         (float)Math.cos((4*(2*pi)/10)),(float)Math.sin((4*(2*pi)/10)),-0.5f, //4 ->14
                         (float)Math.cos((5*(2*pi)/10)),(float)Math.sin((5*(2*pi)/10)),-0.5f, //5 ->15
                         -2f, 0.0f, -0.5f,                                                  //6 ->16
                         -2f, 1.5f, -0.5f,                                                  //7 ->17
                          2f, 1.5f, -0.5f,                                                  //8 ->18
                          2f, 0.0f, -0.5f                                                   //9 ->19
                        };                                  
                         model.setVertices(vertices);
                   }
                  resizeVertex();
            }
                 public void setBoundingBox(double[] b)
               {
                  boundingBox=b;
                  model.setBoundingBox(b);
                  box.setBoundingBox(b);
                  axis.setBoundingBox(b);
                  initVertex();
               }

}
