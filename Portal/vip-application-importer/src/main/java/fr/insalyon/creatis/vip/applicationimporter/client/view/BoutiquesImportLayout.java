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
package fr.insalyon.creatis.vip.applicationimporter.client.view;

import fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay.DisplayTab;
import com.google.gwt.json.client.JSONObject;
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
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import com.google.gwt.json.client.JSONParser;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;


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
     * Calls the service method to read the content of a file, and sets it in the application import tab.
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
                JSONObject json = JSONParser.parseStrict(jsonFileContent).isObject();
                DisplayTab tabImporter = new DisplayTab(Constants.ICON_BOUTIQUES, Constants.TAB_ID_BOUTIQUES_APPLICATION, Constants.TAB_NAME_BOUTIQUES); 
                tabImporter.parseJSON(json);
                Layout.getInstance().addTab(tabImporter);
            }
        };
        modal.show("Getting JSON file...", true);
        ApplicationImporterService.Util.getInstance().readFileAsString(fileLFN, callback);
    }
}
