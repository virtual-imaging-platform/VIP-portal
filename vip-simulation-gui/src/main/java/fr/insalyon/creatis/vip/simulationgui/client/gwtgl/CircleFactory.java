
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;

/**
 *
 * @author moulin
 */
public class CircleFactory extends Data3D
{
    public CircleFactory(String contents)
    {
                // diameter 1 , 8 points,heigth 10, center on z
                // color blue
        id=contents;
        boundingBox = new double[]{-1,1,-1,1,-5,5};
        itemSizeVertex=3;
        itemSizeColor=4;
        double pi=Math.PI;
        enable=false;
        vertices=new float[4*3*8];
        colors=new float[4*4*8];
        indices=new int[8*6];
        numItemIndex=indices.length;
        int k=0;
        for(int i=0;i<vertices.length;i=i+12)
        { 
           
           vertices[i]=(float) Math.cos(((2*pi)/8)*k);
           vertices[i+1]=(float) Math.sin(((2*pi)/8)*k);
           vertices[i+2]=-2;
           k++;
           vertices[i+3]=(float) Math.cos(((2*pi)/8)*k);
           vertices[i+4]=(float) Math.sin(((2*pi)/8)*k);
           vertices[i+5]=-2;
           
           vertices[i+6]=(float) Math.cos(((2*pi)/8)*(k));
           vertices[i+7]=(float) Math.sin(((2*pi)/8)*(k));
           vertices[i+8]=2;
           
           vertices[i+9]=(float) Math.cos(((2*pi)/8)*(k-1));
           vertices[i+10]=(float) Math.sin(((2*pi)/8)*(k-1));
           vertices[i+11]=2;
        }
        for(int i=0;i<colors.length;i=i+4)
        {
            colors[i]=0.6f;   //R
            colors[i+1]=1.0f; //G
            colors[i+2]=0.6f; //B
            colors[i+3]=0.8f; // clear
        }
        k=0;
        for(int i=0;i<indices.length;i=i+6)
        {
            indices[i]=k;
            k++;
            indices[i+1]=k;
            k++;
            indices[i+2]=k;
            indices[i+3]=k-2;
            indices[i+4]=k;
            k++;
            indices[i+5]=k;
            k++;        
        }
        
    }
    public Data3D getObject()
    {
        return this;
    }
}
