/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus;
import fr.insalyon.creatis.vip.core.client.bean.DropboxAccountStatus.AccountStatus;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DropboxLayout extends AbstractFormLayout {

    private CheckboxItem confirmField;
    private IButton linkDropboxButton, unlinkDropboxButton;
    private Label text;

    public DropboxLayout() {

        super(350, 120);
        addTitle("Link Dropbox account", CoreConstants.ICON_DROPBOX);

        configure();
    }

    private void configure() {

        text = WidgetUtil.getLabel("Link your Dropbox account to faciliate data transfers to/from VIP", 20);
        this.addMember(text);

        linkDropboxButton = new IButton("Link Dropbox Account");
        unlinkDropboxButton = new IButton("Unlink Dropbox Account");

        linkDropboxButton.setWidth100();
        unlinkDropboxButton.setWidth100();
        
        linkDropboxButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {


                final AsyncCallback<String> callback = new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Unable to link Dropbox account:<br />" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                       
                            Window.Location.replace(result.replace("REDIRECT",  com.google.gwt.http.client.URL.encode(GWT.getHostPageBaseURL())));
                       
                    }
                };
                ConfigurationService.Util.getInstance().linkDropboxAccount(callback);
                WidgetUtil.setLoadingIButton(linkDropboxButton, "Linking Dropbox account...");
            }
        });

        unlinkDropboxButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConfigurationService.Util.getInstance().unlinkDropboxAccount(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Cannot unlink Dropbox account");
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Layout.getInstance().setNoticeMessage("Dropbox account successfully unlinked");
                        load();
                    }
                });
            }
        });
        load();
        this.addMember(linkDropboxButton);
        this.addMember(unlinkDropboxButton);
    }

    private void load() {
        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        service.getDropboxAccountStatus(new AsyncCallback<AccountStatus>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot get status of Dropbox account");
            }

            @Override
            public void onSuccess(AccountStatus result) {
                linkDropboxButton.setVisible(true);
                unlinkDropboxButton.setVisible(true);
                if (result == DropboxAccountStatus.AccountStatus.OK) {
                    linkDropboxButton.setVisible(false);
                    text.setContents("/Home/Dropbox is linked to your Dropbox account and everything seems to be working fine.");
                } else {
                    if (result == DropboxAccountStatus.AccountStatus.UNCONFIRMED) {
                        text.setContents("You have linked your Dropbox account, but VIP's request hasn't been confirmed in time. Synchronization will not work. You should try to unlink it and link it again.");
                        linkDropboxButton.setVisible(false);
                    } else {
                        if(result == DropboxAccountStatus.AccountStatus.AUTHENTICATION_FAILED){
                            text.setContents("You have linked your Dropbox account, but authentication doesn't seem to be working. You should try to unlink it and link it again.");
                            linkDropboxButton.setVisible(false);
                        }
                        else                            
                            unlinkDropboxButton.setVisible(false);
                    }
                }
            }
        });

    }
}
