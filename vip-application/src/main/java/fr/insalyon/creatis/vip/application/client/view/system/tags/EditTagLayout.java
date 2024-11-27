package fr.insalyon.creatis.vip.application.client.view.system.tags;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class EditTagLayout extends AbstractFormLayout {

    private boolean newTag = true;
    private Tag actualTag;
    private TextItem nameField;
    private IButton saveButton;
    private IButton removeButton;

    public EditTagLayout() {
        super(480, 200);
        addTitle("Add/Edit tag", ApplicationConstants.ICON_TAG);

        configure();
    }

    private void configure() {
        nameField = FieldUtil.getTextItem(350, null);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameField.validate()) {
                            save(new Tag(nameField.getValueAsString().trim()));
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SC.ask("Do you really want to remove this tag?", new BooleanCallback() {
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
        addButtons(saveButton, removeButton);
    }

    public void setTag(String name) {

        if (name != null) {
            this.nameField.setValue(name);
            this.newTag = false;
            this.actualTag = new Tag(name);
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.newTag = true;
            this.removeButton.setDisabled(true);
        }
    }

    private void save(Tag tag) {
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newTag) {
            ApplicationService.Util.getInstance().addTag(tag, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().updateTag(actualTag, tag.getName(), getCallback("update"));
        }
    }

    private void remove(String name) {
        final Tag tagToDelete = new Tag(name);
        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().removeTag(tagToDelete, getCallback("remove"));
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " tag:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setTag(null);
                ManageTagsTab tab = (ManageTagsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_TAG);
                tab.loadTags();
            }
        };
    }
}
