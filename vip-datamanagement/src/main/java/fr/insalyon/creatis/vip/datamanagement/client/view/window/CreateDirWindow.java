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
package fr.insalyon.creatis.vip.datamanagement.client.view.window;

import fr.insalyon.creatis.vip.datamanagement.client.view.panel.BrowserPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DataView.Data;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogServiceAsync;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class CreateDirWindow extends Window {

    public CreateDirWindow(String baseDir) {
        this.setTitle("VIP Create Directory");
        this.setLayout(new FitLayout());
        this.setWidth(450);
        this.add(getFormPanel(baseDir));
        this.show();
    }

    /**
     *
     * @param baseDir
     * @return
     */
    private FormPanel getFormPanel(final String baseDir) {
        FormPanel formPanel = new FormPanel();
        formPanel.setTitle("Create Directory in: " + baseDir);
        formPanel.setFrame(true);
        formPanel.setWidth(350);
        formPanel.setHeight(60);
        formPanel.setLabelWidth(75);

        // Name Field
        final TextField textField = new TextField("Name", "name", 335);
        textField.setAllowBlank(false);
        formPanel.add(textField);

        this.addButton(new Button("Create", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        Ext.get("dm-browser-panel").unmask();
                        MessageBox.alert("Error", "Error executing create dir: " + caught.getMessage());
                    }

                    public void onSuccess(Void result) {
                        Ext.get("dm-browser-panel").unmask();
                        BrowserPanel.getInstance().loadData(baseDir, false);
                    }
                };
//                Context context = Context.getInstance();
//                context.setLastGridFolderBrowsed(baseDir);
//                Authentication auth = context.getAuthentication();
//                service.createDir(auth.getProxyFileName(), baseDir, textField.getText(), callback);
                service.createDir("/tmp/x509up_u501", baseDir, textField.getText(), callback);
                Ext.get("dm-browser-panel").mask("Creating Directory...");
                close();
            }
        }));

        return formPanel;
    }
}
