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

import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.PortalLayout;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Camera;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;
import com.smartgwt.client.widgets.tab.TabSet;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;

/**
 *
 * @author Kevin Moulin, Rafael Silva
 */
public class DefineSceneSection extends SectionStackSection {

    private int compteur = 2;
    static public Scene sceneDraw = Scene.getInstance();
    private SimulationGUIControlBox boxUs;
    private SimulationGUIControlBox boxMri;
    private SimulationGUIControlBox boxCt;
    private SimulationGUIControlBox boxPet;
    private SimulationGUIControlBoxModel boxModel = SimulationGUIControlBoxModel.getInstance();
    private CheckboxItem checkBoxModel = new CheckboxItem("Model");
    private CheckboxItem checkBoxUs;
    private CheckboxItem checkBoxMri;
    private CheckboxItem checkBoxCt;
    private CheckboxItem checkBoxPet;
    private ImgButton buttonFront = new ImgButton();
    private ImgButton buttonBack = new ImgButton();
    private ImgButton buttonTop = new ImgButton();
    private ImgButton buttonBottom = new ImgButton();
    private ImgButton buttonLeft = new ImgButton();
    private ImgButton buttonRight = new ImgButton();
    private Img imgAxis = new Img(SimulationGUIConstants.ICON_AXIS);
    private VLayout vLayout1 = new VLayout();
    private VLayout vLayout2 = new VLayout();
    private HLayout hLayout1 = new HLayout();
    private HLayout hLayout2 = new HLayout();
    private HLayout hLayout3 = new HLayout();
    private HLayout hLayout4 = new HLayout();
    private HLayout hLayout5 = new HLayout();
    private PortalLayout portalLayout1 = new PortalLayout(2);
    private TabSet topTabSet = new TabSet();
    private DynamicForm form = new DynamicForm();
    private DynamicForm form2 = new DynamicForm();
    private DynamicForm form3 = new DynamicForm();
    private ModalWindow modal;
    private Slider hSlider1 = new Slider("scroll sensitivity");
    private Slider hSlider2 = new Slider("translation sensitivity");

    public DefineSceneSection() {

        this.setTitle("Scene");
        this.setExpanded(Boolean.TRUE);
        this.setCanCollapse(Boolean.FALSE);
        this.setShowHeader(Boolean.FALSE);

        topTabSet.setTabBarPosition(Side.TOP);
        topTabSet.setTabBarAlign(Side.RIGHT);
        topTabSet.setWidth(550);
        topTabSet.setHeight(140);

        boxUs = SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_US);
        boxMri = SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_MRI);
        boxCt = SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_CT);
        boxPet = SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_PET);

        checkBoxUs = boxUs.getCheckbox();
        checkBoxMri = boxMri.getCheckbox();
        checkBoxCt = boxCt.getCheckbox();
        checkBoxPet = boxPet.getCheckbox();

        portalLayout1.setShowColumnMenus(false);

        Tab camera = new Tab();
        camera.setIcon(SimulationGUIConstants.ICON_CAMERA);
        Tab tool = new Tab();
        tool.setIcon(SimulationGUIConstants.ICON_TOOL);
        Tab mouse = new Tab();
        mouse.setIcon(SimulationGUIConstants.ICON_MOUSE);

        topTabSet.addTab(camera);
        //topTabSet.addTab(tool);
        topTabSet.addTab(mouse);

        form.setFields(checkBoxModel, checkBoxUs);
        form2.setFields(checkBoxMri, checkBoxCt);
        form3.setFields(checkBoxPet);

        imgAxis.setWidth(100);
        imgAxis.setHeight(100);

        hLayout3.setWidth(465);
        hLayout4.setHeight(50);
        buttonFront.setSrc(SimulationGUIConstants.ICON_ZFRONT);
        buttonBack.setSrc(SimulationGUIConstants.ICON_ZBACK);
        buttonTop.setSrc(SimulationGUIConstants.ICON_YFRONT);
        buttonBottom.setSrc(SimulationGUIConstants.ICON_YBACK);
        buttonRight.setSrc(SimulationGUIConstants.ICON_XFRONT);
        buttonLeft.setSrc(SimulationGUIConstants.ICON_XFRONT);

        hSlider1.setVertical(false);
        hSlider1.setTop(200);
        hSlider1.setLeft(100);

        hSlider2.setVertical(false);
        hSlider2.setTop(200);
        hSlider2.setLeft(100);

        hLayout4.addMember(buttonFront);
        hLayout4.addMember(buttonBack);
        hLayout4.addMember(buttonTop);
        hLayout4.addMember(buttonBottom);
        hLayout4.addMember(buttonLeft);
        hLayout4.addMember(buttonRight);
        hLayout3.addMember(imgAxis);
        hLayout3.addMember(hLayout4);
        camera.setPane(hLayout3);

        hLayout5.addMember(hSlider1);
        hLayout5.addMember(hSlider2);
        mouse.setPane(hLayout5);

        vLayout1.addMember(sceneDraw);
        vLayout1.addMember(topTabSet);
        hLayout2.addMember(form);
        hLayout2.addMember(form2);
        hLayout2.addMember(form3);
        hLayout2.setHeight(50);
        hLayout2.setWidth(25);
        vLayout2.addMember(hLayout2);
        vLayout2.addMember(portalLayout1);
        hLayout1.addMember(vLayout1);
        hLayout1.addMember(vLayout2);

        modal = new ModalWindow(hLayout1);

        this.addItem(hLayout1);
        hLayout1.draw();

        initControls();
    }

    private void initControls() {
        
        checkBoxPet.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (checkBoxPet.getValueAsBoolean()) {
                    boxPet.disableView();
                    sceneDraw.addObject(boxPet.getObjectSimulateur(), 0);
                    portalLayout1.removePortlet(boxPet.getControlPortlet());
                    compteur--;
                } else {
                    boxPet.enableView();
                    sceneDraw.addObject(boxPet.getObjectSimulateur(), 0);
                    portalLayout1.addPortlet(boxPet.getControlPortlet(), compteur % 2, 0);
                    compteur++;
                }
            }
        });
        checkBoxUs.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (checkBoxUs.getValueAsBoolean()) {
                    boxUs.disableView();
                    sceneDraw.addObject(boxUs.getObjectSimulateur(), 1);
                    portalLayout1.removePortlet(boxUs.getControlPortlet());
                    compteur--;
                } else {
                    boxUs.enableView();
                    sceneDraw.addObject(boxUs.getObjectSimulateur(), 1);
                    portalLayout1.addPortlet(boxUs.getControlPortlet(), compteur % 2, 0);
                    compteur++;
                }
            }
        });
        checkBoxMri.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (checkBoxMri.getValueAsBoolean()) {
                    boxMri.disableView();
                    sceneDraw.addObject(boxMri.getObjectSimulateur(), 2);
                    portalLayout1.removePortlet(boxMri.getControlPortlet());
                    compteur--;
                } else {
                    boxMri.enableView();
                    sceneDraw.addObject(boxMri.getObjectSimulateur(), 2);
                    portalLayout1.addPortlet(boxMri.getControlPortlet(), compteur % 2, 0);
                    compteur++;
                }
            }
        });
        checkBoxCt.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (checkBoxCt.getValueAsBoolean()) {
                    boxCt.disableView();
                    sceneDraw.addObject(boxCt.getObjectSimulateur(), 3);
                    portalLayout1.removePortlet(boxCt.getControlPortlet());
                    compteur--;
                } else {
                    boxCt.enableView();
                    sceneDraw.addObject(boxCt.getObjectSimulateur(), 3);
                    portalLayout1.addPortlet(boxCt.getControlPortlet(), compteur % 2, 0);
                    compteur++;
                }
            }
        });

        checkBoxModel.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (checkBoxModel.getValueAsBoolean()) {

                    portalLayout1.removePortlet(boxModel);
                    compteur--;
                } else {
                    portalLayout1.addPortlet(boxModel, compteur % 2, 0);
                    compteur++;
                }
            }
        });
        buttonFront.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(0);
                Camera.getInstance().setAngleY(0);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        buttonBack.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(0);
                Camera.getInstance().setAngleY(180);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        buttonTop.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(270);
                Camera.getInstance().setAngleY(0);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        buttonBottom.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(90);
                Camera.getInstance().setAngleY(0);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        buttonLeft.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(0);
                Camera.getInstance().setAngleY(270);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        buttonRight.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Camera.getInstance().setTranslateX(0);
                Camera.getInstance().setTranslateY(0);
                Camera.getInstance().setAngleX(0);
                Camera.getInstance().setAngleY(90);
                Camera.getInstance().setViewToNormalZ();
                Scene.getInstance().refreshScreen();
            }
        });
        hSlider1.addValueChangedHandler(new ValueChangedHandler() {

            public void onValueChanged(ValueChangedEvent event) {
                Camera.getInstance().setStepOfViewScroll(event.getValue());
            }
        });
        hSlider2.addValueChangedHandler(new ValueChangedHandler() {

            public void onValueChanged(ValueChangedEvent event) {
                Camera.getInstance().setStepOfViewTranslation(event.getValue());
            }
        });
        ///////////////////Hover ///////////////////////:

        buttonFront.setCanHover(Boolean.TRUE);
        buttonFront.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonFront.setPrompt(prompt);
            }
        });
        buttonBack.setCanHover(Boolean.TRUE);
        buttonBack.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonBack.setPrompt(prompt);
            }
        });
        buttonTop.setCanHover(Boolean.TRUE);
        buttonTop.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonTop.setPrompt(prompt);
            }
        });
        buttonBottom.setCanHover(Boolean.TRUE);
        buttonBottom.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonBottom.setPrompt(prompt);
            }
        });
        buttonLeft.setCanHover(Boolean.TRUE);
        buttonLeft.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonLeft.setPrompt(prompt);
            }
        });
        buttonRight.setCanHover(Boolean.TRUE);
        buttonRight.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Preset of camera";
                buttonRight.setPrompt(prompt);
            }
        });
        hSlider1.setCanHover(Boolean.TRUE);
        hSlider1.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Set the sensitivity for the mouse scroll";
                hSlider1.setPrompt(prompt);
            }
        });
        hSlider2.setCanHover(Boolean.TRUE);
        hSlider2.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Set the sensitivity for the mouse translation (CTRL+click to translate)";
                hSlider2.setPrompt(prompt);
            }
        });
    }

    public void hideModal() {
        
        modal.hide();
    }

    public void showModal(String contents) {
        
        modal.show(contents, true);
    }
    /* 
    private BufferedImage canvasToImage(Canvas cnvs) {
    int w = cnvs.getWidth();
    int h = cnvs.getHeight();
    int type = BufferedImage.TYPE_INT_RGB;
    BufferedImage image = new BufferedImage(w,h,type);
    Graphics2D g2 = image.createGraphics();
    //cnvs.paint(g2);
    //cnvs.printComponents(g2);
    cnvs.g
    g2.dispose();
    return image;
    }*/
}
