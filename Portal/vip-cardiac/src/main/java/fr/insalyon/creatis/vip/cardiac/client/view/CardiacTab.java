/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
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

        mainPane.addMember(WidgetUtil.getLabel("This database gathers multimodality (US, TEP, CT and MRI) simulations of the beating heart. It exploits VIP to access the modality simulators. The virtual heart model is the Anthropomorphic DynAmic Model of the beating heart and breathing thorax, ADAM in a healthy and a pathological versions [<a href=\"http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=4353715\">1</a>-<a href=\"http://www.sudoc.abes.fr/DB=2.1/SRCH?IKT=12&TRM=119342014\">2</a>]. Such simulations can serve as a basis for the evaluation of image analysis methods e.g. segmentation, motion estimation...",30));
        
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
                    if (result.size() == 0) {
                        vLayout.addMember(WidgetUtil.getLabel("<b>No data</b>", 15));
                    }
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
