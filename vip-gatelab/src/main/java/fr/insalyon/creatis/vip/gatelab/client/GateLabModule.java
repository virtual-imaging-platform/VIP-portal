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
package fr.insalyon.creatis.vip.gatelab.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.gatelab.client.view.GateLabHomeParser;

/**
 *
 * @author Rafael Silva
 */
public class GateLabModule extends Module {

    public GateLabModule() {

        ApplicationModule.reservedClasses.add(GateLabConstants.GATELAB_CLASS);
        CoreModule.homeExecutor.addParser(new GateLabHomeParser());
        addAccountType(GateLabConstants.ACCOUNT_GATELAB);
    }

    @Override
    public void load() {
    }

    @Override
    public void postLoading() {
    }

    @Override
    public boolean parseAccountType(String accountType) {

        if (accountType.equals(GateLabConstants.ACCOUNT_GATELAB)) {
            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    SC.say("Unable to add user to group '" + GateLabConstants.GROUP_GATELAB
                            + "':<br />" + caught.getMessage());
                }

                public void onSuccess(Void result) {
                }
            };
            service.addUserToGroup(GateLabConstants.GROUP_GATELAB, callback);
            return true;
        }
        return false;
    }

    @Override
    public void terminate() {
    }
}
