/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class SimulationRecord extends ListGridRecord {

    public SimulationRecord() {
    }

    public SimulationRecord(String application, String status,
            String simulationId, String user, String date) {
        
        setStatusIco(status);
        setApplication(application);
        setStatus(status);
        setSimulationId(simulationId);
        setUser(user);
        setDate(date);
    }

    public void setStatusIco(String status) {
        setAttribute("statusIco", "ico_" + status.toLowerCase());
    }
    
    public String getStatusIco() {
        return getAttributeAsString("statusIco");
    }

    public void setApplication(String application) {
        setAttribute("application", application);
    }
    
    public String getApplication() {
        return getAttributeAsString("application");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }
    
    public String getStatus() {
        return getAttributeAsString("status");
    }

    public void setSimulationId(String simulationId) {
        setAttribute("simulationId", simulationId);
    }
    
    public String getSimulationId() {
        return getAttributeAsString("simulationId");
    }

    public void setUser(String user) {
        setAttribute("user", user);
    }
    
    public String getUser() {
        return getAttributeAsString("user");
    }

    public void setDate(String date) {
        setAttribute("date", date);
    }
    
    public String getDate() {
        return getAttributeAsString("date");
    }
}
