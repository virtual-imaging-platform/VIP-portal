package fr.insalyon.creatis.vip.simulationgui.client.util.mesh;

/**
 * Class that holds a mesh's data 
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
        public Mesh add(Mesh other){
                Mesh result = new Mesh(this.verticesArray.length + other.verticesArray.length, this.texCoordsArray.length + other.texCoordsArray.length, this.vertexNormalsArray.length + other.vertexNormalsArray.length);
                
                // merge vertices
                for(int i=0; i<this.verticesArray.length; i++){
                        result.verticesArray[i] = this.verticesArray[i];
                }
                for(int i=0; i<other.verticesArray.length; i++){
                        result.verticesArray[this.verticesArray.length+i] = other.verticesArray[i];
                }
                
                // merge tex coords
                for(int i=0; i<this.texCoordsArray.length; i++){
                        result.texCoordsArray[i] = this.texCoordsArray[i];
                }
                for(int i=0; i<other.texCoordsArray.length; i++){
                        result.texCoordsArray[this.texCoordsArray.length+i] = other.texCoordsArray[i];
                }
                
                // merge vertex normals
                for(int i=0; i<this.vertexNormalsArray.length; i++){
                        result.vertexNormalsArray[i] = this.vertexNormalsArray[i];
                }
                for(int i=0; i<other.vertexNormalsArray.length; i++){
                        result.vertexNormalsArray[this.vertexNormalsArray.length+i] = other.vertexNormalsArray[i];
                }
                
                return result;
        }
}
