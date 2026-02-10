package fr.insalyon.creatis.vip.applicationimporter.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay.DisplayTab;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;

/**
 *
 * @author Nouha Boujelben
 */
public class BoutiquesImportLayout extends AbstractFormLayout {

    private Label jsonFileLabel;
    private TextItem jsonFileItem;
    private DynamicForm jsonFileForm;
    private IButton importButton;
    private ModalWindow modal;

    public BoutiquesImportLayout(int width, String height) {
        super(width, height);
        configure();
    }

    public BoutiquesImportLayout(String width, String height) {
        super(width, height);
        configure();
    }

    private void configure() {
        jsonFileLabel = new Label("<strong>JSON File </strong><font color=red>(*)</font>");
        jsonFileLabel.setHeight(20);
        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });

        browsePicker.setPrompt("Browse");

        jsonFileItem = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        jsonFileItem.setValidators(ValidatorUtil.getStringValidator());
        jsonFileItem.setIcons(browsePicker);
        jsonFileItem.setRequired(Boolean.TRUE);

        jsonFileForm = new DynamicForm();
        jsonFileForm.setFields(jsonFileItem);

        importButton = new IButton();
        importButton = WidgetUtil.getIButton("Import", Constants.ICON_IMPORT,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (!jsonFileItem.validate()) {
                            Layout.getInstance().setWarningMessage("There is an invalid input");
                            return;
                        }
                        loadJSONFile(jsonFileItem.getValueAsString());
                    }
                });

        modal = new ModalWindow(this);

        this.addMember(jsonFileLabel);
        this.addMember(jsonFileForm);
        this.addMember(importButton);

    }

    /**
     * Calls the service method to read the content of a file, and sets it in
     * the application import tab.
     *
     * @param fileLFN the LFN of the JSON file to parse.
     */
    private void loadJSONFile(String fileLFN) {
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to read JSON file :" + caught.getMessage());
            }

            @Override
            public void onSuccess(String jsonFileContent) {
                modal.hide();
                try {
                    BoutiquesApplication boutiquesTool = DisplayTab.parseJSON(jsonFileContent);
                    DisplayTab displayTab =
                        (DisplayTab) Layout.getInstance().addTab(
                            Constants.TAB_ID_BOUTIQUES_APPLICATION,
                            () -> new DisplayTab(
                                Constants.ICON_BOUTIQUES,
                                Constants.TAB_ID_BOUTIQUES_APPLICATION,
                                Constants.TAB_NAME_BOUTIQUES));
                    displayTab.setBoutiqueTool(boutiquesTool);
                    displayTab.loadBoutiquesTags(jsonFileContent);
                } catch (ApplicationImporterException ex) {
                    Layout.getInstance().setWarningMessage(
                        "Unable to parse JSON file :" + ex.getMessage());
                }
            }
        };
        modal.show("Getting JSON file...", true);
        ApplicationImporterService.Util.getInstance().readAndValidateBoutiquesFile(fileLFN, callback);
    }
}
