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
package fr.insalyon.creatis.vip.core.client.view.util;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Rafael Silva
 */
public class WidgetUtil {

    /**
     *
     * @param contents
     * @param height
     * @return
     */
    public static Label getLabel(String contents, int height) {
        return getLabel(contents, null, height);
    }

    /**
     *
     * @param contents
     * @param icon
     * @param height
     * @return
     */
    public static Label getLabel(String contents, String icon, int height) {
        return getLabel(contents, icon, height, Cursor.ARROW);
    }

    /**
     *
     * @param contents
     * @param height
     * @param cursor
     * @return
     */
    public static Label getLabel(String contents, int height, Cursor cursor) {
        return getLabel(contents, null, height, cursor);
    }

    /**
     *
     * @param contents
     * @param icon
     * @param height
     * @param cursor
     * @return
     */
    public static Label getLabel(String contents, String icon, int height, Cursor cursor) {

        Label label = new Label(contents);
        label.setIcon(icon);
        label.setHeight(height);
        label.setCursor(cursor);
        return label;
    }
    
    /**
     * 
     * @param width
     * @return 
     */
    public static VLayout getVIPLayout(int width) {
        
        return getVIPLayout(width, "100%");
    }

    /**
     * 
     * @param width
     * @param height
     * @return 
     */
    public static VLayout getVIPLayout(int width, int height) {
        
        return getVIPLayout(width, Integer.toString(height));
    }
    
    /**
     * 
     * @param width
     * @param height
     * @return 
     */
    public static VLayout getVIPLayout(int width, String height) {

        VLayout layout = new VLayout(5);
        layout.setWidth(width);
        layout.setHeight(height);
        layout.setBorder("1px solid #C0C0C0");
        layout.setBackgroundColor("#F5F5F5");
        layout.setPadding(10);

        Label vipLabel = getLabel("<font color=\"#C0C0C0\"><b>Virtual Imaging Platform</b></font>", 15);
        vipLabel.setWidth100();
        vipLabel.setAlign(Alignment.RIGHT);
        layout.addMember(vipLabel);

        return layout;
    }
}
