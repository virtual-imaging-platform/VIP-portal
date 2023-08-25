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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
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
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class ViewerWindow extends Window {

    private ModalWindow modal;
    private ToolStrip toolStrip;
    private HTMLPane pane;
    private String simulationID;
    private String folder;
    private String fileName;
    private String extension;
    private String content = null;
    private boolean file;

    private void init(String title, String simulationID) {
        this.simulationID = simulationID;
        this.setTitle(title);
        this.setCanDragReposition(true);
        this.setCanDragResize(true);
        this.setShowMaximizeButton(true);
        this.setWidth(700);
        this.setHeight(450);
        this.centerInPage();

        pane = new HTMLPane();
        pane.setPadding(10);
        pane.setOverflow(Overflow.AUTO);
        pane.setStyleName("defaultBorder");

        modal = new ModalWindow(pane);

        configureToolStrip();

        this.addItem(toolStrip);
        this.addItem(pane);

        load();
    }

    /**
     * Views string content
     * @param title
     * @param simulationID
     * @param content
     */
    public ViewerWindow(String title, String simulationID, String content) {
        this.file = false;
        this.content = content;
        init(title, simulationID);

    }

    /**
     * Views file content
     * @param title
     * @param simulationID
     * @param folder
     * @param fileName
     * @param extension
     */
    public ViewerWindow(String title, String simulationID, String folder,
            String fileName, String extension) {
        this.file = true;
        this.folder = folder;
        this.fileName = fileName;
        this.extension = extension;
        init(title, simulationID);

    }

    private void load() {
        if (file) {
            loadFile();
        } else {
            loadString();
        }
    }

    private void loadFile() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get file:<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {
                modal.hide();
                pane.setContents(result.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />"));
            }
        };
        modal.show("Loading file '" + fileName + extension + "'...", true);
        service.readFile(simulationID, folder, fileName, extension, callback);

    }

    private void configureToolStrip() {

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.setTitle("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                load();
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton downloadButton = new ToolStripButton();
        downloadButton.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadButton.setTitle("Download");
        downloadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open(
                        GWT.getModuleBaseURL()
                        + "/getfileservice?filepath=" + "/" + simulationID
                        + "/" + folder + "/" + fileName + extension
                        + "&" + CoreConstants.COOKIES_SESSION
                        + "=" + Cookies.getCookie(CoreConstants.COOKIES_SESSION),
                        "", "");
            }
        });
        toolStrip.addButton(downloadButton);

        if (!file) {
            ToolStripButton jsonDownloadButton = new ToolStripButton();
            jsonDownloadButton.setTitle("Download JSON");
            jsonDownloadButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    downloadJsonContent();
                }
            });
            toolStrip.addButton(jsonDownloadButton);
        }
    }

    private void loadString() {
        pane.setContents(content.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />"));
    }

    private void downloadJsonContent() {
        String jsonContent = this.content;
        String mimeType = "application/json;charset=utf-8;";
        String blobData = "data:" + mimeType + ", " + encodeURIComponent(jsonContent);
        com.google.gwt.user.client.Window.open(blobData, "_blank", "Download JSON");
    }

    // This is a helper method for encoding the content
    private static native String encodeURIComponent(String content) /*-{
    return encodeURIComponent(content);
}-*/;

}
