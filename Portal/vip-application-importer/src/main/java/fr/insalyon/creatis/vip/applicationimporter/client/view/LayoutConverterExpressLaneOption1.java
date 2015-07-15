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

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutConverterExpressLaneOption1 extends AbstractFormLayout {

    private TextItem express;
    private TextItem job;
    private TextItem script;
    private TextItem ext;
    private TextItem env;
    private TextItem title;
    private DynamicForm expressItemForm;
    private DynamicForm jobItemForm;
    private DynamicForm scriptItemForm;
    private DynamicForm extItemForm;
    private DynamicForm envItemForm;
    private DynamicForm titleItemForm;
    private IButton importButton;
    Label jobLabel;
    Label titleLabel;
    Label scriptLabel;
    Label extensionLabel;
    Label expressLabel;
    Label environementLabel;
    ApplicationImporterImportTab tabImporter;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterExpressLaneOption1(String width, String height) {
        super(width, height);
        configure();
    }

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterExpressLaneOption1(int width, int height) {
        super(width, height);
        configure();
    }

    /**
     *
     */
    private void configure() {
        titleLabel = new Label("<strong>Application Name </strong> <font color=red>(*)</font>");
        titleLabel.setHeight(20);

        jobLabel = new Label("<strong>Job File </strong><font color=red>(*)</font>");
        jobLabel.setHeight(20);

        expressLabel = new Label("<strong>Express File </strong><font color=red>(*)</font>");
        expressLabel.setHeight(20);

        scriptLabel = new Label("<strong>Script File </strong><font color=red>(*)</font>");
        scriptLabel.setHeight(20);

        extensionLabel = new Label("<strong>Extension File </strong>");
        extensionLabel.setHeight(20);

        environementLabel = new Label("<strong>Environment File </strong>");
        environementLabel.setHeight(20);

        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });

        browsePicker.setPrompt("Browse on the Grid");


        title = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+_ ]");
        title.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+_ ]"));
        title.setRequired(Boolean.TRUE);

        express = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        express.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        express.setIcons(browsePicker);
        express.setRequired(Boolean.TRUE);

        job = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        job.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        job.setIcons(browsePicker);
        job.setRequired(Boolean.TRUE);

        script = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        script.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        script.setIcons(browsePicker);
        script.setRequired(Boolean.TRUE);

        ext = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        ext.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        ext.setIcons(browsePicker);
        ext.setRequired(Boolean.FALSE);

        env = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_(): ]");
        env.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        env.setIcons(browsePicker);
        env.setRequired(Boolean.FALSE);

        titleItemForm = new DynamicForm();
        titleItemForm.setFields(title);

        expressItemForm = new DynamicForm();
        expressItemForm.setFields(express);

        jobItemForm = new DynamicForm();
        jobItemForm.setFields(job);

        extItemForm = new DynamicForm();
        extItemForm.setFields(ext);

        scriptItemForm = new DynamicForm();
        scriptItemForm.setFields(script);

        envItemForm = new DynamicForm();
        envItemForm.setFields(env);

        importButton = new IButton();
        importButton = WidgetUtil.getIButton("Import", ApplicationImporterConstants.ICON_IMPORT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!title.validate() || !job.validate() || !express.validate() || !ext.validate() || !env.validate()) {
                    Layout.getInstance().setWarningMessage("There is an invalid input");
                } else {
                    final AsyncCallback<int[]> callback = new AsyncCallback<int[]>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to read files :" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(int[] result) {

                            tabImporter = new ApplicationImporterImportTab(ApplicationImporterConstants.ICON_EXPRESSLANE2VIP,ApplicationImporterConstants.TAB_ID_EXPRESSLANE_1,ApplicationImporterConstants.TAB_NAME_EXPRESSLANE);
                            Layout.getInstance().addTab(tabImporter);
                            tabImporter.addFields(EnumFieldTitles.ApplicationName, false, title.getValueAsString(), "[0-9.,A-Za-z-+_ ]", false, true,true);
                            tabImporter.addFields(EnumFieldTitles.ApplicationVersion, false, "", "[0-9.,A-Za-z-+_() ]", false, true,true);
                            tabImporter.addFielDescription("Documentation and Terms of Use", "");
                            tabImporter.addInputField(true, "results-directory ", "Directory where the results will be stored", EnumInputTypes.Parameter, true,null,null);
                            tabImporter.addInputField(true, "job name", "A string identifying the job name", EnumInputTypes.Parameter, true,null,null);
                            for (int i = 1; i < (int) result[0]; i++) {
                                if (i <= result[1]) {
                                    tabImporter.addInputField(false, "", "", EnumInputTypes.File, false,null,null);
                                } else {
                                    tabImporter.addInputField(false, "", "", EnumInputTypes.Parameter, false,null,null);
                                }
                            }

                            tabImporter.addOutputField(true, "Output", "A tar.gz file containing the results", EnumInputTypes.File, true,null,null,null);
                            tabImporter.addClassItem("VIP Class");
                            
                            tabImporter.addFields(EnumFieldTitles.MainExecutable, true, script.getValueAsString(), "[0-9.,A-Za-z-+/_() ]", false, true,false);
                            tabImporter.addFields(EnumFieldTitles.ApplicationLocation, true, "", "[0-9.,A-Za-z-+/_() ]", false, true,false);
                            tabImporter.addFields(EnumFieldTitles.ExtensionFile, true, ext.getValueAsString(), "[0-9.,A-Za-z-+/_() ]", false, false,false);
                            tabImporter.addFields(EnumFieldTitles.EnvironementFile, true, env.getValueAsString(), "[0-9.,A-Za-z-+/_() ]", false, false,false);
                            tabImporter.addFields(EnumFieldTitles.SandboxFile, true, "", "[0-9.,A-Za-z-+/_() ]", false, false,false);
                           
                            tabImporter.addLaunchButton("neugrid");

                        }
                    };
                    FileProcessService.Util.getInstance().fileJobProcess(job.getValueAsString(), express.getValueAsString(), callback);
                }
            }
        });

        this.addMember(titleLabel);
        this.addMember(titleItemForm);
        this.addMember(jobLabel);
        this.addMember(jobItemForm);
        this.addMember(expressLabel);
        this.addMember(expressItemForm);
        this.addMember(scriptLabel);
        this.addMember(scriptItemForm);
        this.addMember(extensionLabel);
        this.addMember(extItemForm);
        this.addMember(environementLabel);
        this.addMember(envItemForm);
        this.addMember(importButton);




    }
}
