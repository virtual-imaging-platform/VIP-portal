/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.sql.Timestamp;

/**
 *
 * @author Nouha Boujelben
 */
public class TermsOfUseLayout extends AbstractFormLayout {

    private Label text;

    public TermsOfUseLayout() {


        super("350", "100");
        addTitle("Terms Of Use", CoreConstants.ICON_TERMS_USE);
        load();
    }

    public void load() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get user data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(User result) {
                String termsOfuse;
                if (result.getTermsOfUse() != null) {
                    termsOfuse = result.getTermsOfUse().toString();
                    String termsUse = termsOfuse.substring(0, termsOfuse.lastIndexOf("."));
                    text = WidgetUtil.getLabel("You accepted <a href=\"documentation/terms.html\">the terms of use</a> on " + termsUse, 20);
                } else {
                    text = WidgetUtil.getLabel("Please accept our Terms of Use", 20);
                    text.setBackgroundColor("#F79191");
                }
                if (getMember(1) == null) {
                    addMember(text);
                } else {
                    removeMember(getMember(1));
                    addMember(text);
                }

            }
        };
        service.getUserData(callback);
    }
}
