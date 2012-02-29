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
package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import java.util.LinkedHashMap;

/**
 *
 * @author Sorina Camarasu
 */
public class GateLabInput extends VLayout {

    private String name;
    private String comment;
    private HLayout hLayout;
    private SelectItem selectItem;
    private TextItem textItem;
    private DynamicForm itemForm;
    private IButton browseButton;
    private Label label;
    private Boolean isSelectItem;
    //private TextItem listItem;

    public GateLabInput(String name, String comment) {

        this.name = name;
        this.comment = comment;

        if (this.name.compareToIgnoreCase("GateRelease") == 0) {
            hLayout = new HLayout(3);
        } else {
            hLayout = new HLayout(2);
        }

        label = new Label(name + ":");
        label.setWidth(150);
        label.setAlign(Alignment.RIGHT);
        if (comment != null) {
            label.setTooltip(comment);
            label.setHoverWidth(500);
        }
        hLayout.addMember(label);

        if ((this.name.compareToIgnoreCase("CPUestimation") == 0)) {

            selectItem = new SelectItem();
            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            valueMap.put("1", "A few minutes");
            valueMap.put("2", "A few hours");
            valueMap.put("3", "A few days");
            valueMap.put("4", "More than a few days");

            selectItem.setValueMap(valueMap);
            //selectItem.setValueMap("1", "2", "3", "4");
            selectItem.setTitle("");
            selectItem.setWidth(400);
            itemForm = FieldUtil.getForm(selectItem);
            isSelectItem = true;

        } else {
            if ((this.name.compareToIgnoreCase("ParallelizationType") == 0)) {
                selectItem = new SelectItem();
                LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
                valueMap.put("stat", "Static");
                valueMap.put("dyn", "Dynamic");

                selectItem.setValueMap(valueMap);
                //selectItem.setValueMap("stat", "dyn");
                selectItem.setTitle("");
                selectItem.setWidth(400);
                itemForm = FieldUtil.getForm(selectItem);
                isSelectItem = true;
            } else {
                textItem = FieldUtil.getTextItem(400, false, "", null);
                itemForm = FieldUtil.getForm(textItem);
                isSelectItem = false;

            }
        }

        hLayout.addMember(itemForm);
        if (this.name.compareToIgnoreCase("GateRelease") == 0) {

            this.textItem.setValue("/vip/GateLab (group)/releases/current_gate_release.tgz");

            browseButton = new IButton("Browse");
            browseButton.setWidth(60);
            browseButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    new PathSelectionWindow(textItem).show();
                }
            });
            hLayout.addMember(browseButton);
        }

        if ((this.name.compareToIgnoreCase("GateInput") == 0) || (this.name.compareToIgnoreCase("NumberOfParticles") == 0)) {
            this.textItem.setDisabled(true);
        }

        this.addMember(hLayout);
    }

    public boolean validate() {

        if (this.isSelectItem) {
            return this.selectItem.validate();
        } else {
            return textItem.validate();
        }
    }

    public String getName() {

        return this.name;
    }

    public String getValue() {

        if (this.isSelectItem) {
            return this.selectItem.getValueAsString();
        } else {
            return this.textItem.getValueAsString();
        }
    }

    public void setValue(String value) {
        if (this.isSelectItem) {
            this.selectItem.setValue(value);
             if (this.name.equals("ParallelizationType") && value.equals("stat")) {
                    this.selectItem.setDisabled(true);
             }
        } else {
            this.textItem.setValue(value);
        }
    }

    public void setTextNonEdit() {
        if (this.isSelectItem) {
            this.selectItem.setDisabled(true);
        } else {
            this.textItem.setDisabled(true);
        }
    }
}
