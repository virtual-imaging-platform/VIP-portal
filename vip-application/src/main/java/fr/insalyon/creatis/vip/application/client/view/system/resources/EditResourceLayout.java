package fr.insalyon.creatis.vip.application.client.view.system.resources;

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
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.ResourceType;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class EditResourceLayout extends AbstractFormLayout {

    private boolean newResource = true;
    private TextItem nameField;
    private BooleanItem visibleField;
    private BooleanItem statusField;
    private SelectItem typeFieldList;
    private TextItem configurationField;
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

        visibleField = new BooleanItem();
        visibleField.setShowTitle(false);
        visibleField.setWidth(350);

        statusField = new BooleanItem();
        statusField.setShowTitle(false);
        statusField.setWidth(350);

        typeFieldList = new SelectItem();
        typeFieldList.setShowTitle(false);
        typeFieldList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        typeFieldList.setValueMap(ResourceType.getValues());
        typeFieldList.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameField.validate() & configurationField.validate()) {

                            save(new Resource(
                                nameField.getValueAsString().trim(),
                                visibleField.getValueAsBoolean(),
                                statusField.getValueAsBoolean(),
                                typeFieldList.getValueAsString(),
                                configurationField.getValueAsString().trim()));
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
        addField("Visible", visibleField);
        addField("Active", statusField);
        addField("Type", typeFieldList);
        addField("Configuration", configurationField);
        addButtons(saveButton, removeButton);
    }

    public void setResource(String name, boolean visible, boolean status, String type, String configuration) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.visibleField.setValue(visible);
            this.statusField.setValue(status);
            this.typeFieldList.setValue(type);
            this.configurationField.setValue(configuration);
            this.newResource = false;
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.visibleField.setValue(false);
            this.statusField.setValue(false);
            this.typeFieldList.setValue(ResourceType.getDefault());
            this.configurationField.setValue("");
            this.newResource = true;
            this.removeButton.setDisabled(true);
        }
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
                setResource(null, false, false, null, null);
                ManageResourcesTab tab = (ManageResourcesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_RESOURCE);
                tab.loadResources();
            }
        };
    }
}
