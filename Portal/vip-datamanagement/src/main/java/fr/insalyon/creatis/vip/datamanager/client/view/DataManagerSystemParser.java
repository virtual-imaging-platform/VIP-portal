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
package fr.insalyon.creatis.vip.datamanager.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.ssh.ManageSSHTab;
import fr.insalyon.creatis.vip.datamanager.client.view.system.cache.ManageCachedFilesTab;
import fr.insalyon.creatis.vip.datamanager.client.view.system.operation.ManageOperationsTab;
import fr.insalyon.creatis.vip.datamanager.client.view.system.zombie.ManageZombieFilesTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DataManagerSystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(DataManagerConstants.APP_OPERATIONS, DataManagerConstants.APP_IMG_OPERATIONS);
            addApplication(DataManagerConstants.APP_CACHED_FILES, DataManagerConstants.APP_IMG_CACHED_FILES);
            addApplication(DataManagerConstants.APP_ZOMBIE_FILES, DataManagerConstants.APP_IMG_ZOMBIE_FILES);
            addApplication(DataManagerConstants.APP_SSH, DataManagerConstants.APP_IMG_SSH);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(DataManagerConstants.APP_OPERATIONS)) {
            Layout.getInstance().addTab(new ManageOperationsTab());
            return true;

        } else if (applicationName.equals(DataManagerConstants.APP_CACHED_FILES)) {
            Layout.getInstance().addTab(new ManageCachedFilesTab());
            return true;

        } else if (applicationName.equals(DataManagerConstants.APP_ZOMBIE_FILES)) {
            Layout.getInstance().addTab(new ManageZombieFilesTab());
            return true;

        }
        
        else if (applicationName.equals(DataManagerConstants.APP_SSH)) {
            Layout.getInstance().addTab(new ManageSSHTab());
            return true;

        }
        return false;
    }
}
