/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.DateDisplayFormatter;
import com.smartgwt.client.util.DateUtil;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boujelben
 */
    
  public class CreateQuery  extends AbstractFormLayout {

   

   
   private TextItem querynameField;
    //pour la description
    private RichTextEditor description;
    private IButton saveButton;
    private IButton removeButton;
    private TextItem body;
  

    public CreateQuery() {

        super(280, 400);
        addTitle("New Query", QueryConstants.ICON_QUERYMAKER);

        configure();
        
    }

    private void configure() {

        querynameField = FieldUtil.getTextItem(450, null);
     
        description = new RichTextEditor();
        description.setHeight(100);
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        
        body=FieldUtil.getTextItem(450, null);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                try {
                  Query q= new Query(description.getValue(), querynameField.getValueAsString(),"admin@vip.creatis.insa-lyon.fr");
                   
                  save(q);
                  //je doit crrer un record query
                   
                  savev(new QueryVersion("36",body.getValueAsString()),q);
                   
   
                } catch (QueryException ex) {
                    Logger.getLogger(CreateQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                        }});

       

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                 
                        
                    }
                });
        removeButton.setDisabled(true);
        

        addField("Name", querynameField);
        this.addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        this.addMember(description);
        
        addField("Body", body);
        
        addButtons(saveButton, removeButton);
        
    }
private void save(Query query) throws QueryException
  {
    QueryService.Util.getInstance().add(query,getCallback("hhh"));
     
   }

    

private void savev(QueryVersion version, Query query) throws QueryException
{
    
     QueryService.Util.getInstance().addVersion(version,query,getCallback("version"));
     
}
    

     private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
               // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to save " + text + " version:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                
               // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
               // setApplication(null, null, null);
               // ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                       // getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                //tab.loadApplications();
            }
        };
    }
    
   
    
    
    
}
