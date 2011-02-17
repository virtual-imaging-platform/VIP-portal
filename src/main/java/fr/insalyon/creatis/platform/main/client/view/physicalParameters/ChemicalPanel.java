/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.platform.main.client.view.physicalParameters;

import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import fr.insalyon.creatis.platform.main.client.bean.PhysicalProperty;
import fr.insalyon.creatis.platform.main.client.view.common.FieldUtil;
import java.util.EventObject;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;


/**
 *
 * @author glatard
 */
class ChemicalPanel extends Panel {
  private static ChemicalPanel instance = null;
  //maintains total number of elements to ensure unique ids
  private static int nElements = 0;
private PhysicalProperty property;

    public static ChemicalPanel getInstance(){
        if(instance == null)
            instance = new ChemicalPanel();
        return instance;
    }
    private ComboBox cb;

    private class elementMFP extends MultiFieldPanel{

        private ComboBox elementCb;
        private TextField massRatio;
        
        public elementMFP(Store elementStore){
            this.setId("elementMfp-"+nElements);
            elementStore.load();

            elementCb = new ComboBox();
            elementCb.setFieldLabel("element "+ChemicalPanel.nElements);
            elementCb.setDisplayField("element "+ChemicalPanel.nElements);
            elementCb.setStore(elementStore);
            elementCb.setTypeAhead(true);
            elementCb.setMode(ComboBox.LOCAL);
            elementCb.setTriggerAction(ComboBox.ALL);
            elementCb.setWidth(350);
            elementCb.setReadOnly(true);
            elementCb.setEmptyText("Select an element...");
            this.add(elementCb);

            massRatio = FieldUtil.getTextField("element-"+nElements, 300, "Mass percentage", false);
            this.add(massRatio);

            nElements++;
        }

        public String getElementName(){
            return elementCb.getValueAsString();
        }
        public Double getMassRatio() {
            double d = -1;
            try {
                d = Double.parseDouble(massRatio.getValueAsString());
            } catch (NumberFormatException e) {
                MessageBox.alert("Cannot parse mass ratio: please enter a numeric value");
            }
            return d;
        }
    }

    private ChemicalPanel(){
        super("Chemical Blend");

        Store ElementStore = new SimpleStore(new String[]{"property-type"}, new Object[][]{new String[]{"H - Hydrogen"},
                    new String[]{"He - Helium"},
                    new String[]{"Li - Lithium"},
                    new String[]{"Be - Beryllium"},
                    new String[]{"B - Boron"},
                    new String[]{"C - Carbon"},
                    new String[]{"N - Nitrogen"},
                    new String[]{"O - Oxygen"},
                    new String[]{"F - Fluorine"},
                    new String[]{"Ne - Neon"},
                    new String[]{"Na - Sodium"},
                    new String[]{"Mg - Magnesium"},
                    new String[]{"Al - Aluminium"},
                    new String[]{"Si - Silicon"},
                    new String[]{"P - Phosphorus"},
                    new String[]{"S - Sulfur"},
                    new String[]{"Cl - Chlorine"},
                    new String[]{"Ar - Argon"},
                    new String[]{"K - Potassium"},
                    new String[]{"Ca - Calcium"},
                    new String[]{"Sc - Scandium"},
                    new String[]{"Ti - Titanium"},
                    new String[]{"V - Vanadium"},
                    new String[]{"Cr - Chromium"},
                    new String[]{"Mn - Manganese"},
                    new String[]{"Fe - Iron"},
                    new String[]{"Co - Cobalt"},
                    new String[]{"Ni - Nickel"},
                    new String[]{"Cu - Copper"},
                    new String[]{"Zn - Zinc"},
                    new String[]{"Ga - Gallium"},
                    new String[]{"Ge - Germanium"},
                    new String[]{"As - Arsenic"},
                    new String[]{"Se - Selenium"},
                    new String[]{"Br - Bromine"},
                    new String[]{"Kr - Krypton"},
                    new String[]{"Rb - Rubidium"},
                    new String[]{"Sr - Strontium"},
                    new String[]{"Y - Yttrium"},
                    new String[]{"Zr - Zirconium"},
                    new String[]{"Nb - Niobium"},
                    new String[]{"Mo - Molybdenum"},
                    new String[]{"Tc - Technetium"},
                    new String[]{"Ru - Ruthenium"},
                    new String[]{"Rh - Rhodium"},
                    new String[]{"Pd - Palladium"},
                    new String[]{"Ag - Silver"},
                    new String[]{"Cd - Cadmium"},
                    new String[]{"In - Indium"},
                    new String[]{"Sn - Tin"},
                    new String[]{"Sb - Antimony"},
                    new String[]{"Te - Tellurium"},
                    new String[]{"I - Iodine"},
                    new String[]{"Xe - Xenon"},
                    new String[]{"Cs - Caesium"},
                    new String[]{"Ba - Barium"},
                    new String[]{"La - Lanthanum"},
                    new String[]{"Ce - Cerium"},
                    new String[]{"Pr - Praseodymium"},
                    new String[]{"Nd - Neodymium"},
                    new String[]{"Pm - Promethium"},
                    new String[]{"Sm - Samarium"},
                    new String[]{"Eu - Europium"},
                    new String[]{"Gd - Gadolinium"},
                    new String[]{"Tb - Terbium"},
                    new String[]{"Dy - Dysprosium"},
                    new String[]{"Ho - Holmium"},
                    new String[]{"Er - Erbium"},
                    new String[]{"Tm - Thulium"},
                    new String[]{"Yb - Ytterbium"},
                    new String[]{"Lu - Lutetium"},
                    new String[]{"Hf - Hafnium"},
                    new String[]{"Ta - Tantalum"},
                    new String[]{"W - Tungsten"},
                    new String[]{"Re - Rhenium"},
                    new String[]{"Os - Osmium"},
                    new String[]{"Ir - Iridium"},
                    new String[]{"Pt - Platinum"},
                    new String[]{"Au - Gold"},
                    new String[]{"Hg - Mercury"},
                    new String[]{"Tl - Thallium"},
                    new String[]{"Pb - Lead"},
                    new String[]{"Bi - Bismuth"},
                    new String[]{"Po - Polonium"},
                    new String[]{"At - Astatine"},
                    new String[]{"Rn - Radon"},
                    new String[]{"Fr - Francium"},
                    new String[]{"Ra - Radium"},
                    new String[]{"Ac - Actinium"},
                    new String[]{"Th - Thorium"},
                    new String[]{"Pa - Protactinium"},
                    new String[]{"U - Uranium"},
                    new String[]{"Np - Neptunium"},
                    new String[]{"Pu - Plutonium"},
                    new String[]{"Am - Americium"},
                    new String[]{"Cm - Curium"},
                    new String[]{"Bk - Berkelium"},
                    new String[]{"Cf - Californium"},
                    new String[]{"Es - Einsteinium"},
                    new String[]{"Fm - Fermium"},
                    new String[]{"Md - Mendelevium"},
                    new String[]{"No - Nobelium"},
                    new String[]{"Lr - Lawrencium"},
                    new String[]{"Rf - Rutherfordium"},
                    new String[]{"Db - Dubnium"},
                    new String[]{"Sg - Seaborgium"},
                    new String[]{"Bh - Bohrium"},
                    new String[]{"Hs - Hassium"},
                    new String[]{"Mt - Meitnerium"},
                    new String[]{"Ds - Darmstadtium"},
                    new String[]{"Rg - Roentgenium"},
                    new String[]{"Cn - Copernicium"},
                    new String[]{"Uut - Ununtrium"},
                    new String[]{"Uuq - Ununquadium"},
                    new String[]{"Uup - Ununpentium"},
                    new String[]{"Uuh - Ununhexium"},
                    new String[]{"Uus - Ununseptium"},
                    new String[]{"Uuo - Ununoctium"}
        });

       FieldSet blendFieldSet =  new FieldSet("Chemical blend");
       blendFieldSet.setWidth(400);
       TextField density = FieldUtil.getTextField("density", 300, "Density", false);
       blendFieldSet.add(density);

        Store phaseStore = new SimpleStore(new String[]{"phase"}, new Object[][]{new String[]{"Liquid"}, new String[]{"Solid"}, new String[]{"Gas"}});
        phaseStore.load();
        cb = new ComboBox();
        cb.setFieldLabel("phase");
        cb.setDisplayField("phase");
        cb.setStore(phaseStore);
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setWidth(350);
        cb.setReadOnly(true);
        cb.setEmptyText("Select a phase...");
        blendFieldSet.add(cb);

       this.add(blendFieldSet);

       FieldSet elementFieldSet = new FieldSet("Blend elements");



       this.add(elementFieldSet);

    }

    public PhysicalProperty getProperty() {
        return property;
    }

    public void setProperty(PhysicalProperty property) {
        this.property = property;
    }

    
}
