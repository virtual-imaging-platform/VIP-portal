/**   
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
 * Represents a vector with two elements.
 * 
 * @author Sönke Sothmann
 * @author Steffen Schäfer
 * 
 */
public class Vector2f implements Vectorf {
        private float u;
        private float v;

        /**
         * Constructs a new instance of the Vector2f with the given coordinates to
         * set.
         * 
         * @param u
         * @param v
         */
        public Vector2f(float u, float v) {
                super();
                this.u = u;
                this.v = v;
        }

        /**
         * Returns the u coordinate.
         * 
         * @return the u coordinate
         */
        public float getU() {
                return u;
        }

        /**
         * Sets the u coordinate.
         * 
         * @param u
         *            the u coordinate to set
         */
        public void setU(float u) {
                this.u = u;
        }

        /**
         * Returns the v coordinate.
         * 
         * @return the v coordinate
         */
        public float getV() {
                return v;
        }

        /**
         * Sets the v coordinate.
         * 
         * @param v
         *            the v coordinate to set
         */
        public void setV(float v) {
                this.v = v;
        }

        /* (non-Javadoc)
         * @see com.googlecode.gwtgl.example.client.util.math.Vector#multiply(float)
         */
        @Override
        public Vectorf multiply(float scalar) {
                return new Vector2f(this.u * scalar, this.v * scalar);
        }

        /* (non-Javadoc)
         * @see com.googlecode.gwtgl.example.client.util.math.Vector#length()
         */
        @Override
        public float length() {
                return (float) Math.sqrt(this.u * this.u + this.v * this.v);
        }

        /* (non-Javadoc)
         * @see com.googlecode.gwtgl.example.client.util.math.Vector#toUnitVector()
         */
        @Override
        public Vectorf toUnitVector() {
                float length = length();
                return new Vector2f(this.u / length, this.v / length);
        }

        /* (non-Javadoc)
         * @see com.googlecode.gwtgl.example.client.util.math.Vector#toArray()
         */
        @Override
        public float[] toArray() {
                return new float[] { this.u, this.v };
        }
}
