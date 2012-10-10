/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.ExpeSummaryTriple;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SemanticRecord;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class SemanticTab extends Tab {

    protected ModalWindow modal;
    private String simulationID;
    private ListGrid grid;
    private ToolStrip toolStrip;
    private SelectItem pathItem;

    public SemanticTab(String simulationID) {

        this.simulationID = simulationID;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SEMANTICS));
        this.setPrompt("Semantics");

        configureToolStrip();
        configureGrid();
        modal = new ModalWindow(grid);

        VLayout vLayout = new VLayout();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(toolStrip);
        vLayout.addMember(grid);

        this.setPane(vLayout);

        loadData("/" + simulationID);
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField icoField = FieldUtil.getIconGridField("icon");
        ListGridField nameField = new ListGridField("subject", "Subject");
        ListGridField sizeField = new ListGridField("predicate", "Predicate");
        ListGridField lastModifiedField = new ListGridField("object", "Object");

        grid.setFields(icoField, nameField, sizeField, lastModifiedField);
        grid.setSortField("icon");
        grid.setSortDirection(SortDirection.DESCENDING);

//        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
//
//            public void onCellDoubleClick(CellDoubleClickEvent event) {
//                String type = event.getRecord().getAttributeAsString("icon");
//                if (type.contains("folder")) {
//                    String baseDir = event.getRecord().getAttributeAsString("baseDir");
//                    String name = event.getRecord().getAttributeAsString("name");
//                    loadData(baseDir + "/" + name);
//                }
//            }
//        });
//        grid.addRowContextClickHandler(new RowContextClickHandler() {
//
//            public void onRowContextClick(RowContextClickEvent event) {
//                event.cancel();
//                showContextMenu(event.getRecord());
//            }
//        });
    }

    private void configureToolStrip() {

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth(400);
        pathItem.setValue("/" + simulationID);
        toolStrip.addFormItem(pathItem);

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.setTooltip("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData(pathItem.getValueAsString());
            }
        });
        toolStrip.addButton(refreshButton);

    }

    public void loadData(final String baseDir) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<ExpeSummaryTriple>> callback = new AsyncCallback<List<ExpeSummaryTriple>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get semantic info:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<ExpeSummaryTriple> result) {
                SemanticRecord[] data = new SemanticRecord[result.size()];
                for (int i = 0; i < result.size(); i++) {

                    data[i] = new SemanticRecord(result.get(i).getSubject().getLabel(), result.get(i).getPredicate().getLabel(), result.get(i).getObject().getLabel());
                    grid.setData(data);
                }
                pathItem.setValue(baseDir);
                modal.hide();

            }
        };
        modal.show("Loading Semantic Data...", true);
        service.getSemantics(baseDir, callback);
    }
//    private void showContextMenu(ListGridRecord record) {
//
//        String type = record.getAttributeAsString("icon");
//        String dataName = record.getAttributeAsString("name");
//        String folder = record.getAttributeAsString("baseDir");
//
//        if (folder.equals("/" + simulationID)) {
//            folder = "./";
//        } else {
//            folder = folder.replace("/" + simulationID + "", "");
//        }
//        new LogsContextMenu(this, simulationID, dataName,
//                folder, type.contains("file")).showContextMenu();
//    }
}
