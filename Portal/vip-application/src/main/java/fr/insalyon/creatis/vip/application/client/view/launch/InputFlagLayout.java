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

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSourceLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;  
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler; 
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

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
        super(name, comment,optional, prettyName, defaultValue);

        cbFlagInputItem = new CheckboxItem("");
        cbFlagInputItem.setValue(false);
        cbFlagInputItem.setValueField(defaultValue);
        cbFlagInputItem.setShowLabel(false);
        cbFlagInputItem.setShowTitle(false);
        cbFlagInputItem.setWidth(0);
        cbFlagInputItem.setHeight(0);
        
        cbFlagInputItem.addChangeHandler(new ChangeHandler() {  
            
                    @Override
                    public void onChange(ChangeEvent event) {  
                        boolean selected = cbFlagInputItem.getValueAsBoolean();  
                        cbFlagInputItem.setValue(!selected);  
                    }  
                });
         
//        Label  ll = (Label) rightVLayout.getMember(0);
//     Label  ll = (Label) rightVLayout.getMemberNumber(comment);

        leftVLayout.addMember(FieldUtil.getForm(cbFlagInputItem));
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

//    private VLayout labelAndCommentVLayout;
//    protected Label sourceComment;
//    protected Label infoHLayoutLabel;




//        hLayout = new HLayout(3);
////                        hLayout.setBorder("4px solid blue");
//        hLayout.setAutoWidth();
        // Flag checkbox

//        hLayout.addMember(FieldUtil.getForm(cbFlagInputItem));




       // VLayout for the flag name, value and comment
//        labelAndCommentVLayout = new VLayout();
//        
//        String labelText = "<b>" + prettyName + " ("+ "<font color=\"blue\">" + defaultValue + "</font>" + ")";
//        if(!optional)
//            labelText += "<font color=\"red\">*</font>";
//        labelText += "</b>";
//        sourceLabel = WidgetUtil.getLabel(labelText, 15);
//        sourceLabel.setWidth(500);
////                    sourceLabel.setBorder("2px solid aqua");
//        labelAndCommentVLayout.addMember(sourceLabel);
//        
//        String commentText = comment;
//        sourceComment = WidgetUtil.getLabel(commentText, 15);
//        sourceComment.setWidth(500);
////        sourceComment.setBorder("2px solid aqua");
//        labelAndCommentVLayout.addMember(sourceComment);
//        
//        hLayout.setAutoWidth();
//        hLayout.addMember(labelAndCommentVLayout);

//        addMember(hLayout);




        //hLayout.setAutoWidth();
//        hLayout.addMember(labelAndCommentVLayout);
//        addMember(hLayout);

//        hLayout.getMember(0).getLeftAsString()
        
//        String infoHLayout = "getCellHeight() :  " + String.valueOf(cbFlagInputItem.getCellHeight()) 
//                + "        getHeight() : " + String.valueOf(cbFlagInputItem.getHeight()) + "      getWidth() :  " + String.valueOf(cbFlagInputItem.getWidth()) 
//                + "     getVisibleHeight : " + String.valueOf(cbFlagInputItem.getVisibleHeight()) +  "     getVisibleWidth() : " + String.valueOf(cbFlagInputItem.getVisibleWidth())
//                +  "     getPageLeft : " + String.valueOf(cbFlagInputItem.getPageLeft()) +  "     getPageTop : " + String.valueOf(cbFlagInputItem.getPageTop());
        
//        String infoHLayout = "getAbsoluteLeft() :  " + String.valueOf(hLayout.getAbsoluteLeft()) + "        getLayoutLeftMargin() : " + String.valueOf(hLayout.getLayoutLeftMargin()) + "  getMember(0).getMargin() : " + hLayout.getMember(0).getMargin() 
//                + "    getLeftAsString() : " + hLayout.getMember(0).getLeftAsString() + "    sourceLabel.getMargin() : " + String.valueOf(sourceLabel.getMargin()) ;
//        + "    getAbsoluteTop :  " + String.valueOf(hLayout.getAbsoluteTop()) + "    getHPolicy : " + hLayout.getHPolicy().toString() ;
               // + "       getLayoutLeftMargin : " + hLayout.getLayoutLeftMargin().toString()
               //+ "        getLayoutTopMargin : " +  hLayout.getLayoutTopMargin().toString() + "     getVPolicy().getValue() : " +  hLayout.getVPolicy().getValue()  ;
               
               //ko + "       getLayoutLeftMargin : " + hLayout.getLayoutLeftMargin().toString();
//        infoHLayoutLabel = WidgetUtil.getLabel(infoHLayout, 15);
//        infoHLayoutLabel.setWidth(500);
//        infoHLayoutLabel.setBorder("2px solid aqua");
//                labelAndCommentVLayout.addMember(infoHLayoutLabel);

//        labelAndCommentVLayout.addMember(infoHLayout);
