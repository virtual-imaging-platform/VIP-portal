/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author glatard
 */
public class SimulationObjectModelLightRecord extends ListGridRecord {

    public SimulationObjectModelLightRecord() {
    }
    public SimulationObjectModelLightRecord(String name, String types, String longitudinal, String movement, String URI) {
        setName(name);
        setTypes(types);
        setLongitudinal(longitudinal);
        setMovement(movement);
        setURI(URI);
    }

    private void setName(String name) {
        setAttribute("name",name);
    }

    private void setTypes(String types) {
        setAttribute("types",types);
    }

    private void setLongitudinal(String longitudinal) {
        setAttribute("longitudinal",longitudinal);
    }

    private void setMovement(String movement) {
        setAttribute("movement",movement);
    }

    private void setURI(String URI) {
        setAttribute("uri",URI);
    }
}
