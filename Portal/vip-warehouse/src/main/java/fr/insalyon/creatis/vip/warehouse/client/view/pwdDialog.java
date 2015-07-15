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
