/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.cardiac.client.CardiacConstants;
import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.cardiac.client.rpc.CardiacService;
import fr.insalyon.creatis.vip.cardiac.client.rpc.CardiacServiceAsync;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author glatard
 */
public class CardiacTab extends Tab {

    private HLayout hLayout;
    private VLayout vLayout, mainPane;
    private SimulationEditLayout simulationEditLayout;
    protected ModalWindow modal;

    public CardiacTab() {
        this.setTitle(Canvas.imgHTML(CardiacConstants.ICON_SD) + " " + CardiacConstants.APP_SD);
        this.setID(CardiacConstants.TAB_SD);
        this.setCanClose(true);

        initLayout();

        loadData();
    }

    private void initLayout() {
         ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin(CardiacConstants.CARDIAC_GROUP)) {
            LabelButton addButton = new LabelButton("Add Simulation", CoreConstants.ICON_ADD);
            addButton.setWidth(150);
            addButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    setSimulation(null);
                }
            });
            toolstrip.addMember(addButton);
        }
        
        LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
        refreshButton.setWidth(150);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolstrip.addMember(refreshButton);

         mainPane = new VLayout();
        mainPane.setWidth100();
        mainPane.setHeight100();
        mainPane.setOverflow(Overflow.AUTO);
        mainPane.addMember(toolstrip);
        
        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        hLayout = new HLayout();
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);

        hLayout.addMember(vLayout);
        
        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin(CardiacConstants.CARDIAC_GROUP)) {
            simulationEditLayout = new SimulationEditLayout();
            hLayout.addMember(simulationEditLayout);
        }
        mainPane.addMember(hLayout);
        this.setPane(mainPane);

        modal = new ModalWindow(vLayout);
    }

    public void loadData() {
        try {
            modal.show("Loading cardiac simulations...", true);
            CardiacServiceAsync csa = CardiacService.Util.getInstance();
            AsyncCallback<List<Simulation>> acb = new AsyncCallback<List<Simulation>>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Cannot load cardiac simulations");
                }

                @Override
                public void onSuccess(List<Simulation> result) {
                    modal.hide();
                    initLayout();
                    if(result.size()==0)
                        vLayout.addMember(WidgetUtil.getLabel("<b>No data</b>", 15));
                    for (Simulation s : result) {
                        vLayout.addMember(new CardiacSimulationLayout(s));
                    }
                }
            };
            csa.getSimulations(acb);
        } catch (CardiacException ex) {
            Logger.getLogger(CardiacTab.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setSimulation(Simulation s) {
        simulationEditLayout.setSimulation(s);
    }
}
