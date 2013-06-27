/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;

/**
 *
 * @author glatard
 */
public class ManageSSHTab extends AbstractManageTab {

    private SSHLayout sshLayout;
    private EditSSHLayout editLayout;
    private Label keyLabel;
    private VLayout keyLayout1;
    
    public ManageSSHTab() {

        super(DataManagerConstants.ICON_SSH, DataManagerConstants.APP_SSH, DataManagerConstants.TAB_MANAGE_SSH);
               vLayout.setWidth100();
        sshLayout = new SSHLayout();
        editLayout = new EditSSHLayout();
       
        
       
       keyLayout1 = new VLayout(5); 
       keyLayout1.setSize("480", "*");
        

        loadKey();
        
       
        
        HLayout sshLayout = new HLayout(5);
        sshLayout.setHeight("50%");
        sshLayout.setWidth100();
        sshLayout.addMember(this.sshLayout);
        
       
        sshLayout.addMember(editLayout);
        vLayout.addMember(sshLayout);
        
        
    }
    
    private void loadKey(){
        DataManagerService.Util.getInstance().getSSHPublicKey(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot get VIP SSH public key:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                
                editLayout.addMember(WidgetUtil.getLabel("<b>VIP's public ssh key (add it to user@host)", 50));
                Label l = WidgetUtil.getLabel(result, 20);
                l.setCanSelectText(true);
                l.setWidth(480);
                l.setOverflow(Overflow.CLIP_H);
                editLayout.addMember(l);
//                VLayout keyLayout = new VLayout(5);
//                keyLayout.addMember(WidgetUtil.getLabel("<b>VIP's public ssh key (add it to user@host)", 20));
//                
//                keyLabel = new Label(result);
//                keyLabel.setWidth100();
//                keyLabel.setHeight100();
//                keyLabel.setAutoHeight();
//                keyLabel.setCanSelectText(true);
//                keyLabel.setPadding(5);
//                keyLabel.setBackgroundColor("#FFFFFF");
//                keyLabel.setBorder("1px solid #CCCCCC");
//                
//                keyLayout.addMember(keyLabel);
//                
//                keyLayout1.addMember(keyLayout);
            }
        });
    }
    
    public void loadSSHConnections() {
        sshLayout.loadData();
    }
    
    public void setSSH(String name, String email,String user,String host,String port,String directory,String status) {
        editLayout.setSSH(email,name,user,host,port,directory,status);
    }
    
}
