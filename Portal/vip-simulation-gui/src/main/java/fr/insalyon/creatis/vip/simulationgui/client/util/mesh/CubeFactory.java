/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.client.util.mesh;

import static fr.insalyon.creatis.vip.simulationgui.client.util.ConversionUtils.floatListToFloatArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.Vector2f;
import fr.insalyon.creatis.vip.simulationgui.client.util.math.Vector3f;

/**
 * Factory for creation of cube {@link Mesh} data
 * 
 * @author Sönke Sothmann
 */
public class CubeFactory {

    /**
     * Constructs a new cube with the given scale.
     * 
     * @param scale
     * @return cube
     */
    public static Mesh createNewInstance(float scale) {
        Mesh mesh = new Mesh();

        float halfscale = scale / 2;

        // create the values
        mesh.verticesArray = createVerticesArray(halfscale);
        mesh.texCoordsArray = createTexCoordsArray();
        mesh.vertexNormalsArray = createVertexNormalsArray();

        return mesh;
    }

    private static List<Vector3f> createPlaneVertices(Vector3f topleft,
            Vector3f topright, Vector3f bottomright, Vector3f bottomleft) {
        List<Vector3f> plane = new ArrayList<Vector3f>(6);
        plane.addAll(Arrays.asList(topleft, topright, bottomleft, bottomleft,
                topright, bottomright));
        return plane;
    }

    private static List<Vector2f> createPlaneTexCoords() {
        List<Vector2f> texCoords = new ArrayList<Vector2f>(6);
        texCoords.addAll(Arrays.asList(
                vec(0, 0),
                vec(1, 0),
                vec(0, 1),
                vec(0, 1),
                vec(1, 0),
                vec(1, 1)));
        return texCoords;
    }

    private static float[] createVerticesArray(float halfscale) {
        List<Vector3f> front = createPlaneVertices(
                // topleft
                vec(-halfscale, halfscale, halfscale),
                // topright
                vec(halfscale, halfscale, halfscale),
                // bottomright
                vec(halfscale, -halfscale, halfscale),
                // bottomleft
                vec(-halfscale, -halfscale, halfscale));
        List<Vector3f> back = createPlaneVertices(
                // topleft
                vec(halfscale, halfscale, -halfscale),
                // topright
                vec(-halfscale, halfscale, -halfscale),
                // bottomright
                vec(-halfscale, -halfscale, -halfscale),
                // bottomleft
                vec(halfscale, -halfscale, -halfscale));
        List<Vector3f> left = createPlaneVertices(
                // topleft
                vec(-halfscale, halfscale, -halfscale),
                // topright
                vec(-halfscale, halfscale, halfscale),
                // bottomright
                vec(-halfscale, -halfscale, halfscale),
                // bottomleft
                vec(-halfscale, -halfscale, -halfscale));
        List<Vector3f> right = createPlaneVertices(
                // topleft
                vec(halfscale, halfscale, halfscale),
                // topright
                vec(halfscale, halfscale, -halfscale),
                // bottomright
                vec(halfscale, -halfscale, -halfscale),
                // bottomleft
                vec(halfscale, -halfscale, halfscale));
        List<Vector3f> top = createPlaneVertices(
                // topleft
                vec(-halfscale, halfscale, halfscale),
                // topright
                vec(-halfscale, halfscale, -halfscale),
                // bottomright
                vec(halfscale, halfscale, -halfscale),
                // bottomleft
                vec(halfscale, halfscale, halfscale));
        List<Vector3f> bottom = createPlaneVertices(
                // topleft
                vec(halfscale, -halfscale, halfscale),
                // topright
                vec(halfscale, -halfscale, -halfscale),
                // bottomright
                vec(-halfscale, -halfscale, -halfscale),
                // bottomleft
                vec(-halfscale, -halfscale, halfscale));

        List<Float> vertices = new ArrayList<Float>(108);
        vertices.addAll(flattenVector3fList(front));
        vertices.addAll(flattenVector3fList(back));
        vertices.addAll(flattenVector3fList(left));
        vertices.addAll(flattenVector3fList(right));
        vertices.addAll(flattenVector3fList(top));
        vertices.addAll(flattenVector3fList(bottom));
        return floatListToFloatArray(vertices);
    }

    private static float[] createTexCoordsArray() {
        List<Vector2f> side = createPlaneTexCoords();

        List<Float> texCoords = new ArrayList<Float>(72);
        texCoords.addAll(flattenVector2fList(side));
        texCoords.addAll(flattenVector2fList(side));
        texCoords.addAll(flattenVector2fList(side));
        texCoords.addAll(flattenVector2fList(side));
        texCoords.addAll(flattenVector2fList(side));
        texCoords.addAll(flattenVector2fList(side));
        return floatListToFloatArray(texCoords);
    }

    private static float[] createVertexNormalsArray() {
        // throw new UnsupportedOperationException("not implemented yet");
        List<Vector3f> normals = new ArrayList<Vector3f>(24);
        Vector3f front = vec(0, 0, 1);
        Vector3f back = vec(0, 0, -1);
        Vector3f left = vec(-1, 0, 0);
        Vector3f right = vec(1, 0, 0);
        Vector3f top = vec(0, 1, 0);
        Vector3f bottom = vec(0, -1, 0);
        normals.add(front);
        normals.add(front);
        normals.add(front);
        normals.add(front);
        normals.add(front);
        normals.add(front);
        normals.add(back);
        normals.add(back);
        normals.add(back);
        normals.add(back);
        normals.add(back);
        normals.add(back);
        normals.add(left);
        normals.add(left);
        normals.add(left);
        normals.add(left);
        normals.add(left);
        normals.add(left);
        normals.add(right);
        normals.add(right);
        normals.add(right);
        normals.add(right);
        normals.add(right);
        normals.add(right);
        normals.add(top);
        normals.add(top);
        normals.add(top);
        normals.add(top);
        normals.add(top);
        normals.add(top);
        normals.add(bottom);
        normals.add(bottom);
        normals.add(bottom);
        normals.add(bottom);
        normals.add(bottom);
        normals.add(bottom);

        List<Float> vertexNormals = flattenVector3fList(normals);
        return floatListToFloatArray(vertexNormals);
    }

    private static Vector3f vec(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    private static Vector2f vec(float u, float v) {
        return new Vector2f(u, v);
    }

    private static List<Float> flattenVector3fList(List<Vector3f> inList) {
        List<Float> outList = new ArrayList<Float>(inList.size() * 3);
        for (Vector3f v : inList) {
            outList.addAll(Arrays.asList(v.getX(), v.getY(), v.getZ()));
        }
        return outList;
    }

    private static List<Float> flattenVector2fList(List<Vector2f> inList) {
        List<Float> outList = new ArrayList<Float>(inList.size() * 2);
        for (Vector2f v : inList) {
            outList.addAll(Arrays.asList(v.getU(), v.getV()));
        }
        return outList;
    }
}
