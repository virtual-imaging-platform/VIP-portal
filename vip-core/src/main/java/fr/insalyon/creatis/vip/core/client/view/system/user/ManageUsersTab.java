package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageUsersTab extends AbstractManageTab {

    private final UsersLayout usersLayout;
    private final EditUserLayout editLayout;

    public ManageUsersTab() {

        super(CoreConstants.ICON_USER, CoreConstants.APP_USER, CoreConstants.TAB_MANAGE_USERS);
       
        usersLayout = new UsersLayout();
        editLayout = new EditUserLayout();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(usersLayout);
        hLayout.addMember(editLayout);
        
        vLayout.addMember(hLayout);
    }

    /**
     * 
     */
    
    public void setFilter() {
        
        usersLayout.setFilter();
    }
    
    public void loadUsers() {
        
        usersLayout.loadData();
    }

    /**
     * 
     * @param name
     * @param email
     * @param confirmed
     * @param level
     * @param countryCode 
     * @param maxRunningSimulations
     */
    public void setUser(String name, String email, boolean confirmed,
            String level, String countryCode, int maxRunningSimulations, boolean locked) {

        editLayout.setUser(name, email, confirmed, level, countryCode, maxRunningSimulations, locked);
    }
}
