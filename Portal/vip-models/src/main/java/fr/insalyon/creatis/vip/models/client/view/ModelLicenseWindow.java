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
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.RichTextItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author cervenansky
 */
public class ModelLicenseWindow  extends Window {
    private DynamicForm form;
    private RichTextItem despItem;
    private String nwLicence ="";
    private CheckboxItem cboxItem;
    
    
    public ModelLicenseWindow( String licence) {
        
      
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + " Rename ");
        this.setWidth(350);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        nwLicence = licence;
        
        form = new DynamicForm();
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);
        despItem = new RichTextItem();
                //FieldUtil.getTextItem(200, true, "Licence", "[0-9A-Za-z-_. ]");
        despItem.setValue(licence);
        despItem.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    
                }
            }
        });

        cboxItem = new CheckboxItem();
        cboxItem.setTitle("I accept the terms in the license agreement.");;
        
        ButtonItem downItem = new ButtonItem("Download own License", "License");
        downItem.setWidth(60);
        downItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                downloadLicense();
            }
        });
        
        ButtonItem okButton = new ButtonItem("ok", "OK");
        okButton.setWidth(60);
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                agree();
            }
        });
        
        form.setFields(despItem,cboxItem, okButton);
        this.addItem(form);
    }
    private void agree()
    {
        if (form.validate() && cboxItem.getValue().toString().contains("0")) {
                hide();
            }
    }

    public String getLicense()
    {
        return nwLicence;
    }
    private void downloadLicense()
    {
          new FileUploadWindow("local", "addFileComplete").show();
    }
    private native void initComplete(ModelImportTab upload) /*-{
        $wnd.addFileComplete = function (fileName) {
        upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::addFileComplete(Ljava/lang/String;)(fileName);
        };
    }-*/;

     public void addFileComplete(String filename) {
        
         // read the License
        ModelServiceAsync service = ModelService.Util.getInstance();
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onSuccess(String result)
            {
                nwLicence = result;
                despItem.setValue(nwLicence); 
            }
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot set the model storage URL");
            }
        };
        service.readLicense(filename, callback);
        //display the License
          
       
    }
}
