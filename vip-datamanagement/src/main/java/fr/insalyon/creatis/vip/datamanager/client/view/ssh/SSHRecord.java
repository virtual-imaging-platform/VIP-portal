/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author glatard
 */
public class SSHRecord extends ListGridRecord {

    public SSHRecord(String name, String email, String user, String host, String port, String directory, String status) {

        setAttribute("name", name);
        setAttribute("email", email);
        setAttribute("user", user);
        setAttribute("host",host);
        setAttribute("port",port);
        setAttribute("directory",directory);
        setAttribute("status",status);
    }
    
}
