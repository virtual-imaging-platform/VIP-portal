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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.cardiac.client.CardiacConstants;
import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import com.smartgwt.client.widgets.events.ClickHandler;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.cardiac.client.rpc.CardiacService;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author glatard
 */
public class CardiacSimulationLayout extends HLayout {

    private VLayout vLayout;
    private ImgButton download, delete, edit, goToSimulation;

    public CardiacSimulationLayout(final Simulation s) {
        this.setMembersMargin(10);
        this.setWidth100();
        this.setHeight(70);
        this.setBorder("1px solid #CCCCCC");
        this.setPadding(4);


        VLayout pictures = new VLayout();
        if (s.getModalities().contains("CT")) {
            Img ct = new Img(CardiacConstants.ICON_CT);
            ct.setWidth(50);
            ct.setHeight(50);
            pictures.addMember(ct);
            pictures.addMember(WidgetUtil.getLabel("<i><font color=\"grey\">CT</font></i>", 25));
        }
        if (s.getModalities().contains("MRI")) {
            Img mri = new Img(CardiacConstants.ICON_MRI);
            mri.setWidth(50);
            mri.setHeight(50);
            pictures.addMember(mri);
            pictures.addMember(WidgetUtil.getLabel("<i><font color=\"grey\">MRI</font></i>", 25));
        }
        if (s.getModalities().contains("PET")) {
            Img pet = new Img(CardiacConstants.ICON_PET);
            pet.setWidth(50);
            pet.setHeight(50);
            pictures.addMember(pet);
            pictures.addMember(WidgetUtil.getLabel("<i><font color=\"grey\">PET</font></i>", 25));
        }
        if (s.getModalities().contains("Ultrasound")) {
            Img us = new Img(CardiacConstants.ICON_US);
            us.setWidth(50);
            us.setHeight(50);
            pictures.addMember(us);
            pictures.addMember(WidgetUtil.getLabel("<i><font color=\"grey\">Ultrasound</font></i>", 25));
        }
        this.addMember(pictures);

        VLayout rightButtons = new VLayout();

        if (s.getSimulationID() == null || s.getSimulationID().equals("")) {
            goToSimulation = getImgButton(CardiacConstants.ICON_NOSIMU, "No simulation is available for this image");
        } else {
            goToSimulation = getImgButton(CardiacConstants.ICON_SIMU, "View simulation");
            goToSimulation.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    viewSimulation(s);
                }
            });
        }
        goToSimulation.setWidth(60);
        goToSimulation.setHeight(60);
        rightButtons.addMember(goToSimulation);

        rightButtons.addMember(WidgetUtil.getLabel("<br/>", 25));

        download = getImgButton(CardiacConstants.ICON_DOWNLOAD, "Download simulation files");
        download.setWidth(60);
        download.setHeight(60);
        rightButtons.addMember(download);
        download.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                downloadFiles(s);
            }
        });



        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight(60);
        this.addMember(vLayout);

        this.addMember(rightButtons);






        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin(CardiacConstants.CARDIAC_GROUP)) {

            delete = getImgButton(CoreConstants.ICON_DELETE, "Delete");
            delete.setWidth(10);
            delete.setHeight(10);
            this.addMember(delete);
            delete.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    SC.ask("Do you really want to remove cardiac simulation \""
                            + s.getName() + "\"?", new BooleanCallback() {

                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                deleteSimu(s);
                            }
                        }
                    });

                }
            });

            edit = getImgButton(CoreConstants.ICON_EDIT, "Edit");
            edit.setWidth(10);
            edit.setHeight(10);
            this.addMember(edit);
            edit.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    CardiacTab tab = (CardiacTab) Layout.getInstance().
                            getTab(CardiacConstants.TAB_SD);
                    tab.setSimulation(s);
                }
            });
        }

        vLayout.addMember(WidgetUtil.getLabel("<b>" + s.getName() + "</b>", 25));
        String files = "";

        for (String f : s.getFiles()) {
            files += f.substring(f.lastIndexOf('/') + 1) + " ";
        }
        if (files.replaceAll(" ", "").equals("")) {
            files = "no file is associated to this simulation";
        }

        vLayout.addMember(WidgetUtil.getLabel("<i><font color=\"grey\">" + files + "</font></i>", 25));
        vLayout.addMember(WidgetUtil.getLabel(s.getDescription(), 25));

//        vLayout.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                downloadFiles(s);
//            }
//        });

    }

    private void downloadFiles(Simulation s) {
        for (final String f : s.getFiles()) {
            AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage("Unable to download file " + f + ":<br />" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    Layout.getInstance().setNoticeMessage("File " + f + " added to transfer queue.");
                    OperationLayout.getInstance().addOperation(result);
                    ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
                }
            };
            DataManagerService.Util.getInstance().downloadFile(f, callback);
        }
    }

    private void deleteSimu(final Simulation s) {

        AsyncCallback<Void> acb = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to delete cardiac simulation " + s.getName() + ":<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                CardiacTab tab = (CardiacTab) Layout.getInstance().
                        getTab(CardiacConstants.TAB_SD);
                tab.loadData();
            }
        };
        try {
            CardiacService.Util.getInstance().deleteSimulation(s, acb);
        } catch (CardiacException ex) {
            Layout.getInstance().setWarningMessage("Unable to delete cardiac simulation " + s.getName() + ":<br />" + ex.getMessage());
        }
    }

    private void viewSimulation(Simulation s) {
        for (String id : s.getSimulationID().split("\\s+")) {
            Layout.getInstance().addTab(new SimulationTab(id,
                    "Simulation " + id.substring(id.lastIndexOf("-") + 1), SimulationStatus.Completed));
        }
    }

    private ImgButton getImgButton(String imgSrc, String prompt) {
        ImgButton button = new ImgButton();
        button.setShowDown(false);
        button.setShowRollOver(false);
        button.setLayoutAlign(Alignment.CENTER);
        button.setSrc(imgSrc);
        button.setPrompt(prompt);
        button.setHeight(16);
        button.setWidth(16);
        return button;
    }
}
