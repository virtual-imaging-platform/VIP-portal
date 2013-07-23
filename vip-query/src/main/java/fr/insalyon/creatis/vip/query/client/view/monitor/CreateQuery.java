/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.DateDisplayFormatter;
import com.smartgwt.client.util.DateUtil;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.view.QueryRecord;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.StringTokenizer;
import java.lang.*;


/**
 *
 * @author Boujelben
 */
    
  public class CreateQuery  extends AbstractFormLayout {

   

    private boolean newQuery;
    private TextItem querynameField;
    private RichTextEditor description;
    private IButton saveButton;
    private ImgButton helpButton;
    private TextItem body;
   
   

    
    public CreateQuery() {
        
    
        super(1410, 280);
        addTitle("New Query", QueryConstants.ICON_QUERYMAKER);

        configure();
     
        
    }

    private void configure() {

        querynameField = FieldUtil.getTextItem(1400, null);
      
      
        description = new RichTextEditor();
        description.setHeight(100);
        description.setWidth100();
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
         body = new TextItem();
        
         body.setHeight(125);
         body.setWidth(1410);
     

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                    if (newQuery) {   
                try {
                  Query q= new Query(description.getValue(), querynameField.getValueAsString()); 
                  save(q);
                  new QueryLayout().loadData();
                  
                } catch (QueryException ex) {
                    Logger.getLogger(CreateQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                        }
                    else{
                        //update
                        
                    };
                    
                }
                }
                );
                

             
   
       helpButton= FieldUtil.getImgButton(QueryConstants.ICON_HELP,"How to create a query",
               new ClickHandler() {
                  @Override
                public void onClick(ClickEvent event) {
                final Window winModal = new Window();  
                winModal.setWidth(360);  
                winModal.setHeight(175);  
                winModal.setTitle("Help");  
                winModal.setShowMinimizeButton(false);  
                winModal.setIsModal(true);  
                winModal.setShowModalMask(true);  
                winModal.centerInPage();
               
                winModal.addCloseClickHandler(new CloseClickHandler() {  
                    public void onCloseClick(CloseClickEvent event) {  
                  winModal.destroy();  
                    }  
                });
               final Label textbox = new Label();  
        textbox.setID("textbox");  
        textbox.setAlign(Alignment.CENTER);  
        textbox.setShowEdges(true);  
        textbox.setPadding(5);  
        textbox.setLeft(50);  
        textbox.setTop(50);  
        textbox.setWidth100();
        textbox.setHeight100();
        textbox.setContents("To create query you have to put parameters of query into «[]»"
                + "and name;type;description;examples separed by «;»"
                + "example of parameter name:[name; String; name of patient; Olivier]");  
       // textbox.setVisibility(Visibility.HIDDEN); 
        winModal.addItem(textbox);
        winModal.show();
                        }});
         helpButton.setLayoutAlign(Alignment.LEFT);
        

        addField("Name", querynameField);
        this.addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        this.addMember(description);
      
       body.setTitleOrientation(TitleOrientation.TOP);
       body.setTextAlign(Alignment.LEFT);
       body.setShowTitle(false);
       
    
       addField("Body", body);
       this.addMember(helpButton);
       addButtons(saveButton);
       
        if(body.getValueField()==null&& querynameField.getValueField()==null ){
           newQuery=true;
          }
        
    }
    
    
  private void save(Query query) throws QueryException
  {
      
      final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to get list of queries:<br />" + caught.getMessage());
            }
 
            @Override
            public void onSuccess(Long result) {
                
                   
              savev(new QueryVersion("v. "+result,result,body.getValueAsString()));
                    
             
               QueryMakerTab queryTab = (QueryMakerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYMAKER);
                queryTab.loadData();
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
             
             
              
             
        }
        };
      
     QueryService.Util.getInstance().add(query,callback);    
   }

   

    
  
  
  
    private void savev(QueryVersion version) {
        QueryService.Util.getInstance().addVersion(version,getCallback("version"));
 
    }
    
     private void saveParameter(Parameter param)  {
         final AsyncCallback<List<Long>> callback = new AsyncCallback<List<Long>>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to save parameters " + caught.getMessage());
            }
 
            @Override
            public void onSuccess(List<Long>result) {
                
               
        }
        };
        QueryService.Util.getInstance().addParameter(param, callback);
 
    }

     private AsyncCallback<Long> getCallback(final String text) {

        return new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
               // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to save " + text + ":<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                if(text.equals("version")){
       
                Parameter p=new Parameter(result);
                saveParameter(p);
                querynameField.setValue("");
                description.setValue("");
                body.setValue("");
                //body.setDefaultValue("[name: ][type: ][descripition ][examples]");
                }
               
                
               // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
               // setApplication(null, null, null);
               // ManageApplicationsTab tab = (ManageApplicationsTab) Layout.getInstance().
                       // getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                //tab.loadApplications();
            }
        };
    }
    
           public void setQuery(boolean test, String name, String description, String body) {  
               
              newQuery=test;
              querynameField.setValue(name);
              this.description.setValue(description);
              this.body.setValue(body);  
           }    
  }


 
