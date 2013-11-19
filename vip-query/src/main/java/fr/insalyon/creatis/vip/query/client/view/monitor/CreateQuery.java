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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.client.view.QueryTitleGrid;
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
    public TextAreaItem body;
    private int rownumber = 0;
    private Long queryID = 0l;
    private boolean testt;

    public CreateQuery() {
        super("100%","100%");
        addTitle("New Query", QueryConstants.ICON_QUERYMAKER);
        configure();
    }

    private void configure() {


        description = new RichTextEditor();
        description.setHeight(100);
        description.setWidth100();
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        
        querynameField = FieldUtil.getTextItem(900, false, "", "[0-9.,A-Za-z-+/_() ]"); 
        querynameField.setWidth("*");
        querynameField.setValidators(ValidatorUtil.getStringValidator());

        body = new TextAreaItem();
        body.setHeight(130);
        body.setWidth(900);
        body.setDisabled(false);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (newQuery) {
                    try {

                        Query q = new Query(querynameField.getValueAsString().trim());
                        save(q);

                    } catch (QueryException ex) {
                        Logger.getLogger(CreateQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                   
                    String getbody = body.getValueAsString();                 
                    getbody = getbody.replaceAll("[\r\n]{2,}", "\r\n");
                    getbody = getbody.replaceAll("\\s", "");
                    getbody = getbody.toLowerCase();
                    String bodyy = new String(getbody);
                    String queryID = getQueryMakerTb().getQueryID();
                    Long qID = Long.parseLong(queryID);
                    final AsyncCallback<Boolean> callbackk = new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to get bodies " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            testt = result.booleanValue();

                            if (testt == true) {
                                update(getVersionID(), querynameField.getValueAsString().trim(), description.getValue());

                            } else {

                                Long queryVersionID = getVersionID();
                                getQueryID(queryVersionID);
                                update(queryVersionID, querynameField.getValueAsString().trim(), description.getValue());
                            }


                        }
                    };

                    QueryService.Util.getInstance().getBodies(qID, bodyy, callbackk);
                }

            }
        });


        testButton = WidgetUtil.getIButton("Test", CoreConstants.ICON_USER_INFO,
                new ClickHandler() {
            ///nouha// String body_val=null;
            @Override
            public void onClick(ClickEvent event) {
                String body_val = body.getValueAsString();
                if (body_val.isEmpty() || body_val == null || body_val.length() == 0 || body_val.equals("null")) {
                    Layout.getInstance().setWarningMessage("there is no Query to test");
                } else {

                    if (body_val.indexOf("[") != -1) {

                        int c = 0;
                        int nn = 0;
                        String s = null;
                        String sequence = null;
                        for (int i = 0; i < body_val.length(); i++) {
                            char b = body_val.charAt(i);
                            if (b == '[') {
                                int kk = 0;
                                for (int j = i + 1; j < body_val.length(); j++) {
                                    char last = body_val.charAt(j);

                                    //substring j+1 non inclus
                                    if (last == ']' && kk == 0) {
                                        kk = 1;
                                        c = j + 1;
                                        sequence = body_val.substring(i + 1, j);

                                        String str[] = sequence.split("\\;");
                                        String example = str[3];
                                        body_val = body_val.replaceFirst("\\[" + sequence + "\\]", example);
                                    }
                                }

                            }
                        }//end for

                    }



                    final AsyncCallback<String> callback;
                    callback = new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {

                            Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());

                        }

                        @Override
                        public void onSuccess(String result) {
                            //_self empeche popup mais ouvre in the cuurent window
                            //Autoriser les fenetre pop-up pour ce site(vip.creatis...)
                            com.google.gwt.user.client.Window.open(result, "_blank", "");

                        }
                    };
                    EndPointSparqlService.Util.getInstance().getUrlResult(body_val, "csv", callback);
                }






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


        addField100("Name", querynameField);
        
        
       
  
        
        addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        addMember(description);

        body.setTitleOrientation(TitleOrientation.TOP);
        body.setTextAlign(Alignment.LEFT);
        body.setShowTitle(false);
        body.setWidth("*");


        addField100("Body", body);
        addMember(helpButton);
        addButtons(saveButton, testButton);



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
                bodyRemplace = bodyRemplace.replaceAll("[\r\n]{2,}", "\r\n");
                // String des=description.getValue().replaceFirst("<br>","");




                //queryversiob,queryid
                String desc = description.getValue().trim();
                desc = desc.replaceAll("<br><br>", "<br>");
                desc = desc.replaceAll("<div><br><div>", "");
                while (desc.indexOf("<br>") == 0) {

                    desc = desc.replaceFirst("<br>", "");
                }
                 
                while(desc.lastIndexOf("<br>")==desc.length()-4){ 
               desc = desc.substring(0,desc.length()-4);
                       }

                savev(new QueryVersion(1L, result, desc, bodyRemplace));
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



                //queryversiob,queryid
                String desc = description.getValue().trim();
                desc = desc.replaceAll("<br><br>", "<br>");
                desc = desc.replaceAll("<div><br><div>", "");
                while (desc.indexOf("<br>") == 0) {

                    desc = desc.replaceFirst("<br>", "");
                }
                while(desc.lastIndexOf("<br>")==desc.length()-4){ 
               desc = desc.substring(0,desc.length()-4);
                       }




                savev(new QueryVersion(nn, queryID, desc, bodyd));
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
