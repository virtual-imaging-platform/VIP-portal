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

import fr.insalyon.creatis.vip.applicationimporter.client.EnumFieldTitles;
import fr.insalyon.creatis.vip.applicationimporter.client.EnumInputTypes;
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
import fr.insalyon.creatis.vip.applicationimporter.client.EnumCardinalityValues;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.FileProcessService;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutConverterBoutiques extends AbstractFormLayout {

    private Label jsonFileLabel;
    private TextItem jsonFileItem;
    private DynamicForm jsonFileForm;
    private IButton importButton;
    ApplicationImporterImportTab tabImporter;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterBoutiques(int width, String height) {
        super(width, height);
        configure();
    }

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterBoutiques(String width, String height) {
        super(width, height);
        configure();
    }

    private void configure() {
        jsonFileLabel = new Label("<strong>Json File </strong><font color=red>(*)</font>");
        jsonFileLabel.setHeight(20);
        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });

        browsePicker.setPrompt("Browse on the Grid");



        jsonFileItem = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        jsonFileItem.setValidators(ValidatorUtil.getStringValidator());
        jsonFileItem.setIcons(browsePicker);
        jsonFileItem.setRequired(Boolean.TRUE);

        jsonFileForm = new DynamicForm();
        jsonFileForm.setFields(jsonFileItem);


        importButton = new IButton();
        importButton = WidgetUtil.getIButton("Import", ApplicationImporterConstants.ICON_IMPORT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!jsonFileItem.validate()) {
                    Layout.getInstance().setWarningMessage("There is an invalid input");
                } else {

                    final AsyncCallback<List<List<HashMap<String,String>>>> callback = new AsyncCallback<List<List<HashMap<String,String>>>>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to read json file :" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(List<List<HashMap<String,String>>> result) {

                            tabImporter = new ApplicationImporterImportTab(ApplicationImporterConstants.ICON_BOUTIQUES,ApplicationImporterConstants.TAB_ID_BOUTIQUES,ApplicationImporterConstants.TAB_NAME_BOUTIQUES);
                            Layout.getInstance().addTab(tabImporter);

                            
                                    tabImporter.addFields(EnumFieldTitles.ApplicationName, false, result.get(0).get(0).get("name"), "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.CommandLine, false, result.get(0).get(0).get("command-line"), "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.DockerImage, false, result.get(0).get(0).get("docker-image"), "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.DockerIndex, false, result.get(0).get(0).get("docker-index"), "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.ApplicationVersion, false, "", "[0-9.,A-Za-z-+_() ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.SchemaVersion, false, result.get(0).get(0).get("schema-version"), "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFielDescription("Documentation and Terms of Use", result.get(0).get(0).get("description"));
                                   //Inputs
                                

                                    for(int k=0; k<(result.get (1)).size();k++){
                                    tabImporter.addInputField(true, result.get(1).get(k).get("name"),result.get(1).get(k).get("description"), EnumInputTypes.valueOf(result.get(1).get(k).get("type")),false,EnumCardinalityValues.valueOf(result.get(1).get(k).get("cardinality")),result.get(1).get(k).get("command-line-key"));
                                    }
                                    for(int k=0; k<(result.get (2)).size();k++){
                                    tabImporter.addOutputField(false, result.get(2).get(k).get("name"),result.get(2).get(k).get("description"), EnumInputTypes.valueOf(result.get(2).get(k).get("type")),false,EnumCardinalityValues.valueOf(result.get(2).get(k).get("cardinality")),result.get(2).get(k).get("command-line-key"),result.get(2).get(k).get("value-template"));
                                    }
                                    
                                

                    
                            tabImporter.addClassItem("VIP Class");
                            tabImporter.addFields(EnumFieldTitles.ApplicationLocation, true, "", "[0-9.,A-Za-z-+/_(): ]", false, true,false);
                            tabImporter.addFields(EnumFieldTitles.SandboxFile, true, "", "[0-9.,A-Za-z-+/_(): ]", false, false,false);
                            tabImporter.addLaunchButton("biomed");
                        
                        }
                    };
                    FileProcessService.Util.getInstance().parseJsonFile(jsonFileItem.getValueAsString(), callback);

                }
            }
        });

        this.addMember(jsonFileLabel);
        this.addMember(jsonFileForm);
        this.addMember(importButton);


    }
}

    

