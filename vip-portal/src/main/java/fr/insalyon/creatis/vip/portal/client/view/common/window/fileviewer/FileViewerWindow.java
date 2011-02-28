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
package fr.insalyon.creatis.vip.portal.client.view.common.window.fileviewer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextArea;
import fr.insalyon.creatis.vip.portal.client.rpc.JobService;
import fr.insalyon.creatis.vip.portal.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class FileViewerWindow extends AbstractFileViewerWindow {

    private FileViewerWindow instance;
    private TextArea textArea;
    private String dir;
    private String fileName;
    private String workflowID;
    private String ext;
    private String filter;

    public FileViewerWindow(String dir, String fileName, String animatedTarget) {

        super(dir + "/" + fileName, animatedTarget);

        this.dir = dir;
        this.fileName = fileName;
        this.instance = this;

        textArea = new TextArea();
        textArea.setSize("100%", "100%");
        textArea.setReadOnly(true);

        this.add(textArea);

        refreshButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                loadFile();
                lastUpdatedItem.setText("Last updated on " + new Date());
                instance.doLayout();
            }
        });

        this.show();
        loadFile();
    }

    public FileViewerWindow(String workflowID, String dir, String fileName, String ext, String animatedTarget, String filter) {
        
        super(dir + "/" + fileName, animatedTarget);

        this.dir = dir;
        this.fileName = fileName;
        this.workflowID = workflowID;
        this.ext = ext;
        this.filter = filter;
        this.instance = this;

        textArea = new TextArea();
        textArea.setSize("100%", "100%");
        textArea.setReadOnly(true);

        this.add(textArea);

        refreshButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                loadFileExt();
                lastUpdatedItem.setText("Last updated on " + new Date());
                instance.doLayout();
            }
        });

        this.show();
        loadFileExt();
    }

    private void loadFile() {
        Ext.get(id).mask("Loading File...");
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get jobs list: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                textArea.setValue(result);
                Ext.get(id).unmask();
            }
        };
        service.getFile(dir, fileName, callback);
    }

    private void loadFileExt() {
        Ext.get(id).mask("Loading File...");
        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get jobs list: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                if (filter != null) {
                    result = result.substring(
                            result.indexOf(filter) + filter.length() + 1,
                            result.indexOf("</" + filter) - 1);
                }
                textArea.setValue(result);
                Ext.get(id).unmask();
            }
        };
        service.getFile(workflowID, dir, fileName, ext, callback);
    }
}
