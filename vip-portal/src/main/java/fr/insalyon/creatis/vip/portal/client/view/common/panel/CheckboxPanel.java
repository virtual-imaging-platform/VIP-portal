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
package fr.insalyon.creatis.vip.portal.client.view.common.panel;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Label;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class CheckboxPanel extends Panel {

    private Panel panel;

    /**
     * Creates a checkbox panel
     *
     * @param id Panel identification
     * @param heading Label of the panel
     * @param width Size of the panel
     */
    public CheckboxPanel(String id, String heading, int width) {
        this.setId(id);
        this.setStyleName("x-form-item");
        this.setBorder(false);
        this.setWidth(width);
        this.add(new Label(heading));

        panel = new Panel();
        panel.setBorder(false);
        this.add(panel);
    }

    /**
     * Add a checkbox to the panel
     * 
     * @param label Label of the checkbox
     * @param value Value of the checkbox
     * @param checked If the checkbox is checked
     */
    public void addCheckbox(String label, String value, boolean checked) {
        Panel row = new Panel();
        row.setBorder(false);
        Checkbox cb = new Checkbox(label);
        cb.setInputValue(value);
        cb.setChecked(checked);
        row.add(cb);
        panel.add(row);
    }

    /**
     * Get a list of values of the selected checkboxes
     *
     * @return
     */
    public List<String> getSelectedValues() {

        List<String> selectedValues = new ArrayList<String>();

        for (Component c : panel.getComponents()) {
            Panel p = (Panel) c;
            for (Component cp : p.getComponents()) {
                Checkbox cb = (Checkbox) cp;
                if (cb.getValue()) {
                    selectedValues.add(cb.getBoxLabel());
                }
            }
        }

        return selectedValues;
    }

    /**
     * Removes all checkboxes in the panel
     */
    public void removeCheckBoxes() {
        panel.removeAll();
    }
}
