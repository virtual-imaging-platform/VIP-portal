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
package fr.insalyon.creatis.vip.simulationgui.client.util;

import fr.insalyon.creatis.vip.simulationgui.client.util.math.FloatMatrix;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * Widget to display a FloatMatrix.
 * 
 * @author Sönke Sothmann
 * @author Steffen Schäfer
 */
public class MatrixWidget extends Composite {

    private int width;
    private int height;
    private FlexTable flextable;
    static private NumberFormat formatter = NumberFormat.getFormat("#########0.00");

    /**
     * Constructs a new instance of the MatrixWidget to view a FloatMatrix of
     * the given size.
     * 
     * @param width
     *            column count of matrix
     * @param height
     *            row count of matrix
     * @param title
     *            the title of the matrix
     */
    public MatrixWidget(int width, int height, String title) {
        
        this.width = width;
        this.height = height;
        flextable = new FlexTable();
        flextable.setWidget(0, 0, new Label(title));
        flextable.getFlexCellFormatter().setColSpan(0, 0, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                flextable.setText(y + 1, x, "[" + String.valueOf(x) + "_"
                        + String.valueOf(y) + "]");
            }
        }

        initWidget(flextable);
        setStyleName("matrixWidget");
    }

    /**
     * Set widget display data to data of the given FloatMatrix
     * 
     * @param matrix
     */
    public void setData(FloatMatrix matrix) {
        
        if (matrix == null) {
            return;
        }
        float[][] data = matrix.getData();
        for (int spalte = 0; spalte < width; spalte++) {
            for (int zeile = 0; zeile < height; zeile++) {
                flextable.setText(zeile + 1, spalte, "["
                        + formatter.format(data[zeile][spalte]) + "]");
            }
        }
    }
}
