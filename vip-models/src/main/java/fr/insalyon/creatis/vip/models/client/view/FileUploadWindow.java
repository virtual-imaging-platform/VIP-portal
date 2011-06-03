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
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import fr.insalyon.creatis.vip.common.client.view.Context;

/**
 *
 * @author Tristan Glatard
 */
public class FileUploadWindow extends Window {

    private DynamicForm form;
    private UploadItem fileItem;

    public FileUploadWindow(String baseDir) {
        
        this.setTitle("Upload file to: " + baseDir);
        this.setWidth(380);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        form = new DynamicForm();
        form.setEncoding(Encoding.MULTIPART);
        form.setMethod(FormMethod.POST);
        form.setAction(GWT.getModuleBaseURL() + "/fileuploadservice");
        form.setTarget("uploadTarget");

        fileItem = new UploadItem("file");
        fileItem.setTitle("File");
        fileItem.setWidth(300);
        fileItem.setRequired(true);

        HiddenItem userItem = new HiddenItem("user");
        userItem.setValue(Context.getInstance().getUser());
        HiddenItem userdnItem = new HiddenItem("userdn");
        userdnItem.setValue(Context.getInstance().getUserDN());
        HiddenItem proxyItem = new HiddenItem("proxy");
        proxyItem.setValue(Context.getInstance().getProxyFileName());
        HiddenItem pathItem = new HiddenItem("path");
        pathItem.setValue(baseDir);

        ButtonItem uploadButton = new ButtonItem("Upload");
        uploadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                if (form.validate()) {
                    form.submitForm();
                    // ModelImportTab.getInstance().addFile(fileItem.getValueAsString());
                    destroy();

                }
            }
        });
        form.addSubmitValuesHandler(new SubmitValuesHandler() {

            public void onSubmitValues(SubmitValuesEvent event) {
                //ModelImportTab.getInstance().addFile(fileItem.getValueAsString());
            }
        });
        form.setItems(fileItem, userItem, userdnItem, proxyItem,
                pathItem, uploadButton);

        this.addItem(form);
    }
}
