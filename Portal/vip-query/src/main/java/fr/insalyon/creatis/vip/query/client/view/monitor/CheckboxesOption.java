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
