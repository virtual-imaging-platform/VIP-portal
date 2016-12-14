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

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ListHLayout extends HLayout {

    private ListHLayout instance;
    private TextItem listItem;
    private DynamicForm listItemForm;

    public ListHLayout(final VLayout parent, boolean master) {
        this(parent, master, "", false);
    }
    
    public ListHLayout(final VLayout parent, boolean master, String value) {
        this(parent, master, value, false);
    }

    /**
     * Constructs a ListHLayout instance which can be included in an optional input.
     * In that case, the required message of the ListHLayout object has to be different from a classical ListHLayout object.
     * And the ListHLayout object (in an optional input) has to be able to create a new "optional" ListHLayout object : see below, onFormItemClick(..) method from morePicker object.
     * 
     * @param parent
     * @param master
     * @param value
     * @param optional 
     */
    public ListHLayout(final VLayout parent, boolean master, String value, final boolean optional) {

        this.instance = this;
        this.setMembersMargin(3);

        listItem = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(){} ]");
        listItem.setValue(value);
        listItem.setValidators(ValidatorUtil.getStringValidator());
        
        if (optional) {
            listItem.setRequiredMessage(ApplicationConstants.INPUT_WITHOUT_VALUE_REQUIRED_MESSAGE);
        }
        
        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                new PathSelectionWindow(listItem).show();
            }
        });
        browsePicker.setPrompt("Browse on the Grid");
        
        PickerIcon morePicker = new PickerIcon(new PickerIcon.Picker(ApplicationConstants.ICON_PICKER_MORE), new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                if (optional) {
                    parent.addMember(new ListHLayout(parent, false, "", optional));
                }
                else {
                     parent.addMember(new ListHLayout(parent, false));
                }
            }
        });
        morePicker.setPrompt("Add");
        
        PickerIcon lessPicker = new PickerIcon(new PickerIcon.Picker(ApplicationConstants.ICON_PICKER_LESS), new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                parent.removeMember(instance);
            }
        });
        lessPicker.setPrompt("Remove");
        
        if (master) {
            listItem.setIcons(morePicker, browsePicker);
        } else {
            listItem.setIcons(lessPicker, browsePicker);
        }
        
        listItemForm = FieldUtil.getForm(listItem);
        this.addMember(listItemForm);
    }

    public boolean validate() {
        return listItem.validate();
    }

    public String getValue() {
        return listItem.getValueAsString();
    }
}
