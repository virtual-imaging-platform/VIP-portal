/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;


import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author Nouha Boujelben
 */
public class CheckboxesOption extends AbstractFormLayout{
       VLayout layout;
       CheckboxItem advancedOption;
       CheckboxItem chekboxShowQuery;
       DynamicForm form;
       
   

    public CheckboxesOption(CheckboxTree checkboxTree) {
        super("100%","100%"); 
        configure(checkboxTree);
       
        
    }
    private void configure(final CheckboxTree checkboxTree){
        advancedOption = new CheckboxItem();
        advancedOption.setName("Advanced Options");
        advancedOption.setTitle("Advanced Options");
        advancedOption.setValue(false);
        advancedOption.setWidth("*");
        advancedOption.setShowTitle(false);
        
          advancedOption.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {
            public void onChange(ChangeEvent event) {

                if ((Boolean) event.getValue()) {
                    form.getItem("ShowQuery").show();
                    checkboxTree.treeGrid.showField("GroupBy");
                    checkboxTree.treeGrid.showField("OrderBy");
                    checkboxTree.treeGrid.setWidth100();
   
                    checkboxTree.formLimit.getItem("Limit").show();

                } else {
                    form.getItem("ShowQuery").hide();
                    checkboxTree.treeGrid.hideField("GroupBy");
                    checkboxTree.treeGrid.hideField("OrderBy");
                    checkboxTree.treeGrid.setWidth100(); 
                    checkboxTree.formLimit.getItem("Limit").hide();

                }
            }
        });
          
          
        chekboxShowQuery = new CheckboxItem();
        chekboxShowQuery.setName("ShowQuery");
        chekboxShowQuery.setTitle("Show Query");
        chekboxShowQuery.setWidth("*");
        chekboxShowQuery.setShowTitle(false);
        chekboxShowQuery.setVisible(false);  
        
        chekboxShowQuery.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {
            public void onChange(ChangeEvent event) {
                if ((Boolean) event.getValue()) {
                  checkboxTree.generateBody();  
                  checkboxTree.body.animateShow(AnimationEffect.SLIDE);    
                } else {
                   checkboxTree.body.animateHide(AnimationEffect.SLIDE);
                }
            }
        });
        
         form = new DynamicForm();
         form.setWidth100();
         form.setNumCols(1);
         form.setFields(advancedOption,chekboxShowQuery); 
         this.addMember(form);
       
        
        
    
    }
    
}
