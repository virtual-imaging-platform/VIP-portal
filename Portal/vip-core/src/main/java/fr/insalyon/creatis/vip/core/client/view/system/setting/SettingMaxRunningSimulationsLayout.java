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
package fr.insalyon.creatis.vip.core.client.view.system.setting;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Nouha Boujelben
 */
public class SettingMaxRunningSimulationsLayout extends AbstractFormLayout {

    private SpinnerItem maxRunningSimulationsItem;
    private IButton maxRunningSimulations ;

    public SettingMaxRunningSimulationsLayout() {
        
        super("350", "100");
        addTitle("Max Running Simulations", CoreConstants.ICON_RUNNING_SIMULATIONS);
        configure();

    }

    private void configure() {

        maxRunningSimulationsItem = new SpinnerItem();
        maxRunningSimulationsItem.setShowTitle(false);
        maxRunningSimulationsItem.setDefaultValue(1);
        maxRunningSimulationsItem.setMin(1);
        maxRunningSimulationsItem.setMax(5000);
        maxRunningSimulationsItem.setStep(1);
        setMaxRunningSimulations();
        //Integer.parseInt(maxRunningSimulationsItem.getValueAsString()
        addField("Max Running Simulations", maxRunningSimulationsItem);
        maxRunningSimulations = WidgetUtil.getIButton("Save", CoreConstants.ICON_EDIT,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

             
            changeMaxRunningSimulations(Integer.parseInt(maxRunningSimulationsItem.getValueAsString()));
            

            }
        });
       
        addButtons(maxRunningSimulations);
    }
    
    
    private void setMaxRunningSimulations(){
      
    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get the max running simulations in the platfrom" + caught.getMessage());
            }

            @Override
            public void onSuccess(Integer result) {
               maxRunningSimulationsItem.setValue(result);
   
            }
        };
        service.getMaxConfiguredPlatformSimulation(callback);
    
    
    }
    
    private void changeMaxRunningSimulations(int maxRunningsimulation){
      
    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to change the max running simulations in the platfrom" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
              Layout.getInstance().setNoticeMessage("the max configured running simulation has been changed");
   
            }
        };
       service.changeMaxConfiguredPlatformSimulation(maxRunningsimulation, callback);
    
    
    }

}
