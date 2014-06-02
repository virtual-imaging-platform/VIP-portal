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
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditApplicationLayout extends AbstractFormLayout {
    
    private boolean newApplication = true;
    private TextItem nameField;
    private SelectItem classesPickList;
    private RichTextEditor richTextEditor;
    private IButton saveButton;
    private IButton removeButton;
    private SelectItem usersPickList;
    
    public EditApplicationLayout() {
        
        super(480, 200);
        addTitle("Add/Edit Application", ApplicationConstants.ICON_APPLICATION);
        
        configure();
        loadClasses();
    }
    
    private void configure() {
        
        nameField = FieldUtil.getTextItem(450, null);
        
        classesPickList = new SelectItem();
        classesPickList.setShowTitle(false);
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(450);
        
        usersPickList = new SelectItem();
        usersPickList.setShowTitle(false);
        usersPickList.setWidth(450);
        usersPickList.setRequired(true);
        
        richTextEditor = new RichTextEditor();
        richTextEditor.setHeight(200);
        richTextEditor.setOverflow(Overflow.HIDDEN);
        richTextEditor.setShowEdges(true);
        richTextEditor.setControlGroups("styleControls", "editControls",
                "colorControls", "insertControls");
        
        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (nameField.validate() & classesPickList.validate()) {
                    
                    List<String> values = new ArrayList<String>();
                    values.addAll(Arrays.asList(classesPickList.getValues()));
                    
                    if (newApplication) {
                        save(new Application(nameField.getValueAsString().trim(),
                                values, richTextEditor.getValue()));
                    } else {
                        save(new Application(nameField.getValueAsString().trim(),
                                values, usersPickList.getValueAsString(), richTextEditor.getValue()));
                        
                    }
                }
            }
        });
        
        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to remove this application?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            remove(nameField.getValueAsString().trim());
                        }
                    }
                });
            }
        });
        removeButton.setDisabled(true);
        
        addField("Name", nameField);
        if (CoreModule.user.isSystemAdministrator()) {
            this.addMember(WidgetUtil.getLabel("<b>Owner</b>", 15));
            this.addMember(FieldUtil.getForm(usersPickList));
        }
        addField("Classes", classesPickList);
        this.addMember(WidgetUtil.getLabel("<b>Citation</b>", 15));
        this.addMember(richTextEditor);
        addButtons(saveButton, removeButton);
    }

    /**
     * Sets an application to edit or creates a blank form.
     *
     * @param name Application name
     * @param classes Application classes
     * @param citation Application citation
     */
    public void setApplication(String name, String owner, String classes, String citation) {
        
        if (name != null) {
            usersPickList.setCanEdit(true);
            loadUsers(owner);
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.classesPickList.setValues(classes.split(", "));
            this.richTextEditor.setValue(citation);
            this.newApplication = false;
            this.removeButton.setDisabled(false);
            
        } else {
            usersPickList.setCanEdit(false);
            usersPickList.setValue("");
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.classesPickList.setValues(new String[]{});
            this.richTextEditor.setValue("");
            this.newApplication = true;
            
            this.removeButton.setDisabled(true);
        }
    }

    /**
     *
     * @param app
     */
    private void save(Application app) {
        
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");
        
        if (newApplication) {
            ApplicationService.Util.getInstance().add(app, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().update(app, getCallback("update"));
        }
    }

    /**
     * Removes an application.
     *
     * @param name Application name
     */
    private void remove(String name) {
        
        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().remove(name, getCallback("remove"));
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
                setApplication(null, null, null, null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadApplications();
            }
        };
    }

    /**
     * Loads classes list
     */
    private void loadClasses() {
        
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of classes:<br />" + caught.getMessage());
            }
            
            @Override
            public void onSuccess(List<AppClass> result) {
                List<String> dataList = new ArrayList<String>();
                for (AppClass c : result) {
                    dataList.add(c.getName());
                }
                classesPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        ApplicationService.Util.getInstance().getClasses(callback);
    }
    
    private void loadUsers(final String currentUser) {
        
        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load users:<br />" + caught.getMessage());
                usersPickList.setValues(currentUser);
                
            }
            
            @Override
            public void onSuccess(List<User> result) {
                
                LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
                
                for (User user : result) {
                    
                    
                    valueMap.put(user.getEmail(), user.getFirstName() + " " + user.getLastName());
                    
                }
                usersPickList.setValueMap(valueMap);
                usersPickList.setValue(currentUser);
                
            }
        };
        ConfigurationService.Util.getInstance().getUsers(callback);
    }
}
