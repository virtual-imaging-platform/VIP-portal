/*
Copyright 2009-2015

CREATIS
CNRS UMR 5220 -- INSERM U1044 -- Université Lyon 1 -- INSA Lyon

Authors

Nouha Boujelben (nouha.boujelben@creatis.insa-lyon.fr)
Frédéric Cervenansky (frederic.cervnansky@creatis.insa-lyon.fr)
Rafael Ferreira da Silva (rafael.silva@creatis.insa-lyon.fr)
Tristan Glatard (tristan.glatard@creatis.insa-lyon.fr)
Ibrahim  Kallel (ibrahim.kallel@creatis.insa-lyon.fr)
Kévin Moulin (kevmoulin@wanadoo.fr)
Sorina Pop (sorina.pop@creatis.insa-lyon.fr)

This software is a web portal for pipeline execution on distributed systems.

This software is governed by the CeCILL-B license under French law and
abiding by the rules of distribution of free software.  You can use,
modify and/ or redistribute the software under the terms of the
CeCILL-B license as circulated by CEA, CNRS and INRIA at the following
URL "http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided
only with a limited warranty and the software's author, the holder of
the economic rights, and the successive licensors have only limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading, using, modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean that it is complicated to manipulate, and that also
therefore means that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards
their requirements in conditions enabling the security of their
systems and/or data to be ensured and, more generally, to use and
operate it in the same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSourceLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;  
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler; 

/**
 *
 * @author pgirard
 * 
 * Class for display an flag type input as a checkbox following by the flag value (command-line-flag in json descriptor). 
 * With this checkbox the user can add or not the flag value on the command line.
 */
public class InputFlagLayout extends AbstractSourceLayout{
    
    private CheckboxItem cbFlagInputItem ;
            
    public InputFlagLayout(String name, String comment, boolean optional, String defaultValue, String vipTypeRestriction, String prettyName) {
        super(name, comment, optional, prettyName);
        cbFlagInputItem = new CheckboxItem(defaultValue);
        cbFlagInputItem.setValue(false);
        cbFlagInputItem.setValueField(defaultValue);
        cbFlagInputItem.addChangeHandler(new ChangeHandler() {  
            
            @Override
            public void onChange(ChangeEvent event) {  
                boolean selected = cbFlagInputItem.getValueAsBoolean();  
                cbFlagInputItem.setValue(!selected);  
            }  
        });
         
        hLayout.addMember(FieldUtil.getForm(cbFlagInputItem));
    }
    
    @Override
    public String getValue() {
        return cbFlagInputItem.getValueAsBoolean().toString();
    }
    
    @Override
    public void setValue(String value) {
        if (value.equals("true")) {
            cbFlagInputItem.setValue(true);
        }
        else {
            cbFlagInputItem.setValue(false);
        }
    }
    
    @Override
    public boolean validate() {
        return cbFlagInputItem.validate();
    }
    
}
