/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.manage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.portal.client.bean.Application;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.application.launch.LaunchTab;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationsStackSection extends SectionStackSection {

    private String applicationClass;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public ApplicationsStackSection(String applicationClass) {

        this.applicationClass = applicationClass;
        this.setTitle("Applications");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureGrid();

        VLayout vLayout = new VLayout();
        vLayout.setMaxHeight(400);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(grid);

        this.addItem(vLayout);
        loadData();
    }

    private void configureGrid() {
        grid = new ListGrid() {

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton("icon-edit.png", "Edit");
                    loadImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            String values = rollOverRecord.getAttribute("values");
                            LaunchTab launchTab = (LaunchTab) Layout.getInstance().
                                    getTab("launch-" + applicationClass.toLowerCase() + "-tab");
                            launchTab.loadInput(values);
                        }
                    });

                    ImgButton deleteImg = getImgButton("icon-delete.png", "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                            SC.confirm("Do you really want to remove the application \"" + name + "\"?", new BooleanCallback() {

                                public void execute(Boolean value) {
                                    if (value != null && value) {
                                        String path = rollOverRecord.getAttribute("path");
                                        List<String> classes = Arrays.asList(
                                                rollOverRecord.getAttribute("appclasses").split("##"));
                                        classes.remove(applicationClass);
                                        remove(new Application(name, path, classes));
                                    }
                                }
                            });
                        }
                    });

                    rollOverCanvas.addMember(loadImg);
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;
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
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField nameField = new ListGridField("name", "Name");
        ListGridField pathField = new ListGridField("path", "Path");

        grid.setFields(nameField, pathField);
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                //TODO call edit
            }
        });
    }

    /**
     * Loads list of applications.
     */
    public void loadData() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Application>> callback = new AsyncCallback<List<Application>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get applications list\n" + caught.getMessage());
            }

            public void onSuccess(List<Application> result) {

                List<ApplicationRecord> dataList = new ArrayList<ApplicationRecord>();

                for (Application app : result) {
                    dataList.add(new ApplicationRecord(app.getName(), app.getLfn(),
                            app.getApplicationClasses().toArray(new String[]{})));
                }
                grid.setData(dataList.toArray(new ApplicationRecord[]{}));
            }
        };
        service.getApplications(applicationClass, callback);
    }

    /**
     * Removes the current class from the application.
     * 
     * @param application Application object
     */
    private void remove(Application application) {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing remove application\n" + caught.getMessage());
            }

            public void onSuccess(String result) {
                if (!result.contains("Error: ")) {
                    loadData();
                }
                SC.say(result);
            }
        };
        service.update(application, callback);
    }
    
    /**
     * 
     * @param application Application object
     */
    private void edit(Application application) {
        
    }
}
