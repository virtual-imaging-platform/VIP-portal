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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class EditApplicationStackSection extends SectionStackSection {

    private ModalWindow modal;
    private boolean newApplication = true;
    private DynamicForm form;
    private TextItem nameItem;
    private TextItem lfnItem;
    private SelectItem classesPickList;

    public EditApplicationStackSection() {

        this.setTitle("Add Application");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureForm();

        VLayout vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        vLayout.addMember(form);

        modal = new ModalWindow(vLayout);

        this.addItem(vLayout);
        loadData();
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setWidth(500);

        nameItem = FieldUtil.getTextItem(450, true, "Name", null);
        lfnItem = FieldUtil.getTextItem(450, true, "LFN", null);

        classesPickList = new SelectItem();
        classesPickList.setTitle("Classes");
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(450);

        ButtonItem saveItem = new ButtonItem("Save");
        saveItem.setWidth(50);
        saveItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    List<String> values = new ArrayList<String>();
                    values.addAll(Arrays.asList(classesPickList.getValues()));

                    save(new Application(nameItem.getValueAsString(),
                            lfnItem.getValueAsString(), values));
                }
            }
        });

        form.setFields(nameItem, lfnItem, classesPickList, saveItem);
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
            this.setTitle("Editing Application: " + name);
            this.nameItem.setValue(name);
            this.nameItem.setDisabled(true);
            this.lfnItem.setValue(lfn);
            this.classesPickList.setValues(classes.split(", "));
            this.newApplication = false;
        } else {
            this.setTitle("Add Application");
            this.nameItem.setValue("");
            this.nameItem.setDisabled(false);
            this.lfnItem.setValue("");
            this.classesPickList.setValues(new String[]{});
            this.newApplication = true;
        }
    }

    private void save(Application app) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newApplication) {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to add application:<br />" + caught.getMessage());
                }

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

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to update application:<br />" + caught.getMessage());
                }

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
    private void loadData() {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get list of groups:<br />" + caught.getMessage());
            }

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
}
