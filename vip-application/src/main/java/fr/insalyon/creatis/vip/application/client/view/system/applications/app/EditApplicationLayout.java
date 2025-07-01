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
package fr.insalyon.creatis.vip.application.client.view.system.applications.app;

import java.util.Arrays;
import java.util.HashMap;

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
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.system.SystemUtils;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditApplicationLayout extends AbstractFormLayout {

    private boolean newApplication = true;
    private TextItem nameField;
    private RichTextEditor richTextEditor;
    private SelectItem groupsList;
    private IButton saveButton;
    private IButton removeButton;
    private SelectItem usersPickList;

    private Map<String, String> groupsMap;

    public EditApplicationLayout() {
        super(480, 200);
        addTitle("Add/Edit Application", ApplicationConstants.ICON_APPLICATION);

        configure();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(450, null);

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

        groupsList = new SelectItem();
        groupsList.setShowTitle(false);
        groupsList.setMultiple(true);
        groupsList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsList.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (nameField.validate()) {
                    List<String> groupsNames = Arrays.asList(groupsList.getValues());
                    List<Group> groups = groupsNames.stream()
                        .map((name) -> new Group(groupsMap.get(name), false, GroupType.RESOURCE)).collect(Collectors.toList());

                    if (newApplication) {
                        save(new Application(
                            nameField.getValueAsString().trim(),
                            richTextEditor.getValue(),
                            groups));
                    } else {
                        save(new Application(
                            nameField.getValueAsString().trim(),
                            usersPickList.getValueAsString(), 
                            null,
                            richTextEditor.getValue(),
                            groups));
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
        addField("Owner", usersPickList);
        addField("Groups", groupsList);
        addMember(WidgetUtil.getLabel("<b>Citation</b>", 15));
        addMember(richTextEditor);
        addMember(removeButton);

        if (CoreModule.user.isDeveloper()){
            addButtons(saveButton);
        } else {
            addButtons(saveButton, removeButton);
        }
    }

    public void setApplication(String name, String owner, String citation, Map<String, String> groups) {
        if (name != null) {
            usersPickList.setCanEdit(true);
            fetchUsers(owner);
            nameField.setValue(name);
            nameField.setDisabled(true);
            groupsList.setValues(groups.keySet().stream().toArray(String[]::new));
            richTextEditor.setValue(citation);
            newApplication = false;
            removeButton.setDisabled(false);
            groupsMap = groups;
        } else {
            usersPickList.setCanEdit(false);
            usersPickList.setValue("");
            nameField.setValue("");
            nameField.setDisabled(false);
            richTextEditor.setValue("");
            newApplication = true;
            removeButton.setDisabled(true);
            groupsMap = new HashMap<>();
        }
        fetchGroups();
    }

    private void save(Application app) {
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newApplication) {
            ApplicationService.Util.getInstance().add(app, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().update(app, getCallback("update"));
        }
    }

    private void remove(String name) {
        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().remove(name, getCallback("remove"));
    }

    private AsyncCallback<String> getCallback(final String text) {

        return new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);

                setApplication(null, null, null, null);
                ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                tab.loadApplications();
                
                if (result != null) {
                    Layout.getInstance().setInformationMessage(result);
                }
            }
        };
    }


    private void fetchUsers(final String currentOwner) {
        if ( ! CoreModule.user.isSystemAdministrator()) {
            usersPickList.setValues(currentOwner);
            return;
        }

        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load users:<br />" + caught.getMessage());
                usersPickList.setValues(currentOwner);
            }

            @Override
            public void onSuccess(List<User> result) {
                LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();

                for (User user : result) {
                    valueMap.put(user.getEmail(), user.getFirstName() + " " + user.getLastName());
                }
                usersPickList.setValueMap(valueMap);
                usersPickList.setValue(currentOwner);
            }
        };
        ConfigurationService.Util.getInstance().getUsers(callback);
    }
    
    private void fetchGroups() {
                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load groups:<br />" + caught.getMessage());
            }
    
            @Override
            public void onSuccess(List<Group> result) {
                List<Group> data = result.stream()
                    .filter((g) -> g.getType() == GroupType.APPLICATION)
                    .collect(Collectors.toList());
                List<String> formatGroups = SystemUtils.formatGroups(data);

                groupsMap.putAll(IntStream.range(0, Math.min(formatGroups.size(), data.size()))
                    .boxed()
                    .collect(Collectors.toMap(formatGroups::get, (i) -> data.get(i).getName())));
                groupsList.setValueMap(formatGroups.stream().toArray(String[]::new));
            }
        };
        service.getGroups(callback);
    }
}
