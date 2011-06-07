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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;

/**
 *
 * @author Rafael Silva
 */
public class FileViewerWindow extends Window {

    private ToolStrip toolStrip;
    private HTMLPane pane;
    private String simulationID;
    private String folder;
    private String fileName;
    private String extension;

    public FileViewerWindow(String title, String simulationID, String folder,
            String fileName, String extension) {

        this.simulationID = simulationID;
        this.folder = folder;
        this.fileName = fileName;
        this.extension = extension;

        this.setTitle(title);
        this.setCanDragReposition(true);
        this.setCanDragResize(true);
        this.setWidth(700);
        this.setHeight(450);
        this.centerInPage();

        pane = new HTMLPane();
        pane.setPadding(10);
        pane.setOverflow(Overflow.AUTO);
        pane.setStyleName("defaultBorder");

        configureToolStrip();

        this.addItem(toolStrip);
        this.addItem(pane);

        loadFile();
    }

    private void loadFile() {
        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get file: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                pane.setContents(result
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("\n", "<br />"));
            }
        };
        service.getFile(simulationID, folder, fileName, extension, callback);
    }

    private void configureToolStrip() {

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setTitle("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                loadFile();
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton downloadButton = new ToolStripButton();
        downloadButton.setIcon("icon-download.png");
        downloadButton.setTitle("Download");
        downloadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open(
                        GWT.getModuleBaseURL()
                        + "/getfileservice?filepath=" + "/" + simulationID
                        + "/" + folder + "/" + fileName + extension,
                        "", "");
            }
        });
        toolStrip.addButton(downloadButton);
    }
}
