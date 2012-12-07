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
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.util.Date;

/**
 *
 * @author Tristan Glatard
 */
public class SimulationObjectModelLightRecord extends ListGridRecord {

    public SimulationObjectModelLightRecord() {
    }

    public SimulationObjectModelLightRecord(String name, String owner, String description, String types, String longitudinal,
            String movement, String URI, String date, String SURL) {
        setName(name);
        setTypes(types);
        setLongitudinal(longitudinal);
        setMovement(movement);
        setURI(URI);
        setOwner(owner);
        setDescription(description);
        if(date != null)
        {
            date = date.replace("T", " ").replace("Z","");
            setDate(date.substring(0,date.lastIndexOf(".")));
        }
        else
            setDate("not available");
        setSURL(SURL);
    }

    private void setName(String name) {
        setAttribute("name", name);
    }

    private void setTypes(String types) {
        setAttribute("types", types);
    }

    private void setLongitudinal(String longitudinal) {
        setAttribute("longitudinal", longitudinal);
    }

    private void setMovement(String movement) {
        setAttribute("movement", movement);
    }

    private void setURI(String URI) {
        setAttribute("uri", URI);
    }
    
    private void setOwner(String owner) {
        setAttribute("owner", owner);
    }
    
    private void setDescription(String description) {
        setAttribute("description", description);
    }
    
    private void setDate(String date){
        setAttribute("date", date);
    }

    private void setSURL(String SURL) {
        setAttribute("surl",SURL);
    }
    
}
