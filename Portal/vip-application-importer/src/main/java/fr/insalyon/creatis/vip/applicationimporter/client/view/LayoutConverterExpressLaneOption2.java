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
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.FileProcessService;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutConverterExpressLaneOption2 extends AbstractFormLayout {

    private Label xmlFileLabel;
    private TextItem xmlFileItem;
    private DynamicForm xmlFileForm;
    private IButton importButton;
    ApplicationImporterImportTab tabImporter;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterExpressLaneOption2(int width, String height) {
        super(width, height);
        configure();
    }

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterExpressLaneOption2(String width, String height) {
        super(width, height);
        configure();
    }

    private void configure() {
        xmlFileLabel = new Label("<strong>XML File </strong><font color=red>(*)</font>");
        xmlFileLabel.setHeight(20);
        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });

        browsePicker.setPrompt("Browse on the Grid");



        xmlFileItem = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        xmlFileItem.setValidators(ValidatorUtil.getStringValidator());
        xmlFileItem.setIcons(browsePicker);
        xmlFileItem.setRequired(Boolean.TRUE);

        xmlFileForm = new DynamicForm();
        xmlFileForm.setFields(xmlFileItem);


        importButton = new IButton();
        importButton = WidgetUtil.getIButton("Import", ApplicationImporterConstants.ICON_IMPORT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!xmlFileItem.validate()) {
                    Layout.getInstance().setWarningMessage("There is an invalid input");
                } else {

                    final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to read xml file :" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(List<String[]> result) {

                            tabImporter = new ApplicationImporterImportTab(ApplicationImporterConstants.ICON_EXPRESSLANE2VIP,ApplicationImporterConstants.TAB_ID_EXPRESSLANE_2,ApplicationImporterConstants.TAB_NAME_EXPRESSLANE);
                            Layout.getInstance().addTab(tabImporter);

                            for (int i = 0; i < result.size(); i++) {
                                if (i == 0) {
                                    tabImporter.addFields(EnumFieldTitles.ApplicationName, false, result.get(i)[0], "[0-9.,A-Za-z-+_ ]", false, true,true);
                                    tabImporter.addFields(EnumFieldTitles.ApplicationVersion, false, result.get(i)[1], "[0-9.,A-Za-z-+_() ]", false, true,true);
                                    tabImporter.addFielDescription("Documentation and Terms of Use", result.get(i)[2]);
                                    tabImporter.addInputField(true, "results-directory ", "Directory where the results will be stored", EnumInputTypes.Parameter, true,null,null);
                                    tabImporter.addInputField(true, "job name", "A string identifying the job name", EnumInputTypes.Parameter, true,null,null);

                                } else {
                                    // name,type;required
                                    //s[0],s[1],s[2]
                                    tabImporter.addInputField(Boolean.valueOf(result.get(i)[2]), result.get(i)[0], result.get(i)[3], EnumInputTypes.valueOf(result.get(i)[1]),false,null,null);
                                }

                            }

                            tabImporter.addOutputField(true, "Output", "A tar.gz file containing the results", EnumInputTypes.File, true,null,null,null);
                            tabImporter.addClassItem("VIP Class");
                            tabImporter.addFields(EnumFieldTitles.MainExecutable, true, "", "[0-9.,A-Za-z-+/_(): ]", false, true,false);
                            tabImporter.addFields(EnumFieldTitles.ApplicationLocation, true, "", "[0-9.,A-Za-z-+/_(): ]", false, true,false);
                            tabImporter.addFields(EnumFieldTitles.ExtensionFile, true,"", "[0-9.,A-Za-z-+/_() ]", false, false,false);
                            tabImporter.addFields(EnumFieldTitles.EnvironementFile, true, "", "[0-9.,A-Za-z-+/_(): ]", false, false,false);
                            tabImporter.addFields(EnumFieldTitles.SandboxFile, true, "", "[0-9.,A-Za-z-+/_(): ]", false, false,false);
                           
                            tabImporter.addLaunchButton("neugrid");

                        }
                    };
                    FileProcessService.Util.getInstance().parseXmlFile(xmlFileItem.getValueAsString(), callback);

                }
            }
        });

        this.addMember(xmlFileLabel);
        this.addMember(xmlFileForm);
        this.addMember(importButton);


    }
}
