package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class AddFolderWindow extends Window {

    private DynamicForm form;
    private TextItem nameItem;
    private ModalWindow modal;
    private String baseDir;

    public AddFolderWindow(ModalWindow modal, String baseDir) {

        this.modal = modal;
        this.baseDir = baseDir;
        
        this.setTitle(Canvas.imgHTML(DataManagerConstants.ICON_FOLDER_ADD) + " Create folder into: " + baseDir);
        this.setWidth(350);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        form = new DynamicForm();
        form.setAutoFocus(true);
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);

        nameItem = FieldUtil.getTextItem(200, true, "Name", "[0-9A-Za-z-_]");
        nameItem.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    createFolder();
                }
            }
        });
        nameItem.setSelectOnFocus(true);

        ButtonItem saveButton = new ButtonItem("addButton", "Create");
        saveButton.setWidth(60);
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                createFolder();
            }
        });

        form.setFields(nameItem, saveButton);
        this.addItem(form);
    }

    private void createFolder() {
        
        if (form.validate()) {
            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to create folder:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    modal.hide();
                    BrowserLayout.getInstance().loadData(baseDir, true);
                }
            };
            modal.show("Creating folder...", true);
            service.createDir(baseDir, nameItem.getValueAsString().trim(), callback);
            destroy();
        }
    }
}
