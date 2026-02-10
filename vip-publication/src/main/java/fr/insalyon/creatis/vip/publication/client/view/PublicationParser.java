package fr.insalyon.creatis.vip.publication.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;


/**
 *
 * @author Rafael Ferreira da Silva, Nouha Boujelben, Sorina Pop
 */
public class PublicationParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        addApplication(PublicationConstants.APP_PUBLICATIONS, PublicationConstants.APP_IMG_PUBLICATIONS);

        //call to test last publication update
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Cannot get the date of last publication update" + caught.getMessage(), 10);

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    showDialog(" You haven't updated your publications for a while. Please take a few minutes to review the list of publications that you made using VIP.");
                }

            }
        };
        ConfigurationService.Util.getInstance().testLastUpdatePublication(callback);
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(PublicationConstants.APP_PUBLICATIONS)) {
            Layout.getInstance().addTab(
                    PublicationConstants.TAB_PUBLICATION, PublicationTab::new);
            return true;
        }
        return false;
    }



    private void showDialog(String message) {
        final Dialog dialog = new Dialog();
        dialog.setTitle("Update publications");
        dialog.setMessage(message);
        dialog.setIcon("[SKIN]ask.png");
        dialog.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                dialog.destroy();
            }
        });
        Button ok = new Button("OK");
        ok.setWidth(180);
        Button anyPublication = new Button("I don't have any publication to add");
        anyPublication.setWidth(180);
        dialog.setButtons(ok, anyPublication);
        dialog.setIsModal(Boolean.TRUE);
        anyPublication.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("can't update field lastUpdatePublication in VIPUsers " + caught.getMessage(), 10);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        dialog.destroy();
                    }
                };
                service.updateLastUpdatePublication(callback);
            }
        });
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                        PublicationConstants.TAB_PUBLICATION, PublicationTab::new);
                dialog.destroy();
            }
        });
        dialog.draw();
    }
}
