/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.view.physicalParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.bean.Distribution;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionParameter;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionParameterValue;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueService;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.FieldUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SelectDistributionFieldSet extends FieldSet {

    private Store distributionStore;
    
    private Distribution currentDist;
    private DistributionInstance currentInstance;

    private HashMap distributions;
    private int previousNParam = 0;
    private MultiFieldPanel valuesMp;

    String title;


    ComboBox cb;
    
    public SelectDistributionFieldSet(String title) {
        super(title);
        this.title = title;

        currentDist = new Distribution();
        currentInstance = null;
        
        distributionStore = new SimpleStore("distribution-name2", new Object[]{});
        distributionStore.load();
        loadDistributions();

        cb = new ComboBox();
        cb.setFieldLabel("Distribution");
        cb.setStore(distributionStore);
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setDisplayField("distribution-name2");
        cb.setValueField("distribution-name2");
        cb.setWidth(150);
        cb.setReadOnly(true);
        cb.setEmptyText("Select a Distribution...");
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                previousNParam = currentDist.getParameters().size();
                currentDist = (Distribution) distributions.get(comboBox.getValueAsString());
                showParams();
            }
        });
        this.add(cb);

    }

    public Distribution getCurrentDist() {
        return currentDist;
    }

    public MultiFieldPanel getValuesMp() {
        return valuesMp;
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
    public void clearParams(){
        currentInstance = null;
    }
    private void showParams() {
        TextField t = null;
        valuesMp = null;
        final int n = currentDist.getParameters().size();
        for (int i = 0; i < previousNParam; i++) {
            this.remove(title + "-mp-" + i);
        }
        int i = 0;
        for (DistributionParameter p : currentDist.getParameters()) {
            //distField.remove("param-" + i);
            t = FieldUtil.getTextField(title + "-param-" + i, 300, p.getName()+" ("+p.getSymbol()+")", false);
            if(currentInstance != null)
            {
                for(DistributionParameterValue val : currentInstance.getValues())
                {
                        if(val.getParam().getName().equals(p.getName())){
                           t.setValue(""+val.getValue());
                        }
                }

            }
            valuesMp = FieldUtil.getMultiFieldPanel(title+"-mp-"+i);
            valuesMp.add(t);
            //t.setValue(p.getName());
            this.add(valuesMp);
            i++;
        }
        this.doLayout();
    }

    public void setDistributionInstance(DistributionInstance inst){
        currentInstance = inst;
        currentDist = currentInstance.getDistributionType();
        cb.setValue(currentDist.getName());
        showParams();
    }

    public void parseInstance() {
        List<DistributionParameterValue> map = new ArrayList<DistributionParameterValue>();
        int i = 0;
        //MessageBox.alert("dfs-a");
        if (currentDist == null) {
            MessageBox.alert("currentdist is null !");
        }
        for (DistributionParameter p : currentDist.getParameters()) {
           // MessageBox.alert("dfs-before-" + i);
            MultiFieldPanel mfp = (MultiFieldPanel) this.getComponent(title + "-mp-" + i);
            //MessageBox.alert("dfs-before-tf" + i);
            TextField tf = (TextField) (mfp).getComponent(title + "-param-" + i);
           // MessageBox.alert("dfs-before-value" + i);
            String value = tf.getValueAsString();
           // MessageBox.alert("dfs-before-put-" + i);
            map.add(new DistributionParameterValue(p, Double.parseDouble(value)));
            i++;
           // MessageBox.alert("dfs-" + i);
        }
        DistributionInstance di = new DistributionInstance(-1, currentDist, map);
        //MessageBox.alert("dfs-z");

        currentInstance = di;

    }

    public DistributionInstance getDistributionInstance() {
        parseInstance();
        return currentInstance;
    }
}
