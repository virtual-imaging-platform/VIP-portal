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
package fr.insalyon.creatis.vip.application.client.view.common;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class AbstractSourceLayout extends VLayout {

    protected String name;
    protected Label sourceLabel;
    protected Label sourceName;
    protected Label sourceComment;
    protected HLayout sourceLabelHLayout; // 
    protected HLayout sourceCommentHLayout;
    protected HLayout sourceFieldHLayout;
    protected HLayout fieldHLayout;
    
    protected HLayout flagCbHLayout; // Layout in which a checkbox (flag input) can be add. This layout is on the left of the sourceLabelHLayout.
    protected LayoutSpacer sourceCommentLayoutSpacer; // Empty layout on the left of the sourceCommentHLayout, to horizontaly align it with sourceLabelHLayout and sourceFieldHLayout.
    protected boolean optional;

    /**
     * Currently GateLabSourceLayout object use it.
     */
    public AbstractSourceLayout(String name, String comment, boolean optional) {
        this.name = name;
        this.optional = optional;
        String labelText = "<b>" + name;
        if(!optional)
            labelText += "<font color=\"red\">*</font>";
        labelText += "</b>";
        this.sourceLabel = WidgetUtil.getLabel(labelText, 15);
        this.sourceLabel.setWidth(300);
        if (comment != null) {
            this.sourceLabel.setTooltip(comment);
            this.sourceLabel.setHoverWidth(450);
        }
        this.setAutoWidth();
        this.addMember(sourceLabel);
        
        this.sourceFieldHLayout = new HLayout(3);
        this.sourceFieldHLayout.setAutoWidth();
        this.addMember(sourceFieldHLayout);   
    }
        
    public AbstractSourceLayout(String name, String comment) {
        this(name,comment,false);
    }
    
     /**
     * Currently this constructor is called by InputFlagLayout and InputFlagLayout objects. 
     * It allows to display an input name which value is contained in prettyName variable and not in name variable.
     */
    public AbstractSourceLayout(String name, String comment, boolean optional, String prettyName, String defaultValue) {
        flagCbHLayout = new HLayout(0);
        flagCbHLayout.setWidth(25);
        flagCbHLayout.setMaxWidth(25);
        flagCbHLayout.setHeight(15);
        flagCbHLayout.setPadding(0);
        flagCbHLayout.setMargin(0);
       
        sourceLabelHLayout = new HLayout(0); 
        sourceLabelHLayout.addMember(flagCbHLayout);

        this.name = name;
        this.optional = optional;
        String labelText;
        if (prettyName != null && !prettyName.isEmpty()) {
            labelText = "<b>" + prettyName;
        }
        else {
            labelText = "<b>" + name;
        }
        
        if (!defaultValue.isEmpty()) { // Add the flag value (command-line-flag in json descriptor).
            labelText += " ("+ "<font color=\"blue\">" + defaultValue + "</font>" + ")";
        }
        if(!optional) {
            labelText += "<font color=\"red\">*</font>";
        }
        labelText += "</b>";
        sourceLabel = WidgetUtil.getLabel(labelText, 15);
        sourceLabel.setWidth(300);
        setAutoWidth();
        sourceLabelHLayout.addMember(sourceLabel);
        addMember(sourceLabelHLayout);

        if (comment != null && !comment.isEmpty()) {
            sourceCommentLayoutSpacer = new LayoutSpacer();
            sourceCommentLayoutSpacer.setWidth(25);
            sourceCommentLayoutSpacer.setMaxWidth(25);
            sourceCommentHLayout = new HLayout(0); 
            sourceCommentHLayout.setWidth(460);
            sourceCommentHLayout.addMember(sourceCommentLayoutSpacer);
            sourceComment = WidgetUtil.getLabel(comment,15);
            sourceComment.setWidth(460);
            sourceCommentHLayout.addMember(sourceComment);
            addMember(sourceCommentHLayout);
        }

        sourceFieldHLayout = new HLayout(0); 
        sourceFieldHLayout.setAutoWidth();
        addMember(sourceFieldHLayout);       
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }
    
    public abstract String getValue();
    
    public abstract void setValue(String value);
    
    public abstract boolean validate();
}
