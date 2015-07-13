/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Portlet;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Object3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectSimulateur;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kevin Moulin, Rafael Ferreira da Silva
 */
public class SimulationGUIControlBox {

    /////////////////////Data Member//////////////////////
    private Portlet portletControl;
    private HLayout hLayout1 = new HLayout();
    private DynamicForm form = new DynamicForm();
    private DynamicForm form2 = new DynamicForm();
    private DynamicForm form3 = new DynamicForm();
    private DynamicForm form4 = new DynamicForm();
    private SpinnerItem spinnerx = new SpinnerItem();
    private SpinnerItem spinnery = new SpinnerItem();
    private SpinnerItem spinnerz = new SpinnerItem();
    private SpinnerItem spinnerax = new SpinnerItem();
    private SpinnerItem spinneray = new SpinnerItem();
    private SpinnerItem spinneraz = new SpinnerItem();
    private Slider hSlider = new Slider("Step");
    private String applicationClass;
    private Object3D simu;
    private CheckboxItem symBox;
    private CheckboxItem modAxis;
    private CheckboxItem masterCheckbox;
    private boolean enabled = false;
    private SelectItem simulatorSelectItem;
    private LaunchTab launchTab = null;
    private static HashMap<String, SimulationGUIControlBox> instances = new HashMap<String, SimulationGUIControlBox>();
    private final String SEPARATOR = "###";

    public static synchronized SimulationGUIControlBox getInstance(String applicationClass) {

        SimulationGUIControlBox inst = instances.get(applicationClass);
        if (inst == null) {
            inst = new SimulationGUIControlBox(applicationClass);
            instances.put(applicationClass, inst);
        }
        return inst;
    }

    private SimulationGUIControlBox(String applicationClass) {

        this.applicationClass = applicationClass;
        //simu = new ObjectSimulateur(applicationClass);

        masterCheckbox = new CheckboxItem(applicationClass);

        portletControl = new Portlet();
        portletControl.setTitle(applicationClass);

        simulatorSelectItem = new SelectItem("simulator ");
        form4.setFields(simulatorSelectItem);

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
        symBox = new CheckboxItem("Symbol");
        symBox.setValue(true);
        modAxis.setValue(true);

        form3.setAutoFocus(false);
        form3.setNumCols(6);
        form3.setFields(symBox, modAxis);

        hSlider.setVertical(false);
        hSlider.setMinValue(1f);
        hSlider.setMaxValue(10f);
        hSlider.setNumValues(1000);

        hSlider.setTop(200);
        hSlider.setLeft(100);

        hLayout1.addMember(hSlider);
        hLayout1.setWidth100();
        hLayout1.setHeight(50);

        portletControl.addItem(form4);
        portletControl.addItem(form2);
        portletControl.addItem(form);
        portletControl.addItem(form3);
        portletControl.addItem(hLayout1);
        simu = new ObjectSimulateur(applicationClass);

        setControl();

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
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnery.addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnerz.addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinnerax.addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinneray.addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinneraz.addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                updateSimu();
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });

        symBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                if (symBox.getValueAsBoolean()) {
                    hideSymbol();
                } else {
                    showSymbol();
                }
            }
        });

        modAxis.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                if (modAxis.getValueAsBoolean()) {
                    hideAxis();
                } else {
                    showAxis();
                }

            }
        });

        simulatorSelectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                hideLaunchTab();
                showLaunchTab(simulatorSelectItem.getValueAsString().split(SEPARATOR));
            }
        });

        //////////////////////////// Hover ///////////////////////
        simulatorSelectItem.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Available simulators for this modality";
                simulatorSelectItem.setPrompt(prompt);
            }
        });

        spinnerx.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the following object to this value in millimeter";
                spinnerx.setPrompt(prompt);
            }
        });
        spinnery.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the following object to this value in millimeter";
                spinnery.setPrompt(prompt);
            }
        });
        spinnerz.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Translate the following object to this value in millimeter";
                spinnerz.setPrompt(prompt);
            }
        });
        spinnerax.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the following object to this value in degree with XYZ convention";
                spinnerx.setPrompt(prompt);
            }
        });
        spinneray.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the following object to this value in degree with XYZ convention";
                spinnery.setPrompt(prompt);
            }
        });
        spinneraz.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Rotate the following object to this value in degree with XYZ convention";
                spinnerz.setPrompt(prompt);
            }
        });
        modAxis.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Show/hide the axis for the following object";
                spinnerz.setPrompt(prompt);
            }
        });
        symBox.addItemHoverHandler(new ItemHoverHandler() {
            public void onItemHover(ItemHoverEvent event) {
                String prompt = "Show/hide the symbol for the following object";
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

    public float[] getTabValue() {

        return new float[]{
                    spinnerx.getAttributeAsFloat(applicationClass),
                    spinnery.getAttributeAsFloat(applicationClass),
                    spinnerz.getAttributeAsFloat(applicationClass),
                    spinnerax.getAttributeAsFloat(applicationClass),
                    spinneray.getAttributeAsFloat(applicationClass),
                    spinneraz.getAttributeAsFloat(applicationClass)
                };
    }

    public Object3D getObjectSimulateur() {

        if (enabled) {
            return simu;
        }
        return null;
    }

    public void enableView() {

        enabled = true;
        loadFormSimulator();
        showSymbol();
        showAxis();

    }

    public void disableView() {
        enabled = false;
        hideLaunchTab();
    }

    public void setControlOnObject(Object3D mod) {
        simu = mod;
    }

    public Portlet getControlPortlet() {
        return portletControl;
    }

    public CheckboxItem getCheckbox() {
        return masterCheckbox;
    }

    private void loadFormSimulator() {

        if (simulatorSelectItem.getValues().length == 0) {
            final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage("Error executing get workflow descriptors lists:<br />" + caught.getMessage());
                    simulatorSelectItem.setValue("Cannot get simulator list");
                    launchTab = null;
                }

                @Override
                public void onSuccess(List<String[]> result) {

                    LinkedHashMap map = new LinkedHashMap();
                    for (String[] app : result) {
                        map.put(app[0] + SEPARATOR + app[1], app[0] + " " + app[1]);
                    }
                    simulatorSelectItem.setValueMap(map);
                    simulatorSelectItem.setDefaultToFirstOption(true);
                    if (!result.isEmpty()) {
                        simulatorSelectItem.setValue(result.get(0)[0] + " " + result.get(0)[1]);
                        showLaunchTab(result.get(0));
                    }
                }
            };
            ApplicationService.Util.getInstance().getApplicationsByClass(applicationClass, callback);
        } else {
            showLaunchTab(simulatorSelectItem.getValueAsString().split(SEPARATOR));
        }
    }

    private void showLaunchTab(String[] application) {

        Map<String, String> inputs = new HashMap<String, String>();
        inputs.put("Transformation", getTransformation());
        inputs.put("Model", SimulationGUITab.getModelStorage());
        inputs.put("Model URI", SimulationGUITab.getModelURI().substring(SimulationGUITab.getModelURI().lastIndexOf('#') + 1));

        String[] disabled = {"Transformation", "Model", "Model URI"};
        launchTab = new LaunchTab(application[0], application[1], applicationClass,
                SimulationGUITab.getModelName() + " simulation", inputs, disabled);//simulatorSelectItem.getValueAsString());

        Layout.getInstance().addTab(launchTab);
        Layout.getInstance().setActiveCenterTab(SimulationGUIConstants.TAB_EDITOR);
    }

    private void hideLaunchTab() {

        if (launchTab != null && launchTab.hasID()) {
            Layout.getInstance().removeTab(launchTab);
            launchTab = null;
        }
    }

    private void updateSimu() {
        simu.setTranslateX(Float.valueOf(spinnerx.getValueAsString()));
        simu.setTranslateY(Float.valueOf(spinnery.getValueAsString()));
        simu.setTranslateZ(Float.valueOf(spinnerz.getValueAsString()));
        simu.setAngleX(Integer.valueOf(spinnerax.getValueAsString()));
        simu.setAngleY(Integer.valueOf(spinneray.getValueAsString()));
        simu.setAngleZ(Integer.valueOf(spinneraz.getValueAsString()));
    }

    public void refreshLaunchTabValue() {

        if (launchTab != null) {
            launchTab.setInputValue("Transformation", getTransformation());
            launchTab.setInputValue("Model", SimulationGUITab.getModelStorage());
        }
        if (SimulationGUITab.getModelURI() != null && launchTab != null) {
            launchTab.setInputValue("Model URI", SimulationGUITab.getModelURI().substring(SimulationGUITab.getModelURI().lastIndexOf('#') + 1));
        }
    }

    public void showSymbol() {
        simu.enable("model");
        simu.enable("box");
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    public void hideSymbol() {
        simu.disable("model");
        simu.disable("box");
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    public void hideAxis() {
        simu.disable("axis");
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    public void showAxis() {
        simu.enable("axis");
        Scene.getInstance().refreshBuffer();
        Scene.getInstance().refreshScreen();
    }

    private String getTransformation() {

        updateSimu();

        float x = simu.getTranslateX() - ObjectModel.getInstance().getTranslateX();
        float y = simu.getTranslateY() - ObjectModel.getInstance().getTranslateY();
        float z = simu.getTranslateZ() - ObjectModel.getInstance().getTranslateZ();
        float ax = (ObjectModel.getInstance().getAngleX() - simu.getAngleX()) % 360;
        float ay = (ObjectModel.getInstance().getAngleY() - simu.getAngleY()) % 360;
        float az = (ObjectModel.getInstance().getAngleZ() - simu.getAngleZ()) % 360;

        return x + " " + y + " " + z + " " + ax + " " + ay + " " + az;

    }
}
