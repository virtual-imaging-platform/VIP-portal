/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.client.view;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;


import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author cervenansky
 */
public class pwdDialog extends Window {
    
    private String mnickname;
    private String murl;
    private String mwarehouse;
    private String mtype;
   
    
    public pwdDialog(final WarehouseListTab opener)
    {
       
       
        setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + " Connection to " + mwarehouse);
        this.setWidth(350);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        DynamicForm form = new DynamicForm();
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);
        final PasswordItem pwd = new PasswordItem();
        pwd.setWidth(200);
        pwd.setTitle("password: ");
        pwd.setRequired(true);
        ButtonItem okButton = new ButtonItem("ok", "OK");
        okButton.setWidth(60);
        okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                opener.connect(pwd.getValueAsString());
                hide();
                
            }
        });
        form.setFields(pwd, okButton);
        this.addItem(form);
    }
    public void configure(String nickname, String url, String warehouse, String type)
    {
         mnickname = nickname;
        murl = url;
        mwarehouse = warehouse;
        mtype = type;
    }
}
