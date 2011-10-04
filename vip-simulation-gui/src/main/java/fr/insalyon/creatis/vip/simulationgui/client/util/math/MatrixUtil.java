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

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Contains several helper methods to construct matrices like projection (etc).
 * 
 * @author Steffen Schäfer
 * @author Sönke Sothmann
 * 
 */
public final class MatrixUtil {

    private MatrixUtil() {
    }

    /**
     * 
     * @param fieldOfViewVertical
     *            Vertikaler Öffnungswinkel in Grad
     * @param aspectRatio
     *            Verhältnis von Höhe zu Breite
     * @param minimumClearance
     *            Mindestabstand sichtbarer Punkte
     * @param maximumClearance
     *            Höchstabstand sichtbarer Punkte
     * @return the created perspective matrix
     */
    public static FloatMatrix4x4 createPerspectiveMatrix(
            int fieldOfViewVertical, float aspectRatio, float minimumClearance,
            float maximumClearance) {
        double fieldOfViewInRad = fieldOfViewVertical * Math.PI / 180.0;
        return new FloatMatrix4x4(new float[][]{
                    new float[]{
                        (float) (Math.tan(fieldOfViewInRad) / aspectRatio), 0,
                        0, 0},
                    new float[]{
                        0,
                        (float) (1 / Math.tan(fieldOfViewVertical * Math.PI
                        / 180.0)), 0, 0},
                    new float[]{
                        0,
                        0,
                        (minimumClearance + maximumClearance)
                        / (minimumClearance - maximumClearance),
                        2 * minimumClearance * maximumClearance
                        / (minimumClearance - maximumClearance)},
                    new float[]{0, 0, -1, 0}});
    }

    ;

        /**
         * Creates a rotation matrix.
         * 
         * @param angleX
         *            the angle in degrees for the rotation around the x axis
         * @param angleY
         *            the angle in degrees for the rotation around the y axis
         * @param angleZ
         *            the angle in degrees for the rotation around the z axis
         * @return the created matrix
         */
        public static FloatMatrix4x4 createRotationMatrix(int angleX, int angleY,
            int angleZ) {
        return createRotationMatrixX(angleX).multiply(
                createRotationMatrixY(angleY)).multiply(
                createRotationMatrixZ(angleZ));
    }

    private static FloatMatrix4x4 createRotationMatrixX(int angle) {
        double angleInRad = angle * (Math.PI / 180.0);

        // 1 0 0 0
        //
        // 0 cos(q) sin(q) 0
        //
        // 0 -sin(q) cos(q) 0
        //
        // 0 0 0 1

        return new FloatMatrix4x4(new float[][]{
                    new float[]{1, 0, 0, 0},
                    new float[]{0, (float) cos(angleInRad),
                        (float) sin(angleInRad), 0},
                    new float[]{0, (float) -sin(angleInRad),
                        (float) cos(angleInRad), 0},
                    new float[]{0, 0, 0, 1}});
    }

    private static FloatMatrix4x4 createRotationMatrixY(int angle) {
        double angleInRad = angle * (Math.PI / 180.0);

        // cos(a) 0 -sin(a) 0
        //
        // 0 1 0 0
        //
        // sin(a) 0 cos(a) 0
        //
        // 0 0 0 1

        return new FloatMatrix4x4(new float[][]{
                    new float[]{(float) cos(angleInRad), 0,
                        (float) -sin(angleInRad), 0},
                    new float[]{0, 1, 0, 0},
                    new float[]{(float) sin(angleInRad), 0.0f,
                        (float) cos(angleInRad), 0.0f},
                    new float[]{0, 0, 0, 1}});
    }

    private static FloatMatrix4x4 createRotationMatrixZ(int angle) {
        double angleInRad = angle * (Math.PI / 180.0);

        // cos(a) sin(a) 0 0
        //
        // -sin(a) cos(a) 0 0
        //
        // 0 0 1 0
        //
        // 0 0 0 1

        return new FloatMatrix4x4(new float[][]{
                    new float[]{(float) cos(angleInRad), (float) sin(angleInRad),
                        0, 0},
                    new float[]{(float) -sin(angleInRad),
                        (float) cos(angleInRad), 0, 0},
                    new float[]{0, 0, 1, 0}, new float[]{0, 0, 0, 1}});
    }

    /**
     * Creates a translation matrix.
     * 
     * @param translateX
     *            the amount to translate parallel to the x axis
     * @param translateY
     *            the amount to translate parallel to the y axis
     * @param translateZ
     *            the amount to translate parallel to the z axis
     * @return the created matrix
     */
    public static FloatMatrix4x4 createTranslationMatrix(float translateX,
            float translateY, float translateZ) {
        return new FloatMatrix4x4(
                new float[][]{new float[]{1, 0, 0, translateX},
                    new float[]{0, 1, 0, translateY},
                    new float[]{0, 0, 1, translateZ},
                    new float[]{0, 0, 0, 1}});
    }
}
