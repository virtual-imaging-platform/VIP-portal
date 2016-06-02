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
package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesTool;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.applicationimporter.client.JSONUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VIPLayout extends AbstractFormLayout {

    private final LocalTextField applicationLocation;
    private final CheckboxItem overwriteIfexists;
   private final TextItem tbAddDescriptor;
   private final SelectItem appCbItem;
    
    public VIPLayout(String width, String height) {
        super(width, height);
        addTitle("Executable", Constants.ICON_EXECUTABLE);
        setMembersMargin(2);
        setOverflow(Overflow.AUTO);
        // Adds application location
        applicationLocation = new LocalTextField("Application file location", true, true);
        applicationLocation.setValue("/vip/Home");
        this.addMember(applicationLocation);
        
        overwriteIfexists = new CheckboxItem("ckbox_over","Overwrite application version if it exists");
        overwriteIfexists.setAlign(Alignment.LEFT);
        
          appCbItem = new SelectItem();
        appCbItem.setTitle("<b>Select type of applicatioN</b>");
       // appCbItem.setHint("<nobr>select type of application</nobr>");
        appCbItem.setType("comboBox");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
        valueMap.put("none", "none");  
        valueMap.put("challenge_msseg", "Challenge MSSEG");
        valueMap.put("challenge_petseg", "Challenge PETSEG");
        appCbItem.setValueMap(valueMap);
        //appCbItem.setsetDefaultValue("none");
        appCbItem.addChangeHandler( new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (event.getValue().toString().contains("none")) {
                    tbAddDescriptor.setValue("");
                    tbAddDescriptor.setDisabled(true);
                }
                else if (event.getValue().toString().contains("msseg")) {
                    tbAddDescriptor.setValue(Constants.APP_IMPORTER_CHALLENGE_PATH_MSSEG);
                    tbAddDescriptor.setDisabled(false);
                }
                else if (event.getValue().toString().contains("petseg")) {
                    tbAddDescriptor.setValue(Constants.APP_IMPORTER_CHALLENGE_PATH_PETSEG);
                    tbAddDescriptor.setDisabled(false);
                }
            }
            
             });
        tbAddDescriptor = new TextItem();
        tbAddDescriptor.setTitle("<b>location of additional descriptor(s)</b>");
        tbAddDescriptor.setValue("");
        tbAddDescriptor.setDisabled(false);
        
        this.addMember(FieldUtil.getForm(appCbItem));
        this.addMember(FieldUtil.getForm(tbAddDescriptor));
        this.addMember(FieldUtil.getForm(overwriteIfexists));
    }
    
    
    public String getApplicationLocation(){
        return applicationLocation.getValue();
    }
    
    public boolean getOverwrite(){
        return this.overwriteIfexists.getValueAsBoolean();
    }
    
    public String getDescriptorLocation() {
        return tbAddDescriptor.getValueAsString();
    }
    
    public String getApplicationType()
    {
        return appCbItem._getValue().toString();
    }
}   
