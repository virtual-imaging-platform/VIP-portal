package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class FileUploadWindow extends Window {

    private DynamicForm form;
    private UploadItem fileItem;

    public FileUploadWindow(final ModalWindow modal, String baseDir, String target) {

        this.setTitle(Canvas.imgHTML(DataManagerConstants.ICON_UPLOAD) + " Upload file to: " + baseDir);
        this.setWidth(400);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        form = new DynamicForm();
        form.setPadding(5);
        form.setEncoding(Encoding.MULTIPART);
        form.setMethod(FormMethod.POST);
        form.setAction(GWT.getModuleBaseURL() + "/fileuploadservice");
        form.setTarget(target);
        form.setCheckFileAccessOnSubmit(false); // make JS upload synchronous as we destroy the form just after submit

        fileItem = new UploadItem("file");
        fileItem.setTitle("File");
        fileItem.setWidth(300);
        fileItem.setRequired(true);

        HiddenItem pathItem = new HiddenItem("path");
        pathItem.setValue(baseDir);
        HiddenItem targetItem = new HiddenItem("target");
        targetItem.setValue(target);

        ButtonItem uploadButton = new ButtonItem("Upload");
        uploadButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent e) {
                if (form.validate()) {
                    modal.show("Uploading file...", true);
                    form.submitForm();
                    destroy();
                }
            }
        });

        form.setItems(fileItem, pathItem, targetItem, uploadButton);

        this.addItem(form);
    }
}
