/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUIControlBoxModel;
import java.util.HashMap;

/**
 *
 * @author Kevin Moulin
 */
public class ObjectModel extends Object3D {

    static private ObjectModel instance;
    private Data3D multiModel[][];
    private HashMap<String, Data3D> elementMap;
    private HashMap<Integer, Data3D> opaqueMap;
    private HashMap<Integer, Data3D> transparentMap;

    public static ObjectModel getInstance() {
        if (instance == null) {
            instance = new ObjectModel();
        }
        return instance;
    }

    private ObjectModel() {

        boundingBox = new double[]{0, 1, 0, 1, 0, 1};
        model = new Data3D("model");
        box = new Data3D("box");
        axis = new Data3D("axis");
        multiModel = new Data3D[1][2];
        initIndices();
        initVertex();
        initColors();

        angleX = 0;
        angleY = 0;
        angleZ = 0;
        translateZ = 0;
        translateX = 0;
        translateY = 0;

        box.setItemSizeVertex(3);//itemSizeVertex=3;
        box.setItemSizeColor(4);//itemSizeColor=4;
        box.setNumItemIndex(36); //numItemIndex=36;

        axis.setItemSizeVertex(3);//itemSizeVertex=3;
        axis.setItemSizeColor(4);//itemSizeColor=4;
        axis.setNumItemIndex(108);//numItemIndex=108;                 

        id = "model";

        box.enable();
        axis.enable();
        buildNormals();
    }

    private void initVertex() {

        float maxx = ((float) boundingBox[1] - (float) boundingBox[0]) / 2;
        float minx = ((float) boundingBox[1] - (float) boundingBox[0]) / 2;
        float maxy = ((float) boundingBox[3] - (float) boundingBox[2]) / 2;
        float miny = ((float) boundingBox[3] - (float) boundingBox[2]) / 2;
        float maxz = ((float) boundingBox[5] - (float) boundingBox[4]) / 2;
        float minz = ((float) boundingBox[5] - (float) boundingBox[4]) / 2;

        if (maxx < 0) {
            maxx = -maxx;
        }
        if (maxy < 0) {
            maxy = -maxy;
        }
        if (maxz < 0) {
            maxz = -maxz;
        }
        if (minx < 0) {
            minx = -minx;
        }
        if (miny < 0) {
            miny = -miny;
        }
        if (minz < 0) {
            minz = -minz;
        }
        float vertices[] = new float[]{
            // Front face
            -1.0f * minx, -1.0f * miny, 1.0f * maxz,
            1.0f * maxx, -1.0f * miny, 1.0f * maxz,
            1.0f * maxx, 1.0f * maxy, 1.0f * maxz,
            -1.0f * minx, 1.0f * maxy, 1.0f * maxz,
            // Back face
            -1.0f * minx, -1.0f * miny, -1.0f * minz,
            -1.0f * minx, 1.0f * maxy, -1.0f * minz,
            1.0f * maxx, 1.0f * maxy, -1.0f * minz,
            1.0f * maxx, -1.0f * miny, -1.0f * minz,
            // Top face
            -1.0f * minx, 1.0f * maxy, -1.0f * minz,
            -1.0f * minx, 1.0f * maxy, 1.0f * maxz,
            1.0f * maxx, 1.0f * maxy, 1.0f * maxz,
            1.0f * maxx, 1.0f * maxy, -1.0f * minz,
            // Bottom face
            -1.0f * minx, -1.0f * miny, -1.0f * minz,
            1.0f * maxx, -1.0f * miny, -1.0f * minz,
            1.0f * maxx, -1.0f * miny, 1.0f * maxz,
            -1.0f * minx, -1.0f * miny, 1.0f * maxz,
            // Right face
            1.0f * maxx, -1.0f * miny, -1.0f * minz,
            1.0f * maxx, 1.0f * maxy, -1.0f * minz,
            1.0f * maxx, 1.0f * maxy, 1.0f * maxz,
            1.0f * maxx, -1.0f * miny, 1.0f * maxz,
            // Left face
            -1.0f * minx, -1.0f * miny, -1.0f * minz,
            -1.0f * minx, -1.0f * miny, 1.0f * maxz,
            -1.0f * minx, 1.0f * maxy, 1.0f * maxz,
            -1.0f * minx, 1.0f * maxy, -1.0f * minz
        };
        box.setVertices(vertices);

        vertices = new float[]{
            // Z direction
            // Front face             numéro de sommet du cube-> numéro de sommet figure
            0.0f, 0.0f, 1.0f, //0 ->0
            -0.02f, 0.0f, 1.0f, //1 ->1
            -0.02f, -0.02f, 1.0f, //2 ->2
            0.0f, -0.02f, 1.0f, //3 ->3

            // Back face
            0.0f, 0.0f, 0.0f, //4 ->4
            -0.02f, 0.0f, 0.0f, //5 ->5
            -0.02f, -0.02f, 0.0f, //6 ->6
            0.0f, -0.02f, 0.0f, //7 ->7

            // Top face
            0.0f, 0.0f, 1.0f, //0 ->8
            -0.02f, 0.0f, 1.0f, //1 ->9
            -0.02f, 0.0f, 0.0f, //5 ->10
            0.0f, 0.0f, 0.0f, //4 ->11

            // Bottom face
            0.0f, -0.02f, 1.0f, //3 ->12
            -0.02f, -0.02f, 1.0f, //2 ->13
            -0.02f, -0.02f, 0.0f, //6 ->14
            0.0f, -0.02f, 0.0f, //7 ->15

            // Right face
            0.0f, 0.0f, 1.0f, //0 ->16
            0.0f, -0.02f, 1.0f, //3 ->17
            0.0f, -0.02f, 0.0f, //7 ->18
            0.0f, 0.0f, 0.0f, //4 ->19

            // Left face
            -0.02f, 0.0f, 1.0f, //1 ->20
            -0.02f, -0.02f, 1.0f, //2 ->21
            -0.02f, -0.02f, 0.0f, //6 ->22
            -0.02f, 0.0f, 0.0f, //5 ->23

            // Y direction
            // Front face  
            0.0f, 1.0f, 0.0f, //0 ->24
            -0.02f, 1.0f, 0.0f, //1 ->25
            -0.02f, 0.0f, 0.0f, //2 ->26
            0.0f, 0.0f, 0.0f, //3 ->27

            // Back face
            0.0f, 1.0f, -0.02f, //4 ->28
            -0.02f, 1.0f, -0.02f, //5 ->29
            -0.02f, 0.0f, -0.02f, //6 ->30
            0.0f, 0.0f, -0.02f, //7 ->31

            // Top face
            0.0f, 1.0f, 0.0f, //0 ->32
            -0.02f, 1.0f, 0.0f, //1 ->33
            -0.02f, 1.0f, -0.02f, //5 ->34
            0.0f, 1.0f, -0.02f, //4 ->35

            // Bottom face
            0.0f, 0.0f, 0.0f, //3 ->36
            -0.02f, 0.0f, 0.0f, //2 ->37
            -0.02f, 0.0f, -0.02f, //6 ->38
            0.0f, 0.0f, -0.02f, //7 ->39

            // Left face
            0.0f, 1.0f, 0.0f, //0 ->40
            0.0f, 0.0f, 0.0f, //3 ->41
            0.0f, 0.0f, -0.02f, //7 ->42
            0.0f, 1.0f, -0.02f, //4  >43

            // Right face
            -0.02f, 1.0f, 0.0f, //1 ->44
            -0.02f, 0.0f, 0.0f, //2 ->45
            -0.02f, 0.0f, -0.02f, //6 ->46
            -0.02f, 1.0f, -0.02f, //5 ->47

            // X direction
            // Front face  
            1.0f, 0.0f, 0.0f, //0 ->48
            0.0f, 0.0f, 0.0f, //1 ->49
            0.0f, -0.02f, 0.0f, //2 ->50
            1.0f, -0.02f, 0.0f, //3 ->51

            // Back face
            1.0f, 0.0f, -0.02f, //4 ->52
            0.0f, 0.0f, -0.02f, //5 ->53
            0.0f, -0.02f, -0.02f, //6 ->54
            1.0f, -0.02f, -0.02f, //7 ->55

            // Top face
            1.0f, 0.0f, 0.0f, //0 ->56
            0.0f, 0.0f, 0.0f, //1 ->57
            0.0f, 0.0f, -0.02f, //5 ->58
            1.0f, 0.0f, -0.02f, //4 ->59

            // Bottom face
            1.0f, -0.02f, 0.0f, //3 ->60
            0.0f, -0.02f, 0.0f, //2 ->61
            0.0f, -0.02f, -0.02f, //6 ->62
            1.0f, -0.02f, -0.02f, //7 ->63

            // Left face
            1.0f, 0.0f, 0.0f, //0  ->64
            1.0f, -0.02f, 0.0f, //3 ->65
            1.0f, -0.02f, -0.02f, //7 ->66
            1.0f, 0.0f, -0.02f, //4  ->67

            // Right face
            1.0f, 0.0f, 0.0f, //1  ->68
            0.0f, -0.02f, 0.0f, //2 ->69
            0.0f, -0.02f, -0.02f, //6 ->70
            0.0f, 0.0f, -0.02f, //5 ->71
        };
        axis.setVertices(vertices);
        resizeVertex();
    }

    private void initColors() {
        float colors[] = new float[]{
            // X direction
            // Front face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            // Back face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            // Top face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            // Bottom face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            // Right face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            // Left face
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,
            0.0f, 0.0f, 1.0f, 1f,};
        box.setColors(colors);

        colors = new float[]{
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
            1.0f, 0.0f, 0.0f, 1.0f,
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
            0.0f, 0.0f, 1.0f, 1.0f
        };
        axis.setColors(colors);
    }

    private void initIndices() {
        int indices[] = new int[]{
            0, 1, 2, 0, 2, 3, //x

            4, 5, 6, 4, 6, 7,
            8, 9, 10, 8, 10, 11,
            12, 13, 14, 12, 14, 15,
            16, 17, 18, 16, 18, 19,
            20, 21, 22, 20, 22, 23,};
        box.setIndices(indices);

        indices = new int[]{
            0, 1, 2, 0, 2, 3, //x

            4, 5, 6, 4, 6, 7,
            8, 9, 10, 8, 10, 11,
            12, 13, 14, 12, 14, 15,
            16, 17, 18, 16, 18, 19,
            20, 21, 22, 20, 22, 23, // ->y

            24, 25, 26, 24, 26, 27,
            28, 29, 30, 28, 30, 31,
            32, 33, 34, 32, 34, 35,
            36, 37, 38, 36, 38, 39,
            40, 41, 42, 40, 42, 43,
            44, 45, 46, 44, 46, 47, // ->z

            48, 49, 50, 48, 50, 51,
            52, 53, 54, 52, 54, 55,
            56, 57, 58, 56, 58, 59,
            60, 61, 62, 60, 62, 63,
            64, 65, 66, 64, 66, 67,
            68, 69, 70, 68, 70, 71
        };
        axis.setIndices(indices);
    }

    public void reconstructor() {
        makeLabel(multiModel);
        model = multiparsor(multiModel);
        model.enable();
        /*  float[]tmp=null;
        float[]tmp2=null;
        int[]tmp3=null;
        float[]tmp4=null;
        int tmp5=0;
        for(int j=0;j<multiModel.length;j++)
        {
        for(int i=0;i<multiModel[j].length;i++)
        {
        if(multiModel[j][i].isEnable()){
        tmp=parsor(tmp,multiModel[j][i].getVertices(),null);
        tmp2=parsor(tmp2,multiModel[j][i].getColors(),null);
        tmp4=parsor(tmp4,multiModel[j][i].getNormals(),null);
        tmp3=mapping(tmp3,multiModel[j][i].getIndices(),null);
        
        tmp5+=multiModel[j][i].getNumItemIndex();
        }
        }
        }       
        model.setVertices(tmp);
        model.setColors(tmp2);  
        model.setIndices(tmp3);
        model.setNormals(tmp4);
        model.setNumItemIndex(tmp5);*/

    
        /* int[]tmp3=null;
        int tmp5=0;
        for(int j=0;j<multiModel.length;j++)
        {
        for(int i=0;i<multiModel[j].length;i++)
        {
        if(multiModel[j][i].isEnable()){
        tmp3=mapping(tmp3,multiModel[j][i].getIndices(),null);
        
        tmp5+=multiModel[j][i].getNumItemIndex();
        }
        }
        }
        model.setNumItemIndex(tmp5);
        model.setIndices(tmp3); */

    }

    public void addModel(Data3D[][] DATA) {

        elementMap = new HashMap<String, Data3D>();
        multiModel = DATA;
        enableAndMapAllElement();
        buildNormalsMultiModel();
        reconstructor();
        box.disable();
        Data3D temp = null;
        boolean bcontinue = true;
        for (int i = 0; i < DATA.length; i++)
            if (DATA[i] != null)
            {
                for (int j = 0; j < DATA[i].length; j++)
                {
                    if(DATA[i][j] != null)
                    {
                        temp = DATA[i][j];
                        bcontinue = false;
                        break;
                     }
                }
                if(!bcontinue) break;
                
            }
                        
        setBoundingBox(temp.getBoundingBox());
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().changeCameraView(temp.getBoundingBox());
        Scene.getInstance().refreshScreen();
        SimulationGUIControlBoxModel.getInstance().uncheckBoxBox();
     //   SC.say("Load succes ! </br> This is an example of model for the simulation gui.</br> You can open the model and the simulator controller with the check box on the top right.</br>In the model controller you have a tree of layout, in each layout you can enable/disable an object or change the color of this object with a double-click on his name");

    }

    public void addModel(Data3D DATA) {

        model = DATA;
        model.buildNormals();
        model.enable();
        setBoundingBox(DATA.getBoundingBox());
        box.disable();
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().changeCameraView(DATA.getBoundingBox());
        Scene.getInstance().refreshScreen();
        SimulationGUIControlBoxModel.getInstance().uncheckBoxBox();
        //SC.say("load succes, number of vertex : " + model.getNumItemIndex() + " [x min : " + model.getBoundingBox()[0] + "mm x max : " + model.getBoundingBox()[1] + "mm] [y min : " + model.getBoundingBox()[2] + "mm y max : " + model.getBoundingBox()[3] + "mm] [z min : " + model.getBoundingBox()[4] + "mm z max : " + model.getBoundingBox()[5] + "mm]");

    }

    public void buildNormalsMultiModel() {

        for (int f = 0; f < multiModel.length; f++) {
            if(multiModel[f] != null)
            for (int k = 0; k < multiModel[f].length; k++) {
                if(multiModel[f][k] != null)
                    multiModel[f][k].buildNormals();
            }
        }

    }

    private void enableAndMapAllElement() {

        for (int j = 0; j < multiModel.length; j++) {
            if(multiModel[j] != null)
            for (int i = 0; i < multiModel[j].length; i++) {
                if(multiModel[j][i] !=null)
                {
                    multiModel[j][i].enable();
                    elementMap.put(multiModel[j][i].getID(), multiModel[j][i]);
                }
            }
        }
    }

    @Override
    public void setBoundingBox(double[] b) {
   
        boundingBox = b;
        model.setBoundingBox(b);
        box.setBoundingBox(b);
        axis.setBoundingBox(b);
        initVertex();
    }

    @Override
    public void resizeVertex() {

        float weight = weighting();
        float[] vertices = new float[axis.getSupVertices().length];
        for (int i = 0; i < axis.getSupVertices().length; i++) {
            vertices[i] = axis.getSupVertices()[i] * weight;
        }
        axis.setVertices(vertices);
    }

    @Override
    public int getNumItemIndex() {
        return box.getNumItemIndex() + axis.getNumItemIndex() + model.getNumItemIndex();
    }

    @Override
    public double[] getBoundingBox() {
        return boundingBox;
    }

    @Override
    public float[] getVertices() {
        return parsor(box.getVertices(), axis.getVertices(), model.getVertices());
    }

    @Override
    public float[] getColors() {
        return parsor(box.getColors(), axis.getColors(), model.getColors());
    }

    @Override
    public int[] getIndices() {
        return mapping(box.getIndices(), axis.getIndices(), model.getIndices());
    }

    @Override
    public float[] getNormals() {
        return parsor(box.getNormals(), axis.getNormals(), model.getNormals());
    }

    public void unsetElement(ListGridRecord[] e) {
        for (int j = 0; j < multiModel.length; j++) {
            if (multiModel[j] != null)
            {
                for (int i = 0; i < multiModel[j].length; i++) {
                    if (multiModel[j][i] != null)
                        multiModel[j][i].disable();
                }
            }
        }

        for (ListGridRecord e1 : e) {
            if (elementMap.get(e1.getAttribute("ElementId")) != null) {
                elementMap.get(e1.getAttribute("ElementId")).enable();
            }
        }
        reconstructor();
        //model.enable();
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    public void colorElement(String name, float r, float g, float b, float alpha) {

        Data3D object;
        object = elementMap.get(name);
        object.setAlphaInfo(alpha);
        float[] colors = new float[object.getSupColors().length];
        for (int i = 0; i < object.getSupColors().length; i = i + 4) // pour chaque ligne de vertex on ajoute une couleur !
        {
            colors[i] = r;
            colors[i + 1] = g;
            colors[i + 2] = b;
            colors[i + 3] = alpha;
        }
        object.setColors(colors);

        reconstructor();
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    public void colorElement(String name, String color, float alpha) {

        if (color.startsWith("#")) {
            float r = (float) (Integer.parseInt(color.substring(1, 2), 16)) / 15;
            float g = (float) (Integer.parseInt(color.substring(3, 4), 16)) / 15;
            float b = (float) (Integer.parseInt(color.substring(5, 6), 16)) / 15;
            colorElement(name, r, g, b, alpha);
        }
    }

    private Data3D multiparsor(Data3D[][] object) {

        Data3D tmp = new Data3D();
        int tailleV = 0;
        int tailleC = 0;
        int tailleN = 0;
        //  int tailleI=0;
        int posV = 0;
        int posC = 0;
        int posN = 0;
        int size = 0;

        for (Data3D d[] : object) {
            if (object != null)
            {
                for (Data3D d1 : d) {
                    if (d1 !=null)
                    {
                        if (d1.isEnable() && d1.getColors() != null) {
                            tailleC += d1.getColors().length;
                        }
                        if (d1.isEnable() && d1.getVertices() != null) {
                            tailleV += d1.getVertices().length;
                        }
                        if (d1.isEnable() && d1.getColors() != null) {
                            tailleN += d1.getNormals().length;
                        }
                        // if(d1.isDisable()&&d1.getIndices()!=null)tailleI+=d1.getIndices().length;
                        size++;
                    }
                }
            }
        }

        
        
        float[] c = new float[tailleC];
        float[] v = new float[tailleV];
        float[] n = new float[tailleN];
        int[] ind = null;
        int num = 0;
        String s = "";
        for (int i = 0; i < size; i++) {
            Data3D d1 = opaqueMap.get(i);
//            for (int j = 0 ; j < d1.getVertices().length; j++)
//            {
//                if (j%333 ==0 )
//                s += d1.getVertices()[j] + "   ";
//            }
            
            s += " " + i + " " + d1.getID() + " " + d1.getLenghtInfo() + "</br>";
            if (d1.isEnable() && d1.getColors() != null && d1.getVertices() != null && d1.getNormals() != null) {
                System.arraycopy(d1.getColors(), 0, c, posC, d1.getColors().length);
                posC += d1.getColors().length;
                System.arraycopy(d1.getVertices(), 0, v, posV, d1.getVertices().length);
                posV += d1.getVertices().length;
                System.arraycopy(d1.getNormals(), 0, n, posN, d1.getNormals().length);
                posN += d1.getNormals().length;
                ind = mapping(ind, d1.getIndices(), null);
                num += d1.getNumItemIndex();
            }

        }
        //SC.say(""+s);
        tmp.setNumItemIndex(num);
        tmp.setIndices(ind);
        tmp.setColors(c);
        tmp.setVertices(v);
        tmp.setNormals(n);
        return tmp;
    }

    private void makeLabel(Data3D[][] object) {

        int i = 0;
        int j = 0;
        opaqueMap = new HashMap<Integer, Data3D>();
        transparentMap = new HashMap<Integer, Data3D>();

        for (Data3D[] obj : object) {
            if (obj != null)
            {
                for (Data3D obj1 : obj) {
                    if (obj1 != null)
                    {
                        if (obj1.getAlphaInfo() == 1.0f) {
                            opaqueMap.put(i, obj1);
                            i++;
                        } else {
                            //opaqueMap.put((size-j),obj1);
                            transparentMap.put(j, obj1);
                            j++;
                        }
                    }
                }
            }
        }

        float max = 999999f;
        int n = 0;
        while (!transparentMap.isEmpty()) {
            max = 999999f;
            int v = 0;
            for (int k = 0; k < j; k++) {
                if (transparentMap.containsKey(k)) {
                    if (transparentMap.get(k).getLenghtInfo() < max) {
                        max = transparentMap.get(k).getLenghtInfo();
                        v = k;
                    }
                }
            }
            opaqueMap.put(i + n, transparentMap.get(v));
            transparentMap.remove(v);
            n++;
        }
    }
}
