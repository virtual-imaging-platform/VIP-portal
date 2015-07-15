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
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Distribution;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.DistributionParameter;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueService;
import fr.insalyon.creatis.vip.physicalproperties.client.rpc.TissueServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author glatard
 */
public class DistributionsCenterPanel extends Panel {

    private static DistributionsCenterPanel instance = null;
    //all the distributions
    private Store distributionStore;
    private HashMap distributions;
    //the current distribution
    private Distribution currentDist;
    private boolean newDist = false;
    //the form panel showing the distribution information
    private FormPanel newEditForm;
    private FieldSet distField;
    private TextField distName;
    private TextField distExp;
    private Button newParam;
    private Button save;
    private Button delete;

    //the window to add a new parameter
    private TextField nameSymbol;
    private TextField paramSymbol;

    public static DistributionsCenterPanel getInstance() {
        if (instance == null) {
            instance = new DistributionsCenterPanel();
        }
        return instance;
    }

    private DistributionsCenterPanel() {
        super("Distributions");
        currentDist = new Distribution();
        distributionStore = new SimpleStore("distribution-name", new Object[]{});
        distributionStore.load();
        loadDistributions();

        distField = new FieldSet("Distribution information");
        distField.setWidth(600);
//        distExp = FieldUtil.getTextField("dist-expression", 300, "Expression", false);
//        distName = FieldUtil.getTextField("dist-name", 300, "Distribution name", false);
//        distField.add(distName);
//        distField.add(distExp);
        distField.setHeight(1000);
        
        Toolbar toolbar = new Toolbar();
        ToolbarButton newButton = new ToolbarButton("New distribution", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                newEditForm.setVisible(true);
                distName.setValue("");
                distExp.setValue("");
                newDist = true;

                newParam.setVisible(true);
                save.setVisible(true);
                delete.setVisible(false);
            }
        });
        toolbar.addButton(newButton);
        this.setTopToolbar(toolbar);

        ComboBox cb = new ComboBox();
        cb.setFieldLabel("Distribution name");
        cb.setStore(distributionStore);
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setDisplayField("distribution-name");
        cb.setValueField("distribution-name");
        cb.setWidth(150);
        cb.setReadOnly(true);
        cb.setEmptyText("Select a Distribution...");
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                currentDist = (Distribution) distributions.get(comboBox.getValueAsString());
                distName.setValue(currentDist.getName());
                distExp.setValue(currentDist.getExpression());
                instance.showSymbols();
                newEditForm.setVisible(true);
                newDist = false;

                newParam.setVisible(false);
                save.setVisible(false);
                delete.setVisible(true);
            }
        });
        this.add(cb);


        newEditForm = new FormPanel();
        newEditForm.setBorder(false);
        newEditForm.setVisible(false);

        showSymbols();
        newEditForm.add(distField);

        save = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                currentDist.setName(distName.getValueAsString());
                currentDist.setExpression(distExp.getValueAsString());
                saveDist(currentDist);
            }
        });
        newEditForm.addButton(save);

        delete = new Button("Delete", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                currentDist.setName(distName.getValueAsString());
                currentDist.setExpression(distExp.getValueAsString());
                deleteDist(currentDist);
            }
        });
        newEditForm.addButton(delete);

        final Window window = new Window();
        window.setTitle("New symbol");
        window.setWidth(500);
        window.setHeight(200);
        window.setMinWidth(500);
        window.setMinHeight(200);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);
        window.addButton(new Button("Add", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                if (nameSymbol.getValueAsString().equals("")) {
                    MessageBox.alert("Please enter a parameter name");
                } else if (paramSymbol.getValueAsString().equals("")) {
                    MessageBox.alert("Please enter a parameter symbol");
                } else {
                    currentDist.getParameters().add(new DistributionParameter(nameSymbol.getValueAsString(), paramSymbol.getValueAsString()));
                    showSymbols();
                    window.hide();
                }
            }
        }));
        window.addButton(new Button("Cancel", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                window.hide();
            }
        }));

        window.setCloseAction(Window.HIDE);
        window.setPlain(true);

        FormPanel formPanel = new FormPanel();
        //strips all Ext styling for the component
        formPanel.setBaseCls("x-plain");
        formPanel.setLabelWidth(55);
        formPanel.setUrl("save-form.php");

        formPanel.setWidth(500);
        formPanel.setHeight(300);

        // anchor width by percentage

//        nameSymbol = FieldUtil.getTextField("name", 300, "Parameter name", false);
//        formPanel.add(nameSymbol, new AnchorLayoutData("100%"));

        // anchor width by percentage
//        paramSymbol = FieldUtil.getTextField("symbol", 300, "Parameter symbol", false);
//        formPanel.add(paramSymbol, new AnchorLayoutData("100%"));

        window.add(formPanel);

        newParam = new Button("New parameter", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                window.show();
            }
        });
        newEditForm.addButton(newParam);

        this.add(newEditForm);

    }

    private void loadDistributions() {
        TissueServiceAsync ts = TissueService.Util.getInstance();

        final AsyncCallback<List<Distribution>> callback = new AsyncCallback<List<Distribution>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error getting distribution list\n" + caught.getMessage());
            }

            public void onSuccess(List<Distribution> result) {
                distributions = new HashMap();
                Object[][] data = new Object[result.size()][1];
                for (int i = 0; i < result.size(); i++) {
                    data[i][0] = ((Distribution) result.get(i)).getName();
                    distributions.put(data[i][0], ((Distribution) result.get(i)));
                }
                MemoryProxy proxy = new MemoryProxy(data);
                distributionStore.setDataProxy(proxy);
                distributionStore.load();
                distributionStore.commitChanges();
            }
        };
        ts.getDistributions(callback);
    }

    private void showSymbols() {      
       TextField t = null;
       final int n = currentDist.getParameters().size();
        
       int i = 0;
       for (DistributionParameter p : currentDist.getParameters()) {
            //distField.remove("param-" + i);
//            t = FieldUtil.getTextField("param-" + i, 300, p.getSymbol(), false);
//            t.setValue(p.getName());
//            distField.add(t);
            i++;
        }
        distField.doLayout();
        
    }

    private void saveDist(final Distribution d) {
        TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Cannot save distribution !");
            }

            public void onSuccess(Void result) {
                loadDistributions();
                MessageBox.alert("Property saved", "Distribution " + d.getName() + " (" + d.getExpression() + ") was successfully saved.");
            }
        };

        if (newDist) {
            ts.addDistribution(d, callback);
        } else {
            ts.updateDistribution(d, callback);
        }

    }

    private void deleteDist(final Distribution d) {
        TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Cannot save distribution !");
            }

            public void onSuccess(Void result) {
                loadDistributions();
                MessageBox.alert("Property saved", "Distribution " + d.getName() + " (" + d.getExpression() + ") was successfully deleted.");
            }
        };
        ts.deleteDistribution(d, callback);
    }
}
