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
package fr.insalyon.creatis.vip.simulationgui.client.util.math;

import java.util.Arrays;

/**
 * TODO should we use a "standard" Java matrix and vector api? Does it work in
 * GWT?
 * 
 * Representation of a matrix constructed of float fields. The first dimension
 * of the data array represents the height of the Matrix and the second
 * dimension represents the width. So the matrix is saved line-wise.
 * 
 * @author Steffen Schäfer
 * @author Sönke Sothmann
 * 
 */
public class FloatMatrix {

    /**
     * The data containing the values of the matix. The format is [row][column].
     */
    protected float[][] data;
    /**
     * The width of the matrix.
     */
    protected final int width;
    /**
     * The heigth of the matrix.
     */
    protected final int height;

    /**
     * Constructs a square FloatMatrix with width and height = size.
     * 
     * @param size
     *            the width and height of the FloatMatrix
     */
    public FloatMatrix(int size) {
        this(size, size);
    }

    /**
     * Constructs a FloatMatrix with the given width and height.
     * 
     * @param width
     *            the width of the FloatMatrix
     * @param height
     *            the height of the FloatMatrix
     */
    public FloatMatrix(int width, int height) {
        this.width = width;
        this.height = height;
        data = new float[height][width];
    }

    /**
     * Constructs a new FloatMatrix with the given data to set.
     * 
     * @param newData
     *            the data to use
     */
    public FloatMatrix(float[][] newData) {
        this(newData[0].length, newData.length);
        setData(newData);
    }

    /**
     * Sets the data of the FloatMatrix in a flat row-wise representation.
     * 
     * @param newData
     *            the new data to set
     */
    public void setData(float... newData) {
        if (newData.length != width * height) {
            throw new IllegalArgumentException(
                    "The given data array has an incorrect size.");
        }
        for (int i = 0; i < height; i++) {
            System.arraycopy(newData, i * width, data[i], 0, width);
        }
    }

    /**
     * Sets the new data of the FloatMatrix.
     * 
     * @param newData
     *            the new data to set
     */
    public void setData(float[][] newData) {
        if (newData.length != height) {
            throw new IllegalArgumentException(
                    "The given data array has an incorrect size.");
        }
        for (int i = 0; i < height; i++) {
            if (newData[i].length != width) {
                throw new IllegalArgumentException(
                        "The given data array has an incorrect size.");
            }
            System.arraycopy(newData[i], 0, data[i], 0, width);
        }
    }

    /**
     * returns a copy of the inner data.
     * 
     * @return 2 dimensional data array - [row][column]
     */
    public float[][] getData() {
        float[][] result = new float[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(data[i], 0, result[i], 0, width);
            // doesn't work in GWT
            // result[i] = Arrays.copyOf(data[i], width);
        }
        return result;
    }

    /**
     * Returns the data in a flat row-wise representation.
     * 
     * @return the flat data as array
     */
    public float[] getFlatData() {
        float[] flatDta = new float[height * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(data[i], 0, flatDta, i * width, width);
        }
        return flatDta;
    }

    /**
     * Returns the data in a flat column-wise representation.
     * 
     * @return the flat data as array
     */
    public float[] getColumnWiseFlatData() {
        float[] flatDta = new float[height * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                flatDta[i * height + j] = data[j][i];
            }
        }
        return flatDta;
    }

    /**
     * Creates a new FloatMatrix that is the transposed to this FloatMatrix.
     * 
     * @return the transposed matrix
     */
    public FloatMatrix transpose() {
        FloatMatrix transposed = new FloatMatrix(height, width);
        transposeImpl(transposed);
        return transposed;
    }

    /**
     * The inner impl of the transpose method.
     * 
     * @param result
     */
    protected void transposeImpl(FloatMatrix result) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.data[j][i] = this.data[i][j];
            }
        }
    }

    /**
     * Creates a new FloatMatrix that is the result of the multiplication of
     * this and the other FloatMatrix.
     * 
     * @param other
     * @return the multiplied FloatMatrix
     */
    public FloatMatrix multiply(FloatMatrix other) {
        if (this.width != other.height) {
            throw new RuntimeException(
                    "The width of this matrix must match the height of the other matrix!");
        }
        FloatMatrix result = new FloatMatrix(other.width, this.height);
        multiplyImpl(other, result);

        return result;
    }

    /**
     * Impl of the multiply method.
     * 
     * @param other
     * @param result
     */
    protected void multiplyImpl(FloatMatrix other, FloatMatrix result) {
        for (int i = 0; i < other.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (int k = 0; k < this.width; k++) {
                    result.data[j][i] += (this.data[j][k] * other.data[k][i]);
                }
            }
        }
    }

    /**
     * Main method for simple testing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        FloatMatrix m1 = new FloatMatrix(new float[][]{new float[]{1, 2},
                    new float[]{3, 4}});
        FloatMatrix m2 = new FloatMatrix(new float[][]{new float[]{5, 6},
                    new float[]{7, 8}});

        System.out.println(Arrays.toString(m1.multiply(m2).getFlatData()));
    }
}
