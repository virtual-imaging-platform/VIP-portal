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
package fr.insalyon.creatis.vip.portal.client.view.system.configuration.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class EditGroupStackSection extends SectionStackSection {

    private String oldName = null;
    private boolean newGroup = true;
    private DynamicForm form;
    private TextItem nameItem;

    public EditGroupStackSection() {

        this.setTitle("Add Group");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureForm();

        VLayout vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        vLayout.addMember(form);

        this.addItem(vLayout);
    }

    private void configureForm() {
        form = new DynamicForm();
        form.setWidth(500);

        nameItem = new TextItem("name", "Name");
        nameItem.setWidth(350); 
        nameItem.setRequired(true);

        ButtonItem saveItem = new ButtonItem("Save");
        saveItem.setWidth(50);
        saveItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    save(nameItem.getValueAsString());
                }
            }
        });

        form.setFields(nameItem, saveItem);
    }

    /**
     * Sets a group to edit or creates a blank form.
     * 
     * @param name Group name
     */
    public void setGroup(String name) {
        if (name != null) {
            this.setTitle("Editing Group: " + name);
            this.oldName = name;
            this.nameItem.setValue(name);
            this.newGroup = false;
        } else {
            this.setTitle("Add Group");
            this.oldName = null;
            this.nameItem.setValue("");
            this.newGroup = true;
        }
    }

    private void save(final String name) {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        if (newGroup) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing add group\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageGroupsTab groupsTab = (ManageGroupsTab) Layout.getInstance().
                                getTab("manage-groups-tab");
                        groupsTab.loadGroups();
                        setGroup(null);
                    }
                    SC.say(result);
                }
            };
            service.addGroup(Context.getInstance().getProxyFileName(), name, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing update group\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageGroupsTab groupsTab = (ManageGroupsTab) Layout.getInstance().
                                getTab("manage-groups-tab");
                        groupsTab.loadGroups();
                        setGroup(null);
                    }
                    SC.say(result);
                }
            };
            service.updateGroup(Context.getInstance().getProxyFileName(),
                    oldName, name, callback);
        }
    }
}
