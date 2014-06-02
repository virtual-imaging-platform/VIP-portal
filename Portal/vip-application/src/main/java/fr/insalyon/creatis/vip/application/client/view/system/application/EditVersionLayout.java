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
package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditVersionLayout extends AbstractFormLayout {

    private boolean newVersion;
    private String applicationName;
    private Label applicationLabel;
    private TextItem versionField;
    private TextItem lfnField;
    private CheckboxItem isVisibleField;
    private IButton saveButton;
    private IButton removeButton;

    public EditVersionLayout() {

        super(480, 200);
        addTitle("Add/Edit Version", ApplicationConstants.ICON_APPLICATION);

        configure();
    }

    private void configure() {

        newVersion = true;
        applicationLabel = WidgetUtil.getLabel("", 15);

        versionField = FieldUtil.getTextItem(450, null);
        versionField.setDisabled(true);

        lfnField = FieldUtil.getTextItem(450, null);
        lfnField.setDisabled(true);

        isVisibleField = new CheckboxItem();
        isVisibleField.setTitle("Visible");
        isVisibleField.setWidth(450);
        isVisibleField.setValue(true);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (versionField.validate() & lfnField.validate()) {
                    if (newVersion) {
                        save(new AppVersion(applicationName, versionField.getValueAsString().trim(),
                                lfnField.getValueAsString().trim(), isVisibleField.getValueAsBoolean()));
                    } else {
                        save(new AppVersion(applicationName, versionField.getValueAsString().trim(),
                                lfnField.getValueAsString().trim(), isVisibleField.getValueAsBoolean()));

                    }
                }
            }
        });
        saveButton.setDisabled(true);

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to remove this version?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            remove(applicationName, versionField.getValueAsString().trim());
                        }
                    }
                });
            }
        });
        removeButton.setDisabled(true);

        this.addMember(applicationLabel);
        addField("Version", versionField);
        addField("LFN", lfnField);
        this.addMember(FieldUtil.getForm(isVisibleField));
        addButtons(saveButton, removeButton);
    }

    /**
     *
     * @param version
     */
    private void save(AppVersion version) {

        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newVersion) {
            ApplicationService.Util.getInstance().addVersion(version, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().updateVersion(version, getCallback("update"));
        }
    }

    /**
     * Removes an application.
     *
     * @param applicationName Application name
     * @param version Application version
     */
    private void remove(String applicationName, String version) {

        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().removeVersion(applicationName, version, getCallback("remove"));
    }

    /**
     *
     * @param text
     * @return
     */
    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setVersion(null, null, true);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadVersions(applicationName);
            }
        };
    }

    /**
     *
     * @param applicationName
     */
    public void setApplication(String applicationName) {

        this.applicationName = applicationName;
        this.applicationLabel.setContents("<b>Application:</b> " + applicationName);
        this.versionField.setDisabled(false);
        this.lfnField.setDisabled(false);
        this.saveButton.setDisabled(false);
    }

    /**
     * Sets a version to edit or creates a blank form.
     *
     * @param version
     * @param lfn
     * @param isVisible
     */
    public void setVersion(String version, String lfn, boolean isVisible) {

        if (version != null) {
            this.versionField.setValue(version);
            this.versionField.setDisabled(true);
            this.lfnField.setValue(lfn);
            this.isVisibleField.setValue(isVisible);
            this.newVersion = false;
            this.removeButton.setDisabled(false);

        } else {

            this.versionField.setValue("");
            this.versionField.setDisabled(false);
            this.lfnField.setValue("");
            this.isVisibleField.setValue(true);
            this.newVersion = true;
            this.removeButton.setDisabled(true);
        }
    }
}
