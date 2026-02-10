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
                        + "/" + folder + "/" + fileName + extension,
                        "", "");
            }
        });
        toolStrip.addButton(downloadButton);
    }

    private void loadString() {
        pane.setContents(content.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />"));
    }

}
