/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
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
        
        for (Module module : modules) {
            module.load();            
        }
        for (Module module : modules) {
            module.postLoading();
        }        
    }
    
    public void finalizeModules() {
        
        for (Module module : modules) {
            module.terminate();
        }
    }
    
    public void initializeAccountTypes() {
        
        CoreModule.accountTypes = new ArrayList<String>();
        
        for (Module module : modules) {
            CoreModule.accountTypes.addAll(module.getAccountTypes());
        }
    }
    
    public void parseAccountType(String accountType) {
        
        for (Module module : modules) {
            if (module.parseAccountType(accountType)) {
                return;
            }
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
