/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.n4u.client.EnumFieldTitles;
import fr.insalyon.creatis.vip.n4u.client.EnumInputTypes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.EditorExitEvent;
import com.smartgwt.client.widgets.form.fields.events.EditorExitHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uImportTab extends Tab {

    GeneralInformation layoutGeneralInformation;
    LayoutOutput layoutOutput;
    LayoutInputs layoutInputs;
    LayoutExecutable layoutExecutable;
    HLayout hLayout1;
    HLayout hLayout2;
    VLayout vlayout;
    private VLayout layout;
    PickerIcon browsePicker;
    IButton createApplicationButton;
    HashMap<Integer, HashMap<String, String>> listInputs;
    HashMap<Integer, HashMap<String, String>> listOutputs;
    List<TextItem> listItems;
    int item = 0;
    int key = 0;
    int outputItems = 0;
    String scriptFileName;
    String applicationName;
    String sandbox = "";
    String environementFile = "";
    String extensionFile = "";
    String descriptionValue = null;
    String applicationLocation;
    String version;

    public N4uImportTab() {

        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + " ExpressLaneImporter");
        this.setID(N4uConstants.TAB_EXPRESSLANE_2);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        listInputs = new HashMap<Integer, HashMap<String, String>>();
        listOutputs = new HashMap<Integer, HashMap<String, String>>();
        listItems = new ArrayList<TextItem>();
        this.setPane(layout);
    }

    private void configure() {
        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);

        hLayout1 = new HLayout();
        hLayout1.setMembersMargin(10);
        hLayout1.setHeight("50%");

        hLayout2 = new HLayout();
        hLayout2.setMembersMargin(10);
        hLayout2.setHeight("50%");

        vlayout = new VLayout();
        vlayout.setHeight100();
        vlayout.setWidth100();

        vlayout.setMembersMargin(2);
        vlayout.setOverflow(Overflow.AUTO);

        layoutGeneralInformation = new GeneralInformation("50%", "100%");

        layoutOutput = new LayoutOutput("50%", "100%");
        layoutInputs = new LayoutInputs("50%", "100%");
        layoutInputs.setBorder("1px solid #C0C0C0");

        layoutExecutable = new LayoutExecutable("50%", "100%");
        hLayout1.addMember(layoutGeneralInformation);
        hLayout1.addMember(layoutOutput);
        hLayout2.addMember(layoutInputs);
        hLayout2.addMember(layoutExecutable);

        layout.addMember(hLayout1);
        layout.addMember(hLayout2);

        browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });
        browsePicker.setPrompt("Browse on the Grid");

        layoutInputs.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addInputField(false, "", "", null, false);
            }
        });
        layoutInputs.addMember(vlayout);

        createApplicationButton = WidgetUtil.getIButton("Create Application", N4uConstants.ICON_LAUNCH,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        for (TextItem i : listItems) {
                            if (!i.validate()) {
                                Layout.getInstance().setWarningMessage("There is an invalid input");
                                return;
                            }
                        }

                        //verifying the application existance
                        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Layout.getInstance().setWarningMessage(caught.getMessage());
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                        //application exist and will create new version 
                                //create new application and new version

                                if (result.booleanValue()) {
                                    Layout.getInstance().setNoticeMessage("Creating the application (this can take a while)");
                                    createScriptFile(true, false);
                                } else {
                                    Layout.getInstance().setNoticeMessage("Creating the application (this can take a while)");
                                    createScriptFile(true, true);
                                }
                            }
                        };

                        ApplicationService.Util.getInstance().checkApplicationExistWithAnOtherOwner(applicationName, callback);

                    }
                });

        createApplicationButton.setWidth("150");
    }

    /**
     *
     * @param lfn
     */
    private void addApplication(final String lfn) {
        final List<String> applicationClasses = new ArrayList<String>();

        final AsyncCallback<String> classApplicationCallback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get Application Class  " + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {

                applicationClasses.add(result);
                final AsyncCallback<Void> call = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("can't add application  " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        ApplicationService.Util.getInstance().addVersion(new AppVersion(applicationName, version, lfn, true), getCallback("add Version"));
                    }
                };
                ApplicationService.Util.getInstance().add(new Application(applicationName, applicationClasses, ""), call);
            }
        };

        FileProcessService.Util.getInstance().getApplicationClass(classApplicationCallback);
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("could not" + text + " " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        };
    }

    /**
     *
     * @param newVersion
     * @param newApplication
     * @param maxVersion
     */
    private void createGwendiaFile(final boolean newVersion, final boolean newApplication) {
        final AsyncCallback<String> callback1 = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to create your Gwendia File" + caught.getMessage());
            }

            @Override
            public void onSuccess(final String result1) {
                Layout.getInstance().setNoticeMessage("Your Gwendia file was successfully created");

                if (newVersion && !newApplication) {

                    ApplicationService.Util.getInstance().addVersion(new AppVersion(applicationName, version, result1, true), getCallback("add Version"));

                } else if (newVersion && newApplication) {
                    addApplication(result1);
                }

            }
        };

        FileProcessService.Util.getInstance().generateGwendiaFile(listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, descriptionValue, callback1);

    }

    /**
     *
     * @param newVersion
     * @param newApplication
     * @param maxVersion
     */
    private void createGaswFile(final boolean newVersion, final boolean newApplication) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to create your Gasw File  " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Your Gasw file was successfully created");
                createGwendiaFile(newVersion, newApplication);

            }
        };

        FileProcessService.Util.getInstance().generateGaswFile(listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, descriptionValue, sandbox, environementFile, extensionFile, callback);

    }

    /**
     *
     * @param newVersion
     * @param newApplication
     * @param maxVersion
     */
    private void createScriptFile(final boolean newVersion, final boolean newApplication) {

        final AsyncCallback<Void> callback2 = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to create your Script File  " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Your Script file was successfully created");
                createGaswFile(newVersion, newApplication);
            }
        };

        FileProcessService.Util.getInstance().generateScriptFile(listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, environementFile, descriptionValue, callback2);
    }

//inputs
    public VLayout addInputField(boolean requiredInput, String value, String descriptionValue, EnumInputTypes typeValue, boolean isFixedType) {
        item++;
        key++;
        final int nombre = key;
        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();
        hlayout.setMembersMargin(2);

        final HashMap<String, String> map = new HashMap<String, String>();
        final TextItem fieldItem = FieldUtil.getTextItem("30%", false, "", "[0-9.,A-Za-z-+/_(): ]", false);
        fieldItem.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_(): ]"));
        fieldItem.setRequired(true);
        fieldItem.setValue(value);

        map.put("name", fieldItem.getValueAsString());
        fieldItem.addEditorExitHandler(new EditorExitHandler() {
            @Override
            public void onEditorExit(EditorExitEvent event) {
                map.put("name", fieldItem.getValueAsString());
            }
        });

        final TextArea description = new TextArea();
        description.setHeight("100%");
        description.setWidth("100%");
        description.setTitle("description");
        description.setValue(descriptionValue);
        description.addChangeHandler(new com.google.gwt.event.dom.client.ChangeHandler() {
            @Override
            public void onChange(com.google.gwt.event.dom.client.ChangeEvent event) {
                map.put("description", description.getValue());
            }
        });
        map.put("description", description.getValue());
        map.put("option", "no" + item);

        SelectItem selectItem = new SelectItem();
        selectItem.setShowTitle(false);

        selectItem.setValue(typeValue);

        selectItem.setWidth("100%");
        if (!isFixedType) {
            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            valueMap.put(EnumInputTypes.File.toString(), EnumInputTypes.File.toString());
            valueMap.put(EnumInputTypes.Parameter.toString(), EnumInputTypes.Parameter.toString());
            selectItem.setValueMap(valueMap);
        } else {
            selectItem.setValueMap(typeValue.toString());
        }

        selectItem.setValue(typeValue);

        if (typeValue.equals(EnumInputTypes.File)) {
            map.put("type", "LFN");
        } else {
            map.put("type", typeValue.toString());
        }
        selectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String ds = (String) event.getValue();
                if (ds.equals(EnumInputTypes.File.toString())) {

                    map.put("type", "LFN");
                } else {
                    map.put("type", event.getValue().toString());
                }

            }
        });
        String title;

        title = "<strong>" + "Input " + item + "</strong>";

        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setHeight(20);
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(fieldItem);

        DynamicForm selectItemForm = new DynamicForm();
        selectItemForm.setHeight(20);
        selectItemForm.setFields(selectItem);

        Label typeLabel = new Label("<strong>" + "Type:" + "</strong>");
        typeLabel.setHeight(20);
        typeLabel.setWidth(20);

        Label nameLabel = new Label("<strong>" + "Name:" + "</strong>");
        nameLabel.setHeight(20);
        nameLabel.setWidth(20);
        Label descriptionLabel = new Label("<strong>" + "Description:" + "</strong>");
        descriptionLabel.setHeight(20);
        descriptionLabel.setWidth(25);
        hlayout.addMember(nameLabel);
        hlayout.addMember(titleItemForm);
        hlayout.addMember(typeLabel);
        hlayout.addMember(selectItemForm);
        hlayout.addMember(descriptionLabel);
        hlayout.addMember(description);

        final SectionStack sectionStack = new SectionStack();

        SectionStackSection section1 = new SectionStackSection();

        section1.setTitle(title);
        final ImgButton removeButton = new ImgButton();
        removeButton.setSrc("[SKIN]actions/remove.png");
        removeButton.setSize(16);
        removeButton.setShowFocused(false);
        removeButton.setShowRollOver(false);
        removeButton.setShowDown(false);

        removeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                listItems.remove(fieldItem);
                item = item - 1;
                listInputs.remove(nombre);
                vlayout.removeMember(sectionStack);
            }
        });
        section1.setItems(hlayout);
        if (!requiredInput) {
            section1.setControls(removeButton);
        }

        section1.setExpanded(true);
        section1.setCanCollapse(false);
        sectionStack.setSections(section1);
        sectionStack.setHeight(120);
        sectionStack.setTitle("" + item);
        sectionStack.setShowHover(false);
        listInputs.put(nombre, map);
        listItems.add(fieldItem);
        vlayout.addMember(sectionStack);

        return vlayout;

    }

    public VLayout addOutputField(boolean fixedInput, String value, String descriptionValue, EnumInputTypes typeValue, boolean fixedType) {
        outputItems++;
        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();
        hlayout.setMembersMargin(2);

        final HashMap<String, String> map = new HashMap<String, String>();

        final TextItem fieldItem = FieldUtil.getTextItem("30%", false, "", "[0-9.,A-Za-z-+/_() ]", false);
        fieldItem.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_() ]"));
        fieldItem.setRequired(true);
        fieldItem.setValue(value);
        map.put("name", fieldItem.getValueAsString());
        fieldItem.addEditorExitHandler(new EditorExitHandler() {
            @Override
            public void onEditorExit(EditorExitEvent event) {
                map.put("name", fieldItem.getValueAsString());
            }
        });
        final TextArea description = new TextArea();
        description.setHeight("100%");
        description.setWidth("100%");
        description.setTitle("description");
        description.setValue(descriptionValue);
        description.addChangeHandler(new com.google.gwt.event.dom.client.ChangeHandler() {
            @Override
            public void onChange(com.google.gwt.event.dom.client.ChangeEvent event) {
                map.put("description", description.getValue());
            }
        });

        int k = item + outputItems;
        map.put("option", "no" + k);
        map.put("description", description.getValue());
        SelectItem selectItem = new SelectItem();
        selectItem.setShowTitle(false);

        selectItem.setValue(typeValue);

        selectItem.setWidth("100%");
        if (!fixedType) {
            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            valueMap.put(EnumInputTypes.File.toString(), EnumInputTypes.File.toString());
            valueMap.put(EnumInputTypes.Parameter.toString(), EnumInputTypes.Parameter.toString());
            selectItem.setValueMap(valueMap);
        } else {
            selectItem.setValueMap(typeValue.toString());
        }

        selectItem.setValue(typeValue);
        if (typeValue.equals(EnumInputTypes.File)) {
            map.put("type", "LFN");
        } else {
            map.put("type", typeValue.toString());
        }

        selectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String ds = (String) event.getValue();
                if (ds.equals(EnumInputTypes.File.toString())) {

                    map.put("type", "LFN");
                } else {
                    map.put("type", event.getValue().toString());
                }
            }
        });
        String title;

        title = "<strong>" + "Output" + outputItems + "</strong>";
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setHeight(20);
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(fieldItem);

        DynamicForm selectItemForm = new DynamicForm();
        selectItemForm.setHeight(20);
        selectItemForm.setFields(selectItem);

        Label typeLabel = new Label("<strong>" + "Type:" + "</strong>");
        typeLabel.setHeight(20);
        typeLabel.setWidth(20);

        Label nameLabel = new Label("<strong>" + "Name:" + "</strong>");
        nameLabel.setHeight(20);
        nameLabel.setWidth(20);
        Label descriptionLabel = new Label("<strong>" + "Description:" + "</strong>");
        descriptionLabel.setHeight(20);
        descriptionLabel.setWidth(25);
        hlayout.addMember(nameLabel);
        hlayout.addMember(titleItemForm);
        hlayout.addMember(typeLabel);
        hlayout.addMember(selectItemForm);
        hlayout.addMember(descriptionLabel);
        hlayout.addMember(description);

        final SectionStack sectionStack = new SectionStack();

        SectionStackSection section1 = new SectionStackSection();

        section1.setTitle(title);
        section1.setItems(hlayout);
        section1.setExpanded(true);
        section1.setCanCollapse(false);

        sectionStack.setSections(section1);
        sectionStack.setHeight(120);
        sectionStack.setTitle("" + item);

        listOutputs.put(outputItems, map);
        listItems.add(fieldItem);
        layoutOutput.addMember(sectionStack);
        return layoutOutput;
    }

    //Name,script, extention,env
    public void addFields(EnumFieldTitles title, boolean addBrowseIcon, String value, String keyPressFilter, boolean disabled, boolean required) {

        Label itemLabel = new Label("<strong>" + title + "</strong>");
        itemLabel.setHeight(20);

        final TextItem fieldItem = FieldUtil.getTextItem("*", false, "", keyPressFilter, disabled, required);
        fieldItem.setValidators(ValidatorUtil.getStringValidator(keyPressFilter));
        fieldItem.setValue(value);
        if (addBrowseIcon) {
            fieldItem.setIcons(browsePicker);
        }
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setWidth100();
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(fieldItem);

        if (title.equals(EnumFieldTitles.EnvironementFile)) {
            if (value != null) {
                environementFile = value;
            }
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    environementFile = fieldItem.getValueAsString();
                }
            });

        }

        if (title.equals(EnumFieldTitles.ExtensionFile)) {
            if (value != null) {
                extensionFile = value;
            }
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    extensionFile = fieldItem.getValueAsString();
                }
            });

        }

        if (title.equals(EnumFieldTitles.MainExecutable)) {
            scriptFileName = value;
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {

                    scriptFileName = fieldItem.getValueAsString();
                }
            });

        }

        if (title.equals(EnumFieldTitles.SandboxFile)) {

            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    sandbox = fieldItem.getValueAsString();
                }
            });

        }

        if (title.equals(EnumFieldTitles.ApplicationLocation)) {

            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    applicationLocation = fieldItem.getValueAsString();
                }
            });

        }
        if (title.equals(EnumFieldTitles.ApplicationName)) {

            applicationName = value.replaceAll("/", "");
            fieldItem.setValue(applicationName);
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    applicationName = fieldItem.getValueAsString();
                }
            });

        }

        if (title.equals(EnumFieldTitles.ApplicationVersion)) {
            fieldItem.setValue(value);
            version = value;
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    version = fieldItem.getValueAsString();
                }
            });

        }
        //add Member
        if (title.equals(EnumFieldTitles.ApplicationName) || title.equals(EnumFieldTitles.ApplicationVersion)) {
            layoutGeneralInformation.addMember(itemLabel);
            layoutGeneralInformation.addMember(titleItemForm);
        } else {
            layoutExecutable.addMember(itemLabel);
            layoutExecutable.addMember(titleItemForm);
        }
        listItems.add(fieldItem);

    }

    public void addFielDescription(String title, String value) {
        Label itemLabel = new Label("<strong>" + title + "</strong>");
        itemLabel.setHeight(20);

        final RichTextEditor description = new RichTextEditor();
        description.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().length() == 1) {
                    descriptionValue = description.getValue() + event.getKeyName().toLowerCase();
                }
            }
        });

        description.setHeight("40%");
        description.setWidth("100%");
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        description.setValue(value);
        layoutGeneralInformation.addMember(itemLabel);
        layoutGeneralInformation.addMember(description);
    }

    public void addLaunchButton() {

        layout.addMember(createApplicationButton);
    }
}
