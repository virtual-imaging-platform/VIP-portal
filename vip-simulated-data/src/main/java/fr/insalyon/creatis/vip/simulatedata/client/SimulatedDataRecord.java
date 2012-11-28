/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SemEntity;
import java.util.Date;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SimulatedDataRecord extends ListGridRecord {

    public SimulatedDataRecord() {
    }

    public SimulatedDataRecord(String file, String type, List<SemEntity> parameters, List<SemEntity> models, String simulation, Date date) {

        setAttribute("icon", "icon-file");
        setAttribute("file", file);
        setAttribute("type", type);
   
        setAttribute("simulation", simulation);
        setAttribute("date", date);

        setAttribute("short-file", file.substring(file.lastIndexOf('/') + 1));
        String shownParams = "", realParams = "";
        boolean first = true;
        for (SemEntity se : parameters) {
            if (!first) {
                shownParams += " ; ";
                realParams += " ; ";
            }
            first = false;
            realParams += se.getLabel();
            shownParams += se.getLabel().substring(se.getLabel().lastIndexOf('/') + 1);
            if (parameters.size() != 1) {

                realParams += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
                shownParams += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";

            }

        }

        setAttribute("parameters", realParams);

        setAttribute("short-param", shownParams);
        String shownModels = "", realModels ="";
        first= true;
        for (SemEntity se : models) {
            if(!first){
                shownModels += ";";
                realModels += ";";
            }
            first = false;
            shownModels += se.getLabel().substring(se.getLabel().lastIndexOf('/') + 1);
            realModels += se.getLabel().substring(se.getLabel().lastIndexOf('/') + 1);;
            if (models.size() != 1) {
                shownModels += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
                realModels += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
            }
        }
        setAttribute("short-model", shownModels);
        setAttribute("model", realModels);


    }
}
