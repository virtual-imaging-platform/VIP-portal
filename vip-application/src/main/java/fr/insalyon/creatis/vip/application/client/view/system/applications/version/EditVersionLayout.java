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
package fr.insalyon.creatis.vip.application.client.view.system.applications.version;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.util.*;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.*;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditVersionLayout extends AbstractFormLayout {

    private boolean newVersion;
    private String applicationName;
    private Label applicationLabel;
    private TextItem versionField;
    private TextItem descriptorField;
    private ListGrid settingsGrid;
    private IButton newSettingsButton;
    private CheckboxItem isVisibleField;
    private TextItem sourceField;
    private SelectItem tagsList;
    private SelectItem resourcesList;
    private IButton saveButton;
    private IButton removeButton;

    public EditVersionLayout() {

        super(480, 200);
        addTitle("Add/Edit Version", ApplicationConstants.ICON_APPLICATION);

        configure();
    }

    private void configure() {
        applicationLabel = WidgetUtil.getLabel("", 15);

        versionField = FieldUtil.getTextItem(450, null);
        versionField.setDisabled(true);

        descriptorField = FieldUtil.getTextItem(450, null);
        descriptorField.setDisabled(true);

        settingsGrid = new ListGrid();
        settingsGrid.setWidth(450);
        settingsGrid.setCanEdit(true);
        settingsGrid.setCanRemoveRecords(true);
        settingsGrid.setEditEvent(ListGridEditEvent.CLICK);
        settingsGrid.setHeight(250);
        settingsGrid.setFields(
            new ListGridField("key", "Key"),
            new ListGridField("value", "Value")
        );

        newSettingsButton = new IButton("Add new setting");
        newSettingsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                settingsGrid.startEditingNew();
            }
        });

        isVisibleField = new CheckboxItem();
        isVisibleField.setTitle("Visible");
        isVisibleField.setWidth(450);
        isVisibleField.setValue(true);

        sourceField = FieldUtil.getTextItem(450, null);
        sourceField.setRequired(false);

        tagsList = new SelectItem();
        tagsList.setMultiple(true);
        tagsList.setWidth(450);
  
        resourcesList = new SelectItem();
        resourcesList.setMultiple(true);
        resourcesList.setWidth(450);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (versionField.validate()) {
                    AppVersion toSave = new AppVersion(applicationName, versionField.getValueAsString().trim(),
                            descriptorField.getValueAsString(), settingsToMap(), isVisibleField.getValueAsBoolean(),
                            sourceField.getValueAsString());
                    toSave.setResources(Arrays.asList(resourcesList.getValues()));
                    toSave.setTags(Arrays.asList(tagsList.getValues()));
                    save(toSave);
                }
            }
        });
        saveButton.setDisabled(true);

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to remove this version?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            remove(applicationName, versionField.getValueAsString().trim());
                        }
                    }
                });
            }
        });
        removeButton.setDisabled(true);

        addMember(applicationLabel);
        addField("Version", versionField);
        addField("Descriptor", descriptorField);
        addMember(FieldUtil.getForm(isVisibleField));
        addField("Source", sourceField);
        addField("Tags associated", tagsList);
        addField("Resources authorized", resourcesList);
        addMember(WidgetUtil.getLabel("<b>" + "Execution Settings" + "</b>", 15));
        addMember(settingsGrid);
        addMember(newSettingsButton);
        addButtons(saveButton, removeButton);
    }

    private void save(AppVersion version) {
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newVersion) {
            ApplicationService.Util.getInstance().addVersion(version, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().updateVersion(version, getCallback("update"));
        }
    }

    private void remove(String applicationName, String version) {

        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().removeVersion(applicationName, version, getCallback("remove"));
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setVersion(null, null, true, null, null, null, null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadVersions(applicationName);
            }
        };
    }

    public void setApplication(String applicationName) {
        setVersion(null, null, true, null, null, null, null);
        this.applicationName = applicationName;
        this.applicationLabel.setContents("<b>Application:</b> " + applicationName);
        this.versionField.setDisabled(false);
        this.descriptorField.setDisabled(true);
        this.saveButton.setDisabled(false);
    }

    public void setVersion(String version, String descriptor, boolean isVisible, String source,
            Map<String, String> settings, String[] tags, String[] resources) {
        if (version != null) {
            this.versionField.setValue(version);
            this.versionField.setDisabled(true);
            this.descriptorField.setValue(descriptor);
            this.descriptorField.setDisabled(true);
            this.isVisibleField.setValue(isVisible);
            this.sourceField.setValue(source);
            this.tagsList.setValues(tags);
            this.resourcesList.setValues(resources);
            this.removeButton.setDisabled(false);
            this.newVersion = false;
            fillSettingsInGrid(settings);
        } else {
            this.versionField.setValue("");
            this.versionField.setDisabled(false);
            this.descriptorField.setValue("");
            this.descriptorField.setDisabled(true);
            this.isVisibleField.setValue(true);
            this.sourceField.setValue("");
            this.removeButton.setDisabled(true);
            this.newVersion = true;
        }
        fetchData();
    }

    private Map<String, String> settingsToMap() {
        Map<String, String> result = new HashMap<>();

        for (ListGridRecord record : settingsGrid.getRecords()) {
            result.put(
                record.getAttribute("key"), 
                record.getAttribute("value"));
        }
        return result;
    }

    private void fillSettingsInGrid(Map<String, String> settings) {
        settingsGrid.setData(new ListGridRecord[0]); // cleaning between

        settings.forEach((k, v) -> {
            ListGridRecord record = new ListGridRecord();

            record.setAttribute("key", k);
            record.setAttribute("value", v);
            settingsGrid.addData(record);
        });
    }

    private void fetchData() {
        loadTags();
        loadResources();
    }

    private void loadTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Tag> result) {
                String[] data = result.stream().map(Tag::getName).toArray(String[]::new);
                tagsList.setValueMap(data);
            }
        };
        service.getTags(callback);
    }

    private void loadResources() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Resource>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load resources:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Resource> result) {
                String[] data = result.stream().map(Resource::getName).toArray(String[]::new);
                resourcesList.setValueMap(data);
            }
        };
        service.getResources(callback);
    }
}
