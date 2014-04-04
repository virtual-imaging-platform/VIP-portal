/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.events.HasDataChangedHandlers;
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
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
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
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.events.DataChangedEvent;
import com.smartgwt.client.data.events.DataChangedHandler;
import com.smartgwt.client.data.events.HasDataChangedHandlers;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.FocusChangedEvent;
import com.smartgwt.client.widgets.events.FocusChangedHandler;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.events.KeyDownHandler;
import com.smartgwt.client.widgets.events.KeyPressEvent;


import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
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
    private VLayout layout2;
    PickerIcon browsePicker;
    PickerIcon morePicker;
    PickerIcon lessPicker;
    PickerIcon editPicker;
    IButton createApplicationButton;
    ArrayList listInputs;
    ArrayList listOutputs;
    List<TextItem>listItems;
    int i = 0;
    int j = 0;
    String scriptFileName;
    String applicationName;
    String descriptionValue;
    String applicationLocation;

    public N4uImportTab() {

        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + " ExpressLaneImporter");
        this.setID(N4uConstants.TAB_EXPRESSLANE_2);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);


        configure();
        listInputs = new ArrayList();
        listOutputs = new ArrayList();
        listItems = new ArrayList<TextItem>();
        this.setPane(layout);
    }

    public void configure() {
        layout2 = new VLayout();
        layout2.setWidth100();
        layout2.setHeight(20);

        layout2.setMembersMargin(5);

        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);


        hLayout1 = new HLayout();
        hLayout1.setMembersMargin(10);
        hLayout1.setHeight(250);
        hLayout2 = new HLayout();
        hLayout2.setMembersMargin(10);

        vlayout = new VLayout();
        vlayout.setHeight100();
        vlayout.setWidth100();

        vlayout.setMembersMargin(2);
        vlayout.setOverflow(Overflow.SCROLL);


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
                addFielsInputs(false, "", "", false);
            }
        });
        layoutInputs.addMember(vlayout);




        createApplicationButton = WidgetUtil.getIButton("Create Application", N4uConstants.ICON_LAUNCH,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean go=true;
                for (TextItem i:listItems){
               if(!i.validate()||i.getValueAsString()==""||i.getValueAsString()==null){
                    go=false;
                   Layout.getInstance().setWarningMessage("There is an invalid Input");
                }
                }
            if(go==true){
                final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("Unable  " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Layout.getInstance().setNoticeMessage("Your application was successfully created");
                    }
                };

                FileProcessService.Util.getInstance().generateFiles(listInputs, listOutputs, "", scriptFileName, applicationName, descriptionValue, applicationLocation, callback);
            }
            }
        });


        createApplicationButton.setWidth("150");





    }
//inputs

    public VLayout addFielsInputs(boolean fixedInput, String value, String typeValue, boolean fixedType) {
        i++;
       

        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();

        hlayout.setMembersMargin(2);


        final Map map = new HashMap();

        final TextItem fieldItem = FieldUtil.getTextItem(280, false, "", "[0-9.,A-Za-z-+/_() ]");
        fieldItem.setValue(value);
        fieldItem.setValidators(ValidatorUtil.getStringValidator());
        
        map.put("name", fieldItem.getValueAsString());

        fieldItem.addEditorExitHandler(new EditorExitHandler() {
            @Override
            public void onEditorExit(EditorExitEvent event) {
                map.put("name", fieldItem.getValueAsString());
            }
        });
        final RichTextEditor description = new RichTextEditor();
        description.setHeight(80);
        description.setWidth(380);
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setTitle("description");

        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        description.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if(event.getKeyName().length()==1){
              map.put("description", description.getValue()+event.getKeyName().toLowerCase());
                }
            }
        });
        map.put("description", description.getValue());
        map.put("option", "no" + i);

        SelectItem selectItem = new SelectItem();
        selectItem.setShowTitle(false);

        selectItem.setValue(typeValue);

        selectItem.setWidth(110);
        if (!fixedType) {
            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            valueMap.put(InputType.File.name(), InputType.File.name());
            valueMap.put(InputType.Parameter.name(), InputType.Parameter.name());
            //valueMap.put(InputType.Directory.name(), InputType.Directory.name());
            selectItem.setValueMap(valueMap);
        } else {
            selectItem.setValueMap(typeValue);
        }

         selectItem.setValue(typeValue);
         
         if(typeValue==InputType.File.name()){
          map.put("type", "LFN");
         } else{
          map.put("type", typeValue);
         } 
        selectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String ds = (String) event.getValue();
                if (ds.equals(InputType.File.name())) {

                    map.put("type", "LFN");
                } else {
                    map.put("type", event.getValue().toString());
                }

            }
        });
        String title;

        title = "<strong>" + "Input " + i + "</strong>";

        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setHeight(20);
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

                vlayout.removeMember(sectionStack);
                int id = Integer.parseInt(sectionStack.getTitle());
                i = i - 1;
                listInputs.remove(id - 1);
                listItems.remove(fieldItem);
                
           

            }
        });
        section1.setItems(hlayout);
        if (!fixedInput) {
            section1.setControls(removeButton);
        }

        section1.setExpanded(true);
        section1.setCanCollapse(false);

        sectionStack.setSections(section1);
        sectionStack.setHeight(120);
        sectionStack.setTitle("" + i);

        listInputs.add(map);
        listItems.add(fieldItem);
        vlayout.addMember(sectionStack);

        return vlayout;

    }

    public VLayout addFielsOutput(boolean fixedInput, String value, String typeValue, boolean fixedType) {
        j++;
        HLayout hlayout = new HLayout();
        hlayout.setHeight(80);
        hlayout.setWidth100();
        hlayout.setMembersMargin(2);

        final Map map = new HashMap();
        final TextItem fieldItem = FieldUtil.getTextItem(280, false, "", "[0-9.,A-Za-z-+/_() ]");
        fieldItem.setValue(value);
        fieldItem.setValidators(ValidatorUtil.getStringValidator());
        map.put("name", fieldItem.getValueAsString());

        fieldItem.addEditorExitHandler(new EditorExitHandler() {
            @Override
            public void onEditorExit(EditorExitEvent event) {
                map.put("name", fieldItem.getValueAsString());
            }
        });
        final RichTextEditor description = new RichTextEditor();
        description.setHeight(80);
        description.setWidth(380);
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setTitle("description");
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        description.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if(event.getKeyName().length()==1){
                map.put("description", description.getValue()+event.getKeyName().toLowerCase());
                }
            }
        });

        int k = i + j;
        map.put("option", "no" + k);
        map.put("description", description.getValue());

        SelectItem selectItem = new SelectItem();
        selectItem.setShowTitle(false);

        selectItem.setValue(typeValue);

        selectItem.setWidth(110);
        if (!fixedType) {
            LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            valueMap.put(InputType.File.name(), InputType.File.name());
            valueMap.put(InputType.Parameter.name(), InputType.Parameter.name());
            //valueMap.put(InputType.Directory.name(), InputType.Directory.name());
            selectItem.setValueMap(valueMap);
        } else {
            selectItem.setValueMap(typeValue);
        }

        selectItem.setValue(typeValue);
        if(typeValue==InputType.File.name()){
          map.put("type", "LFN");
         } else{
          map.put("type", typeValue);
         } 

        selectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String ds = (String) event.getValue();
                if (ds.equals(InputType.File.name())) {

                    map.put("type", "LFN");
                } else {
                    map.put("type", event.getValue().toString());
                }

            }
        });
        String title;


        title = "<strong>" + "Output" + j + "</strong>";
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setHeight(20);
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
        sectionStack.setTitle("" + i);
        listOutputs.add(map);
        listItems.add(fieldItem);
        layoutOutput.addMember(sectionStack);
        return layoutOutput;
    }

    //script, extention,env
    public void addfiels(String title, boolean addBrowseIcon, String value, boolean disabled) {

        Label itemLabel = new Label("<strong>" + title + "</strong>");


        itemLabel.setHeight(20);

        final TextItem fieldItem = FieldUtil.getTextItem(620, "[0-9.,A-Za-z-+/_() ]", disabled);
        fieldItem.setValidators(ValidatorUtil.getStringValidator());
        fieldItem.setName("editable");
        fieldItem.setValue(value);
        if (addBrowseIcon) {
            fieldItem.setIcons(browsePicker);
        }

        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setFields(fieldItem);

        if (title == "Main Executable <font color=red>(*)</font>") {
            scriptFileName=value;
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {

                    scriptFileName = fieldItem.getValueAsString();
                }
            });

        }

        if (title == "Application Location <font color=red>(*)</font>") {

            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    applicationLocation = fieldItem.getValueAsString();
                }
            });

        }
        if (title == "Application Name <font color=red>(*)</font>") {
             applicationName =value;
            fieldItem.addEditorExitHandler(new EditorExitHandler() {
                @Override
                public void onEditorExit(EditorExitEvent event) {
                    applicationName = fieldItem.getValueAsString();
                }
            });
            layoutGeneralInformation.addMember(itemLabel);
            layoutGeneralInformation.addMember(titleItemForm);
        } else {

            layoutExecutable.addMember(itemLabel);
            layoutExecutable.addMember(titleItemForm);
        }
      listItems.add(fieldItem);


    }

    public void addFielDescription(String title) {
        Label itemLabel = new Label("<strong>" + title + "</strong>");
        itemLabel.setHeight(20);

        final RichTextEditor description = new RichTextEditor();
         description.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if(event.getKeyName().length()==1){
                  descriptionValue =description.getValue()+event.getKeyName().toLowerCase();  
                }
            }
        });

        description.setHeight(80);
        description.setWidth(620);
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");

        layoutGeneralInformation.addMember(itemLabel);
        layoutGeneralInformation.addMember(description);


    }

    public static enum InputType {

        File, Parameter;
    }

    public void addLaunchButton() {
        layout.addMember(createApplicationButton);

    }

    public class MyRichTextEditor extends RichTextEditor implements com.smartgwt.client.widgets.form.fields.events.HasEditorExitHandlers, HasHandlers {

        public void test() {
            fireEvent(new EditorExitEvent(this.getJsObj()));

        }

        @Override
        public HandlerRegistration addEditorExitHandler(com.smartgwt.client.widgets.form.fields.events.EditorExitHandler handler) {

            return doAddHandler(handler, com.smartgwt.client.widgets.form.fields.events.EditorExitEvent.getType());

        }
    }
    
    
      

}
