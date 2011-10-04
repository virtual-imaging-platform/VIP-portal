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
package fr.insalyon.creatis.vip.simulationgui.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Kevin Moulin
 */
public class Data3D implements IsSerializable {

    protected float alphaInfo;
    protected float lenghtInfo;
    protected double[] boundingBox;
    protected int itemSizeVertex;
    protected int itemSizeColor;
    protected int numItemIndex;
    protected String id;
    protected String type;
    protected float[] vertices;
    protected float[] colors;
    protected int[] indices;
    protected float[] normals;
    protected boolean enable;

    public Data3D() {
    }

    public Data3D(String ID) {
        boundingBox = new double[]{-1, 1, -1, 1, -1, 1};
        itemSizeVertex = 0;
        itemSizeColor = 0;
        numItemIndex = 0;
        alphaInfo = 1f;
        lenghtInfo = 1f;
        vertices = null;
        colors = null;
        indices = null;
        normals = null;
        id = ID;
        enable = false;
    }

    public float[] getSupVertices() {
        return vertices;
    }

    public float[] getSupColors() {
        return colors;

    }

    public int[] getSupIndices() {
        return indices;
    }

    public float[] getSupNormals() {
        return normals;

    }

    public float[] getVertices() {
        if (enable) {
            return vertices;
        }
        return null;
    }

    public float[] getColors() {
        if (enable) {
            return colors;
        }
        return null;
    }

    public int[] getIndices() {
        if (enable) {
            return indices;
        }
        return null;
    }

    public float[] getNormals() {
        if (enable) {
            return normals;
        }
        return null;
    }

    public int getItemSizeColor() {
        return itemSizeColor;
    }

    public int getItemSizeVertex() {
        return itemSizeVertex;
    }

    public int getSupNumItemIndex() {
        return numItemIndex;
    }

    public int getNumItemIndex() {
        if (enable) {
            return numItemIndex;
        }
        return 0;
    }

    public double[] getBoundingBox() {
        return boundingBox;
    }

    public String getID() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getAlphaInfo() {
        return alphaInfo;
    }

    public float getLenghtInfo() {
        return lenghtInfo;
    }

    public void setVertices(float[] v) {
        vertices = v;
    }

    public void setColors(float[] c) {
        colors = c;
    }

    public void setIndices(int[] i) {
        indices = i;
    }

    public void setNormals(float[] n) {
        normals = n;
    }

    public void setItemSizeColor(int c) {
        itemSizeColor = c;
    }

    public void setItemSizeVertex(int v) {
        itemSizeVertex = v;
    }

    public void setNumItemIndex(int i) {
        numItemIndex = i;
    }

    public void reconstructor(Data3D DATA) {
    }

    public void setBoundingBox(double[] box) {
        boundingBox = box;
    }

    public void setType(String typ) {
        type = typ;
    }

    public void setAlphaInfo(float a) {
        alphaInfo = a;
    }

    public void setLenghtInfo(float l) {
        lenghtInfo = l;
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isDisable() {
        return !enable;
    }

    public void buildNormals() {

        if ((getSupVertices() != null) && (getSupIndices() != null)) {

            Vector3 A, B, C, N, AB, AC;
            A = new Vector3();
            B = new Vector3();
            C = new Vector3();
            N = new Vector3();
            AB = new Vector3();
            AC = new Vector3();

            int i1, i2, i3;
            int sizeNormal = getSupVertices().length;
            normals = new float[sizeNormal];
            for (int i = 0; i < sizeNormal; i++) {
                normals[i] = 0;
            }

            for (int j = 0; j < getSupIndices().length; j = j + 3) {

                i1 = getSupIndices()[j];
                i2 = getSupIndices()[j + 1];
                i3 = getSupIndices()[j + 2];

                A.x = getSupVertices()[3 * i1];
                A.y = getSupVertices()[3 * i1 + 1];
                A.z = getSupVertices()[3 * i1 + 2];

                B.x = getSupVertices()[3 * i2];
                B.y = getSupVertices()[3 * i2 + 1];
                B.z = getSupVertices()[3 * i2 + 2];

                C.x = getSupVertices()[3 * i3];
                C.y = getSupVertices()[3 * i3 + 1];
                C.z = getSupVertices()[3 * i3 + 2];

                AB.x = B.x - A.x;
                AB.y = B.y - A.y;
                AB.z = B.z - A.z;

                AC.x = C.x - A.x;
                AC.y = C.y - A.y;
                AC.z = C.z - A.z;

                N.x = (AC.y * AB.z) - (AC.z * AB.y);
                N.y = (AC.z * AB.x) - (AC.x * AB.z);
                N.z = (AC.x * AB.y) - (AC.y * AB.x);

                normals[3 * i1] += N.x;
                normals[3 * i1 + 1] += N.y;
                normals[3 * i1 + 2] += N.z;

                normals[3 * i2] += N.x;
                normals[3 * i2 + 1] += N.y;
                normals[3 * i2 + 2] += N.z;

                normals[3 * i3] += N.x;
                normals[3 * i3 + 1] += N.y;
                normals[3 * i3 + 2] += N.z;
            }
            
            for (int i = 0; i < normals.length; i = i + 3) {
                float x, y, z;
                float mod;
                x = normals[i];
                y = normals[i + 1];
                z = normals[i + 2];
                mod = (float) Math.sqrt((x * x + y * y + z * z));
                normals[i] = normals[i] / mod;
                normals[i + 1] = normals[i + 1] / mod;
                normals[i + 2] = normals[i + 2] / mod;
            }
        }
    }

    public class Vector3 {

        public float x = 0;
        public float y = 0;
        public float z = 0;

        public Vector3() {
        }
    }
}
