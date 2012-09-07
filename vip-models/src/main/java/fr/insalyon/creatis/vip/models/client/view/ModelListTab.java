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
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.models.client.ModelConstants;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class ModelListTab extends Tab {

    private ListGrid grid;
    private ModalWindow modal;
    private HandlerRegistration rowContextClickHandler;
    private HandlerRegistration rowMouseDownHandler;
    private SearchStackSection searchSection;


    public ModelListTab() {

        this.setTitle(Canvas.imgHTML(ModelConstants.ICON_MODEL) + " " + ModelConstants.APP_MODEL);
        this.setID(ModelConstants.TAB_MODEL_BROWSER);
        this.setCanClose(true);

        configureGrid();
        modal = new ModalWindow(grid);

        
             
        VLayout layout = new VLayout();

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.setTitle("Refresh");
        
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                searchSection.setExpanded(false);
                loadModels();
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton addButton = new ToolStripButton();
        addButton.setIcon(CoreConstants.ICON_ADD);
        addButton.setTitle("Upload");
        addButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new ModelImportTab(true, "Import model",""));
            }
        });
        toolStrip.addButton(addButton);

        ToolStripButton deleteButton = new ToolStripButton();
        deleteButton.setIcon(CoreConstants.ICON_CLEAR);
        deleteButton.setTitle("Delete all");
        if (!CoreModule.user.isGroupAdmin())
            deleteButton.disable();
        deleteButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to delete all the models? Model files will not be removed: you will have to clean them yourself.", new BooleanCallback() {

                    public void execute(Boolean value) {

                        if (value != null && value) {
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                public void onFailure(Throwable caught) {
                                    SC.warn("Failed to delete all models");
                                }

                                public void onSuccess(Void result) {
                                    SC.say("All models were deleted");
                                    loadModels();
                                }
                            };

                            ModelServiceAsync ms = ModelService.Util.getInstance();
                            ms.deleteAllModelsInTheTripleStore(callback);
                        }
                    }
                });
            }
        });
        //disable model deletion for now
        toolStrip.addButton(deleteButton);

        //Search
        searchSection = new SearchStackSection(this.getID());
        SectionStackSection gridSection = new SectionStackSection();

        gridSection.setCanCollapse(false);
        gridSection.setShowHeader(false);
        gridSection.addItem(grid);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        sectionStack.setSections(gridSection, searchSection);

        ToolStripButton searchButton = new ToolStripButton();
        searchButton.setIcon(ApplicationConstants.ICON_SEARCH);
        searchButton.setTitle("Search");
        searchButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                searchSection.setExpanded(true);
            }
        });
        toolStrip.addButton(searchButton);

        loadModels();

        layout.addMember(toolStrip);
        layout.addMember(sectionStack);

        //this will be triggered from the context menu
        //TODO: call model.storageURL when Germain implements it
        // String lfnModel = "/grid/biomed/creatis/vip/data/groups/VIP/Models/adam.zip";
        //  downloadModel(lfnModel) ;

        this.setPane(layout);
    }

    public void resetTab() {
        return;
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        // grid.setSelectionType(SelectionStyle.SIMPLE);
        //  grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        grid.setEmptyMessage("<br>No result.");

        //    ListGridField statusIcoField = FieldUtil.getIconGridField("statusIco");
        ListGridField modelNameField = new ListGridField("name", "Name");
        ListGridField typeField = new ListGridField("types", "Type(s)");
        ListGridField longitudinalField = new ListGridField("longitudinal", "Longitudinal");
        ListGridField movementField = new ListGridField("movement", "Movement");
        ListGridField URIField = new ListGridField("uri", "URI");
        ListGridField ownerField = new ListGridField("owner", "Owner");

       // modelNameField.setPrompt(grid.getSelectedRecord().getAttribute("uri"));
        modelNameField.setShowHover(true);
        
        modelNameField.setHoverCustomizer(new HoverCustomizer() {  
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {  
//                CountryRecord countryRecord = (CountryRecord) record;  
//                int governmentDesc = countryRecord.getGovernmentDesc();  
                return record.getAttribute("uri");  
            }  
        });  
        
        
     
        grid.setFields(modelNameField, ownerField,typeField, longitudinalField, movementField, URIField);
        grid.hideField("uri");

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {

            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String modelURI = event.getRecord().getAttribute("uri");
                String title = event.getRecord().getAttribute("name");
                String owner =  event.getRecord().getAttribute("owner");

                boolean bdelete = false;
                if (owner.equals( CoreModule.user.getLastName()))
                        bdelete = true;
                new ModelContextMenu(modal, modelURI, title, bdelete).showContextMenu();
            }
            
        });
        rowMouseDownHandler = grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            public void onRowMouseDown(RowMouseDownEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(event.getRecord().getAttribute("uri"), event.getRecord().getAttribute("name")));
            }
        });
    }

    public void loadModels() {
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<List<SimulationObjectModelLight>> callback = new AsyncCallback<List<SimulationObjectModelLight>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot list models:<br />" + caught.getMessage());
                modal.hide();
            }

            public void onSuccess(List<SimulationObjectModelLight> result) {
                setModelList(result);
                modal.hide();
            }
        };
        ms.listAllModels(callback);
        modal.show("Loading Models...", true);
    }

    public void setModelList(List<SimulationObjectModelLight> result) {
        List<SimulationObjectModelLightRecord> dataList = new ArrayList<SimulationObjectModelLightRecord>();

        for (SimulationObjectModelLight s : result) {
          
            String type = "";
            boolean[] saxes = s.getSemanticAxes();
            boolean init = false;
            if (saxes[0]) {
                type += "anatomical";
                init = true;
            }
            if (saxes[1]) {
                if (init) {
                    type += ", ";
                }
                init = true;
                type += "pathological";
            }
            if (saxes[2]) {
                if (init) {
                    type += ", ";
                }
                init = true;
                type += "geometrical";
            }
            if (saxes[3]) {
                if (init) {
                    type += ", ";
                }
                init = true;
                type += "foreign object";
            }
            if (saxes[4]) {
                if (init) {
                    type += ", ";
                }
                init = true;
                type += "external agent";
            }
            dataList.add(new SimulationObjectModelLightRecord(s.getModelName(), 
                    type, "" + s.isLongitudinal(), "" + s.isMoving(), s.getURI(),s.getOwner()));
        }
        grid.setData(dataList.toArray(new SimulationObjectModelLightRecord[]{}));
    }
}
