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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSourceLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class InputLayout extends AbstractSourceLayout {

    private static enum InputType {

        List, Range;

        public static String[] valuesAsString() {
            List<String> list = new ArrayList<String>();
            for (InputType type : values()) {
                list.add(type.name());
            }
            return list.toArray(new String[]{});
        }
    };
    private SelectItem selectItem;
    // List items
    private VLayout listLayout;
    // Range Items
    private TextItem startItem;
    private TextItem stopItem;
    private TextItem stepItem;
    private DynamicForm startItemForm;
    private DynamicForm stopItemForm;
    private DynamicForm stepItemForm;

    public InputLayout(String name, String comment, boolean optional) {
        super(name, comment,optional);
        
        configureTypeSelectItem();
        hLayout.addMember(FieldUtil.getForm(selectItem));

        // List
        listLayout = new VLayout();
        listLayout.addMember(new ListHLayout(listLayout, true));
        hLayout.addMember(listLayout);

        // Range
        startItem = FieldUtil.getTextItem(70, true, "Start", "[0-9.]");
        stopItem = FieldUtil.getTextItem(70, true, "Stop", "[0-9.]");
        stepItem = FieldUtil.getTextItem(70, true, "Step", "[0-9.]");
        startItemForm = FieldUtil.getForm(startItem);
        stopItemForm = FieldUtil.getForm(stopItem);
        stepItemForm = FieldUtil.getForm(stepItem);
    }

    
    public InputLayout(String name, String comment) {
        this(name,comment,false);
    }

    private void configureTypeSelectItem() {

        selectItem = new SelectItem();
        selectItem.setShowTitle(false);
        selectItem.setValueMap(InputType.valuesAsString());
        selectItem.setValue(InputType.List.name());
        selectItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                InputType type = InputType.valueOf((String) event.getValue());
                if (type == InputType.List) {
                    setList();
                } else {
                    setRange();
                }
            }
        });
    }

    private void setList() {

        hLayout.addMember(listLayout);
        hLayout.removeMember(startItemForm);
        hLayout.removeMember(stopItemForm);
        hLayout.removeMember(stepItemForm);
    }

    private void setRange() {

        hLayout.removeMember(listLayout);
        hLayout.addMember(startItemForm);
        hLayout.addMember(stopItemForm);
        hLayout.addMember(stepItemForm);
    }

    @Override
    public boolean validate() {

        InputType type = InputType.valueOf(selectItem.getValueAsString());
        if (type == InputType.List) {
            boolean valid = true;
            for (Canvas canvas : listLayout.getMembers()) {
                if (canvas instanceof ListHLayout) {
                    ListHLayout item = (ListHLayout) canvas;
                    if (!item.validate()) {
                        valid = false;
                    }
                }
            }
            return valid;

        } else {
            return startItem.validate() & stopItem.validate() & stepItem.validate();
        }
    }

    @Override
    public String getValue() {

        InputType type = InputType.valueOf(selectItem.getValueAsString());
        if (type == InputType.List) {
            StringBuilder sb = new StringBuilder();
            for (Canvas canvas : listLayout.getMembers()) {
                if (canvas instanceof ListHLayout) {
                    ListHLayout item = (ListHLayout) canvas;
                    if (sb.length() > 0) {
                        sb.append(ApplicationConstants.SEPARATOR_LIST);
                    }
                    sb.append(item.getValue());
                }
            }
            return sb.toString();

        } else {
            return startItem.getValueAsString().trim()
                    + ApplicationConstants.SEPARATOR_INPUT
                    + stopItem.getValueAsString().trim()
                    + ApplicationConstants.SEPARATOR_INPUT
                    + stepItem.getValueAsString().trim();
        }
    }

    @Override
    public void setValue(String value) {

        if (value.contains("Start: ")) { // Range
            selectItem.setValue(InputType.Range.name());
            setRange();
            String[] v = value.split("[:-]");
            startItem.setValue(v[1].trim());
            stopItem.setValue(v[3].trim());
            stepItem.setValue(v[5].trim());

        } else { // List
            selectItem.setValue(InputType.List.name());
            setList();
            listLayout.removeMembers(listLayout.getMembers());
            boolean master = true;
            for (String v : value.split("; ")) {
                listLayout.addMember(new ListHLayout(listLayout, master, v));
                if (master) {
                    master = false;
                }
            }
        }
    }
}
