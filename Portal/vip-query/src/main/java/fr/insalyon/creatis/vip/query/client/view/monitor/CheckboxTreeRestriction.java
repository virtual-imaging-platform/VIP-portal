/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;
import com.smartgwt.client.widgets.calendar.*;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;

/**
 *
 * @author nouha
 */
public class CheckboxTreeRestriction extends AbstractFormLayout {

    VLayout layout;
    TextAreaItem body;
   

    public CheckboxTreeRestriction() {
         super("30%","100%");
        layout = new VLayout();
        layout.setMembersMargin(5);
        this.addMember(layout);
    }

    public void setForm() {
        String type = getQueryExplorerTb().getType();
        String name = getQueryExplorerTb().getName();
        Label label = WidgetUtil.getLabel("<b>" + name + "</b>",
                QueryConstants.ICON_EXPLORE, 15);
        label.setWidth100();
        label.setAlign(Alignment.LEFT);
        if (type == "int") {
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth(300);
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);


            IButton b0 = new IButton();
            b0.setWidth(50);
            b0.setHeight(50);
            b0.setTitle("OR");
            final String tile0 = b0.getTitle();
            b0.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile0);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile0);
                    }
                }
            });

            IButton b = new IButton();
            b.setWidth(50);
            b.setHeight(50);
            b.setTitle("AND");
            final String tile = b.getTitle();
            b.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile);
                    }


                }
            });

            IButton b1 = new IButton();
            b1.setWidth(50);
            b1.setHeight(50);
            b1.setTitle("<");
            final String tile1 = b1.getTitle();
            b1.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile1);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile1);
                    }


                }
            });
            
            IButton b2 = new IButton();
            b2.setWidth(50);
            b2.setHeight(50);
            b2.setTitle(">");
            final String tile2 = b2.getTitle();
            b2.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile2);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile2);
                    }
                }
            });


            IButton b3 = new IButton();
            b3.setWidth(50);
            b3.setHeight(50);
            b3.setTitle("=");
            final String tile3 = b3.getTitle();
            b3.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile3);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile3);
                    }


                }
            });

            IButton saveButton = WidgetUtil.getIButton("Add Restriction", CoreConstants.ICON_SAVE,
                    new ClickHandler() {
                ///nouha// String body_val=null;
                @Override
                public void onClick(ClickEvent event) {
                    //save condition
                  
                    getQueryExplorerTb().addRestriction(body.getValueAsString());
                    getQueryExplorerTb().refrechGrid();
                    

                }
            });
             
            IButton resetButton = WidgetUtil.getIButton("Reset", QueryConstants.ICON_LAUNCH,
                    new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    body.setValue("");
                }
            });

            hlayoutbuttons.addMembers(saveButton, resetButton);

            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);
            l.addMember(b0);
            l.addMember(b);
            l.addMember(b1);
            l.addMember(b2);
            l.addMember(b3);

            l.setMembersMargin(5);
            layout.addMember(label);
            layout.addMember(l);
            layout.addMember(FieldUtil.getForm(body));
            layout.addMember(hlayoutbuttons);


        } else if (type == "string") {
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth(300);
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);


            IButton b0 = new IButton();
            b0.setWidth(50);
            b0.setHeight(50);
            b0.setTitle("OR");
            final String tile0 = b0.getTitle();
            b0.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile0);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile0);
                    }


                }
            });

            IButton b = new IButton();
            b.setWidth(50);
            b.setHeight(50);
            b.setTitle("AND");
            final String tile = b.getTitle();
            b.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile);
                    }


                }
            });

            IButton b3 = new IButton();
            b3.setWidth(50);
            b3.setHeight(50);
            b3.setTitle("=");
            final String tile3 = b3.getTitle();
            b3.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile3);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile3);
                    }


                }
            });

            IButton saveButton = WidgetUtil.getIButton("Add Restriction", CoreConstants.ICON_SAVE,
                    new ClickHandler() {
                ///nouha// String body_val=null;
                @Override
                public void onClick(ClickEvent event) {
                    //save condition
                 
                    getQueryExplorerTb().addRestriction(body.getValueAsString());
                    getQueryExplorerTb().refrechGrid();
                    

                }
            });
            


            IButton resetButton = WidgetUtil.getIButton("Reset", QueryConstants.ICON_LAUNCH,
                    new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    body.setValue("");
                }
            });

            hlayoutbuttons.addMembers(saveButton, resetButton);

            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);
            l.addMember(b0);
            l.addMember(b);
            l.addMember(b3);

            l.setMembersMargin(5);
            layout.addMember(label);
            layout.addMember(l);

            layout.addMember(FieldUtil.getForm(body));
            layout.addMember(hlayoutbuttons);
        } else if (type == "nothing") {

            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);
        } else if (type == "date") {
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth(300);
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);

            IButton b0 = new IButton();
            b0.setWidth(50);
            b0.setHeight(50);
            b0.setTitle("OR");
            final String tile0 = b0.getTitle();
            b0.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile0);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile0);
                    }


                }
            });

            IButton b = new IButton();
            b.setWidth(50);
            b.setHeight(50);
            b.setTitle("AND");
            final String tile = b.getTitle();
            b.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile);
                    }


                }
            });

            IButton b1 = new IButton();
            b1.setWidth(50);
            b1.setHeight(50);
            b1.setTitle("<");
            final String tile1 = b1.getTitle();
            b1.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile1);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile1);
                    }


                }
            });



            IButton b2 = new IButton();
            b2.setWidth(50);
            b2.setHeight(50);
            b2.setTitle(">");
            final String tile2 = b2.getTitle();
            b2.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile2);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile2);
                    }
                }
            });


            IButton b3 = new IButton();
            b3.setWidth(50);
            b3.setHeight(50);
            b3.setTitle("=");
            final String tile3 = b3.getTitle();
            b3.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile3);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile3);
                    }


                }
            });

            IButton saveButton = WidgetUtil.getIButton("Add Restriction", CoreConstants.ICON_SAVE,
                    new ClickHandler() {
                ///nouha// String body_val=null;
                @Override
                public void onClick(ClickEvent event) {
                    //save condition
                  
                    getQueryExplorerTb().addRestriction(body.getValueAsString());
                    getQueryExplorerTb().refrechGrid();
                    

                }
            });
             



            IButton resetButton = WidgetUtil.getIButton("Reset", QueryConstants.ICON_LAUNCH,
                    new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    body.setValue("");
                }
            });

            hlayoutbuttons.addMembers(saveButton, resetButton);
            
            DynamicForm df=new DynamicForm();
            //df.setNumCols(1);
            df.setWidth100();
            DateItem date = new DateItem();  
            date.setName("Date");  
            date.setShowTitle(false);
            date.setUseTextField(false);
            date.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
                             if (body.getValueAsString() == null) {
                        body.setValue("" + event.getValue().toString());
                    } else {
                        body.setValue(body.getValueAsString() + " " + event.getValue().toString());
                    }
			
                      
			}
		});

            df.setFields(date);

            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);
            
            l.addMember(b0);
            l.addMember(b);
            l.addMember(b1);
            l.addMember(b2);
            l.addMember(b3);

            l.setMembersMargin(5);



            layout.addMember(label);

            layout.addMember(l);
            layout.addMember(df);

            layout.addMember(FieldUtil.getForm(body));
            layout.addMember(hlayoutbuttons);
 
        }
        else{
            
            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);
        }
        


    }

    public QueryExplorerTab getQueryExplorerTb() {

        QueryExplorerTab queryExplorerTab = (QueryExplorerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYEXPLORER);
        return queryExplorerTab;
    }
}
