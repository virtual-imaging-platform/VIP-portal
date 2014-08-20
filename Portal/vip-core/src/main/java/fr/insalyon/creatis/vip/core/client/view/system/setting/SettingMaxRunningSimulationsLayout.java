/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
