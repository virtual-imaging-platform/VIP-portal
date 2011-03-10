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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.TabPanel;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanagement.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class EastPanel extends TabPanel {

    private static EastPanel instance;
    private UploadPanel uploadPanel;
    private DownloadPanel downloadPanel;

    public static EastPanel getInstance() {
        if (instance == null) {
            instance = new EastPanel();
        }
        return instance;
    }

    private EastPanel() {
        this.setId("dm-east-panel");
        this.setWidth(300);
        this.setEnableTabScroll(true);
        this.setResizeTabs(true);
        this.setMinTabWidth(80);
        this.setCollapsible(false);

        uploadPanel = UploadPanel.getInstance();
        downloadPanel = DownloadPanel.getInstance();

        this.add(uploadPanel);
        this.add(downloadPanel);
    }

    public void loadData() {
        TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
        AsyncCallback<List<PoolOperation>> callback = new AsyncCallback<List<PoolOperation>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get files list: " + caught.getMessage());
                Ext.get("dm-east-panel").unmask();
            }

            public void onSuccess(List<PoolOperation> result) {

                if (result != null) {

                    List<PoolOperation> uploads = new ArrayList<PoolOperation>();
                    List<PoolOperation> downloads = new ArrayList<PoolOperation>();

                    for (PoolOperation op : result) {
                        if (op.getType().equals("Upload")) {
                            uploads.add(op);
                        } else {
                            downloads.add(op);
                        }
                    }
                    uploadPanel.loadData(uploads);
                    downloadPanel.loadData(downloads);
                } else {
                    MessageBox.alert("Error", "Unable to get list of operations.");
                }
                Ext.get("dm-east-panel").unmask();
            }
        };
        Authentication auth = Context.getInstance().getAuthentication();
        service.getOperations(auth.getUserDN(), auth.getProxyFileName(), callback);
        Ext.get("dm-east-panel").mask("Loading...");
    }
}
