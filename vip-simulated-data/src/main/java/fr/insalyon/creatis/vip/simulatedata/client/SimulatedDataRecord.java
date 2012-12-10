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

    public SimulatedDataRecord(List<SemEntity> files, List<SemEntity> parameters, List<SemEntity> models, String simulation, Date date, String name) {

        setAttribute("icon", "icon-file");
    
   
        setAttribute("simulation", simulation);
        setAttribute("date", date);
        setAttribute("simulation-name",name);

       
        String shownFiles = "" , realFiles = "";
        boolean first = true;
        for(SemEntity se : files){
         if(!first){
             shownFiles += " ; ";
             realFiles += " ; ";
         }
         first = false;
         realFiles += se.getLabel();
         shownFiles += se.getLabel().substring(se.getLabel().lastIndexOf('/') + 1);
//         if(files.size() != 1){
//              realFiles += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
//              shownFiles += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
//         }
         setAttribute("type",se.getUri().substring(se.getUri().lastIndexOf('#') + 1));
        }
         
         setAttribute("short-files", shownFiles);
         setAttribute("files", realFiles);
        
        String shownParams = "", realParams = "";
        first = true;
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
                shownParams += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1).replace("-value-information", "") + ")";

            }

        }

        setAttribute("parameters", realParams);

        setAttribute("short-param", shownParams);
        String shownModels = "", realModels = "", modelURI = null;
        if (models.size() == 1 && models.get(0).getUri().equals("")) {
            modelURI = models.get(0).getLabel();
            setAttribute("short-model",models.get(0).getName());
            setAttribute("model",models.get(0).getName()+" ("+modelURI+")");
   
        } else {
            first = true;
            for (SemEntity se : models) {
                if (!first) {
                    shownModels += ";";
                    realModels += ";";
                }
                first = false;
                shownModels += se.getLabel().substring(se.getLabel().lastIndexOf('/') + 1);
                realModels += se.getLabel();
                if (models.size() != 1) {
                    shownModels += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
                    realModels += " (" + se.getUri().substring(se.getUri().lastIndexOf('#') + 1) + ")";
                }
            }
            
            setAttribute("short-model", shownModels);
            setAttribute("model", realModels);
        }
        setAttribute("model-uri", modelURI);


    }
}
