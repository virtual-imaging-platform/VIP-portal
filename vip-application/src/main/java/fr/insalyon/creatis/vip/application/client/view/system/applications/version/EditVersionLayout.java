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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.*;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.Tag.ValueType;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.*;

public class EditVersionLayout extends AbstractFormLayout {
    
    private boolean newVersion;
    private String applicationName;
    private String version;

    private Label applicationLabel;
    private TextItem versionField;
    private TextItem lfnField;
    private TextItem jsonLfnField;
    private ListGrid settingsGrid;
    private IButton newSettingsButton;
    private CheckboxItem isVisibleField;
    private CheckboxItem isBoutiquesFormField;
    private ListGrid tagsGrid;
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

        lfnField = FieldUtil.getTextItem(450, null);
        lfnField.setDisabled(true);

        jsonLfnField = FieldUtil.getTextItem(450, null);
        jsonLfnField.setDisabled(true);
        jsonLfnField.setRequired(false);

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

        isBoutiquesFormField = new CheckboxItem();
        isBoutiquesFormField.setTitle("Use Boutiques Form");
        isBoutiquesFormField.setWidth(450);
        isBoutiquesFormField.setValue(true);

        tagsGrid = new ListGrid();
        tagsGrid.setWidth(450);
        tagsGrid.setCanEdit(true);
        tagsGrid.setCanRemoveRecords(true);
        tagsGrid.setEditEvent(ListGridEditEvent.CLICK);
        tagsGrid.setHeight(250);
        tagsGrid.setFields(
            new ListGridField("key", "Key").setCanEdit(true),
            new ListGridField("value", "Value").setCanEdit(true),
            new ListGridField("visible", "Visible").setType(ListGridFieldType.BOOLEAN),
            new ListGridField("boutiques", "Boutiques").setType(ListGridFieldType.BOOLEAN)
        );
  
        resourcesList = new SelectItem();
        resourcesList.setMultiple(true);
        resourcesList.setWidth(450);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (versionField.validate() && lfnField.validate() && jsonLfnField.validate()) {
                    String jsonLfn = jsonLfnField.getValueAsString();
                    if (jsonLfn != null) jsonLfn.trim();
                    AppVersion toSave = new AppVersion(applicationName, versionField.getValueAsString().trim(),
                            lfnField.getValueAsString().trim(), jsonLfn, settingsToMap(),
                            isVisibleField.getValueAsBoolean(), isBoutiquesFormField.getValueAsBoolean());

                    toSave.setResources(resourcesToList(Arrays.asList(resourcesList.getValues())));
                    toSave.setTags(tagsToList(toSave.getApplicationName(), toSave.getVersion()));
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
        addField("Gwendia LFN", lfnField);
        addField("JSON LFN", jsonLfnField);
        addMember(FieldUtil.getForm(isVisibleField));
        addMember(FieldUtil.getForm(isBoutiquesFormField));
        addField("Resources authorized", resourcesList);
        addMember(WidgetUtil.getLabel("<b>" + "Tags Settings" + "</b>", 15));
        addMember(tagsGrid);
        addMember(createTagCreationEntry());
        addMember(WidgetUtil.getLabel("<b>" + "Execution Settings" + "</b>", 15));
        addMember(settingsGrid);
        addMember(newSettingsButton);
        addButtons(saveButton, removeButton);
    }

    private DynamicForm createTagCreationEntry() {
        DynamicForm form = new DynamicForm();

        ComboBoxItem combo = new ComboBoxItem("autoComplete", "Add custom Tag");

        combo.setWidth(175);
        combo.setTitleOrientation(TitleOrientation.TOP);

        combo.addKeyPressHandler(event -> {
            if ("Enter".equals(event.getKeyName())) {
                String inputValue = combo.getEnteredValue();
                
                if (inputValue != null && ! inputValue.trim().isEmpty()) {
                    tagsGrid.addData(createTagRecord(inputValue, "default", ValueType.STRING, true, false));
                }
                combo.clearValue();
            }
        });

        form.setFields(combo);
        return form;
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
                setVersion(null, null, null, true, true, null, null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadVersions(applicationName);
            }
        };
    }

    public void setApplication(String applicationName) {
        setVersion(null, null, null, true, true, null, null);
        this.applicationName = applicationName;
        this.applicationLabel.setContents("<b>Application:</b> " + applicationName);
        this.versionField.setDisabled(false);
        this.lfnField.setDisabled(false);
        this.jsonLfnField.setDisabled(false);
        this.saveButton.setDisabled(false);
    }

    public void setVersion(String version, String lfn, String jsonLfn, boolean isVisible, boolean isBoutiquesForm, 
            Map<String, String> settings, String[] resources) {
        if (version != null) {
            this.versionField.setValue(version);
            this.versionField.setDisabled(true);
            this.lfnField.setValue(lfn);
            this.jsonLfnField.setValue(jsonLfn);
            this.isVisibleField.setValue(isVisible);
            this.isBoutiquesFormField.setValue(isBoutiquesForm);;
            this.resourcesList.setValues(resources);
            this.removeButton.setDisabled(false);
            this.newVersion = false;
            this.version = version;
            fillSettingsInGrid(settings);
        } else {
            this.versionField.setValue("");
            this.versionField.setDisabled(false);
            this.lfnField.setValue("");
            this.jsonLfnField.setValue("");
            this.isVisibleField.setValue(true);
            this.isBoutiquesFormField.setValue(true);
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

    private List<Tag> tagsToList(String application, String version) {
        List<Tag> result = new ArrayList<>();

        for (ListGridRecord record : tagsGrid.getRecords()) {
            result.add(new Tag(
                record.getAttribute("key"),
                record.getAttribute("value"),
                Tag.ValueType.valueOf(record.getAttribute("type")),
                application,
                version,
                record.getAttributeAsBoolean("visible"),
                record.getAttributeAsBoolean("boutiques")
            ));
        }
        return result;
    }

    private void fillTagsInGrid(List<Tag> tags) {
        tagsGrid.setData(new ListGridRecord[0]); // cleaning between

        tags.forEach((t) -> {
            tagsGrid.addData(createTagRecord(t.getKey(), t.getValue(), t.getType(), t.isVisible(), t.isBoutiques()));
        });
    }

    private Record createTagRecord(String key, String value, ValueType type, boolean visible, boolean boutiques) {
        Record record = new Record();

        record.setAttribute("key", key);
        record.setAttribute("value", value);
        record.setAttribute("type", type);
        record.setAttribute("visible", visible);
        record.setAttribute("boutiques", boutiques);
        return record;
    }

    private List<Resource> resourcesToList(List<String> resources) {
        List<Resource> result = new ArrayList<>();

        for (String rsrc : resources) {
            result.add(new Resource(rsrc));
        }
        return result;
    }

    private void fetchData() {
        if (applicationName != null && version != null) {
            loadAppVersionTags();
        }
        loadResources();
    }

    private void loadAppVersionTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Tag> result) {
                fillTagsInGrid(result);
            }
        };
        service.getTags(new AppVersion(applicationName, version), callback);
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
