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
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

public class VIPLayout extends AbstractFormLayout {

    private final LocalTextField applicationLocation;
    private final SelectItem classesPickList;
    
    public VIPLayout(String width, String height) {
        super(width, height);
        addTitle("Executable", Constants.ICON_EXECUTABLE);
        setMembersMargin(2);
        setOverflow(Overflow.AUTO);
              
        // Add class pick list
        Label itemLabel = new Label("<strong>VIP class</strong>"+"<font color=red>(*)</font>");
        itemLabel.setHeight(20);
        classesPickList = new SelectItem();
        classesPickList.setShowTitle(false);
        classesPickList.setMultiple(true);
        classesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        classesPickList.setWidth(450);
        loadClasses(classesPickList);
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setWidth100();
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(classesPickList);
        addMember(itemLabel);
        addMember(titleItemForm);
        
        // Adds application location
        applicationLocation = new LocalTextField("Application location", true, true);
        this.addMember(applicationLocation);
    }
    
    private void loadClasses(final SelectItem selectItem) {
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of classes:<br />" + caught.getMessage());
            }
            @Override
            public void onSuccess(List<AppClass> result) {
                List<String> dataList = new ArrayList<String>();
                for (AppClass c : result) {
                    dataList.add(c.getName());
                }
                selectItem.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        ApplicationService.Util.getInstance().getClasses(callback);
    }
    
    public String getApplicationLocation(){
        return applicationLocation.getValue();
    }
    
    public String[] getVIPClasses(){
        return classesPickList.getValues();
    }
}
