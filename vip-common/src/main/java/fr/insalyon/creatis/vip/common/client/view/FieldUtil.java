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
package fr.insalyon.creatis.vip.common.client.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 *
 * @author Rafael Silva
 */
public class FieldUtil {
    
    /**
     * Gets a ListGridField configured to display an icon.
     * 
     * @param name Field name
     * @return List grid field
     */
    public static ListGridField getIconGridField(String name) {
        ListGridField iconField = new ListGridField(name, " ", 30);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLSuffix(".png");
        iconField.setImageWidth(12);
        iconField.setImageHeight(12);
        return iconField;
    }
    
    /**
     * Gets a ListGridField configured to display dates.
     * 
     * @return List grid field
     */
    public static ListGridField getDateField() {
        ListGridField dateField = new ListGridField("date", "Date", 120);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);
        return dateField;
    }
}
