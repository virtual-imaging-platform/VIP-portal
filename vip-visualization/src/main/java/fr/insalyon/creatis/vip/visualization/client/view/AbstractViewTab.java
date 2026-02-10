package fr.insalyon.creatis.vip.visualization.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.visualization.client.rpc.VisualizationService;
import fr.insalyon.creatis.vip.visualization.client.rpc.VisualizationServiceAsync;
import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** @author Tristan Glatard */
public abstract class AbstractViewTab extends Tab {

    protected final String filename;
    private final String lfn;

    public AbstractViewTab(String lfn) {
        this.filename = lfn.substring(lfn.lastIndexOf('/') + 1);
        this.setTitle(filename);
        this.setCanClose(true);
        this.setPane(new Canvas());
        this.lfn = lfn;
    }

    public void load() {
        loadLFN(lfn);
    }

    private void loadLFN(String lfn) {
        VisualizationServiceAsync vs = VisualizationService.Util.getInstance();
        Layout.getInstance().setNoticeMessage(
            "Downloading file "
            + lfn.substring(lfn.lastIndexOf('/') + 1)
            + " from storage element.  Please wait, this may take a while …", 0);

        vs.getVisualizationItemFromLFN(
            lfn,
            new AsyncCallback<VisualizationItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage(
                        "Cannot load file: " + caught.getMessage());
                }

                @Override
                public void onSuccess(VisualizationItem result) {
                    displayFile(result);
                }
            });
    }

    protected String getFileUrl(String lfn) {
        String filename = lfn.substring(lfn.lastIndexOf('/') + 1);
        // filename param is needed for ami to determine type
        return GWT.getModuleBaseURL() +
                "/filedownloadservice" +
                "?path=" + URL.encode(lfn) +
                "&filename=" + filename;
    }

    public abstract void displayFile(VisualizationItem url);
}
