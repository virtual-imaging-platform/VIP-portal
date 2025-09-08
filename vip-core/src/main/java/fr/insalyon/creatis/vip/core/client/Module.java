package fr.insalyon.creatis.vip.core.client;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.User;

import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class Module {

    public abstract void load();

    public abstract void postLoading();

    public abstract void terminate(Set<Tab> removedTabs);

    /**
     * This method is invoked when a user is removed from the platform. Its 
     * default implementation does nothing. It should be overwritten by a module
     * if some action should be performed when a user is removed.
     */
    public void userRemoved(User user) {
    }

    /**
     * This method is invoked when a user update her personal information on the 
     * platform. Its default implementation does nothing. It should be 
     * overwritten by a module if some action should be performed when a user 
     * updates her information.
     */
    public void userUpdated(User oldUser, User updatedUser) {
    }
}
