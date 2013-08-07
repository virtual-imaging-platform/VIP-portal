/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.smartgwt.client.types.Alignment;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;

import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
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
import fr.insalyon.creatis.vip.query.client.view.QueryTitleGrid;
import java.io.UnsupportedEncodingException;
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
public class CreateQuery extends AbstractFormLayout {

    private boolean newQuery = true;
    private TextItem querynameField;
    private RichTextEditor description;
    private IButton saveButton;
    private IButton testButton;
    private ImgButton helpButton;
    private TextAreaItem body;
    private int rownumber = 0;
    private Long queryID = 0l;
    private boolean testt;  

    // messageItem.setShowTitle(false);
    // messageItem.setLength(5000);
    //  messageItem.setColSpan(2);
    //messageItem.setWidth("*");
    //  messageItem.setHeight("*");
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
        body = new TextAreaItem();
        

        body.setHeight(125);
        body.setWidth(1410);
        body.setDisabled(false);


        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (newQuery) {
                    try {

                        Query q = new Query(description.getValue(), querynameField.getValueAsString());
                        save(q);


                    } catch (QueryException ex) {
                        Logger.getLogger(CreateQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                   //String bodyOnSelect = getQueryMakerTb().getBody();
                    //bodyOnSelect=bodyOnSelect.replaceAll("\\s","");
                   // bodyOnSelect=bodyOnSelect.toLowerCase();
                    String getbody = body.getValueAsString();
                    //
                    getbody = getbody.replaceAll("[\r\n]{2,}", "\r\n");
                    getbody=getbody.replaceAll("\\s","");
                    getbody=getbody.toLowerCase();
                    String bodyy=new String(getbody);
                    String queryID=getQueryMakerTb().getQueryID();
                    Long qID=Long.parseLong(queryID);
                    final AsyncCallback<Boolean> callbackk = new AsyncCallback<Boolean>() {
                     @Override
                     public void onFailure(Throwable caught) {

                     Layout.getInstance().setWarningMessage("Unable to get bodies " + caught.getMessage());
                     }

                       @Override
                   public void onSuccess(Boolean result) {
                       testt=result.booleanValue();
                       
                       if (testt==true) {
                        update(getVersionID(), querynameField.getValueAsString(), description.getValue());

                    } else {

                        Long queryVersionID = getVersionID();
                        getQueryID(queryVersionID);
                        update(queryVersionID, querynameField.getValueAsString(), description.getValue());

                    }
                 
                   
            }
            };
                    
                      QueryService.Util.getInstance().getBodies(qID, bodyy, callbackk);
                   
                    


                }

            }
        });


        testButton = WidgetUtil.getIButton("Test", CoreConstants.ICON_USER_INFO,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
         

        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());
                
            }

            @Override
            public void onSuccess(String result) {

              
             //_self empeche popup mais ouvre in the cuurent window
             //Autoriser les fenetre pop-up pour ce site(vip.creatis...)
            
            com.google.gwt.user.client.Window.open(result, "_blank","");
               


                


            }
        };

        EndPointSparqlService.Util.getInstance().getUrlResult(body.getValueAsString(),"csv", callback);



    
                /*final Window winModal = new Window();
                winModal.setWidth100();
                winModal.setHeight100();
                winModal.setTitle("Test");
                winModal.setShowMinimizeButton(false);
                winModal.setIsModal(true);
                winModal.setShowModalMask(true);
                winModal.centerInPage();
                winModal.show();

                winModal.addCloseClickHandler(new CloseClickHandler() {
                    public void onCloseClick(CloseClickEvent event) {
                        winModal.destroy();
                    }
                });
                */

            }
        });




        helpButton = FieldUtil.getImgButton(QueryConstants.ICON_HELP, "How to create a query",
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
                textbox.setContents("<b>To create query you have to put parameters of query into «[]»"
                        + "and name;type;description;examples separed by «;»"
                        + "example of parameter name:[name; String; name of patient; Olivier]</b>");
                 
                winModal.addItem(textbox);
                winModal.show();
            }
        });
        helpButton.setLayoutAlign(Alignment.LEFT);


        addField("Name", querynameField);
        this.addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        this.addMember(description);

        body.setTitleOrientation(TitleOrientation.TOP);
        body.setTextAlign(Alignment.LEFT);
        body.setShowTitle(false);


        addField("Body", body);
        this.addMember(helpButton);
        addButtons(saveButton,testButton);



    }

    private void save(Query query) throws QueryException {

        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get list of queries:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                String bodyRemplace = body.getValueAsString();
                 bodyRemplace = bodyRemplace.trim();
                //bodyd=bodyd.replaceAll("\\s{2,}", " ");
                bodyRemplace=bodyRemplace.replaceAll("[\r\n]{2,}", "\r\n");;
                savev(new QueryVersion(1L, result,  bodyRemplace));
                reset();


            }
        };

        QueryService.Util.getInstance().add(query, callback);
    }

    private void savev(QueryVersion version) {
        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to save query:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);


                Parameter p = new Parameter(result);
                saveParameter(p);

            }
        };
        QueryService.Util.getInstance().addVersion(version, callback);

    }

    private void saveParameter(Parameter param) {
        final AsyncCallback<List<Long>> callback = new AsyncCallback<List<Long>>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to save parameters " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Long> result) {
            }
        };
        QueryService.Util.getInstance().addParameter(param, callback);

    }
      
          
       
        
      
        
        
       

    public void setQuery(boolean nameState, boolean test, String name, String description, String body) {

        newQuery = test;
        querynameField.setValue(name);
        this.description.setValue(description);
        this.body.setValue(body);
        // this.querynameField.setDisabled(nameState);
    }

    public Long getVersionID() {


        String version = getQueryMakerTb().getVersionID();
        Long versionID = Long.parseLong(version);
        return versionID;
    }

    public void update(Long queryVersionID, String name, String descriptionn) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to update query " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {

                getQueryMakerTb().loadData();

                Layout.getInstance().setNoticeMessage("The queryVersion was successfully updated");

            }
        };
        QueryService.Util.getInstance().updateQueryVersion(queryVersionID, name, descriptionn, callback);

    }

    public void maxVersion(final long queryID) {


        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to update query " + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {


                Long nn = result;

                nn = nn + 1;
                String bodyRemplace = body.getValueAsString();
                String bodyd = bodyRemplace.replaceAll("[\r\n]{2,}", "\r\n");
               

                savev(new QueryVersion(nn, queryID, bodyd));
                reset();

            }
        };

        QueryService.Util.getInstance().maxVersion(queryID, callback);
        //return rownumber;

    }

    public QueryMakerTab getQueryMakerTb() {

        QueryMakerTab queryTab = (QueryMakerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYMAKER);
        return queryTab;
    }

    public void getQueryID(Long queryVersionID) {

       
        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get queryID " + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {


                maxVersion(result);
            }
        };

        QueryService.Util.getInstance().getQueryID(queryVersionID, callback);
        

    }

    public void reset() {
        querynameField.setValue("");
        description.setValue("");
        body.setValue("");
        getQueryMakerTb().loadData();
        WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
        Layout.getInstance().setNoticeMessage("The query was successfully added");
    }
}
