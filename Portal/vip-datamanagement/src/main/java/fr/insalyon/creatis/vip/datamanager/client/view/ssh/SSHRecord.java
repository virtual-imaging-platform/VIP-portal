/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import fr.insalyon.creatis.vip.datamanager.client.bean.TransfertType;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author glatard, Nouha Boujelben
 */
public class SSHRecord extends ListGridRecord {

    public SSHRecord(String name, String email, String user, String host, int port, TransfertType transfertType, String directory, String status, long numberSynchronizationFailed, boolean deleteFilesFromSource) {

        setAttribute("name", name);
        setAttribute("email", email);
        setAttribute("user", user);
        setAttribute("host", host);
        setAttribute("port", port);
        setAttribute("transfertType", transfertType);
        setAttribute("directory", directory);
        setAttribute("status", status);
        setAttribute("numberSynchronizationFailed", numberSynchronizationFailed);
        setAttribute("deleteFilesFromSource", deleteFilesFromSource);
    }

}
