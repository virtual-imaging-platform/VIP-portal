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
package fr.insalyon.creatis.vip.application.client.view.system.engines;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditEngineLayout extends AbstractFormLayout {

    private boolean newEngine = true;
    private TextItem nameField;
    private TextItem endpointField;
    private IButton saveButton;
    private IButton removeButton;
    private SelectItem statusPickList;
    private String[] statusList = {"enabled", "disabled"};

    public EditEngineLayout() {

        super(480, 200);
        addTitle("Add/Edit Engine", ApplicationConstants.ICON_ENGINE);

        configure();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(450, null);
        endpointField = FieldUtil.getTextItem(450, null);
        statusPickList = new SelectItem();
        statusPickList.setShowTitle(false);
        statusPickList.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameField.validate() & endpointField.validate()) {

                            save(new Engine(nameField.getValueAsString().trim(),
                                    endpointField.getValueAsString().trim(), 
                                    statusPickList.getValueAsString()));
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SC.ask("Do you really want to remove this engine?", new BooleanCallback() {
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
        addField("End-Point", endpointField);
        addField("Status", statusPickList);
        addButtons(saveButton, removeButton);
        
        statusPickList.setValueMap(statusList);
    }

    /**
     * Sets an engine to edit or creates a blank form.
     * 
     * @param name Engine name
     * @param endpoint Engine endpoint
     */
    public void setEngine(String name, String endpoint, String status) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.endpointField.setValue(endpoint);
            this.statusPickList.setValue(status);
            this.newEngine = false;
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.endpointField.setValue("");
            this.statusPickList.setValue("");
            this.newEngine = true;
            this.removeButton.setDisabled(true);
        }
    }

    /**
     *  Adds or updates an engine.
     * 
     * @param engine
     */
    private void save(Engine engine) {

        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newEngine) {
            ApplicationService.Util.getInstance().addEngine(engine, getCallback("add"));
        } else {
            ApplicationService.Util.getInstance().updateEngine(engine, getCallback("update"));
        }
    }

    /**
     * Removes an engine.
     *
     * @param name Engine name
     */
    private void remove(String name) {

        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        ApplicationService.Util.getInstance().removeEngine(name, getCallback("remove"));
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
                Layout.getInstance().setWarningMessage("Unable to " + text + " engine:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setEngine(null, null, null);
                ManageEnginesTab tab = (ManageEnginesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_ENGINE);
                tab.loadEngines();
            }
        };
    }
}
