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
