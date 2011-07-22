
package fr.insalyon.creatis.vip.simulationgui.server;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.util.Random;
import java.util.StringTokenizer;
import vtk.*;

/**
 *
 * @author moulin
 */

public class ObjectFactory {
     private double [] Bounds=new double[6];
     private float xgravit;
     private float ygravit;
     private float zgravit;
     Data3D object;
     Data3D[][] objectTab;
    
        private static ObjectFactory instance;
        
        public static ObjectFactory getInstance() {
            if (instance == null) {
                instance = new ObjectFactory();
            }
            return instance;
        }
        private ObjectFactory()
        {
          loadLibrary();
        }
     public ObjectFactory(String path)
     {       
         if (path.endsWith(".vtp")) {
             VTPBuilder(path);
         } else if (path.endsWith(".mhd")) {
             MHDBuilder(path);
         } else {
             object = null;
         }
     }
     public void addPath(String path, String[][] objectList , String [] type)
     {
         String test="";
         objectTab= new Data3D[objectList.length][1];
         int i=0;
         int j=0;
         
         for (String[] st : objectList)
         { 
             j=0;
             for (String st1d : st)
             {
                 String tmp=st1d.replace("[", "").replace("]", "");
                 if(tmp.endsWith(".zraw")||tmp.endsWith(".raw"))j--;
                 j++;
             }
             objectTab[i]=new Data3D[j+1];
             i++;
         }
         test+="begin i : "+i+" j : "+j;
         i=0;   
         for (String[] st : objectList)
         {
             
             for (String st1d : st)
             {
                 String tmp=st1d.replace("[", "").replace("]", "");
                 if(tmp.endsWith(".zraw")||tmp.endsWith(".raw"))
                 {
                    StringTokenizer st1dTokenize = new StringTokenizer(tmp);
                   addMHD(i,path ,st1dTokenize.nextToken(","), type[i]);                 
                 }
             }
             i++;
         }
         i=0;
         j=1;
         for (String[] st2 : objectList)
         {         
             j=1;
             for (String st2d : st2)
             {
                 String tmp=st2d.replace("[", "").replace("]", "");
                 if(tmp.endsWith(".vtp"))
                 {
                     addVTP(j,i,path,tmp,type[i]);
                     j++;
                 }
             }
             i++;
         }
         test+="ends i : "+i+" j : "+j;
         System.out.println(test);
     }
       private void addMHD(int p,String path,String name, String type )
      {     
            objectTab[p][0]=new Data3D(name);
            objectTab[p][0].setType(type);
            vtkMetaImageReader readerImg = new vtkMetaImageReader();
            readerImg.SetFileName(path+"/"+name);
            readerImg.Update();
            vtkImageData currentImage = new vtkImageData();
            currentImage = readerImg.GetOutput();
            objectTab[p][0].setBoundingBox(currentImage.GetBounds()); 
      }
     private void addVTP(int k,int p,String path,String name, String type)
     {
        objectTab[p][k]=new Data3D(name);
        objectTab[p][k].setType(type);
        vtkPolyDataReader reader = new vtkPolyDataReader();
        reader.SetFileName(path+"/"+name);
        reader.Update();
        vtkPolyData DATA=reader.GetOutput();
        
        xgravit=(float)(objectTab[p][0].getBoundingBox()[1]+objectTab[p][0].getBoundingBox()[0])/2;
        ygravit=(float)(objectTab[p][0].getBoundingBox()[3]+objectTab[p][0].getBoundingBox()[2])/2;
        zgravit=(float)(objectTab[p][0].getBoundingBox()[5]+objectTab[p][0].getBoundingBox()[4])/2;
        
        Random r = new Random(); 
        float R = r.nextFloat();//Returns random float >= 0.0 and < 1.0
        float G = r.nextFloat(); 	
        float B = r.nextFloat();
        
        float[]vertex= new float[DATA.GetNumberOfPoints()*3]; // 3 données par points !
        int[]indices=new int[DATA.GetNumberOfPolys()*3]; // 3 données par lignes
        float[]colors=new float[DATA.GetNumberOfPoints()*4]; // 4 données par lignes : RGB et DETH
        
        vtkPoints point=DATA.GetPoints(); 
        
        int j=0; // compteur
        
        // indices parsor
        for(int i=0;i<(DATA.GetNumberOfPolys()*4);i=i+4)
        {
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+1));
          j++;
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+2));
          j++;
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+3));
          j++;         
        }
        
        // vertex parsor
        j=0;
        for(int i=0;i<DATA.GetNumberOfPoints()*3;i=i+3)
        {
          vertex[i]=((float)point.GetPoint(j)[0])-xgravit;
          vertex[i+1]=((float)point.GetPoint(j)[1])-ygravit;
          vertex[i+2]=((float)point.GetPoint(j)[2])-zgravit;
          j++;
        }
        	
        // color maker
        for(int i=0;i<DATA.GetNumberOfPoints()*4;i=i+4) // pour chaque ligne de vertex on ajoute une couleur !
        {
          colors[i]=r.nextFloat();
          colors[i+1]=r.nextFloat();
          colors[i+2]=r.nextFloat();
          colors[i+3]=1.2f;
        }
        objectTab[p][k].setVertices(vertex);
        objectTab[p][k].setColors(colors); 
        objectTab[p][k].setIndices(indices);
        objectTab[p][k].setBoundingBox(objectTab[p][0].getBoundingBox());
        objectTab[p][k].setNumItemIndex(indices.length);
        
        
        
       // double b[]=new double[]{-2,2,-2,2,-2,2};
       // addToModel(objectTab[p],colors,indices,vertex,objectTab[p].GetBoundingBox(),indices.length);
      
      }
    
     private void VTPBuilder(String path)
     {
        object=new Data3D("model");
        vtkPolyDataReader reader = new vtkPolyDataReader();
        reader.SetFileName(path);
        reader.Update();
        vtkPolyData DATA=reader.GetOutput();
       
        Bounds=DATA.GetBounds(); // boite anglobante ( xmin xmax ymin ymax zmin zmax )
        
        xgravit=(float)(Bounds[1]+Bounds[0])/2;
        ygravit=(float)(Bounds[3]+Bounds[2])/2;
        zgravit=(float)(Bounds[5]+Bounds[4])/2;
        
        Random r = new Random(); 
        float R = r.nextFloat();//Returns random float >= 0.0 and < 1.0
        float G = r.nextFloat(); 	
        float B = r.nextFloat();
        
        float[]vertex= new float[DATA.GetNumberOfPoints()*3]; // 3 données par points !
        int[]indices=new int[DATA.GetNumberOfPolys()*3]; // 3 données par lignes
        float[]colors=new float[DATA.GetNumberOfPoints()*4]; // 4 données par lignes : RGB et DETH
        
        vtkPoints point=DATA.GetPoints(); 
        
        int j=0; // compteur
        
        // indices parsor
        for(int i=0;i<(DATA.GetNumberOfPolys()*4);i=i+4)
        {
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+1));
          j++;
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+2));
          j++;
          indices[j]=(((DATA.GetPolys()).GetData()).GetValue(i+3));
          j++;         
        }
        
        // vertex parsor
        j=0;
        for(int i=0;i<DATA.GetNumberOfPoints()*3;i=i+3)
        {
          vertex[i]=((float)point.GetPoint(j)[0])-xgravit;
          vertex[i+1]=((float)point.GetPoint(j)[1])-ygravit;
          vertex[i+2]=((float)point.GetPoint(j)[2])-zgravit;
          j++;
        }
        	
        // color maker
        for(int i=0;i<DATA.GetNumberOfPoints()*4;i=i+4) // pour chaque ligne de vertex on ajoute une couleur !
        {
          colors[i]=r.nextFloat();
          colors[i+1]=r.nextFloat();
          colors[i+2]=r.nextFloat();
          colors[i+3]=1.2f;
        }
        make(colors,indices,vertex,Bounds,indices.length);
      }
      private void MHDBuilder(String path)
      {
         object=new Data3D("box");
         vtkMetaImageReader readerImg = new vtkMetaImageReader();
         readerImg.SetFileName(path);
         readerImg.Update();
         vtkImageData currentImage = new vtkImageData();
         currentImage = readerImg.GetOutput();
         make(null,null,null,currentImage.GetBounds(),0);
      }
     

      private void make(float[]c,int[]i,float[]v,double[]b,int n)
      {
        object.setColors(c);
        object.setIndices(i);
        object.setVertices(v);
        object.setBoundingBox(b);
        object.setNumItemIndex(n);
      }
      private void addToModel(Data3D data,float[]c,int[]i,float[]v,double[]b,int n)
      {       
                data.setColors(parsor(data.getSupColors(),c,null));
                data.setIndices(mapping(data.getSupIndices(),i,null));
                data.setVertices(parsor(data.getSupVertices(),v,null));
                data.setBoundingBox(b);
                data.setNumItemIndex(data.getSupNumItemIndex()+n);        
      }
      
      
      public Data3D GetObject()
      {
          return object;
      }
       public Data3D[][] GetObjectTab()
      {
          return objectTab;
      }
        public Data3D GetObjectTab(int type,int index)
      {
          return objectTab[type][index];
      }
      private float[] parsor(float[]vert0, float[]vert1, float[]vert2)
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
                    tmp= new float[taille];

                    int k=0;
                    for(int j=0 ; j<V.length;j++)
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
         private  int[] mapping(int indic0[],int indic1[],int indic2[])
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
                   tmp= new int[taille];
                   int max=0;
                   int  k=0;
                    int ad=0;
                    // indicateur !
                    for(int j=0 ; j<I.length;j++)
                    {
                        if(I[j]!=null)
                        {
                            max=0;
                            for(int i=0 ; i<(I[j].length);i++)
                            {
                              tmp[k]=I[j][i]+ad;
                             // System.out.println("tmp["+k+"] : "+tmp[k]);
                              if(tmp[k]>max)max=tmp[k];
                              k++;
                            }
                            ad=max+1;
                            
                        }
                    }               
                    return tmp;
           }
         private void loadLibrary()
         {
            try
            {
              //System.getSecurityManager().checkLink("vtkCommonJava");
              System.loadLibrary("vtkCommonJava");
              System.loadLibrary("vtkFilteringJava");
              System.loadLibrary("vtkIOJava");
              System.loadLibrary("vtkImagingJava");
              System.loadLibrary("vtkGraphicsJava");
              System.loadLibrary("vtkRenderingJava");        
            }
            catch (Exception ex) {
             
           }
             
         }
 
}
