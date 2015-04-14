/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 *
 * @author Nouha Boujelben
 */
public class DataUsersLayout extends DataSource {

    public DataUsersLayout() {
        
        DataSourceBooleanField confirmedField = new DataSourceBooleanField("confirmed", "Confirmed");
        DataSourceBooleanField accountLocked = new DataSourceBooleanField("accountLocked", "Locked");
        DataSourceTextField email = new DataSourceTextField("email", "Email");
        email.setPrimaryKey(true);
        DataSourceTextField name = new DataSourceTextField("username", "Name");
        DataSourceTextField lastLogin = new DataSourceTextField("lastLogin", "Last Login");
        setFields(confirmedField, email, name, lastLogin, accountLocked);

        setClientOnly(true);
        


    }
}
