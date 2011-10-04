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
package fr.insalyon.creatis.vip.simulationgui.client.util.math;

/**
 * Represents a 4x4 matrix.
 * 
 * @author Sönke Sothmann
 * 
 */
public class FloatMatrix4x4 extends FloatMatrix {

    /**
     * Constructs a new instance of the FloatMatrix4x4.
     */
    public FloatMatrix4x4() {
        super(4, 4);
    }

    /**
     * Constructs a new instance of the FloatMatrix4x4 with the given data to
     * set.
     * 
     * @param data
     *            the data to set
     */
    public FloatMatrix4x4(float[][] data) {
        super(data);
        if ((data.length != 4) || (data[0].length != 4)
                || (data[1].length != 4) || (data[2].length != 4)
                || (data[3].length != 4)) {
            throw new IllegalArgumentException("matrix dimensions must be 4x4");
        }
    }

    /**
     * Creates a new FloatMatrix4x4 that is the result of the multiplication of
     * this and the other FloatMatrix.
     * 
     * @param other
     * @return the multiplied FloatMatrix
     */
    public FloatMatrix4x4 multiply(FloatMatrix4x4 other) {
        FloatMatrix4x4 result = new FloatMatrix4x4();
        multiplyImpl(other, result);
        return result;
    }

    /**
     * Creates a new FloatMatrix4x4 that is the transposed to this FloatMatrix.
     * 
     * @return the transposed matrix
     */
    @Override
    public FloatMatrix4x4 transpose() {
        FloatMatrix4x4 transposed = new FloatMatrix4x4();
        transposeImpl(transposed);
        return transposed;
    }

    /**
     * Calculate determinant of matrix
     * 
     * @return determinant
     */
    public float det() {
        if (this.width != 4 || this.height != 4) {
            throw new UnsupportedOperationException(
                    "calculation of det is only supported for 4x4 matrices");
        }
        float det = data[0][0] * data[1][1] * data[2][2] * data[3][3]
                + data[0][0] * data[1][2] * data[2][3] * data[3][1]
                + data[0][0] * data[1][3] * data[2][1] * data[3][2]
                + data[0][1] * data[1][0] * data[2][3] * data[3][2]
                + data[0][1] * data[1][2] * data[2][0] * data[3][3]
                + data[0][1] * data[1][3] * data[2][2] * data[3][0]
                + data[0][2] * data[1][0] * data[2][1] * data[3][3]
                + data[0][2] * data[1][1] * data[2][3] * data[3][0]
                + data[0][2] * data[1][3] * data[2][0] * data[3][1]
                + data[0][3] * data[1][0] * data[2][2] * data[3][1]
                + data[0][3] * data[1][1] * data[2][0] * data[3][2]
                + data[0][3] * data[1][2] * data[2][1] * data[3][0]
                - data[0][0] * data[1][1] * data[2][3] * data[3][2]
                - data[0][0] * data[1][2] * data[2][1] * data[3][3]
                - data[0][0] * data[1][3] * data[2][2] * data[3][1]
                - data[0][1] * data[1][0] * data[2][2] * data[3][3]
                - data[0][1] * data[1][2] * data[2][3] * data[3][0]
                - data[0][1] * data[1][3] * data[2][0] * data[3][2]
                - data[0][2] * data[1][0] * data[2][3] * data[3][1]
                - data[0][2] * data[1][1] * data[2][0] * data[3][3]
                - data[0][2] * data[1][3] * data[2][1] * data[3][0]
                - data[0][3] * data[1][0] * data[2][1] * data[3][2]
                - data[0][3] * data[1][1] * data[2][2] * data[3][0]
                - data[0][3] * data[1][2] * data[2][0] * data[3][1];
        return det;
    }

    /**
     * Calculate inverse matrix
     * 
     * @return inverse matrix
     */
    public FloatMatrix4x4 inverse() {
        if (this.width != 4 || this.height != 4) {
            throw new UnsupportedOperationException(
                    "inverse is only supported for 4x4 matrices");
        }
        float det = det();
        if (det == 0) {
            throw new IllegalStateException(
                    "inverse cannot be calculated because det is 0");
        }

        float b00, b01, b02, b03, b10, b11, b12, b13, b20, b21, b22, b23, b30, b31, b32, b33;
        b00 = data[1][1] * data[2][2] * data[3][3] + data[1][2] * data[2][3]
                * data[3][1] + data[1][3] * data[2][1] * data[3][2]
                - data[1][1] * data[2][3] * data[3][2] - data[1][2]
                * data[2][1] * data[3][3] - data[1][3] * data[2][2]
                * data[3][1];
        b01 = data[0][1] * data[2][3] * data[3][2] + data[0][2] * data[2][1]
                * data[3][3] + data[0][3] * data[2][2] * data[3][1]
                - data[0][1] * data[2][2] * data[3][3] - data[0][2]
                * data[2][3] * data[3][1] - data[0][3] * data[2][1]
                * data[3][2];
        b02 = data[0][1] * data[1][2] * data[3][3] + data[0][2] * data[1][3]
                * data[3][1] + data[0][3] * data[1][1] * data[3][2]
                - data[0][1] * data[1][3] * data[3][2] - data[0][2]
                * data[1][1] * data[3][3] - data[0][3] * data[1][2]
                * data[3][1];
        b03 = data[0][1] * data[1][3] * data[2][2] + data[0][2] * data[1][1]
                * data[2][3] + data[0][3] * data[1][2] * data[2][1]
                - data[0][1] * data[1][2] * data[2][3] - data[0][2]
                * data[1][3] * data[2][1] - data[0][3] * data[1][1]
                * data[2][2];
        b10 = data[1][0] * data[2][3] * data[3][2] + data[1][2] * data[2][0]
                * data[3][3] + data[1][3] * data[2][2] * data[3][0]
                - data[1][0] * data[2][2] * data[3][3] - data[1][2]
                * data[2][3] * data[3][0] - data[1][3] * data[2][0]
                * data[3][2];
        b11 = data[0][0] * data[2][2] * data[3][3] + data[0][2] * data[2][3]
                * data[3][0] + data[0][3] * data[2][0] * data[3][2]
                - data[0][0] * data[2][3] * data[3][2] - data[0][2]
                * data[2][0] * data[3][3] - data[0][3] * data[2][2]
                * data[3][0];
        b12 = data[0][0] * data[1][3] * data[3][2] + data[0][2] * data[1][0]
                * data[3][3] + data[0][3] * data[1][2] * data[3][0]
                - data[0][0] * data[1][2] * data[3][3] - data[0][2]
                * data[1][3] * data[3][0] - data[0][3] * data[1][0]
                * data[3][2];
        b13 = data[0][0] * data[1][2] * data[2][3] + data[0][2] * data[1][3]
                * data[2][0] + data[0][3] * data[1][0] * data[2][2]
                - data[0][0] * data[1][3] * data[2][2] - data[0][2]
                * data[1][0] * data[2][3] - data[0][3] * data[1][2]
                * data[2][0];
        b20 = data[1][0] * data[2][1] * data[3][3] + data[1][1] * data[2][3]
                * data[3][0] + data[1][3] * data[2][0] * data[3][1]
                - data[1][0] * data[2][3] * data[3][1] - data[1][1]
                * data[2][0] * data[3][3] - data[1][3] * data[2][1]
                * data[3][0];
        b21 = data[0][0] * data[2][3] * data[3][1] + data[0][1] * data[2][0]
                * data[3][3] + data[0][3] * data[2][1] * data[3][0]
                - data[0][0] * data[2][1] * data[3][3] - data[0][1]
                * data[2][3] * data[3][0] - data[0][3] * data[2][0]
                * data[3][1];
        b22 = data[0][0] * data[1][1] * data[3][3] + data[0][1] * data[1][3]
                * data[3][0] + data[0][3] * data[1][0] * data[3][1]
                - data[0][0] * data[1][3] * data[3][1] - data[0][1]
                * data[1][0] * data[3][3] - data[0][3] * data[1][1]
                * data[3][0];
        b23 = data[0][0] * data[1][3] * data[2][1] + data[0][1] * data[1][0]
                * data[2][3] + data[0][3] * data[1][1] * data[2][0]
                - data[0][0] * data[1][1] * data[2][3] - data[0][1]
                * data[1][3] * data[2][0] - data[0][3] * data[1][0]
                * data[2][1];
        b30 = data[1][0] * data[2][2] * data[3][1] + data[1][1] * data[2][0]
                * data[3][2] + data[1][2] * data[2][1] * data[3][0]
                - data[1][0] * data[2][1] * data[3][2] - data[1][1]
                * data[2][2] * data[3][0] - data[1][2] * data[2][0]
                * data[3][1];
        b31 = data[0][0] * data[2][1] * data[3][2] + data[0][1] * data[2][2]
                * data[3][0] + data[0][2] * data[2][0] * data[3][1]
                - data[0][0] * data[2][2] * data[3][1] - data[0][1]
                * data[2][0] * data[3][2] - data[0][2] * data[2][1]
                * data[3][0];
        b32 = data[0][0] * data[1][2] * data[3][1] + data[0][1] * data[1][0]
                * data[3][2] + data[0][2] * data[1][1] * data[3][0]
                - data[0][0] * data[1][1] * data[3][2] - data[0][1]
                * data[1][2] * data[3][0] - data[0][2] * data[1][0]
                * data[3][1];
        b33 = data[0][0] * data[1][1] * data[2][2] + data[0][1] * data[1][2]
                * data[2][0] + data[0][2] * data[1][0] * data[2][1]
                - data[0][0] * data[1][2] * data[2][1] - data[0][1]
                * data[1][0] * data[2][2] - data[0][2] * data[1][1]
                * data[2][0];

        FloatMatrix4x4 result = new FloatMatrix4x4(new float[][]{
                    new float[]{b00, b01, b02, b03},
                    new float[]{b10, b11, b12, b13},
                    new float[]{b20, b21, b22, b23},
                    new float[]{b30, b31, b32, b33}});
        return result;
    }
}