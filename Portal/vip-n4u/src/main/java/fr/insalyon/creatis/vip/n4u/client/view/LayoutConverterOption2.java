/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.n4u.client.EnumFieldTitles;
import fr.insalyon.creatis.vip.n4u.client.EnumInputTypes;
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
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class LayoutConverterOption2 extends AbstractFormLayout {

    private Label xmlFileLabel;
    private TextItem xmlFileItem;
    private DynamicForm xmlFileForm;
    private IButton importButton;
    N4uImportTab tabImporter;

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterOption2(int width, String height) {
        super(width, height);
        configure();
    }

    /**
     *
     * @param width
     * @param height
     */
    public LayoutConverterOption2(String width, String height) {
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
        importButton = WidgetUtil.getIButton("Import", N4uConstants.ICON_IMPORT,
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

                            tabImporter = new N4uImportTab();
                            Layout.getInstance().addTab(tabImporter);

                            for (int i = 0; i < result.size(); i++) {
                                if (i == 0) {
                                    tabImporter.addFields(EnumFieldTitles.ApplicationName, false, result.get(i)[0], "[0-9.,A-Za-z-+_ ]", false, true);
                                    tabImporter.addFields(EnumFieldTitles.ApplicationVersion, false, result.get(i)[1], "[0-9.,A-Za-z-+_() ]", false, true);
                                    tabImporter.addFielDescription("Documentation and Terms of Use", result.get(i)[2]);
                                    tabImporter.addInputField(true, "results-directory ", "Directory where the results will be stored", EnumInputTypes.Parameter, true);
                                    tabImporter.addInputField(true, "job name", "A string identifying the job name", EnumInputTypes.Parameter, true);

                                } else {
                                    // name,type;required
                                    //s[0],s[1],s[2]
                                    tabImporter.addInputField(Boolean.valueOf(result.get(i)[2]), result.get(i)[0], result.get(i)[3], EnumInputTypes.valueOf(result.get(i)[1]),false);
                                }

                            }

                            tabImporter.addOutputField(true, "Output", "A tar.gz file containing the results", EnumInputTypes.File, true);
                            tabImporter.addFields(EnumFieldTitles.MainExecutable, true, "", "[0-9.,A-Za-z-+/_(): ]", false, true);
                            tabImporter.addFields(EnumFieldTitles.ApplicationLocation, true, "", "[0-9.,A-Za-z-+/_(): ]", false, true);
                            tabImporter.addFields(EnumFieldTitles.ExtensionFile, true,"", "[0-9.,A-Za-z-+/_() ]", false, false);
                            tabImporter.addFields(EnumFieldTitles.EnvironementFile, true, "", "[0-9.,A-Za-z-+/_(): ]", false, false);
                            tabImporter.addFields(EnumFieldTitles.SandboxFile, true, "", "[0-9.,A-Za-z-+/_(): ]", false, false);
                            tabImporter.addLaunchButton();

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
