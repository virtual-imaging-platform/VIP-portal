/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uConverterTab extends Tab {

    private TextItem express;
    private TextItem job;
    private TextItem script;
    private TextItem ext;
    private TextItem env;
    private TextItem title;
    private VLayout layout;
    private DynamicForm expressItemForm;
    private DynamicForm jobItemForm;
    private DynamicForm scriptItemForm;
    private DynamicForm extItemForm;
    private DynamicForm envItemForm;
    private DynamicForm titleItemForm;
    private IButton launchButton;
    Label jobLabel;
    Label titleLabel;
    Label scriptLabel;
    Label extensionLabel;
    Label expressLabel;
    Label environementLabel;
    N4uImportTab tabImporter;

    public N4uConverterTab() {
        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + "ExpressLane2VIP");
        this.setID(N4uConstants.TAB_EXPRESSLANE_1);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        this.setPane(layout);
    }

    public void configure() {
        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);

        titleLabel = new Label("<strong>Application Name </strong> <font color=red>(*)</font>");
        titleLabel.setHeight(20);

        jobLabel = new Label("<strong>Job File</strong><font color=red>(*)</font>");
        jobLabel.setHeight(20);

        expressLabel = new Label("<strong>Express File</strong><font color=red>(*)</font>");
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

        PickerIcon morePicker = new PickerIcon(new PickerIcon.Picker(N4uConstants.ICON_PICKER_MORE), new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
            }
        });

        morePicker.setPrompt("Add");
        PickerIcon lessPicker = new PickerIcon(new PickerIcon.Picker(N4uConstants.ICON_PICKER_LESS), new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                //parent.removeMember(instance);
            }
        });
        lessPicker.setPrompt("Remove");


        title = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        title.setValidators(ValidatorUtil.getStringValidator());


        express = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        express.setValidators(ValidatorUtil.getStringValidator());
        express.setIcons(browsePicker);

        job = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        job.setValidators(ValidatorUtil.getStringValidator());
        job.setIcons(browsePicker);

        script = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        script.setValidators(ValidatorUtil.getStringValidator());
        script.setIcons(browsePicker);

        ext = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        ext.setValidators(ValidatorUtil.getStringValidator());
        ext.setIcons(browsePicker);

        env = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        env.setValidators(ValidatorUtil.getStringValidator());
        env.setIcons(browsePicker);

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

        launchButton = new IButton();
        launchButton = WidgetUtil.getIButton("Import", N4uConstants.ICON_IMPORT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tabImporter = new N4uImportTab();
                Layout.getInstance().addTab(tabImporter);
                tabImporter.addfielsInputs("Appliction Name", title.getValueAsString());


                final AsyncCallback<List<List<String>>> callback = new AsyncCallback<List<List<String>>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("Unable to save parameters " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<List<String>> result) {


                        for (String s : result.get(0)) {
                            tabImporter.addfielsInputs("InputDir", s);
                        }
                        for (String s : result.get(1)) {
                            tabImporter.addfielsInputs("OutputDir", s);

                        }



                    }
                };
                 FileProcessService.Util.getInstance().fileTraitement(express.getValueAsString(), callback);
                //FileProcessService.Util.getInstance().fileTraitement("/home/nouha/express.txt", callback);
                tabImporter.addfielsInputs("Script File", script.getValueAsString());
                if (ext.getValueAsString() != null || ext.getValueAsString().isEmpty()) {
                    tabImporter.addfielsInputs("Extension File", ext.getValueAsString());
                }
                if (env.getValueAsString() != null || env.getValueAsString().isEmpty()) {
                    tabImporter.addfielsInputs("Environment File", env.getValueAsString());
                }

            }
        });

        layout.addMember(titleLabel);
        layout.addMember(titleItemForm);
        layout.addMember(jobLabel);
        layout.addMember(jobItemForm);
        layout.addMember(expressLabel);
        layout.addMember(expressItemForm);
        layout.addMember(scriptLabel);
        layout.addMember(scriptItemForm);
        layout.addMember(extensionLabel);
        layout.addMember(extItemForm);
        layout.addMember(environementLabel);
        layout.addMember(envItemForm);
        layout.addMember(launchButton);

    }
}