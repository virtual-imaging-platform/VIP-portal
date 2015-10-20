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
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.TransferType;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SSHLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;
    
       public SSHLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);

        loadData();
    }
    
       private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        LabelButton addButton = new LabelButton("Add SSH Connection", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageSSHTab sshTab = (ManageSSHTab) Layout.getInstance().
                        getTab(DataManagerConstants.TAB_MANAGE_SSH);
                sshTab.setSSH(null, null, null,null,null,null,null,null,false);
            }
        });
        toolstrip.addMember(addButton);

        LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
        refreshButton.setWidth(150);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolstrip.addMember(refreshButton);

        this.addMember(toolstrip);
    }
       
       
    private void configureGrid() {

        grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("name"),
                                    rollOverRecord.getAttribute("email"),
                                    rollOverRecord.getAttribute("user"),
                                    rollOverRecord.getAttribute("host"),
                                    rollOverRecord.getAttribute("port"),
                                    rollOverRecord.getAttribute("transferType"),
                                    rollOverRecord.getAttribute("directory"),
                                    rollOverRecord.getAttribute("status"),
                                    rollOverRecord.getAttributeAsBoolean("deleteFilesFromSource")
                                    );
                        }
                    });
                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                             final String email = rollOverRecord.getAttribute("email");
                            SC.ask("Do you really want to remove the SSH connection \""
                                    + name + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        remove(name,email);
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;
            }

            private ImgButton getImgButton(String imgSrc, String prompt) {
                ImgButton button = new ImgButton();
                button.setShowDown(false);
                button.setShowRollOver(false);
                button.setLayoutAlign(Alignment.CENTER);
                button.setSrc(imgSrc);
                button.setPrompt(prompt);
                button.setHeight(16);
                button.setWidth(16);
                return button;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        
        
        ListGridField deleteFilesFromSourceField = new ListGridField("deleteFilesFromSource", "Delete Files From Source");
        deleteFilesFromSourceField.setType(ListGridFieldType.BOOLEAN);
        grid.setFields(new ListGridField("name", "Connection Name"),
                new ListGridField("email", "VIP User"),
                new ListGridField("user","SSH user"),
                new ListGridField("host","SSH host name"),
                new ListGridField("port","SSH port"),
                new ListGridField("transferType","Transfer Type"),
                new ListGridField("directory","SSH directory"),
                new ListGridField("status","Connection Status"),
                new ListGridField("theEarliestNextSynchronistation","The Earliest Next Synchronistation"),
                new ListGridField("numberSynchronizationFailed","Number Synchronization Failed"),
        deleteFilesFromSourceField);
              
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {

                edit(event.getRecord().getAttribute("name"),
                        event.getRecord().getAttribute("email"),
                        event.getRecord().getAttribute("user"),
                        event.getRecord().getAttribute("host"),
                        event.getRecord().getAttribute("port"),
                        event.getRecord().getAttribute("transferType"),
                        event.getRecord().getAttribute("directory"),
                        event.getRecord().getAttribute("status"),
                        //event.getRecord().getAttribute("numberSynchronizationFailed"),
                        event.getRecord().getAttributeAsBoolean("deleteFilesFromSource"));
            }
        });
        this.addMember(grid);
    }
    
     private void remove(String name, String email) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove SSH connection:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The SSH connection was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing ssh connection '" + name + "'...", true);
        DataManagerService.Util.getInstance().removeSSH(email, name, callback);
    }

    private void edit(String name, String email, String user, String host, String port,String transferType, String directory, String status, boolean deleteFilesFromSource) {

        ManageSSHTab sshTab = (ManageSSHTab) Layout.getInstance().
                getTab(DataManagerConstants.TAB_MANAGE_SSH);
        
        
        sshTab.setSSH(name, email,user,host,port,TransferType.valueOf(transferType),directory,status,deleteFilesFromSource);
    }   
    
     public void loadData() {

        final AsyncCallback<List<SSH>> callback = new AsyncCallback<List<SSH>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of SSH connections:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<SSH> result) {
                modal.hide();
                List<SSHRecord> dataList = new ArrayList<SSHRecord>();

                for (SSH ssh : result) {
                    dataList.add(new SSHRecord(ssh.getName(),ssh.getEmail(),ssh.getUser(),ssh.getHost(),ssh.getPort(),ssh.getTransferType(),ssh.getDirectory(),ssh.getStatus(),String.valueOf(ssh.getTheEarliestNextSynchronistation()).split("\\.")[0],ssh.getNumberSynchronizationFailed(),ssh.isDeleteFilesFromSource()));
                }
                grid.setData(dataList.toArray(new SSHRecord[]{}));
            }
        };
        modal.show("Loading ssh connections...", true);
        DataManagerService.Util.getInstance().getSSHConnections(callback);
    }
}
