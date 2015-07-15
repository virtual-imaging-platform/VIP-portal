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
package fr.insalyon.creatis.vip.physicalproperties.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Tissue;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueService;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;

/**
 *
 * @author glatard
 */
public class NewTissuePanel extends Panel {
    private TissuePanel leftPanel;
    private static NewTissuePanel instance = null;
    
    public static NewTissuePanel getInstance(TissuePanel t){
        if(instance == null)
                instance = new NewTissuePanel("New Tissue",t);
        return instance;
    }


    private NewTissuePanel(String title, TissuePanel leftPanel){
        super(title);
    this.leftPanel = leftPanel;
        this.setLayout(new VerticalLayout());
        this.setBorder(false);
        this.setId("tissue-formpanel");
        this.setHeight(200);
        this.setWidth(600);

        FieldSet tissueFields = new FieldSet("Tissue information");
        tissueFields.setId("tissue-fieldset");
        tissueFields.setHeight(100);
        tissueFields.setWidth(450);

//        final TextField tissueNameField = FieldUtil.getTextField("tissue-name", 300, "Name", false);
//        tissueNameField.setLayoutData(new RowLayout());
//        tissueNameField.setDisabled(false);
//        //tissueNameField.setHeight(50);
//        tissueFields.add(tissueNameField);

        this.add(tissueFields);

        Panel tissueControl = new Panel();
        tissueControl.setHeight(25);
        tissueControl.setWidth(300);
        tissueControl.setBorder(false);

         //save and delete buttons
        Button save = new Button("Save",  new ButtonListenerAdapter(){
             @Override
            public void onClick(Button button, EventObject e) {

                 //save tissue
//                String tissueName = tissueNameField.getValueAsString();

//                if (tissueName.trim().equals("")) {
//                    MessageBox.alert("Error", "You should provide a tissue name.");
//                    return;
//                }
//                saveTissue(new Tissue(tissueName));
            }
        });
        tissueControl.addButton(save);
       
         this.add(tissueControl);

    }

       private void saveTissue(final Tissue t){
        TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>(){

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error","Cannot save tissue "+t.getTissueName()+" ("+t.getTissueName()+")");
            }

            public void onSuccess(Void result) {
                MessageBox.alert("Tissue update", "Tissue list was updated");
                leftPanel.loadTissues();
            }
        };
            ts.addTissue(t, callback);
    }

    
}
