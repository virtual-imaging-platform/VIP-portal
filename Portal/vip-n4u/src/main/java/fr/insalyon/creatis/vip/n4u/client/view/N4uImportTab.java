/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.n4u.client.EnumFieldTitles;
import fr.insalyon.creatis.vip.n4u.client.EnumInputTypes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.types.MultipleAppearance;
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
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.n4u.client.EnumCardinalityValues;
import java.util.Arrays;
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
    SelectItem classesPickList;
    
    HashMap<Integer, HashMap<String, String>> listInputs;
    HashMap<Integer, HashMap<String, String>> listOutputs;
    List<TextItem> listItems;
    int item = 0;
    int key = 0;
    int outputItems = 0;
    String scriptFileName=null;
    String applicationName;
    String sandbox =null;
    String environementFile  =null;
    String extensionFile =null;
    String descriptionValue = null;
    String applicationLocation;
    String version=null;
    String dockerImage=null;
    String commandLine=null;
    public N4uImportTab() {

        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE) + " ExpressLaneImporter");
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
        layoutGeneralInformation.setMembersMargin(2);
        layoutGeneralInformation.setOverflow(Overflow.AUTO);

        layoutOutput = new LayoutOutput("50%", "100%");
        layoutOutput.setMembersMargin(2);
        layoutOutput.setOverflow(Overflow.AUTO);
        layoutInputs = new LayoutInputs("50%", "100%");
        layoutInputs.setBorder("1px solid #C0C0C0");
        layoutInputs.setMembersMargin(2);
        layoutInputs.setOverflow(Overflow.AUTO);

        layoutExecutable = new LayoutExecutable("50%", "100%");
        layoutExecutable.setMembersMargin(2);
        layoutExecutable.setOverflow(Overflow.AUTO);
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
                addInputField(false, "", "", null, false,null,null);
            }
        });
        layoutInputs.addMember(vlayout);

     
    }

    /**
     *
     * @param lfn
     */
    private void addApplication(final String lfn) {
                     
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
                ApplicationService.Util.getInstance().add(new Application(applicationName, Arrays.asList(classesPickList.getValues()), ""), call);
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
    private void createGwendiaFile(final boolean newVersion, final boolean newApplication,String vo) {
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

        FileProcessService.Util.getInstance().generateGwendiaFile("vm/"+vo+"/gwendia.vm",listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, descriptionValue,vo, callback1);

    }

    /**
     *
     * @param newVersion
     * @param newApplication
     * @param maxVersion
     */
    private void createGaswFile(final boolean newVersion, final boolean newApplication,final String vo) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to create your Gasw File  " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Your Gasw file was successfully created");
                createGwendiaFile(newVersion, newApplication,vo);

            }
        };

        FileProcessService.Util.getInstance().generateGaswFile("vm/"+vo+"/gasw.vm",listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, descriptionValue, sandbox, environementFile, extensionFile, callback);

    }

    /**
     *
     * @param newVersion
     * @param newApplication
     * @param maxVersion
     */
    private void createScriptFile(final boolean newVersion, final boolean newApplication,final String vo) {

        final AsyncCallback<Void> callback2 = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to create your Script File  " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Your Script file was successfully created");
                createGaswFile(newVersion, newApplication,vo);
            }
        };

        FileProcessService.Util.getInstance().generateScriptFile("vm/"+vo+"/wrapper_script.vm",listInputs, listOutputs, "", scriptFileName, applicationName, applicationLocation, environementFile, descriptionValue,dockerImage,commandLine, callback2);
    }

//inputs
    public VLayout addInputField(boolean requiredInput, String value, String descriptionValue, EnumInputTypes typeValue, boolean isFixedType, EnumCardinalityValues cardinality,String commandLineKey) {

        item++;
        key++;
        final int nombre = key;
        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();
        hlayout.setMembersMargin(2);
        
        hlayout.setOverflow(Overflow.AUTO);
        final HashMap<String, String> map = new HashMap<String, String>();
        final TextItem fieldItem = FieldUtil.getTextItem("80", false, "", "[0-9.,A-Za-z-+/_(): ]", false);
        final TextItem fieldItem2 = FieldUtil.getTextItem("90", false, "", "[0-9.,A-Za-z-+/_(): ]", false);
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
        
        if (typeValue != null) {
            if (typeValue.equals(EnumInputTypes.File)) {
                map.put("type", "LFN");
            } else {
                map.put("type", typeValue.toString());
            }
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
        DynamicForm selectItemForm = new DynamicForm();
        selectItemForm.setHeight(20);
        selectItemForm.setFields(selectItem);
      
        hlayout.addMember(addLabel("Name",20,20));
        hlayout.addMember(addTextItem(fieldItem,value,map,"name"));
        hlayout.addMember(addLabel("Type",20,20));
        hlayout.addMember(selectItemForm);
        if(cardinality!=null){
        hlayout.addMember(addLabel("Cardinality",20,20));
        hlayout.addMember(addSelectItem(cardinality.toString(),map,"cardinality",EnumCardinalityValues.Single.toString(),EnumCardinalityValues.Single.toString(),20));
         }
        if(commandLineKey!=null){
        hlayout.addMember(addLabel("Command-Line-Key",20,20));
        hlayout.addMember(addTextItem(fieldItem2,commandLineKey,map,"commandLineKey"));
         }
        hlayout.addMember(addLabel("Description",20,25));
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

    public VLayout addOutputField(boolean fixedInput, String value, String descriptionValue, EnumInputTypes typeValue, boolean fixedType,EnumCardinalityValues cardinality,String commandLineKey,String valueTemplate) {
        outputItems++;
        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();
        hlayout.setMembersMargin(2);
        hlayout.setOverflow(Overflow.AUTO);

        final HashMap<String, String> map = new HashMap<String, String>();

        final TextItem fieldItem = FieldUtil.getTextItem("80", false, "", "[0-9.,A-Za-z-+/_() ]", false);
        final TextItem fieldItem2 = FieldUtil.getTextItem("90", false, "", "[0-9.,A-Za-z-+/_(): ]", false);
        final TextItem fieldItem3 = FieldUtil.getTextItem("90", false, "", "[0-9.,A-Za-z-+/_(): ]", false);
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
      

        DynamicForm selectItemForm = new DynamicForm();
        selectItemForm.setHeight(20);
        selectItemForm.setFields(selectItem);

        hlayout.addMember(addLabel("Name", 20, 20));
        hlayout.addMember(addTextItem(fieldItem,value,map,"name"));
        hlayout.addMember(addLabel("Type", 20, 20));
        hlayout.addMember(selectItemForm);
        if(cardinality!=null){
        hlayout.addMember(addLabel("Cardinality",20,20));
        hlayout.addMember(addSelectItem(cardinality.toString(),map,"cardinality",EnumCardinalityValues.Single.toString(),EnumCardinalityValues.Multiple.toString(),20));
        }
        if(commandLineKey!=null){
        hlayout.addMember(addLabel("Command-Line-Key",20,20));
        hlayout.addMember(addTextItem(fieldItem2,commandLineKey,map,"commandLineKey"));
         }
        if(valueTemplate!=null){
        hlayout.addMember(addLabel("Template Value",20,20));
        hlayout.addMember(addTextItem(fieldItem3,valueTemplate,map,"valueTemplate"));
         }
        hlayout.addMember(addLabel("Description", 20, 25));
        hlayout.addMember(description);
       
        final SectionStack sectionStack = new SectionStack();

        SectionStackSection section1 = new SectionStackSection();

        section1.setTitle(title);
        section1.setItems(hlayout);
        section1.setExpanded(true);
        section1.setCanCollapse(false);

        sectionStack.setSections(section1);
        sectionStack.setHeight(120);
        sectionStack.setTitle("" + outputItems);
        sectionStack.setShowHover(false);

        listOutputs.put(outputItems, map);
        listItems.add(fieldItem);
        layoutOutput.addMember(sectionStack);
        return layoutOutput;
    }

    //Name,script, extention,env
    public void addFields(EnumFieldTitles title, boolean addBrowseIcon, String value, String keyPressFilter, boolean disabled, boolean required, boolean inGeneralInformation) {

        Label itemLabel = new Label("<strong>" + title + "</strong>");
        itemLabel.setHeight(20);

        final TextItem fieldItem = FieldUtil.getTextItem("*", false, "", keyPressFilter, disabled, required);
        fieldItem.setValidators(ValidatorUtil.getStringValidator(keyPressFilter));
        fieldItem.setValidateOnChange(true);
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

                    fieldItem.validate();
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
          if (title.equals(EnumFieldTitles.DockerImage)) {
            if (value != null) {
               dockerImage = value;
            }
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    dockerImage = fieldItem.getValueAsString();
                }
            });

        }
          if (title.equals(EnumFieldTitles.CommandLine)) {
            if (value != null) {
               commandLine = value;
            }
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                     commandLine = fieldItem.getValueAsString();
                }
            });

        }
        //add Member
        if (inGeneralInformation) {
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

    public void addLaunchButton(final String vo) {
        IButton createApplicationButton;
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
                                    createScriptFile(true, false,vo);
                                } else {
                                    Layout.getInstance().setNoticeMessage("Creating the application (this can take a while)");
                                    createScriptFile(true, true,vo);
                                }
                            }
                        };

                        ApplicationService.Util.getInstance().checkApplicationExistWithAnOtherOwner(applicationName, callback);

                    }
                });

        createApplicationButton.setWidth("150");

        layout.addMember(createApplicationButton);
    }
    
    public void addClassItem(String title) {
        Label itemLabel = new Label("<strong>" + title + "</strong>"+"<font color=red>(*)</font>");
        itemLabel.setHeight(20);
        classesPickList = new SelectItem();
        classesPickList = new SelectItem();
        classesPickList.setShowTitle(false);
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(450);
        loadClasses( classesPickList);
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setWidth100();
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(classesPickList);
       
        layoutExecutable.addMember(itemLabel);
        layoutExecutable.addMember(titleItemForm);
    }

    private DynamicForm addSelectItem(String cardinality,final HashMap map,final String mapValue,final String val1,final String val2,int height){
    //enumValue, map, cardinality,EnumCardinalityValues.Single.toString(),EnumCardinalityValues.Multiple.toString()
       SelectItem selectItem = new SelectItem();
        selectItem.setShowTitle(false);
        selectItem.setWidth("100%");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(val1,val1);
        valueMap.put(val2, val2);
        selectItem.setValueMap(valueMap);
        selectItem.setValue(cardinality);
        if (cardinality != null) {
            if (cardinality.equals(val1)) {
                map.put(mapValue, val1);
            } else {
                map.put(mapValue, val2);
            }
        }
        selectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String ds = (String) event.getValue();
                if (ds.equals(val1)) {

                    map.put(mapValue,val1);
                } else {
                    map.put(mapValue, val2);
                }

            }
        });

        DynamicForm selectItemForm = new DynamicForm();
        selectItemForm.setHeight(height);
        selectItemForm.setFields(selectItem);
        return selectItemForm;
    
    }
    
    private Label addLabel(String name,int height, int width)
    {
        Label label = new Label("<strong>" + name+":" + "</strong>");
        label.setHeight(height);
        label.setWidth(width);
        return label;
    }
    
    private  DynamicForm addTextItem(final TextItem fieldItem,String value,final HashMap map,final String mapKey){
       
        fieldItem.setValidators(ValidatorUtil.getStringValidator("[0-9.,A-Za-z-+/_():[]@()$* ]"));
        fieldItem.setRequired(true);
        fieldItem.setValue(value);

        map.put(mapKey, fieldItem.getValueAsString());
        fieldItem.addEditorExitHandler(new EditorExitHandler() {
            @Override
            public void onEditorExit(EditorExitEvent event) {
                map.put(mapKey, fieldItem.getValueAsString());
            }
        });
        DynamicForm textItemForm = new DynamicForm();
        textItemForm.setHeight(20);
        
        textItemForm.setAutoWidth();
        textItemForm.setOverflow(Overflow.VISIBLE);
        textItemForm.setNumCols(1);
        textItemForm.setFields(fieldItem);
        return textItemForm;
    }
    
      private void loadClasses(final SelectItem selectItem) {

        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of classes:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<AppClass> result) {
                List<String> dataList = new ArrayList<String>();
                for (AppClass c : result) {
                    dataList.add(c.getName());
                }
                selectItem.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        ApplicationService.Util.getInstance().getClasses(callback);
    }
}
