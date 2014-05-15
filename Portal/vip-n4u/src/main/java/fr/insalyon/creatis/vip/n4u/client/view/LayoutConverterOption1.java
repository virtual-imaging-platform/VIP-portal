/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

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
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutConverterOption1 extends AbstractFormLayout {

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
    N4uImportTab tabImporter;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterOption1(String width, String height) {
        super(width, height);
        this.setTitle("sdfdsdd");
        configure();
    }

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterOption1(int width, int height) {
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


        title = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        title.setValidators(ValidatorUtil.getStringValidator());
        title.setRequired(Boolean.TRUE);


        express = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        express.setValidators(ValidatorUtil.getStringValidator());
        express.setIcons(browsePicker);
        express.setRequired(Boolean.TRUE);

        job = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        job.setValidators(ValidatorUtil.getStringValidator());
        job.setIcons(browsePicker);
        job.setRequired(Boolean.TRUE);

        script = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        script.setValidators(ValidatorUtil.getStringValidator());
        script.setIcons(browsePicker);
        script.setRequired(Boolean.TRUE);

        ext = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        ext.setValidators(ValidatorUtil.getStringValidator());
        ext.setIcons(browsePicker);
        ext.setRequired(Boolean.FALSE);

        env = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        env.setValidators(ValidatorUtil.getStringValidator());
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
        importButton = WidgetUtil.getIButton("Import", N4uConstants.ICON_IMPORT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (!title.validate() || !express.validate() || !job.validate() || !ext.validate() || !env.validate()) {
                    Layout.getInstance().setWarningMessage("There is an invalid input");
                } else {


                    final AsyncCallback<int[]> callback = new AsyncCallback<int[]>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to read files :" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(int[] result) {

                            tabImporter = new N4uImportTab();
                            Layout.getInstance().addTab(tabImporter);
                            tabImporter.addfiels("Application Name <font color=red>(*)</font>", false, title.getValueAsString(), false);
                            tabImporter.addFielDescription("Documentation and Terms of Use","");
                            tabImporter.addFielsInputs(true, "results-directory ", "Directory where the results will be stored", N4uImportTab.InputType.Parameter.name(), true);
                            tabImporter.addFielsInputs(true, "job name", "A string identifying the job name", N4uImportTab.InputType.Parameter.name(), false);
                            for (int i = 1; i < (int) result[0]; i++) {
                                if (i <= result[1]) {
                                    tabImporter.addFielsInputs(false, "", "", N4uImportTab.InputType.File.name(), false);
                                } else {
                                    tabImporter.addFielsInputs(false, "", "", N4uImportTab.InputType.Parameter.name(), false);
                                }
                            }

                            tabImporter.addFielsOutput(true, "result", "A tar.gz file containing the results", N4uImportTab.InputType.File.name(), true);
                            tabImporter.addfiels("Main Executable <font color=red>(*)</font>", true, script.getValueAsString(), false);
                            /* if (ext.getValueAsString() != "" || ext.getValueAsString() != null || !ext.getValueAsString().isEmpty()) {
                             tabImporter.addfiels("Extension File", true, false, ext.getValueAsString(), false);
                             }

                             if (env.getValueAsString()!="" || env.getValueAsString() != null || env.getValueAsString().length() == 0||!env.getValueAsString().isEmpty()) {
                             tabImporter.addfiels("Environement File", true, false, env.getValueAsString(), false);
                             }
                             **/
                            tabImporter.addfiels("Application Location <font color=red>(*)</font>", true, "", false);
                            tabImporter.addLaunchButton();

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
