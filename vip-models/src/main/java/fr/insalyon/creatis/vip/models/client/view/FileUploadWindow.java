/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author glatard
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
