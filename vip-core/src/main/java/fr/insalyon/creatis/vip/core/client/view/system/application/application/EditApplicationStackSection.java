/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.core.client.view.system.application.application;

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
import fr.insalyon.creatis.vip.core.client.bean.AppClass;
import fr.insalyon.creatis.vip.core.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class EditApplicationStackSection extends SectionStackSection {

    private String applicationClass;
    private boolean newApplication = true;
    private DynamicForm form;
    private TextItem nameItem;
    private TextItem lfnItem;
    private SelectItem classesPickList;

    public EditApplicationStackSection(String applicationClass) {

        this.applicationClass = applicationClass;
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

        this.addItem(vLayout);
        loadData();
    }

    private void configureForm() {
        form = new DynamicForm();
        form.setWidth(500);

        nameItem = new TextItem("name", "Name");
        nameItem.setWidth(350);
        nameItem.setRequired(true);

        lfnItem = new TextItem("lfn", "LFN");
        lfnItem.setWidth(350);
        lfnItem.setRequired(true);

        classesPickList = new SelectItem();
        classesPickList.setTitle("Classes");
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(350);

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
            if (applicationClass == null) {
                this.classesPickList.setValues(new String[]{});
            } else {
                this.classesPickList.setValues(new String[]{applicationClass});
            }
            this.newApplication = true;
        }
    }

    private void save(Application app) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newApplication) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing add application\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                                getTab("manage-apps-tab");
                        appsTab.loadApplications();
                        setApplication(null, null, null);
                    }
                    SC.say(result);
                }
            };
            service.add(app, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing update application\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                                getTab("manage-apps-tab");
                        appsTab.loadApplications();
                        setApplication(null, null, null);
                    }
                    SC.say(result);
                }
            };
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
                SC.warn("Error executing get groups list\n" + caught.getMessage());
            }

            public void onSuccess(List<AppClass> result) {

                List<String> dataList = new ArrayList<String>();
                for (AppClass c : result) {
                    dataList.add(c.getName());
                }
                classesPickList.setValueMap(dataList.toArray(new String[]{}));
                if (applicationClass != null) {
                    classesPickList.setValues(new String[]{applicationClass});
                    classesPickList.setDisabled(true);
                } else {
                    classesPickList.setDisabled(false);
                }
            }
        };
        service.getClasses(callback);
    }
}
