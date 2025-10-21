package fr.insalyon.creatis.vip.application.client.view.system.resources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.ResourceType;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.system.SystemUtils;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;

public class EditResourceLayout extends AbstractFormLayout {

    private boolean newResource = true;
    private TextItem nameField;
    private BooleanItem statusField;
    private SelectItem typeFieldList;
    private TextItem configurationField;
    private SelectItem enginesList;
    private SelectItem groupsList;
    private IButton saveButton;
    private IButton removeButton;

    private Map<String, String> groupsMap;

    public EditResourceLayout() {
        super(480, 200);
        addTitle("Add/Edit Resource", ApplicationConstants.ICON_ENGINE);

        configure();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(350, null);
        configurationField = FieldUtil.getTextItem(350, null);
        configurationField.setRequired(true);

        statusField = new BooleanItem();
        statusField.setShowTitle(false);
        statusField.setWidth(350);

        typeFieldList = new SelectItem();
        typeFieldList.setShowTitle(false);
        typeFieldList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        typeFieldList.setValueMap(ResourceType.getValues());
        typeFieldList.setWidth(350);

        enginesList = new SelectItem();
        enginesList.setShowTitle(false);
        enginesList.setMultiple(true);
        enginesList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        enginesList.setWidth(350);

        groupsList = new SelectItem();
        groupsList.setShowTitle(false);
        groupsList.setMultiple(true);
        groupsList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsList.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        List<String> groupsNames = Arrays.asList(groupsList.getValues());
                        List<Group> groups = groupsNames.stream()
                            .map((name) -> new Group(groupsMap.get(name), false, GroupType.RESOURCE)).collect(Collectors.toList());
                        if (nameField.validate() && configurationField.validate()) {
                            save(new Resource(
                                nameField.getValueAsString().trim(),
                                statusField.getValueAsBoolean(),
                                typeFieldList.getValueAsString(),
                                configurationField.getValueAsString().trim(),
                                Arrays.asList(enginesList.getValues()),
                                groups
                            ));
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SC.ask("Do you really want to remove this resource?", new BooleanCallback() {
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
        addField("Active", statusField);
        addField("Type", typeFieldList);
        addField("Configuration Folder", configurationField);
        addField("Engines", enginesList);
        addField("Groups Resources", groupsList);
        addButtons(saveButton, removeButton);
    }

    public void setResource(String name, boolean status, String type, String configuration, String[] engines, Map<String, String> groups) {
        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.statusField.setValue(status);
            this.typeFieldList.setValue(type);
            this.configurationField.setValue(configuration);
            this.enginesList.setValues(engines);
            this.groupsList.setValues(groups.keySet().stream().toArray(String[]::new));
            this.newResource = false;
            this.removeButton.setDisabled(false);
            this.groupsMap = groups;
        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.statusField.setValue(false);
            this.typeFieldList.setValue(ResourceType.getDefault());
            this.configurationField.setValue("");
            this.enginesList.setValue("");
            this.groupsList.setValue("");
            this.newResource = true;
            this.removeButton.setDisabled(true);
            this.groupsMap = new HashMap<>();
        }
        fetchData();
    }

    private void save(Resource resource) {
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newResource) {
            ApplicationService.Util.getInstance().addResource(resource, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().updateResource(resource, getCallback("update"));
        }
    }

    private void remove(String name) {
        final Resource resourceToDelete = new Resource(name);
        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().removeResource(resourceToDelete, getCallback("remove"));
    }

    private AsyncCallback<String> getCallback(final String text) {
        return new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " resource:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setResource(null, false, null, null, null, null);
                ManageResourcesTab tab = (ManageResourcesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_RESOURCE);
                tab.loadResources();

                if (result != null) {
                    Layout.getInstance().setInformationMessage(result);
                }
            }
        };
    }

    private void fetchData() {
        fetchEngines();
        fetchGroups();
    }

    private void fetchEngines() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Engine>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load engines:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Engine> result) {
                String[] data = result.stream().map(Engine::getName).toArray(String[]::new);
                enginesList.setValueMap(data);
            }
        };
        service.getEngines(callback);
    }

    private void fetchGroups() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load groups:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Group> result) {
                List<Group> data = result.stream()
                    .filter((g) -> g.getType() == GroupType.RESOURCE)
                    .collect(Collectors.toList());
                List<String> formatGroups = SystemUtils.formatGroups(data);

                groupsMap.putAll(IntStream.range(0, Math.min(formatGroups.size(), data.size()))
                    .boxed()
                    .collect(Collectors.toMap(formatGroups::get, (i) -> data.get(i).getName())));
                groupsList.setValueMap(formatGroups.stream().toArray(String[]::new));
            }
        };
        service.getGroups(callback);
    }
}
