/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.client.view;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.ColorPicker;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ColorSelectedEvent;
import com.smartgwt.client.widgets.form.events.ColorSelectedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Portlet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Object3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Moulin, Rafael Silva
 */
public class SimulationGUIControlBoxModel extends Portlet {

    /////////////////////Data Member//////////////////////
    private DynamicForm form = new DynamicForm();
    private DynamicForm form2 = new DynamicForm();
    private DynamicForm form3 = new DynamicForm();
    private SpinnerItem spinnerx = new SpinnerItem();
    private SpinnerItem spinnery = new SpinnerItem();
    private SpinnerItem spinnerz = new SpinnerItem();
    private SpinnerItem spinnerax = new SpinnerItem();
    private SpinnerItem spinneray = new SpinnerItem();
    private SpinnerItem spinneraz = new SpinnerItem();
    private Slider hSlider = new Slider("Step");
    private String id;
    private ObjectModel objectModel;
    private CheckboxItem modBox;
    private CheckboxItem modAxis;
    private boolean enable = false;
    private TreeNode[] elementData;
    private Tree elementTree;
    private TreeGrid elementTreeGrid;
    private HLayout hLayout1 = new HLayout();
    private HLayout hLayout2 = new HLayout();
    static private SimulationGUIControlBoxModel instance;
        private Logger logger = null;
    
    public static SimulationGUIControlBoxModel getInstance() {

        if (instance == null) {
            instance = new SimulationGUIControlBoxModel();
        }
        return instance;
    }

    private SimulationGUIControlBoxModel() {
        logger = Logger.getLogger("Simulation-model");
        this.setTitle("Model");

        id = "Model";
        objectModel = ObjectModel.getInstance();

        spinnerx.setName("x");
        spinnerx.setDefaultValue(0);

        spinnery.setName("y");
        spinnery.setDefaultValue(0);

        spinnerz.setName("z");
        spinnerz.setDefaultValue(0);

        spinnerax.setName("angle x");
        spinnerax.setDefaultValue(0);
        spinnerax.setMax(360);
        spinnerax.setMin(-360);

        spinneray.setName("angle y");
        spinneray.setDefaultValue(0);
        spinneray.setMax(360);
        spinneray.setMin(-360);

        spinneraz.setName("angle z");
        spinneraz.setDefaultValue(0);
        spinneraz.setMax(360);
        spinneraz.setMin(-360);

        form.setFields(spinnerx, spinnery, spinnerz);
        form2.setFields(spinnerax, spinneray, spinneraz);

        modAxis = new CheckboxItem("Axis");
        modBox = new CheckboxItem("Bounding box");

        modBox.setValue(true);
        //model.setValue(true);
        modAxis.setValue(true);

        form3.setAutoFocus(false);
        form3.setNumCols(6);
        form3.setFields(modBox, modAxis);

        hSlider.setVertical(false);
        hSlider.setMinValue(1f);
        hSlider.setMaxValue(10f);
        hSlider.setNumValues(1000);
        hSlider.setTop(200);
        hSlider.setLeft(100);

        hLayout1.setWidth100();
        hLayout1.setHeight(100);
        hLayout1.addMember(hSlider);

        elementTree = new Tree();
        elementTree.setModelType(TreeModelType.PARENT);
        elementTree.setRootValue("1");
        elementTree.setNameProperty("Element");
        elementTree.setIdField("ElementId");
        elementTree.setParentIdField("ReportsTo");
        elementTree.setOpenProperty("isOpen");
        elementTree.setData(elementData);

        elementTreeGrid = new TreeGrid();
        elementTreeGrid.setData(elementTree);
        elementTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        elementTreeGrid.setShowSelectedStyle(false);
        elementTreeGrid.setShowPartialSelection(true);
        elementTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
        elementTreeGrid.setCascadeSelection(true);
        elementTreeGrid.draw();

        hLayout2.setWidth100();
        hLayout2.setHeight(400);
        hLayout2.addMember(elementTreeGrid);

        setControl();

        this.addItem(form2);
        this.addItem(form);
        this.addItem(form3);
        this.addItem(hLayout1);
        this.addItem(hLayout2);
    }

    private void setControl() {

        hSlider.addValueChangedHandler(new ValueChangedHandler() {

            public void onValueChanged(ValueChangedEvent event) {
                int value = event.getValue();
                spinnerx.setStep(value);
                spinnery.setStep(value);
                spinnerz.setStep(value);
            }
        });

        spinnerx.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setTranslateX(Float.valueOf(spinnerx.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnery.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setTranslateY(Float.valueOf(spinnery.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnerz.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setTranslateZ(Float.valueOf(spinnerz.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnerax.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setAngleX(Integer.valueOf(spinnerax.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinneray.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setAngleY(Integer.valueOf(spinneray.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinneraz.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                objectModel.setAngleZ(Integer.valueOf(spinneraz.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });

        modBox.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (modBox.getValueAsBoolean()) {
                    objectModel.disable("box");
                } else {
                    objectModel.enable("box");
                }
                Scene.getInstance().refreshBuffer();
                Scene.getInstance().refreshScreen();
            }
        });
        modAxis.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (modAxis.getValueAsBoolean()) {
                    objectModel.disable("axis");
                } else {
                    objectModel.enable("axis");
                }
                Scene.getInstance().refreshBuffer();
                Scene.getInstance().refreshScreen();
            }
        });

        //////////////////////////// Hover ///////////////////////
        spinnerx.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the model to this value in millimeter";
                spinnerx.setPrompt(prompt);
            }
        });
        spinnery.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the model to this value in millimeter";
                spinnery.setPrompt(prompt);
            }
        });
        spinnerz.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the model to this value in millimeter";
                spinnerz.setPrompt(prompt);
            }
        });
        spinnerax.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the model to this value in degree with XYZ convention";
                spinnerx.setPrompt(prompt);
            }
        });
        spinneray.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the model to this value in degree with XYZ convention";
                spinnery.setPrompt(prompt);
            }
        });
        spinneraz.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the model to this value in degree with XYZ convention";
                spinnerz.setPrompt(prompt);
            }
        });
        modAxis.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Show/hide the axis for the model";
                spinnerz.setPrompt(prompt);
            }
        });
        modBox.addItemHoverHandler(new ItemHoverHandler() {

            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Show/hide the bouding box for the model";
                spinnerz.setPrompt(prompt);
            }
        });
        hSlider.setCanHover(Boolean.TRUE);
        hSlider.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Set the step for translation spinners";
                hSlider.setPrompt(prompt);
            }
        });
    }

    public void checkBoxBox() {
        modBox.setValue(true);
    }

    public void uncheckBoxBox() {
        modBox.setValue(false);
    }

    public void checkBoxAxis() {
        modAxis.setValue(true);
    }

    public void uncheckBoxAxis() {
        modAxis.setValue(false);
    }

    public Object3D getObjectSimulateur() {
        return objectModel;
    }

    public void enableView() {
        enable = true;
    }

    public void disableView() {
        enable = false;
    }

    public void setTreeNode(Data3D[][] DATA) {
        logger.info("size data ");
        logger.info("size data " + String.valueOf(DATA.length));
        int i = 0;
        for (Data3D[] d : DATA) {
            if (d != null)
            {
                i += d.length;
            }
        }
        String s = " i initial : " + i;
        elementData = new TreeNode[i];
        i = 0;

        for (Data3D[] d : DATA) {
            if(d != null)
            {
                for (Data3D d1 : d) {
                    if(d1 != null)
                    {
                        if (d1.getID().endsWith(".mhd")) {
                            elementData[i] = new ElementTreeNode(d1.getType(), "1", d1.getType(), false);
                            s += "/////";
                            i++;
                        }
                    }
                }
            }
        }
        for (Data3D[] d : DATA) {
            if(d != null)
            { //logger.info("size data " + String.valueOf(d.length));
                for (Data3D d1 : d) {
                    if(d1 != null)
                    {
                        if (d1.getID().endsWith(".vtp") || d1.getID().endsWith(".vtk") ) {
                            //logger.info(d1.getID());
                            Canvas canvas = new Canvas();
                            canvas.setBackgroundColor("red");

                            elementData[i] = new ElementTreeNode(d1.getID(), d1.getType(), d1.getID(), false);
                            elementData[i].setBackgroundComponent(canvas);
                            s += " [" + i + "] " + " Type : " + d1.getType() + " name " + d1.getID();
                            i++;
                        }
                    }
                }
            }
        }



        /* ColorPickerItem colorPicker = new ColorPickerItem();  
        colorPicker.setTitle("Color Picker");  
        colorPicker.setWidth(85);  */
        hLayout2.removeMember(elementTreeGrid);

        elementTree = new Tree();
        elementTree.setModelType(TreeModelType.PARENT);
        elementTree.setRootValue("1");
        elementTree.setNameProperty("Element");
        elementTree.setIdField("ElementId");
        elementTree.setParentIdField("ReportsTo");
        elementTree.setOpenProperty("isOpen");
        elementTree.setData(elementData);
        


        elementTreeGrid = new TreeGrid();
        elementTreeGrid.setData(elementTree);
        elementTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        elementTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
        elementTreeGrid.setShowSelectedStyle(false);
        elementTreeGrid.setShowPartialSelection(true);
        elementTreeGrid.setCascadeSelection(true);
        elementTreeGrid.setShowBackgroundComponent(true);
        elementTreeGrid.selectAllRecords();
        elementTreeGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                //
                ColorPicker cp = new ColorPicker();
                cp.show();
                final String name = event.getRecord().getAttribute("ElementId");

                cp.addColorSelectedHandler(new ColorSelectedHandler() {

                    public void onColorSelected(ColorSelectedEvent event) {
                        if (name.endsWith(".vtp")) {
                            objectModel.colorElement(name, event.getColor(), ((float) event.getOpacity()) / 100);
                        }
                    }
                });

            }
        });
        elementTreeGrid.draw();
        elementTreeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                objectModel.unsetElement(event.getSelection());
            }
        });

        hLayout2.addMember(elementTreeGrid);
    }

    public static class ElementTreeNode extends TreeNode {

        public ElementTreeNode(String elementId, String reportsTo, String element, boolean isOpen) {

            setAttribute("ElementId", elementId);
            setAttribute("ReportsTo", reportsTo);
            setAttribute("Element", element);
            setAttribute("isOpen", isOpen);
        }
    }

    private void refreshLaunchTabValue() {
        
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_US).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_MRI).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_CT).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_PET).refreshLaunchTabValue();
    }
}
