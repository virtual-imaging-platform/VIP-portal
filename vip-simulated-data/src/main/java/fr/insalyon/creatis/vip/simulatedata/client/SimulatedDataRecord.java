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
    
    public SimulatedDataRecord(String file, String type, List<SemEntity> parameters, List<SemEntity> models,String simulation,Date date) {
        
        setAttribute("icon", "icon-file");
        setAttribute("file", file);
        setAttribute("type", type);
        setAttribute("parameters", parameters);
        setAttribute("model",models);
        setAttribute("simulation",simulation);
        setAttribute("date",date);
        
        setAttribute("short-file",file.substring(file.lastIndexOf('/')+1));
        String shownParams = "";
        for(SemEntity se : parameters){
            shownParams+=se.getLabel().substring(se.getLabel().lastIndexOf('/')+1);
            if(parameters.size()!=1)
                shownParams+=" ("+se.getUri().substring(se.getUri().lastIndexOf('#')+1)+")";
        }
        setAttribute("short-param",shownParams);
        String shownModels = "";
        for(SemEntity se : models){
            
            shownModels+=se.getLabel().substring(se.getLabel().lastIndexOf('/')+1);
            if(models.size()!=1)
                shownModels+=" ("+se.getUri().substring(se.getUri().lastIndexOf('#')+1)+")";
        }
        setAttribute("short-model",shownModels);
        
        
    }
    
}
