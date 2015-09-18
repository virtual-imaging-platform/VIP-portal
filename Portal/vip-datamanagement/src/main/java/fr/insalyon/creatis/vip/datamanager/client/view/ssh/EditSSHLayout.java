/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap; 
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;  
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;  
import java.util.Locale;
/**
 *
 * @author glatard
 */
public class EditSSHLayout extends AbstractFormLayout {
    
    
    private boolean newSSH = true;
    private TextItem emailField;
    private TextItem nameField;
    private TextItem userField;
    private TextItem hostField;
    private TextItem portField;
    private TextItem directoryField;
    private TextItem statusField;
    private SelectItem transfertType;
    private IButton saveButton;
    private IButton removeButton;
    public EditSSHLayout() {

        super(480, 200);
        addTitle("Add/Edit SSH Connection", DataManagerConstants.ICON_SSH);

        configure();
    }

    private void configure() {
        emailField = FieldUtil.getTextItem(450, null);
        nameField = FieldUtil.getTextItem(450, null);
        userField = FieldUtil.getTextItem(450, null);
        hostField = FieldUtil.getTextItem(450, null);
        portField = FieldUtil.getTextItem(450, null);
        directoryField = FieldUtil.getTextItem(450, null);
        statusField = FieldUtil.getTextItem(450, null);
        transfertType = new SelectItem();
        transfertType.setShowTitle(false);
        transfertType.setWidth(450);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
        valueMap.put(TransfertType.Synchronization.toString(), TransfertType.Synchronization.toString());  
        valueMap.put(TransfertType.DeviceToLFC.toString(),TransfertType.DeviceToLFC.toString());  
        valueMap.put(TransfertType.LFCToDevice.toString(), TransfertType.LFCToDevice.toString());  
        
        transfertType.setValueMap(valueMap);
        
        
        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (emailField.validate() & nameField.validate() & userField.validate() & hostField.validate() & portField.validate() & directoryField.validate()) {

                            List<String> values = new ArrayList<String>();
                            
                            save(new SSH(emailField.getValueAsString().trim(),
                                    nameField.getValueAsString().trim(),
                                    userField.getValueAsString().trim(),
                                    hostField.getValueAsString().trim(),
                                    portField.getValueAsString().trim(),
                                    transfertType.getValueAsString(),
                                    directoryField.getValueAsString().trim(),
                                    statusField.getValueAsString()
                                    ));
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        SC.ask("Do you really want to remove this SSH connection?", new BooleanCallback() {
                            @Override
                            public void execute(Boolean value) {
                                if (value) {
                                    remove(nameField.getValueAsString().trim(),emailField.getValueAsString().trim());
                                }
                            }
                        });
                    }
                });
        removeButton.setDisabled(true);

        addField("VIP User", emailField);
        addField("Connection Name", nameField);
        addField("SSH User", userField);
        addField("SSH Host",hostField);
        addField("SSH Port",portField);
        addField("Transfert Type", transfertType);
        addField("SSH Directory (absolute path)",directoryField);
        //addField("Connection Status",statusField);
               
        addButtons(saveButton, removeButton);
    }

    public void setSSH(String email, String name, String user, String host, String port,String transferType, String directory,String status) {
        
        
        if (name != null & email != null & user != null & host != null  & transferType!=null & directory != null & status != null & port != null) {
            this.emailField.setValue(email);
            this.emailField.setDisabled(true);
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.userField.setValue(user);
            this.hostField.setValue(host);
            this.portField.setValue(port);
            this.transfertType.setValue(transferType);
            this.directoryField.setValue(directory);
            this.statusField.setValue(status);
            this.newSSH = false;
            this.removeButton.setDisabled(false);

        } else {
            this.emailField.setValue(CoreModule.user.getEmail());
            if(!CoreModule.user.isSystemAdministrator())
                this.emailField.setDisabled(true);
            else
                this.emailField.setDisabled(false);
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.userField.setValue("");
            this.hostField.setValue("");
            this.portField.setValue("22");
            this.transfertType.setValue("");
            this.directoryField.setValue("");
            this.statusField.setValue("");
            this.newSSH = true;
            this.removeButton.setDisabled(true);
        }
        
    }

  
    private void save(SSH ssh) {

        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newSSH) {
            DataManagerService.Util.getInstance().addSSH(ssh, getCallback("add"));
        } else {
            DataManagerService.Util.getInstance().updateSSH(ssh, getCallback("update"));
        }
    }

    private void remove(String name, String email) {

        WidgetUtil.setLoadingIButton(removeButton, "Removing...");
        DataManagerService.Util.getInstance().removeSSH(email, name, getCallback("remove"));
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " SSH connection:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                setSSH(null, null, null, null, null, null, null, null);
                ManageSSHTab tab = (ManageSSHTab) Layout.getInstance().
                        getTab(DataManagerConstants.TAB_MANAGE_SSH);
                tab.loadSSHConnections();
               
            }
        };
    }

}
