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
package fr.insalyon.creatis.vip.simulationgui.client.util.mesh;

/**
 * Class that holds a mesh's data 
 * 
 * @author Sönke Sothmann
 */
public class Mesh {

    /**
     * float array containing the vertices float values
     */
    protected float[] verticesArray;
    /**
     * float array containing the tex coords float values
     */
    protected float[] texCoordsArray;
    /**
     * float array containing the vertex normals float values
     */
    protected float[] vertexNormalsArray;

    /**
     * Default constructor, does nothing.
     */
    public Mesh() {
    }

    /**
     * Initializes the Mesh's data arrays with the given sizes
     * @param numVerticesElements number of vertices floats
     * @param numTexCoordsElements number of tex coords floats
     * @param numVertexNormalsElements number of vertex normals floats
     */
    public Mesh(int numVerticesElements, int numTexCoordsElements, int numVertexNormalsElements) {
        this.verticesArray = new float[numVerticesElements];
        this.texCoordsArray = new float[numTexCoordsElements];
        this.vertexNormalsArray = new float[numVertexNormalsElements];
    }

    /**
     * Returns the vertex coordinates for this cube.
     * 
     * @return the vertex coordinates
     */
    public float[] getVertices() {
        return verticesArray;
    }

    /**
     * Returns the texture coordinates for this cube.
     * 
     * @return the texture coordinates
     */
    public float[] getTexCoords() {
        return texCoordsArray;
    }

    /**
     * Returns the vertex normals for this cube.
     * 
     * @return the vertex normals
     */
    public float[] getVertexNormals() {
        return vertexNormalsArray;
    }

    /**
     * Adds a Mesh to this Mesh, resulting in a Mesh consisting of all the data of the two Meshes
     * @param other Mesh to add
     * @return Mesh consisting of all the data of the two Meshes
     */
    public Mesh add(Mesh other) {
        Mesh result = new Mesh(this.verticesArray.length + other.verticesArray.length, this.texCoordsArray.length + other.texCoordsArray.length, this.vertexNormalsArray.length + other.vertexNormalsArray.length);

        // merge vertices
        for (int i = 0; i < this.verticesArray.length; i++) {
            result.verticesArray[i] = this.verticesArray[i];
        }
        for (int i = 0; i < other.verticesArray.length; i++) {
            result.verticesArray[this.verticesArray.length + i] = other.verticesArray[i];
        }

        // merge tex coords
        for (int i = 0; i < this.texCoordsArray.length; i++) {
            result.texCoordsArray[i] = this.texCoordsArray[i];
        }
        for (int i = 0; i < other.texCoordsArray.length; i++) {
            result.texCoordsArray[this.texCoordsArray.length + i] = other.texCoordsArray[i];
        }

        // merge vertex normals
        for (int i = 0; i < this.vertexNormalsArray.length; i++) {
            result.vertexNormalsArray[i] = this.vertexNormalsArray[i];
        }
        for (int i = 0; i < other.vertexNormalsArray.length; i++) {
            result.vertexNormalsArray[this.vertexNormalsArray.length + i] = other.vertexNormalsArray[i];
        }

        return result;
    }
}
