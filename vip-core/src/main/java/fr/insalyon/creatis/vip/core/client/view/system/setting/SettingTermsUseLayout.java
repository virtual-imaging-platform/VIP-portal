package fr.insalyon.creatis.vip.core.client.view.system.setting;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Arrays;

/**
 *
 * @author Nouha Boujelben
 */
public class SettingTermsUseLayout extends AbstractFormLayout {

    private IButton updateTermsUse;

    public SettingTermsUseLayout() {


        super("350", "100");
        addTitle("Terms Of Use", CoreConstants.ICON_TERMS_USE);
        configure();
    }

    private void configure() {
        updateTermsUse = WidgetUtil.getIButton("Terms of Use have been modified", CoreConstants.ICON_EDIT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                ConfigurationService.Util.getInstance().addTermsUse(getCallback("add"));


            }
        });
        updateTermsUse.setWidth(200);
        addButtons(updateTermsUse);
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to " + text + " terms of use:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                if(text.equals("add")){
                Layout.getInstance().setNoticeMessage("Terms of use has been updated");
                }
            }
        };
    }
}
