package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.core.models.User;

import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class UserRecord extends ListGridRecord {

    public UserRecord() {
    }

    public UserRecord(User user) {

        setAttribute("username", user.getFirstName() + " " + user.getLastName().toUpperCase());
        setAttribute("email", user.getEmail());
        setAttribute("institution", user.getInstitution());
        setAttribute("confirmed", user.isConfirmed());
        setAttribute("folder", user.getFolder());
        setAttribute("registration", user.getRegistration());
        setAttribute("lastLogin", user.getLastLogin());
        setAttribute("level", user.getLevel().name());
        setAttribute("maxRunningSimulations", user.getMaxRunningSimulations());
        setAttribute("countryCode", user.getCountryCode().name());
        setAttribute("countryCodeIcon", "core/flags/" + user.getCountryCode().name());
        setAttribute("countryName", user.getCountryCode().getCountryName());
        setAttribute("accountLocked",user.isAccountLocked());
    }
    
    public Date getDate() {
        
        return getAttributeAsDate("lastLogin");
    }
}
