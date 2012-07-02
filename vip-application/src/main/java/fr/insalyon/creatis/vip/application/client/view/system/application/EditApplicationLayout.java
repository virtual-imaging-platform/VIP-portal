/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class EditApplicationLayout extends AbstractFormLayout {

    private boolean newApplication = true;
    private TextItem nameField;
    private TextItem lfnField;
    private SelectItem classesPickList;
    private IButton removeButton;

    public EditApplicationLayout() {

        super(480, 200);
        addTitle("Add/Edit Application", ApplicationConstants.ICON_APPLICATION);

        configure();
        loadClasses();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(450, null);
        lfnField = FieldUtil.getTextItem(450, null);

        classesPickList = new SelectItem();
        classesPickList.setShowTitle(false);
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(450);

        IButton saveButton = new IButton("Save");
        saveButton.setIcon(CoreConstants.ICON_SAVE);
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (nameField.validate() & lfnField.validate() & classesPickList.validate()) {

                    List<String> values = new ArrayList<String>();
                    values.addAll(Arrays.asList(classesPickList.getValues()));

                    save(new Application(nameField.getValueAsString().trim(),
                            lfnField.getValueAsString().trim(), values));
                }
            }
        });

        removeButton = new IButton("Remove", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                remove(nameField.getValueAsString().trim());
            }
        });
        removeButton.setIcon(CoreConstants.ICON_DELETE);
        removeButton.setDisabled(true);

        addField("Name", nameField);
        addField("LFN", lfnField);
        addField("Classes", classesPickList);
        addButtons(saveButton, removeButton);
    }

    /**
     * Sets an application to edit or creates a blank form.
     *
     * @param name Class name
     * @param lfn Application LFN
     * @param groups Class groups
     */
    public void setApplication(String name, String lfn, String classes) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.lfnField.setValue(lfn);
            this.classesPickList.setValues(classes.split(", "));
            this.newApplication = false;
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.lfnField.setValue("");
            this.classesPickList.setValues(new String[]{});
            this.newApplication = true;
            this.removeButton.setDisabled(true);
        }
    }

    private void save(Application app) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newApplication) {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to add application:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    modal.hide();
                    ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                            getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                    appsTab.loadApplications();
                    setApplication(null, null, null);
                }
            };
            modal.show("Adding application '" + app.getName() + "'...", true);
            service.add(app, callback);

        } else {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to update application:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    modal.hide();
                    ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                            getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                    appsTab.loadApplications();
                    setApplication(null, null, null);
                }
            };
            modal.show("Updating application '" + app.getName() + "'...", true);
            service.update(app, callback);
        }
    }

    /**
     * Loads classes list
     */
    private void loadClasses() {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get list of classes:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<AppClass> result) {
                modal.hide();
                List<String> dataList = new ArrayList<String>();
                for (AppClass c : result) {
                    dataList.add(c.getName());
                }
                classesPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        modal.show("Loading classes...", true);
        service.getClasses(callback);
    }

    /**
     * Removes an application.
     *
     * @param name Application name
     */
    private void remove(String name) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to remove application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                SC.say("The application was successfully removed!");
                ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                appsTab.loadApplications();
            }
        };
        modal.show("Removing application '" + name + "'...", true);
        service.remove(name, callback);
    }
}
