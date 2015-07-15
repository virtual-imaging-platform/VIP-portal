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
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.FormLayout;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Tissue;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueService;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueServiceAsync;
//import fr.insalyon.creatis.vip.portal.client.view.layout.AbstractLeftPanel;
import java.util.List;

/**
 *
 * @author glatard
 */
public class TissuePanel {

    private static TissuePanel instance = null;
    //panel used to create/update tissues
    private Store tissueStore;

    /**
     * Pattern Singleton: Gets an unique instance of TissuePanel
     *
     * @return Unique instance ofTissuePanel
     */
    public static TissuePanel getInstance() {
        if (instance == null) {
            instance = new TissuePanel("Tissues");
        } else {
            instance.reloadData();
        }
        return instance;
    }
    private String tissueName;

    /**
     * Default constructor
     * @param title Panel title
     */
    private TissuePanel(final String title) {
//        super(null);
    }

    private void reloadData() {
        loadTissues();
        MessageBox.alert("reload data: not yet fully implemented");
    }


//    @Override
    public Panel getPanel() {
        FormPanel p = new FormPanel();
        p.setTitle("Tissues");
        p.setId("tissue-panel");
        p.setLayout(new FormLayout());
        p.setLabelAlign(Position.TOP);
        p.setMargins(0, 0, 0, 0);
        p.setPaddings(10, 5, 5, 5);
        p.setBorder(false);

        //tissue selection
        tissueStore = new SimpleStore("tissue-name", new Object[]{});
        tissueStore.load();
        loadTissues();

        ComboBox cb = new ComboBox();
        cb.setId("combo-tisue-name");
        cb.setFieldLabel("Tissue name");
        cb.setStore(tissueStore);
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setDisplayField("tissue-name");
        cb.setValueField("tissue-name");
        cb.setWidth(150);
        cb.setReadOnly(true);
        cb.setEmptyText("Select a Tissue...");
        cb.addListener(new ComboBoxListenerAdapter() {
            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                    tissueName = comboBox.getValueAsString();
              }
        });
        p.add(cb);

     //tissue toolbar
        ToolbarButton newButton = new ToolbarButton("New", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
//                 Layout.getInstance().setCenterPanel(NewTissuePanel.getInstance(instance));
            }
        });
        Toolbar tool = new Toolbar();
        tool.addButton(newButton);
        p.setTopToolbar(tool);
        Button viewButton = new Button("View properties", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                if (tissueName == null) {
                    MessageBox.alert("Please select a tissue first");
                } else {
//                    Layout.getInstance().setCenterPanel(PropertiesPanel.getInstance(tissueName));
                }
            }
        });
       p.addButton(viewButton);

        Button delete = new Button("Remove", new ButtonListenerAdapter(){
            @Override
            public void onClick(Button button, EventObject e){
                MessageBox.confirm("Confirm", "Do you really want to remove tissue \"" + tissueName + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    TissueServiceAsync service = TissueService.Util.getInstance();

                                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert("Error", "Cannot remove tissue\n" + caught.getMessage());
                                        }

                                        public void onSuccess(Void result) {
                                            loadTissues();
                                            MessageBox.alert("The tissue was successfully removed!");
                                        }
                                    };
                                    service.deleteTissue(new Tissue(tissueName), callback);
                                }
                            }
                        });
            }
        });

        p.addButton(delete);
        return p;
    }
    public void loadTissues() {
         TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<List<Tissue>> callback = new AsyncCallback<List<Tissue>>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error getting tissue list\n" + caught.getMessage());
            }
            public void onSuccess(List<Tissue> result) {
                Object[][] data = new Object[result.size()][1];
                for (int i = 0; i < result.size(); i++) {
                    data[i][0] = ((Tissue) result.get(i)).getTissueName();
                }
                MemoryProxy proxy = new MemoryProxy(data);
                tissueStore.setDataProxy(proxy);
                tissueStore.load();
                tissueStore.commitChanges();
            }
        };
        ts.getTissues(callback);
    }
    }
