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
package fr.insalyon.creatis.vip.core.client.view.system.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditAccountLayout extends AbstractFormLayout {

    private boolean newAccount;
    private String oldName;
    private TextItem nameItem;
    private SelectItem groupsPickList;
    private IButton removeButton;

    public EditAccountLayout() {

        super(380, 200);
        addTitle("Add/Edit Account Type", CoreConstants.ICON_ACCOUNT);

        configure();
        loadData();
    }

    private void configure() {

        this.newAccount = true;
        this.oldName = null;

        nameItem = FieldUtil.getTextItem(350, null);

        groupsPickList = new SelectItem();
        groupsPickList.setShowTitle(false);
        groupsPickList.setMultiple(true);
        groupsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsPickList.setWidth(350);

        IButton saveButton = new IButton("Save", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (nameItem.validate()) {
                    save(nameItem.getValueAsString().trim(),
                            Arrays.asList(groupsPickList.getValues()));
                }
            }
        });
        saveButton.setIcon(CoreConstants.ICON_SAVE);

        removeButton = new IButton("Remove", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (nameItem.validate()) {
                    remove(nameItem.getValueAsString().trim());
                }
            }
        });
        removeButton.setIcon(CoreConstants.ICON_DELETE);
        removeButton.setDisabled(true);

        addField("Name", nameItem);
        addField("Groups", groupsPickList);
        addButtons(saveButton, removeButton);
    }

    /**
     * Sets an account to edit.
     *
     * @param name Account name
     * @param groups List of groups
     */
    public void setAccount(String name, List<Group> groups) {

        if (name != null) {
            this.newAccount = false;
            this.oldName = name;
            this.nameItem.setValue(name);
            String[] groupsArray = new String[groups.size()];
            for (int i = 0; i < groups.size(); i++) {
                groupsArray[i] = groups.get(i).getName();
            }
            this.groupsPickList.setValues(groupsArray);
            this.removeButton.setDisabled(false);

        } else {
            this.newAccount = true;
            this.oldName = null;
            this.nameItem.setValue("");
            this.groupsPickList.setValues(new String[]{});
            this.removeButton.setDisabled(true);
        }
    }

    /**
     *
     * @param email User's email
     * @param level User's level
     * @param groups List of groups
     */
    private void save(String newName, List<String> groups) {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        if (newAccount) {
            modal.show("Adding " + newName + "...", true);
            service.addAccount(newName, groups, getCallback("add"));

        } else {
            modal.show("Updating " + newName + "...", true);
            service.updateAccount(oldName, newName, groups, getCallback("update"));
        }
    }

    /**
     *
     * @param name
     */
    private void remove(final String name) {

        SC.ask("Do you really want to remove \"" + name + "\" account type?", new BooleanCallback() {

            @Override
            public void execute(Boolean value) {
                if (value) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    modal.show("Removing " + name + "...", true);
                    service.removeAccount(name, getCallback("remove"));
                }
            }
        });
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to " + text + " account type:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                setAccount(null, null);
                ((ManageAccountsTab) Layout.getInstance().getTab(
                        CoreConstants.TAB_MANAGE_ACCOUNTS)).loadAccounts();
            }
        };
    }

    /**
     * Loads list of groups.
     */
    private void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<List<Group>>() {

            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {

                List<String> dataList = new ArrayList<String>();
                for (Group group : result) {
                    dataList.add(group.getName());
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        service.getGroups(callback);
    }
}
