package fr.insalyon.creatis.vip.application.client.view.system.resources;

import java.util.Arrays;
import java.util.List;

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
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class EditResourceLayout extends AbstractFormLayout {

    private boolean newResource = true;
    private TextItem nameField;
    private BooleanItem publicField;
    private BooleanItem statusField;
    private SelectItem typeFieldList;
    private TextItem configurationField;
    private SelectItem enginesList;
    private SelectItem groupsList;
    private IButton saveButton;
    private IButton removeButton;

    public EditResourceLayout() {
        super(480, 200);
        addTitle("Add/Edit Resource", ApplicationConstants.ICON_ENGINE);

        configure();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(350, null);
        configurationField = FieldUtil.getTextItem(350, null);

        publicField = new BooleanItem();
        publicField.setShowTitle(false);
        publicField.setWidth(350);

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
                        if (nameField.validate() & configurationField.validate()) {

                            save(new Resource(
                                nameField.getValueAsString().trim(),
                                publicField.getValueAsBoolean(),
                                statusField.getValueAsBoolean(),
                                typeFieldList.getValueAsString(),
                                configurationField.getValueAsString().trim(),
                                Arrays.asList(enginesList.getValues()),
                                Arrays.asList(groupsList.getValues())
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
        addField("Public", publicField);
        addField("Active", statusField);
        addField("Type", typeFieldList);
        addField("Configuration", configurationField);
        addField("Engines", enginesList);
        addField("Groups Resources", groupsList);
        addButtons(saveButton, removeButton);
    }

    public void setResource(String name, boolean isPublic, boolean status, String type, String configuration, String[] engines, String[] groups) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.publicField.setValue(isPublic);
            this.statusField.setValue(status);
            this.typeFieldList.setValue(type);
            this.configurationField.setValue(configuration);
            this.enginesList.setValues(engines);
            this.groupsList.setValues(groups);
            this.newResource = false;
            this.removeButton.setDisabled(false);
        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.publicField.setValue(false);
            this.statusField.setValue(false);
            this.typeFieldList.setValue(ResourceType.getDefault());
            this.configurationField.setValue("");
            this.newResource = true;
            this.removeButton.setDisabled(true);
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

    private AsyncCallback<Void> getCallback(final String text) {
        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " resource:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setResource(null, false, false, null, null, null, null);
                ManageResourcesTab tab = (ManageResourcesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_RESOURCE);
                tab.loadResources();
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
                String[] data = result.stream()
                    .filter((g) -> g.getType() == GroupType.RESOURCE)
                    .map(Group::getName)
                    .toArray(String[]::new);
                groupsList.setValueMap(data);
            }
        };
        service.getGroups(callback);
    }
}
