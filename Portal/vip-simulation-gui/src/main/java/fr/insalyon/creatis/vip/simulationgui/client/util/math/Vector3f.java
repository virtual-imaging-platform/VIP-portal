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
 * 
 * --
 * Copyright 2009-2010 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.insalyon.creatis.vip.simulationgui.client.util.math;

/**
 * Represents a vector with three elements.
 * 
 * @author Sönke Sothmann
 * 
 */
public class Vector3f implements Vectorf {

    private float x;
    private float y;
    private float z;

    /**
     * Constructs a new instance of the Vector3f with the given coordinates to
     * set.
     * 
     * @param x
     * @param y
     * @param z
     */
    public Vector3f(float x, float y, float z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x coordinate.
     * 
     * @return the x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     * 
     * @param x
     *            the x coordinate to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Returns the z coordinate.
     * 
     * @return the z coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     * 
     * @param y
     *            the y coordinate to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns the y coordinate.
     * 
     * @return the y coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Sets the z coordinate.
     * 
     * @param z
     *            the z coordinate to set
     */
    public void setZ(float z) {
        this.z = z;
    }

    /* (non-Javadoc)
     * @see com.googlecode.gwtgl.example.client.util.math.Vector#multiply(float)
     */
    @Override
    public Vectorf multiply(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /* (non-Javadoc)
     * @see com.googlecode.gwtgl.example.client.util.math.Vector#length()
     */
    @Override
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z
                * this.z);
    }

    /* (non-Javadoc)
     * @see com.googlecode.gwtgl.example.client.util.math.Vector#toUnitVector()
     */
    @Override
    public Vectorf toUnitVector() {
        float length = length();
        return new Vector3f(this.x / length, this.y / length, this.z / length);
    }

    /* (non-Javadoc)
     * @see com.googlecode.gwtgl.example.client.util.math.Vector#toArray()
     */
    @Override
    public float[] toArray() {
        return new float[]{this.x, this.y, this.z};
    }
}