/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view.dialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;

import com.smartgwt.client.types.*;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.List;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.models.client.view.ModelTreeGrid;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author cervenansky
 */
public class ModelCreateObjectLayerDialog extends Window {
    
    private Label selectLayerLabel = null;
    private Record selectRecord = null;
    private DynamicForm extensionForm = null;
    private RadioGroupItem layerRadio = null;
    private ModelTreeGrid tree = null;
    private int tp = -1;
    private int ins = -1;
    private DynamicForm layerForm = null;
    DynamicForm validateForm = null;

    public ModelCreateObjectLayerDialog(ModelTreeGrid treegrid, int timepoint, int instant) {
        tree = treegrid;
        tp = timepoint;
        ins = instant;
        this.setTitle("add Object Layer at timepoint: " + timepoint + " instant: " + instant);
        init();
    }

    public void init() {

        this.setTitle("add Object Layer");
        this.setWidth(380);
        this.setHeight(150);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        initForms();
        createOKBt();

    }


    private void createOKBt() {
        ButtonItem submitButton = new ButtonItem("OK");
        submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(
                    com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        addObjectLayer();
                        hide();
            }
        });
        validateForm = new DynamicForm();
        validateForm.setNumCols(8);
        validateForm.setMethod(FormMethod.POST);

        validateForm.setFields(submitButton);
        this.addItem(validateForm);
    }

   
    private boolean addObjectLayer() {
                tree.addObjectLayer(tp, ins, getLayer() );
        return true;
    }

    
  
    public DynamicForm getFormExt() {
        return extensionForm;
    }

    public String getOntoName() {
        return selectRecord.getAttribute("name");
    }

    public void setInstant(int ins) {
    }

    public void setTimePoint(int tp) {
    }

    public String getLayer() {
        String title = layerRadio.getValueAsString();
        return tree.getTypeFromMap(title).toString();
       
    }


    public void initForms() {
        
        selectLayerLabel = new Label("<b>Select Layer</b>");
        selectLayerLabel.setAlign(Alignment.LEFT);
        selectLayerLabel.setWidth100();
        selectLayerLabel.setHeight(20);
        selectLayerLabel.setBackgroundColor("#F2F2F2");
        this.addItem(selectLayerLabel);

        LinkedHashMap<String, String> layerMap = new LinkedHashMap<String, String>();
        layerMap.put("Anatomy", "Anatomy");
        layerMap.put("Pathology", "Pathology");
        layerMap.put("Geometry", "Geometry");
        layerMap.put("Foreign body", "Foreign body");
        layerMap.put("External agent", "External agent");
        

        layerRadio = new RadioGroupItem();
        layerRadio.setDefaultValue("exampleStyleOnline");
        layerRadio.setVertical(false);
        layerRadio.setShowTitle(false);
        layerRadio.setValueMap(layerMap);
        layerRadio.setValue("Anatomy");
        layerForm = new DynamicForm();
        layerForm.setFields(layerRadio);
        this.addItem(layerForm);

    }

   

    
}
