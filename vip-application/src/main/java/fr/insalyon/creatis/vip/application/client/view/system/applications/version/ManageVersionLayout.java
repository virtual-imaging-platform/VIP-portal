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
                setVersion(null,null,null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadVersions(applicationName);
            }
        };

        ApplicationService.Util.getInstance().publishVersion(applicationName, applicationVersion, publishCallback);
    }

    public void setApplication(String applicationName) {
        setVersion(null,null,null);
        this.applicationName = applicationName;
    }

    public void setVersion(String applicationVersion, String descriptor, String doi) {
        this.applicationVersion = applicationVersion;

        boolean isPublished = doi != null;
        boolean canBePublished = !isPublished && descriptor != null;
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
