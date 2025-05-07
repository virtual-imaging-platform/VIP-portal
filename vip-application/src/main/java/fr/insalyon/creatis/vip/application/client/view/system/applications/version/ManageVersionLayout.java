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
package fr.insalyon.creatis.vip.application.client.view.system.applications.version;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Axel Bonnet
 */
public class ManageVersionLayout extends AbstractFormLayout {

    private String applicationName;
    private String applicationVersion;

    private Label statusLabel;
    private Label doiLabel;
    private IButton publishButton;

    public ManageVersionLayout() {

        super(480, 120);
        addTitle("Publish Version", ApplicationConstants.ICON_APPLICATION);

        configure();
    }

    private void configure() {

        statusLabel = WidgetUtil.getLabel("<b>Status:</b>", 15);
        doiLabel = WidgetUtil.getLabel("", 15);
        doiLabel.setVisible(false);

        publishButton = WidgetUtil.getIButton("Publish", CoreConstants.ICON_SAVE,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                publishVersion();
            }
        });
        publishButton.setDisabled(true);

        this.addMember(statusLabel);
        this.addMember(doiLabel);
        addButtons(publishButton);
    }

    private void publishVersion() {
        WidgetUtil.setLoadingIButton(publishButton, "Publishing...");

        AsyncCallback<String> publishCallback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(publishButton, "Publish", CoreConstants.ICON_SAVE);
                Layout.getInstance().setWarningMessage("Unable to publish this version.<br />Please verify it has an author configured.<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String doi) {
                WidgetUtil.resetIButton(publishButton, "Publish", CoreConstants.ICON_SAVE);
                Layout.getInstance().setNoticeMessage("Version published with success. DOI : " + doi);
                // reload all version to avoid cache issues
                setVersion(null,null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadVersions(applicationName);
            }
        };

        ApplicationService.Util.getInstance().publishVersion(applicationName, applicationVersion, publishCallback);
    }

    public void setApplication(String applicationName) {
        setVersion(null,null);
        this.applicationName = applicationName;
    }

    public void setVersion(String applicationVersion, String doi) {
        this.applicationVersion = applicationVersion;

        boolean isPublished = doi != null;
        boolean canBePublished = !isPublished; // XXX check non-empty descriptor field ?
        doiLabel.setVisible(isPublished);
        if (isPublished) {
            doiLabel.setContents("<b>DOI:</b> " + doi);
            statusLabel.setContents("<b>Status:</b> Published");
        } else {
            statusLabel.setContents("<b>Status:</b> Not published");
        }
        publishButton.setDisabled( ! canBePublished);
        if (!isPublished && !canBePublished) {
            publishButton.setTooltip("This version must have a json configured to be be published");
        } else {
            publishButton.setTooltip(null);
        }
    }
}
