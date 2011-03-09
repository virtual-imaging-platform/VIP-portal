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
package fr.insalyon.creatis.vip.datamanagement.client.view;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import fr.insalyon.creatis.vip.common.client.view.Context;

/**
 *
 * @author Rafael Silva
 */
public class FileUploadWindow extends Window {

    public FileUploadWindow(String baseDir) {

        this.setTitle("VIP File Upload");
        this.setLayout(new FitLayout());
        this.setWidth(400);

        this.add(getFormPanel(baseDir));
        this.show();
    }

    /**
     * 
     * @param baseDir
     * @return
     */
    private FormPanel getFormPanel(String baseDir) {
        final FormPanel formPanel = new FormPanel();
        formPanel.setTitle("Upload file to: " + baseDir);
        formPanel.setFileUpload(true);
        formPanel.setFrame(true);
        formPanel.setWidth(350);
        formPanel.setHeight(60);
        formPanel.setLabelWidth(75);

        // Setup error reader to process from submit response from server
        RecordDef errorRecordDef = new RecordDef(new FieldDef[]{
                    new StringFieldDef("id"),
                    new StringFieldDef("msg")
                });
        XmlReader errorReader = new XmlReader("field", errorRecordDef);
        errorReader.setSuccess("@success");
        formPanel.setErrorReader(errorReader);

        // Hidden Fields
        Hidden userHidden = new Hidden("userdn", Context.getInstance().getAuthentication().getUserDN());
//        Hidden proxyHidden = new Hidden("proxy", Context.getInstance().getAuthentication().getProxyFileName());
        Hidden proxyHidden = new Hidden("proxy", "/tmp/x509up_u501"); //TODO remove it
        Hidden pathHidden = new Hidden("path", baseDir);
        formPanel.add(proxyHidden);
        formPanel.add(userHidden);
        formPanel.add(pathHidden);

        // File Field
        TextField textField = new TextField("File", "file", 335);
        textField.setAllowBlank(false);
        textField.setInputType("file");
        formPanel.add(textField);

        this.addButton(new Button("Submit", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                formPanel.getForm().submit(
                        GWT.getModuleBaseURL() + "/fileuploadservice",
                        null, Connection.POST,
                        "Adding data to pool of transfers...",
                        false);
                EastPanel.getInstance().loadData();
            }
        }));

        formPanel.addFormListener(new FormListenerAdapter() {

            @Override
            public void onActionComplete(Form form, int httpStatus, java.lang.String responseText) {
                // TODO
            }

            @Override
            public void onActionFailed(Form form, int httpStatus, java.lang.String responseText) {
                com.google.gwt.user.client.Window.alert("File upload is failed.");
            }
        });

        return formPanel;
    }
}
