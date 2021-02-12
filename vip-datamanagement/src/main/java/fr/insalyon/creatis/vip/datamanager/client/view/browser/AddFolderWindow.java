/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
