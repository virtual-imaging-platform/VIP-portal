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

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;

/**
 *
 * @author Kevin Moulin
 */
public class ObjectSimulateur extends Object3D {

    public ObjectSimulateur(String contents) {
        
        boundingBox = new double[]{-1, 1, -1, 1, -1, 1};
        model = new Data3D("simulator");
        box = new Data3D("area");
        axis = new Data3D("axis");
        id = contents;

        angleX = 0;
        angleY = 0;
        angleZ = 0;
        translateZ = 0;
        translateX = 0;
        translateY = 0;
        axis.setItemSizeVertex(3);//itemSizeVertex=3;
        axis.setItemSizeColor(4);//itemSizeColor=4;
        axis.setNumItemIndex(108);//numItemIndex=108;
        
        if (id.equals("US")) {
            box.setItemSizeVertex(3);//itemSizeVertex=3;
            box.setItemSizeColor(4);//itemSizeColor=4;
            box.setNumItemIndex(6);//numItemIndex=108;
            model.setItemSizeVertex(3);//itemSizeVertex=3;
            model.setItemSizeColor(4);//itemSizeColor=4;
            model.setNumItemIndex(360);//numItemIndex=108;

        } else {
            box.setItemSizeVertex(3);//itemSizeVertex=3;
            box.setItemSizeColor(4);//itemSizeColor=4;
            box.setNumItemIndex(6);//numItemIndex=108;
            model.setItemSizeVertex(3);
            model.setItemSizeColor(4);
            model.setNumItemIndex(192);
        }
        initIndices();
        initVertex();
        initColors();
        axis.enable();
        buildNormals();
    }

    private void initIndices() {
        
        int indices[] = new int[]{
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

        if (id.equals("US")) {
            indices = new int[]{
                0, 1, 2, 0, 2, 3
            };
            box.setIndices(indices);
            indices = new int[]{
                // middle >  top
                0, 1, 20, 1, 20, 21,
                1, 2, 21, 2, 21, 22,
                2, 3, 22, 3, 22, 23,
                3, 4, 23, 4, 23, 24,
                4, 5, 24, 5, 24, 25,
                5, 6, 25, 6, 25, 26,
                6, 7, 26, 7, 26, 27,
                7, 8, 27, 8, 27, 28,
                8, 9, 28, 9, 28, 29,
                9, 10, 29, 10, 29, 30,
                10, 11, 30, 11, 30, 31,
                11, 12, 31, 12, 31, 32,
                12, 13, 32, 13, 32, 33,
                13, 14, 33, 14, 33, 34,
                14, 15, 34, 15, 34, 35,
                15, 16, 35, 16, 35, 36,
                16, 17, 36, 17, 36, 37,
                17, 18, 37, 18, 37, 38,
                18, 19, 38, 19, 38, 39,
                // middle > bottom
                0, 1, 40, 1, 40, 41,
                1, 2, 41, 2, 41, 42,
                2, 3, 42, 3, 42, 43,
                3, 4, 43, 4, 43, 44,
                4, 5, 44, 5, 44, 45,
                5, 6, 45, 6, 45, 46,
                6, 7, 46, 7, 46, 47,
                7, 8, 47, 8, 47, 48,
                8, 9, 48, 9, 48, 49,
                9, 10, 49, 10, 49, 50,
                10, 11, 50, 11, 50, 51,
                11, 12, 51, 12, 51, 52,
                12, 13, 52, 13, 52, 53,
                13, 14, 53, 14, 53, 54,
                14, 15, 54, 15, 54, 55,
                15, 16, 55, 16, 55, 56,
                16, 17, 56, 17, 56, 57,
                17, 18, 57, 18, 57, 58,
                18, 19, 58, 19, 58, 59,
                // front
                0, 20, 19, 19, 20, 39,
                0, 40, 19, 19, 40, 59,
                // top
                20, 21, 24, 20, 24, 25,
                21, 22, 23, 21, 23, 24,
                20, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 39,
                34, 35, 38, 34, 38, 39,
                35, 36, 37, 35, 37, 38,
                20, 33, 39, 20, 26, 33,
                26, 27, 32, 26, 32, 33,
                27, 28, 31, 31, 32, 27,
                28, 29, 30, 28, 30, 31,
                // bottom
                40, 41, 44, 40, 44, 45,
                41, 42, 43, 41, 43, 44,
                40, 45, 46, 47, 48, 49,
                50, 51, 52, 53, 54, 59,
                54, 55, 58, 54, 58, 59,
                55, 56, 57, 55, 57, 58,
                40, 53, 59, 40, 46, 53,
                46, 47, 52, 46, 52, 53,
                47, 48, 51, 51, 52, 47,
                48, 49, 50, 48, 50, 51,};
            
            model.setIndices(indices);
            
        } else {
            
            indices = new int[]{
                0, 1, 2, 0, 2, 3
            };
            box.setIndices(indices);
            indices = new int[]{
                0, 4, 1, 0, 4, 5,
                0, 5, 6, 0, 3, 6,
                1, 4, 11, 1, 11, 10,
                1, 2, 10, 2, 9, 10,
                2, 8, 9, 2, 3, 8,
                3, 8, 7, 3, 6, 7,
                12, 16, 13, 12, 16, 17,
                12, 17, 18, 12, 15, 18,
                13, 16, 23, 13, 23, 22,
                13, 14, 22, 14, 21, 22,
                14, 20, 21, 14, 15, 20,
                15, 20, 19, 15, 18, 19,
                4, 5, 25, 4, 24, 25,
                5, 6, 26, 5, 25, 26,
                6, 7, 27, 6, 26, 27,
                7, 8, 28, 7, 27, 28,
                8, 9, 29, 8, 28, 29,
                9, 10, 30, 9, 29, 30,
                10, 11, 31, 10, 30, 31,
                4, 11, 31, 4, 24, 31,
                16, 17, 25, 16, 24, 25,
                17, 18, 26, 17, 25, 26,
                18, 19, 27, 18, 26, 27,
                19, 20, 28, 19, 27, 28,
                20, 21, 29, 20, 28, 29,
                21, 22, 30, 21, 29, 30,
                22, 23, 31, 22, 30, 31,
                16, 23, 31, 16, 24, 31,
                0, 1, 13, 0, 12, 13,
                0, 3, 15, 0, 15, 12,
                2, 3, 14, 3, 14, 15,
                1, 2, 13, 2, 13, 14,};
            
            model.setIndices(indices);
        }
    }

    private void initColors() {

        float colors[] = new float[]{
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
            0.0f, 0.0f, 1.0f, 1.0f,};
        axis.setColors(colors);

        if (id.equals("US")) {

            colors = new float[]{
                // X direction
                // Front face
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,};
            box.setColors(colors);
            colors = new float[]{
                // X direction
                // Layout middle
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                // Layout top
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                // Layout bottom
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,
                0.564705882f, 0.564705882f, 0.533333333f, 1.0f,};
            model.setColors(colors);
            
        } else {
            colors = new float[]{
                // X direction
                // Front face
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,
                0.5f, 0.5f, 1.0f, 0.6f,};
            box.setColors(colors);
            colors = new float[]{
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
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                // Back Face
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
                // Middle circle
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,
                0.62f, 0.7f, 0.8f, 1.0f,};
            model.setColors(colors);
        }
    }

    private void initVertex() {

        float[] vertices = new float[]{
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

        if (id.equals("US")) {
            vertices = new float[]{
                // X direction
                // Front face             numéro de sommet du cube-> numéro de sommet figure
                0.5f, 0.5f, 0.0f, //0 ->0
                -0.5f, 0.5f, 0.0f, //1 ->1
                -0.25f, 0.0f, 0.0f, //2 ->2
                0.25f, 0.0f, 0.0f, //3 ->3
            };
            box.setVertices(vertices);
            vertices = new float[]{
                // X direction

                // Layout middle            numéro de sommet du cube-> numéro de sommet figure
                0.1f, 0f, 0f, //0
                0.17f, -0.05f, 0f, //1
                0.25f, -0.1f, 0f, //2

                0.25f, -0.25f, 0f, //3
                0.135f, -0.30f, 0f, //4 
                0.115f, -0.35f, 0f, //5 
                0.1f, -0.4f, 0f, //6 

                0.1f, -0.6f, 0f, //7
                0.08f, -0.7f, 0f, //8
                0.06f, -0.75f, 0f, //9

                -0.06f, -0.75f, 0f, //10
                -0.08f, -0.7f, 0f, //11
                -0.1f, -0.6f, 0f, //12

                -0.1f, -0.4f, 0f, //13
                -0.115f, -0.35f, 0f, //14
                -0.135f, -0.30f, 0f, //15
                -0.25f, -0.25f, 0f, //16

                -0.25f, -0.1f, 0f, //17
                -0.17f, -0.05f, 0f, //18
                -0.1f, 0f, 0f, //19

                // Layout Top         numéro de sommet du cube-> numéro de sommet figure
                0.09f, -0.01f, 0.05f, //0 -> 20
                0.16f, -0.06f, 0.05f, //1 -> 21
                0.22f, -0.11f, 0.05f, //2 -> 22

                0.22f, -0.22f, 0.05f, //3 -> 23
                0.125f, -0.28f, 0.05f, //4 -> 24
                0.105f, -0.33f, 0.05f, //5 -> 25
                0.09f, -0.3f, 0.05f, //6 -> 26

                0.09f, -0.55f, 0.05f, //7 -> 27
                0.07f, -0.65f, 0.05f, //8 -> 28 
                0.05f, -0.70f, 0.05f, //9 -> 29

                -0.05f, -0.70f, 0.05f, //10 -> 30
                -0.07f, -0.65f, 0.05f, //11 -> 31
                -0.09f, -0.55f, 0.05f, //12 -> 32

                -0.09f, -0.3f, 0.05f, //13 -> 33
                -0.105f, -0.33f, 0.05f, //14 -> 34
                -0.125f, -0.28f, 0.05f, //15 -> 35
                -0.22f, -0.22f, 0.05f, //16 -> 36

                -0.22f, -0.11f, 0.05f, //17 -> 37
                -0.16f, -0.06f, 0.05f, //18 -> 38
                -0.09f, -0.01f, 0.05f, //19 -> 39

                // Layout Bottom         numéro de sommet du cube-> numéro de sommet figure
                0.09f, -0.01f, -0.05f, //0 -> 40
                0.16f, -0.06f, -0.05f, //1 -> 41
                0.22f, -0.11f, -0.05f, //2 -> 42

                0.22f, -0.22f, -0.05f, //3 -> 43
                0.125f, -0.28f, -0.05f, //4 -> 44
                0.105f, -0.33f, -0.05f, //5 -> 45
                0.09f, -0.3f, -0.05f, //6 -> 46

                0.09f, -0.55f, -0.05f, //7 -> 47
                0.07f, -0.65f, -0.05f, //8 -> 48 
                0.05f, -0.70f, -0.05f, //9 -> 49

                -0.05f, -0.70f, -0.05f, //10 -> 50
                -0.07f, -0.65f, -0.05f, //11 -> 51
                -0.09f, -0.55f, -0.05f, //12 -> 52

                -0.09f, -0.3f, -0.05f, //13 -> 53
                -0.105f, -0.33f, -0.05f, //14 -> 54
                -0.125f, -0.28f, -0.05f, //15 -> 55
                -0.22f, -0.22f, -0.05f, //16 -> 56

                -0.22f, -0.11f, -0.05f, //17 -> 57
                -0.16f, -0.06f, -0.05f, //18 -> 58
                -0.09f, -0.01f, -0.05f, //19 -> 59
            };
            model.setVertices(vertices);
            
        } else {
            vertices = new float[]{
                // X direction
                // Front face             numéro de sommet du cube-> numéro de sommet figure
                1f, 1f, 0.0f, //0 ->0
                -1f, 1f, 0.0f, //1 ->1
                -1f, -1f, 0.0f, //2 ->2
                1f, -1f, 0.0f, //3 ->3
            };
            box.setVertices(vertices);
            double pi = Math.PI;
            vertices = new float[]{
                // X direction
                // Front face             numéro de sommet du cube-> numéro de sommet figure

                2f, 1.5f, 0.5f, //0 ->0
                2f, -1.5f, 0.5f, //1 ->1
                -2f, -1.5f, 0.5f, //2 ->2
                -2f, 1.5f, 0.5f, //3 ->3

                1.5f * (float) Math.cos((0 * (2 * pi) / 8)), 1.5f * (float) Math.sin((0 * (2 * pi) / 8)), 0.5f, //4 ->4
                1.5f * (float) Math.cos((1 * (2 * pi) / 8)), 1.5f * (float) Math.sin((1 * (2 * pi) / 8)), 0.5f, //5 ->5
                1.5f * (float) Math.cos((2 * (2 * pi) / 8)), 1.5f * (float) Math.sin((2 * (2 * pi) / 8)), 0.5f, //6 ->6
                1.5f * (float) Math.cos((3 * (2 * pi) / 8)), 1.5f * (float) Math.sin((3 * (2 * pi) / 8)), 0.5f, //7 ->7
                1.5f * (float) Math.cos((4 * (2 * pi) / 8)), 1.5f * (float) Math.sin((4 * (2 * pi) / 8)), 0.5f, //8 ->8
                1.5f * (float) Math.cos((5 * (2 * pi) / 8)), 1.5f * (float) Math.sin((5 * (2 * pi) / 8)), 0.5f, //9 ->9
                1.5f * (float) Math.cos((6 * (2 * pi) / 8)), 1.5f * (float) Math.sin((6 * (2 * pi) / 8)), 0.5f, //10 ->10
                1.5f * (float) Math.cos((7 * (2 * pi) / 8)), 1.5f * (float) Math.sin((7 * (2 * pi) / 8)), 0.5f, //11 ->11

                // Back face             numéro de sommet du cube-> numéro de sommet figure

                2f, 1.5f, -0.5f, //0 ->12
                2f, -1.5f, -0.5f, //1 ->13
                -2f, -1.5f, -0.5f, //2 ->14
                -2f, 1.5f, -0.5f, //3 ->15

                1.5f * (float) Math.cos((0 * (2 * pi) / 8)), 1.5f * (float) Math.sin((0 * (2 * pi) / 8)), -0.5f, //4 ->16
                1.5f * (float) Math.cos((1 * (2 * pi) / 8)), 1.5f * (float) Math.sin((1 * (2 * pi) / 8)), -0.5f, //5 ->17
                1.5f * (float) Math.cos((2 * (2 * pi) / 8)), 1.5f * (float) Math.sin((2 * (2 * pi) / 8)), -0.5f, //6 ->18
                1.5f * (float) Math.cos((3 * (2 * pi) / 8)), 1.5f * (float) Math.sin((3 * (2 * pi) / 8)), -0.5f, //7 ->19
                1.5f * (float) Math.cos((4 * (2 * pi) / 8)), 1.5f * (float) Math.sin((4 * (2 * pi) / 8)), -0.5f, //8 ->20
                1.5f * (float) Math.cos((5 * (2 * pi) / 8)), 1.5f * (float) Math.sin((5 * (2 * pi) / 8)), -0.5f, //9 ->21
                1.5f * (float) Math.cos((6 * (2 * pi) / 8)), 1.5f * (float) Math.sin((6 * (2 * pi) / 8)), -0.5f, //10 ->22
                1.5f * (float) Math.cos((7 * (2 * pi) / 8)), 1.5f * (float) Math.sin((7 * (2 * pi) / 8)), -0.5f, //11 ->23

                //middle
                (float) Math.cos((0 * (2 * pi) / 8)), (float) Math.sin((0 * (2 * pi) / 8)), 0f, //4 ->24
                (float) Math.cos((1 * (2 * pi) / 8)), (float) Math.sin((1 * (2 * pi) / 8)), 0f, //5 ->25
                (float) Math.cos((2 * (2 * pi) / 8)), (float) Math.sin((2 * (2 * pi) / 8)), 0f, //6 ->26
                (float) Math.cos((3 * (2 * pi) / 8)), (float) Math.sin((3 * (2 * pi) / 8)), 0f, //7 ->27
                (float) Math.cos((4 * (2 * pi) / 8)), (float) Math.sin((4 * (2 * pi) / 8)), 0f, //8 ->28
                (float) Math.cos((5 * (2 * pi) / 8)), (float) Math.sin((5 * (2 * pi) / 8)), 0f, //9 ->29
                (float) Math.cos((6 * (2 * pi) / 8)), (float) Math.sin((6 * (2 * pi) / 8)), 0f, //10 ->30
                (float) Math.cos((7 * (2 * pi) / 8)), (float) Math.sin((7 * (2 * pi) / 8)), 0f, //11 ->31
            };
            model.setVertices(vertices);
        }
        resizeVertex();
    }

    @Override
    public void setBoundingBox(double[] b) {
        boundingBox = b;
        model.setBoundingBox(b);
        box.setBoundingBox(b);
        axis.setBoundingBox(b);
        initVertex();
    }
}
