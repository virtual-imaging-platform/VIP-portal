package fr.insalyon.creatis.vip.application.client.view.system.applications.version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.models.Tag.ValueType;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class EditVersionLayout extends AbstractFormLayout {
    private boolean newVersion;
    private String applicationName;
    private String version;

    private Label applicationLabel;
    private TextItem versionField;
    private TextItem descriptorField;
    private ListGrid settingsGrid;
    private IButton newSettingsButton;
    private CheckboxItem isVisibleField;
    private TextItem sourceField;
    private TextAreaItem noteField;
    private ListGrid tagsGrid;
    private ListGrid resourcesGrid;
    private Set<String> selectedResourceNames = new HashSet<>();
    private Map<String, Integer> resourcePriorityValues = new HashMap<>();
    private IButton saveButton;
    private IButton removeButton;
    private DataSource suggestionTags;

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

        noteField = new TextAreaItem();
        noteField.setRequired(false);
        noteField.setShowTitle(false);
        noteField.setWidth(450);
        noteField.setHeight(70);

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

        resourcesGrid = new ListGrid();
        resourcesGrid.setWidth(450);
        resourcesGrid.setHeight(250);
        resourcesGrid.setCanEdit(true);
        resourcesGrid.setCanRemoveRecords(false);
        resourcesGrid.setEditEvent(ListGridEditEvent.CLICK);

        ListGridField selectedField = new ListGridField("selected", "Authorized");
        selectedField.setType(ListGridFieldType.BOOLEAN);
        selectedField.setCanEdit(true);
        selectedField.setWidth(90);
        selectedField.setEditorType(new CheckboxItem());

        ListGridField resourceNameField = new ListGridField("resourceName", "Resource");
        resourceNameField.setCanEdit(false);

        ListGridField priorityField = new ListGridField("priority", "Priority (lower = preferred)");
        priorityField.setType(ListGridFieldType.INTEGER);
        priorityField.setCanEdit(true);
        priorityField.setWidth(180);

        resourcesGrid.setFields(selectedField, resourceNameField, priorityField);

        suggestionTags = new DataSource();
        suggestionTags.setClientOnly(true);
        suggestionTags.setFields(new DataSourceTextField("key", "Key"));

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (versionField.validate()) {
                    AppVersion toSave = new AppVersion(
                            applicationName,
                            versionField.getValueAsString().trim(),
                            descriptorField.getValueAsString(),
                            settingsToMap(),
                            isVisibleField.getValueAsBoolean(),
                            sourceField.getValueAsString(),
                            noteField.getValueAsString() == null ? null : noteField.getValueAsString().trim());

                    toSave.setResources(getSelectedResourcesFromGrid());
                    toSave.setResourcePriorities(getSelectedPrioritiesFromGrid());
                    toSave.setTags(tagsToSet(toSave.getApplicationName(), toSave.getVersion()));
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

        Label descriptorLabel = WidgetUtil.getLabel("<b>" + "Descriptor" + "</b>", 15);
        descriptorLabel.setWidth(24);

        ImgButton descriptorBtn = new ImgButton();
        descriptorBtn.setSrc(ApplicationConstants.ICON_COPY);
        descriptorBtn.setWidth(16);
        descriptorBtn.setHeight(16);
        descriptorBtn.setShowRollOver(false);
        descriptorBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().setNoticeMessage("Descriptor copied to clipboard!");
                copyToClipboard(descriptorField.getValueAsString());
            }
        });
        addInline(descriptorLabel, descriptorBtn);
        addMember(FieldUtil.getForm(descriptorField));

        addMember(FieldUtil.getForm(isVisibleField));

        Label sourceLabel = WidgetUtil.getLabel("<b>" + "Source" + "</b>", 15);
        sourceLabel.setWidth(24);

        ImgButton sourceBtn = new ImgButton();
        sourceBtn.setSrc(ApplicationConstants.ICON_LINK);
        sourceBtn.setWidth(16);
        sourceBtn.setHeight(16);
        sourceBtn.setShowRollOver(false);
        sourceBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String url = sourceField.getValueAsString();

                if ( ! url.startsWith("http")) {
                    url = "https://" + url;
                }
                Window.open(url, null, null);
            }
        });
        addInline(sourceLabel, sourceBtn);
        addMember(FieldUtil.getForm(sourceField));
        addFieldResponsiveHeight("Note", noteField);
        addMember(WidgetUtil.getLabel("<b>" + "Resources authorized" + "</b>", 15));
        addMember(resourcesGrid);
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

        combo.setOptionDataSource(suggestionTags);
        combo.setDisplayField("key");
        combo.setValueField("key");
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
                           String note, Map<String, String> settings, String[] resources) {
        setVersion(version, descriptor, isVisible, source, note, settings, resources, null);
    }

    public void setVersion(String version, String descriptor, boolean isVisible, String source,
        String note, Map<String, String> settings, String[] resources, Map<String, Integer> priorities) {
        selectedResourceNames.clear();
        if (resources != null) {
            selectedResourceNames.addAll(Arrays.asList(resources));
        }

        resourcePriorityValues.clear();
        if (priorities != null) {
            resourcePriorityValues.putAll(priorities);
        }

        resourcesGrid.setData(new ListGridRecord[0]);

        if (version != null) {
            this.versionField.setValue(version);
            this.versionField.setDisabled(true);
            this.descriptorField.setValue(descriptor);
            this.descriptorField.setDisabled(true);
            this.isVisibleField.setValue(isVisible);
            this.sourceField.setValue(source);
            this.noteField.setValue(note == null ? "" : note);
            this.removeButton.setDisabled(false);
            this.newVersion = false;
            this.version = version;
            fillSettingsInGrid(settings);
        } else {
            this.versionField.setValue("");
            this.versionField.setDisabled(false);
            this.descriptorField.setValue("{}");
            this.descriptorField.setDisabled(true);
            this.isVisibleField.setValue(true);
            this.sourceField.setValue("");
            this.noteField.setValue("");
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

    private Set<Tag> tagsToSet(String application, String version) {
        Set<Tag> result = new HashSet<>();

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

    private Set<Resource> getSelectedResourcesFromGrid() {
        Set<Resource> selected = new HashSet<>();
        for (ListGridRecord record : resourcesGrid.getRecords()) {
            if (record.getAttributeAsBoolean("selected")) {
                String name = record.getAttribute("resourceName");
                if (name == null) continue;

                selected.add(new Resource(name));
            }
        }

        return selected;
    }

    private Map<String, Integer> getSelectedPrioritiesFromGrid() {
        Map<String, Integer> priorities = new HashMap<>();
        for (ListGridRecord record : resourcesGrid.getRecords()) {
            if (record.getAttributeAsBoolean("selected")) {
                String name = record.getAttribute("resourceName");
                if (name == null) continue;

                priorities.put(name, record.getAttributeAsInt("priority"));
            }
        }

        return priorities;
    }

    private void fetchData() {
        if (applicationName != null && version != null) {
            loadAppVersionTags();
        }
        loadResources();
        loadExistingTagsKeys();
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
        service.getTags(new AppVersion(applicationName, version, "", false), callback);
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
                ListGridRecord[] records = result.stream()
                        .map(resource -> {
                            String name = resource.getName();
                            ListGridRecord record = new ListGridRecord();
                            record.setAttribute("selected", selectedResourceNames.contains(name));
                            record.setAttribute("resourceName", name);
                            record.setAttribute("priority", resourcePriorityValues.getOrDefault(name, 0));
                            return record;
                        })
                        .sorted(
                            Comparator.comparing(
                                (ListGridRecord r) -> !r.getAttributeAsBoolean("selected"))
                                .thenComparingInt(r -> r.getAttributeAsInt("priority")))
                        .toArray(ListGridRecord[]::new);
                resourcesGrid.setData(records);
            }
        };
        service.getResources(callback);
    }

    private void loadExistingTagsKeys() {
                ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load existing tags keys:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Tag> result) {
                List<String> keys = new ArrayList<>(result.stream().map(Tag::getKey).collect(Collectors.toSet()));

                Record[] records = keys.stream().map(key -> {
                    Record record = new Record();
                    record.setAttribute("key", key);
                    return record;
                }).toArray(Record[]::new);

                suggestionTags.setCacheData(records);
            }
        };
        service.getTags(callback);
    }

    public static native void copyToClipboard(String text) /*-{
        var textarea = document.createElement("textarea");
        textarea.value = text;
        textarea.style.position = "fixed";
        textarea.style.left = "-9999px";
        document.body.appendChild(textarea);
        textarea.focus();
        textarea.select();
        try {
            document.execCommand("copy");
        } catch (e) {}
        document.body.removeChild(textarea);
    }-*/;
}
