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
package fr.insalyon.creatis.vip.core.client;

import fr.insalyon.creatis.vip.core.client.bean.User;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class Module {

    public abstract void load();

    public abstract void postLoading();

    public abstract void terminate();

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
    
    /**
     * This method returns true when the Module must be displayed only when the User is in at least 1 grid job group
     * @return 
     */
    public boolean requiresGridJob(){ return false; }

     /**
     * This method returns true when the Module must be displayed only when the User is in at least 1 grid file group
     * @return 
     */
    public boolean requiresGridFile() { return false; }
    
}
