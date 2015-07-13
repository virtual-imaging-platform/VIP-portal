/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

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
            boolean isGridFile;
            boolean isGridJobs;

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get group properties:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Boolean> result) {
               
                    isGridFile =result.get(1);
                    isGridJobs = result.get(2);
                
                for (Module module : modules) {
                    if ((!module.requiresGridFile() || isGridFile) && (!module.requiresGridJob() || isGridJobs)) {
                        module.load();
                    }
                }
                for (Module module : modules) {
                    if ((!module.requiresGridFile() || isGridFile) && (!module.requiresGridJob() || isGridJobs)) {
                        module.postLoading();
                    }
                }
            }
        };
        ConfigurationService.Util.getInstance().getUserPropertiesGroups(callback);        
    }
    
    public void finalizeModules() {
        
        for (Module module : modules) {
            module.terminate();
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
