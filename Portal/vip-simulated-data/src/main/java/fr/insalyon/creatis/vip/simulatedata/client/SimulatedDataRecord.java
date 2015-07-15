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
