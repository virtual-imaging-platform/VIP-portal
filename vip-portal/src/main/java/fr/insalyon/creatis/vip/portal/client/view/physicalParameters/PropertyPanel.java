/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.view.physicalParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.portal.client.bean.Echogenicity;
import fr.insalyon.creatis.vip.portal.client.bean.MagneticProperty;
import fr.insalyon.creatis.vip.portal.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.portal.client.bean.Tissue;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueService;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author glatard
 */
class PropertyPanel extends Panel {

    private static PropertyPanel instance = null;
    private String tissueName;
    private TextField authorField;
    private DateField dateField;
    private TextField commentField;
    private ComboBox cb;
    private int n = -1;
    private Button save;

    private MagneticPanel magneticPanel = MagneticPanel.getInstance();
    private EchogenicityPanel echogenicityPanel = EchogenicityPanel.getInstance();
    private ChemicalPanel chemicalPanel = ChemicalPanel.getInstance();

    private boolean newProperty = true;
    private PhysicalProperty currentProp;

    public void setTissueName(String tissueName) {
        this.tissueName = tissueName;
    }

    public static PropertyPanel getInstance() {
        if (instance == null) {
            instance = new PropertyPanel();
        }
        return instance;
    }

    private PropertyPanel() {
        super("New Physical Property");


        currentProp = new PhysicalProperty();
        //proxy type combo box
        final Store propertyStore = new SimpleStore(new String[]{"property-type"}, new Object[][]{new String[]{"Echogenicity"}, new String[]{"Magnetic properties"}, new String[]{"Chemical composition"}});
        propertyStore.load();
        cb = new ComboBox();
        cb.setFieldLabel("property-type");
        cb.setDisplayField("property-type");
        cb.setStore(propertyStore);

        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setWidth(350);
        cb.setReadOnly(true);
        cb.setEmptyText("Select a physical property type...");

        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                if(comboBox.getValueAsString().equals("Echogenicity")){
                        magneticPanel.setVisible(false);
                        echogenicityPanel.setVisible(true);
                        chemicalPanel.setVisible(false);

                }
                if(comboBox.getValueAsString().equals("Magnetic properties")){
                        magneticPanel.setVisible(true);
                        echogenicityPanel.setVisible(false);
                        chemicalPanel.setVisible(false);

                }
                if(comboBox.getValueAsString().equals("Chemical composition")){
                        magneticPanel.setVisible(false);
                        echogenicityPanel.setVisible(false);
                        chemicalPanel.setVisible(true);

                }
            }
        });
        this.add(cb);

        FieldSet propertyDetails = new FieldSet("Property information");
        propertyDetails.setHeight(100);
        propertyDetails.setWidth(450);



        authorField = FieldUtil.getTextField("author", 300, "Author", false);
        authorField.setEmptyText(fr.insalyon.creatis.vip.common.client.view.Context.getInstance().getAuthentication().getUser());
        authorField.setDisabled(true);

        dateField = new DateField("Date","d-M-Y");
        dateField.setValue(new Date(System.currentTimeMillis()));
        dateField.setDisabled(true);
        dateField.setVisible(false);

        commentField = FieldUtil.getTextField("comment", 300, "Comment", false);
        commentField.setEmptyText("Some comments here...");

        propertyDetails.add(authorField);
        propertyDetails.add(dateField);
        propertyDetails.add(commentField);

        this.add(propertyDetails);

        

        save = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                if (cb.getValueAsString().equals("")) {
                    MessageBox.alert("Please select a property type");
                } else if (commentField.getValueAsString().equals("")) {
                    MessageBox.alert("Please enter a (meaningful) comment indicating where you found this property");
                } else {
                    saveProperty();
                }
            }
        });
        

        magneticPanel.setVisible(false);
        echogenicityPanel.setVisible(false);
        chemicalPanel.setVisible(false);

        this.add(magneticPanel);
        this.add(echogenicityPanel);
        this.add(chemicalPanel);

        this.add(save);

    }

    private void saveProperty() {

            java.sql.Date d = new java.sql.Date(dateField.getValue().getTime());
            currentProp.setAuthor(Context.getInstance().getAuthentication().getUser());
            currentProp.setComment(commentField.getValueAsString());
            currentProp.setType(cb.getValueAsString());
            currentProp.setDate(d);

            TissueServiceAsync ts = TissueService.Util.getInstance();

            final String propName = cb.getValueAsString();

            final AsyncCallback<DistributionInstance> callback = new AsyncCallback<DistributionInstance>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Cannot get distributioninstnace. Error:\n" + caught.getMessage() + "\nStack trace:\n" + caught.getStackTrace().toString());
                }

                public void onSuccess(DistributionInstance result) {
                    MessageBox.alert(propName + " saved", propName + " was added to tissue " + tissueName);
                }
            };

            final AsyncCallback<Void> callbackEcho = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Cannot set " + propName + ". Error:\n" + caught.getMessage() + "\nStack trace:\n" + caught.getStackTrace().toString());
                }

                public void onSuccess(Void result) {
                    MessageBox.alert(propName + " saved", propName + " was added to tissue " + tissueName);
//                    Layout.getInstance().setCenterPanel(PropertiesPanel.getInstance(tissueName));
                }
            };
            if (propName.equals("Echogenicity")) {
                Echogenicity e = new Echogenicity();
                e.setSpatialDistribution(echogenicityPanel.getSpatialDistributionInstance());
                e.setAmplitudeDistribution(echogenicityPanel.getAmplitudeDistributionInstance());
                if(newProperty)
                    ts.setEchogenicity(new Tissue(tissueName), currentProp, e, callbackEcho);
                else
                    ts.updateEchogenicity(currentProp, e, callbackEcho);
            }
            if (propName.equals("Magnetic properties")) {
                List<MagneticProperty> magProps = new ArrayList<MagneticProperty>();

                MagneticProperty rho = new MagneticProperty();
                rho.setPropertyName("rho");
                if (magneticPanel.getRhoDistName() != null) {
                    rho.setDistributionInstance(magneticPanel.getRhoDistributionInstance());
                    magProps.add(rho);
                }

                MagneticProperty t1 = new MagneticProperty();
                t1.setPropertyName("T1");
                if (magneticPanel.getT1DistName() != null) {
                    t1.setDistributionInstance(magneticPanel.getT1DistributionInstance());
                    magProps.add(t1);
                }

                MagneticProperty t2 = new MagneticProperty();
                t2.setPropertyName("T2");
                if (magneticPanel.getT2DistName() != null) {
                    t2.setDistributionInstance(magneticPanel.getT2DistributionInstance());
                    magProps.add(t2);
                }

                MagneticProperty khi = new MagneticProperty();
                khi.setPropertyName("Khi");
                if (magneticPanel.getKhiDistName() != null) {
                    khi.setDistributionInstance(magneticPanel.getKhiDistributionInstance());
                    magProps.add(khi);
                }
            if(newProperty)
                ts.setMagneticProperties(new Tissue(tissueName), currentProp, magProps, callbackEcho);
            else
                ts.updateMagneticProperties(currentProp, magProps, callbackEcho);

            }

    }

    public void setProperty(final int propId,String type, String author, String comment,String date){

        
        currentProp.setId(propId);
        currentProp.setType(type);
        currentProp.setAuthor(author);
        currentProp.setDate(new java.util.Date(date));
        currentProp.setComment(comment);

        cb.setValue(type);
        authorField.setValue(author);
        dateField.setValue(date);
        commentField.setValue(comment);

        TissueServiceAsync ts = TissueService.Util.getInstance();

        if (type.equals("Echogenicity")) {
            final AsyncCallback<Echogenicity> callback = new AsyncCallback<Echogenicity>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Cannot get echogenicity for property " + propId);
                }

                public void onSuccess(Echogenicity result) {
                    echogenicityPanel.setAmplitudeDistributionInstance(result.getAmplitudeDistribution());
                    echogenicityPanel.setSpatialDistributionInstance(result.getSpatialDistribution());
                    echogenicityPanel.setVisible(true);
                    magneticPanel.setVisible(false);
                    chemicalPanel.setVisible(false);
                }
            };
            ts.getEchogenicity(currentProp, callback);
        }

        if(type.equals("Magnetic properties")){
            final AsyncCallback<List<MagneticProperty>> callback = new AsyncCallback<List<MagneticProperty>>(){

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Cannot get magnetic properties for property "+propId);
                }

                public void onSuccess(List<MagneticProperty> result) {
                    for(MagneticProperty m : result){
                        if(m.getPropertyName().equals("rho"))
                            magneticPanel.setRhoDistributionInstance(m.getDistributionInstance());
                        if(m.getPropertyName().equals("T1"))
                            magneticPanel.setT1DistributionInstance(m.getDistributionInstance());
                        if(m.getPropertyName().equals("T2"))
                            magneticPanel.setT2DistributionInstance(m.getDistributionInstance());
                        if(m.getPropertyName().equals("Khi"))
                            magneticPanel.setKhiDistributionInstance(m.getDistributionInstance());

                        echogenicityPanel.setVisible(false);
                        chemicalPanel.setVisible(false);
                        magneticPanel.setVisible(true);
                    }
                }
            };
            ts.getMagneticProperties(currentProp, callback);
        }

    }

    public void setNewProperty(boolean newProperty) {
        this.newProperty = newProperty;
        if(newProperty){
            setTitle("New Physical Property");
            save.setTitle("Save");
            cb.clearValue();
            commentField.setValue("");
            magneticPanel.clearParams();
            echogenicityPanel.clearParams();
            magneticPanel.setVisible(false);
            echogenicityPanel.setVisible(false);
            chemicalPanel.setVisible(false);
        }
        else{
            setTitle("Physical Property");
            save.setTitle("Update");
        }
    }


}
