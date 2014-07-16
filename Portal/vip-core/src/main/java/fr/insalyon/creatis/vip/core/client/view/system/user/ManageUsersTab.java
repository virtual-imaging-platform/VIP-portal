/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageUsersTab extends AbstractManageTab {

    private UsersLayout usersLayout;
    private EditUserLayout editLayout;

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
            String level, String countryCode, int maxRunningSimulations) {

        editLayout.setUser(name, email, confirmed, level, countryCode, maxRunningSimulations);
    }
}
