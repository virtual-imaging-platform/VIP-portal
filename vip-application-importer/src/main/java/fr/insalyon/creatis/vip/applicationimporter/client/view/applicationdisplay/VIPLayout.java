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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.ValidatorUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VIPLayout extends AbstractFormLayout {

    private final LocalTextField applicationLocation;
    private final CheckboxItem isRunOnGrid; // And not locally.
    private final CheckboxItem overwriteIfexists;
    private final SelectItem appCbItem;
    private final SelectItem tagsCbItem;
    private final SelectItem fileAccessProtocolItem;

    public VIPLayout(String width, String height) {
        super(width, height);
        addTitle("Executable", Constants.ICON_EXECUTABLE);
        setMembersMargin(2);
        setOverflow(Overflow.AUTO);
        // Adds application location
        applicationLocation = new LocalTextField("Application file location", true, true);
        setApplicationLocationValue();
        this.addMember(applicationLocation);

        overwriteIfexists = new CheckboxItem("ckbox_over", "Overwrite application version if it exists");
        overwriteIfexists.setAlign(Alignment.LEFT);

        isRunOnGrid = new CheckboxItem("ckbox_isRunOnGrid", "Application must run on grid, and not locally");
        isRunOnGrid.setAlign(Alignment.LEFT);

        //ComboBox to select type of application
        appCbItem = new SelectItem();
        appCbItem.setTitle("<b>Select type of application</b>");
        appCbItem.setType("comboBox");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<>();
        valueMap.put(Constants.APP_IMPORTER_STANDALONE_TYPE, "standalone");
        valueMap.put(Constants.APP_IMPORTER_DOT_INPUTS_TYPE, "Dot inputs (Challenge)");
        appCbItem.setValueMap(valueMap);

        tagsCbItem = createTagsSelect();

        //select list to choose the execution type
        fileAccessProtocolItem = new SelectItem();
        fileAccessProtocolItem.setTitle("<br>Select where the application files must be located</b>");
        fileAccessProtocolItem.setType("comboBox");
        LinkedHashMap<String, String> fileAccessProtocolValueMap = new LinkedHashMap<>();
        fileAccessProtocolValueMap.put(Constants.APP_IMPORTER_FILE_PROTOCOL, "Local (file)");
        fileAccessProtocolValueMap.put(Constants.APP_IMPORTER_LFN_PROTOCOL, "Grid (lfn)");
        fileAccessProtocolItem.setValueMap(fileAccessProtocolValueMap);

        this.addMember(FieldUtil.getForm(appCbItem));
        this.addMember(FieldUtil.getForm(isRunOnGrid));
        this.addMember(FieldUtil.getForm(overwriteIfexists));
        this.addMember(FieldUtil.getForm(tagsCbItem));
        this.addMember(FieldUtil.getForm(fileAccessProtocolItem));

    }

    public void setApplicationLocationValue(){
        
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to retrieve configurated root folder for application importer, setting it to Home:<br />" + caught.getMessage());
                applicationLocation.setValue("/vip/Home");
            }

            @Override
            public void onSuccess(String result) {
                if (ValidatorUtil.validateRootPath(result, "create a folder in")
                        && ValidatorUtil.validateUserLevel(result, "create a folder in")) {
                    applicationLocation.setValue(result);
                }else{
                    applicationLocation.setValue("/vip/Home");
                }       
            }
        };
        ApplicationImporterService.Util.getInstance().getApplicationImporterRootFolder(callback);

    }
    

    /**
     * Get the location where to create the application
     *
     * @return the location
     */
    public String getApplicationLocation() {
        return applicationLocation.getValue();
    }

    public boolean getOverwrite() {
        return this.overwriteIfexists.getValueAsBoolean();
    }

    public boolean getIsRunOnGrid() {
        return this.isRunOnGrid.getValueAsBoolean();
    }

    /**
     * Get the type of application (standalone or challenge)
     *
     * @return the type
     */
    public String getApplicationType() {
        if (appCbItem._getValue() == null){
            return null;
        } else {
            return appCbItem._getValue().toString();
        }
    }

    private SelectItem createTagsSelect() {
        // ComboBox to select tags.
        SelectItem tagsCb = new SelectItem();
        tagsCb.setTitle("<b>Dirac tag</b>");
        tagsCb.setType("comboBox");

        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to retrieve configurated list of requirements, setting it to None:<br />" + caught.getMessage());
                tagsCb.setValueMap("None");
            }

            @Override
            public void onSuccess(List<String> result) {
                result = new ArrayList<>(result); // make a new list because the returned one does not support the add method
                if(!result.contains("None")){
                    result.add("None");
                }
                
                Map<String, String> requirementsValues = new LinkedHashMap<>();
                for (String requirement : result) {
                    requirementsValues.put(requirement, requirement);
                }               
                tagsCb.setValueMap(requirementsValues);
                
            }
        };
        ApplicationImporterService.Util.getInstance().getApplicationImporterRequirements(callback);
        
        tagsCb.setValue("None");

        return tagsCb;
    }

    public String getTag() {
        return tagsCbItem._getValue().toString();
    }
    public String getFileAccessProtocol(){
        return fileAccessProtocolItem._getValue().toString();
    }
}
