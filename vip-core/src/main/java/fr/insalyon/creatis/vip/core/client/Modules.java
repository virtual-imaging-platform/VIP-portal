package fr.insalyon.creatis.vip.core.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Modules {

    private static Modules instance;
    private List<Module> modules;

    public static Modules getInstance() {
        if (instance == null) {
            instance = new Modules();
        }
        return instance;
    }

    private Modules() {

        modules = new ArrayList<Module>();
    }

    public void add(Module module) {

        modules.add(module);
    }
    
    public void initializeModules(User user) {
        CoreModule.user = user;      
        final AsyncCallback<List<Boolean>> callback = new AsyncCallback<List<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get group properties:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Boolean> result) {
               
                for (Module module : modules) {
                    // if ((!module.requiresGridFile() || isGridFile) && (!module.requiresGridJob() || isGridJobs)) {
                        module.load();
                    // }
                }
                for (Module module : modules) {
                    // if ((!module.requiresGridFile() || isGridFile) && (!module.requiresGridJob() || isGridJobs)) {
                        module.postLoading();
                    // }
                }
            }
        };
        ConfigurationService.Util.getInstance().getUserPropertiesGroups(callback);
    }

    public void finalizeModules(Set<Tab> removedTabs) {
        for (Module module : modules) {
            module.terminate(removedTabs);
        }
    }
    
    public void userRemoved(User user) {
        for (Module module : modules) {
            module.userRemoved(user);
        }
    }
    
    public void userUpdated(User oldUser, User updatedUser) {
        for (Module module : modules) {
            module.userUpdated(oldUser, updatedUser);
        }
    }
} 
