/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.smartgwt.client.types.Alignment;
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

    public static MultiFieldPanel getMultiFieldPanel(String id) {

        MultiFieldPanel mfp = new MultiFieldPanel();
        mfp.setBorder(false);
        mfp.setId(id);

        return mfp;
    }

    public static TextField getTextField(String id, int size, String label,
            boolean hideLabel) {

        TextField textField = new TextField(label);
        textField.setWidth(size);
        textField.setId(id);
        textField.setHideLabel(hideLabel);
        textField.setFieldLabel(label);

        return textField;
    }

    public static ComboBox getComboBox(String id, String label, int size,
            String emptyText, Store store, String displayField) {
        return getComboBox(id, label, size, emptyText, store, displayField, false);
    }

    public static ComboBox getComboBox(String id, String label, int size,
            String emptyText, Store store, String displayField, boolean forceSelection) {

        ComboBox comboBox = new ComboBox(label, id, size);
        comboBox.setReadOnly(true);
        comboBox.setEmptyText(emptyText);
        comboBox.setStore(store);
        comboBox.setDisplayField(displayField);
        comboBox.setMode(ComboBox.LOCAL);
        comboBox.setTriggerAction(ComboBox.ALL);
        comboBox.setForceSelection(forceSelection);

        return comboBox;
    }

    public static Store getComboBoxStore(String fieldDef) {

        MemoryProxy memoryProxy = new MemoryProxy(new Object[][]{new Object[]{}});
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef(fieldDef)
                });

        return new Store(memoryProxy, new ArrayReader(recordDef));
    }
}
