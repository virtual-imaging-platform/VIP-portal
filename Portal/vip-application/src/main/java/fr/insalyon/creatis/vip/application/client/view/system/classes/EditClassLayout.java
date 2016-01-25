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
package fr.insalyon.creatis.vip.application.client.view.system.classes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditClassLayout extends AbstractFormLayout {

    private boolean newClass = true;
    private TextItem nameField;
    private SelectItem enginesPickList;
    private SelectItem groupsPickList;
    private IButton saveButton;
    private IButton removeButton;

    public EditClassLayout() {

        super(380, 200);
        addTitle("Add/Edit Class", ApplicationConstants.ICON_CLASSES);
        
        configure();
        loadData();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(350, null);

        enginesPickList = new SelectItem();
        enginesPickList.setShowTitle(false);
        enginesPickList.setMultiple(true);
        enginesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        enginesPickList.setWidth(350);
        
        groupsPickList = new SelectItem();
        groupsPickList.setShowTitle(false);
        groupsPickList.setMultiple(true);
        groupsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsPickList.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameField.validate()) {
                            save(new AppClass(nameField.getValueAsString().trim(),
                                    Arrays.asList(enginesPickList.getValues()),
                                    Arrays.asList(groupsPickList.getValues())));
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SC.ask("Do you really want to remove this class?", new BooleanCallback() {
                            @Override
                            public void execute(Boolean value) {
                                if (value) {
                                    remove(nameField.getValueAsString().trim());
                                }
                            }
                        });
                    }
                });
        removeButton.setDisabled(true);

        addField("Name", nameField);
        addField("Engines", enginesPickList);
        addField("Groups", groupsPickList);
        addButtons(saveButton, removeButton);
    }

    /**
     * Sets a class to edit or creates a blank form.
     *
     * @param name Class name
     * @param groups Class groups
     * @param engines Class engines
     */
    public void setClass(String name, String groups, String engines) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.enginesPickList.setValue(engines.split(", "));
            this.groupsPickList.setValues(groups.split(", "));
            this.newClass = false;
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.enginesPickList.setValues(new String[]{});
            this.groupsPickList.setValues(new String[]{});
            this.newClass = true;
            this.removeButton.setDisabled(true);
        }
    }

    /**
     *
     * @param appClass
     */
    private void save(AppClass appClass) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newClass) {
            service.addClass(appClass, getCallback("add"));
        } else {
            service.updateClass(appClass, getCallback("update"));
        }
    }

    /**
     * Removes a class.
     *
     * @param name Class name
     */
    private void remove(String name) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        service.removeClass(name, getCallback("remove"));
    }

    /**
     *
     * @param text
     * @return
     */
    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " class:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setClass(null, null, null);
                ManageClassesTab tab = (ManageClassesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_CLASSES);
                tab.loadClasses();
            }
        };
    }

    /**
     * Loads groups and engines list.
     */
    private void loadData() {

        AsyncCallback<List<Group>> callback = new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                List<String> dataList = new ArrayList<String>();
                for (Group group : result) {
                    dataList.add(group.getName());
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        ConfigurationService.Util.getInstance().getGroups(callback);
        
        AsyncCallback<List<Engine>> callback2 = new AsyncCallback<List<Engine>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of engines:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Engine> result) {
                List<String> dataList = new ArrayList<String>();
                for (Engine engine : result) {
                    dataList.add(engine.getName());
                }
                enginesPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        ApplicationService.Util.getInstance().getEngines(callback2);
    }
}
