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

import java.util.ArrayList;
import java.util.List;

import static fr.insalyon.creatis.vip.simulationgui.client.util.ConversionUtils.*;

/**
 * Helper class for creation of sphere mesh Inspired by "The WebGL Cookbook"
 * - http://learningwebgl.com/cookbook/index.php/How_to_draw_a_sphere
 * 
 * @author Sönke Sothmann
 * 
 */
public class Sphere extends IndexedMesh {

    /**
     * Create sphere geometry
     * @param latitudeBands
     * @param longitudeBands
     * @param radius
     */
    public Sphere(int latitudeBands, int longitudeBands, int radius) {
        List<Float> vertexNormalsList = new ArrayList<Float>();
        List<Float> texCoordsList = new ArrayList<Float>();
        List<Float> verticesList = new ArrayList<Float>();

        for (int latNumber = 0; latNumber <= latitudeBands; latNumber++) {
            double theta = latNumber * Math.PI / latitudeBands;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                double phi = longNumber * 2 * Math.PI / longitudeBands;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = cosPhi * sinTheta;
                double y = cosTheta;
                double z = sinPhi * sinTheta;
                double u = 1 - (longNumber / longitudeBands);
                double v = latNumber / latitudeBands;

                vertexNormalsList.add((float) x);
                vertexNormalsList.add((float) y);
                vertexNormalsList.add((float) z);
                texCoordsList.add((float) u);
                texCoordsList.add((float) v);
                verticesList.add((float) (radius * x));
                verticesList.add((float) (radius * y));
                verticesList.add((float) (radius * z));
            }
        }

        List<Integer> indicesList = new ArrayList<Integer>();
        for (int latNumber = 0; latNumber < latitudeBands; latNumber++) {
            for (int longNumber = 0; longNumber < longitudeBands; longNumber++) {
                int first = ((latNumber * (longitudeBands + 1)) + longNumber);
                int second = (first + longitudeBands + 1);
                indicesList.add(first);
                indicesList.add(second);
                indicesList.add((first + 1));

                indicesList.add(second);
                indicesList.add(second + 1);
                indicesList.add(first + 1);
            }
        }

        verticesArray = floatListToFloatArray(verticesList);
        texCoordsArray = floatListToFloatArray(texCoordsList);
        vertexNormalsArray = floatListToFloatArray(vertexNormalsList);
        indices = integerListToIntegerArray(indicesList);
    }

    /**
     * Create sphere geometry with latitudeBands=30, longitudeBands=30 and radius=2
     */
    public Sphere() {
        this(30, 30, 2);
    }
}
