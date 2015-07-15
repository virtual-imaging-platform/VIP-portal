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
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.calendar.*;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
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
import java.util.Date;

/**
 *
 * @author nouha
 */
public class CheckboxTreeRestriction extends AbstractFormLayout {

  
    TextAreaItem body;

    public CheckboxTreeRestriction() {
        super("100%","100%");
    }

    public void setForm(String restriction) {
        String type = getQueryExplorerTb().getType();
        String name = getQueryExplorerTb().getName();
        Label label = WidgetUtil.getLabel("<b>" + name + "</b>",
                QueryConstants.ICON_EXPLORE, 15);
        label.setWidth100();
        label.setAlign(Alignment.LEFT);
      

       if (type == "http://www.w3.org/2001/XMLSchema#string") {
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth("*");
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);
            body.setValue(restriction);


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

            IButton b4 = new IButton();
            b4.setWidth(50);
            b4.setHeight(50);
            b4.setTitle("!=");
            final String tile4 = b4.getTitle();
            b4.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile4);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile4);
                    }


                }
            });



            IButton b5 = new IButton();
            b5.setWidth(50);
            b5.setHeight(50);
            b5.setTitle("regex");

            b5.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + "regex:value");
                    } else {
                        body.setValue(body.getValueAsString() + " " + "regex:value");
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

            Canvas[] children = getChildren();
            removeMembers(children);
            l.addMember(b0);
            l.addMember(b);
            l.addMember(b3);
            l.addMember(b4);
            l.addMember(b5);

            l.setMembersMargin(5);
            this.addMember(label);
            this.addMember(l);

            this.addMember(FieldUtil.getFormOneColumn(body));
            this.addMember(hlayoutbuttons);
       /** } else if (type == "nothing") {

            Canvas[] children = layout.getChildren();
            layout.removeMembers(children);**/
        } else if (type == "http://www.w3.org/2001/XMLSchema#dateTime") {
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth("*");
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);
            body.setValue(restriction);

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


            IButton b4 = new IButton();
            b4.setWidth(50);
            b4.setHeight(50);
            b4.setTitle("!=");
            final String tile4 = b4.getTitle();
            b4.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile4);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile4);
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

            DynamicForm df = new DynamicForm();
            df.setWidth100();
            DateItem date = new DateItem();
            date.setName("Date");
            date.setShowTitle(false);
            date.setUseTextField(true);

            date.setEditorValueFormatter(new FormItemValueFormatter() {
                public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {

                    if (value != null && value instanceof Date) {
                        return "\"" + DateTimeFormat.getFormat("yyyy-MM-dd").format((Date) value) + "\"" + "^^xsd:date";
                    } else {
                        return null;
                    }

                }
            });


            date.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + event.getValue());

                    } else {
                        body.setValue(body.getValueAsString() + " " + event.getValue());
                    }
                }
            });

            df.setFields(date);

            Canvas[] children = this.getChildren();
            this.removeMembers(children);

            l.addMember(b0);
            l.addMember(b);
            l.addMember(b1);
            l.addMember(b2);
            l.addMember(b3);
            l.addMember(b4);
            l.setMembersMargin(5);
            addMember(label);
            addMember(l);
            addMember(df);
            addMember(FieldUtil.getFormOneColumn(body));
            addMember(hlayoutbuttons);

      
       /* }else  if (type == "int") {**/
        }else{
            HLayout l = new HLayout();
            l.setWidth100();
            l.setHeight(60);
            HLayout hlayoutbuttons = new HLayout();
            hlayoutbuttons.setMembersMargin(5);

            body = new TextAreaItem();
            body.setWidth("*");
            body.setHeight(150);
            body.setTitleOrientation(TitleOrientation.TOP);
            body.setTextAlign(Alignment.LEFT);
            body.setShowTitle(false);
            body.setValue(restriction);


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


            IButton b4 = new IButton();
            b4.setWidth(50);
            b4.setHeight(50);
            b4.setTitle("!=");
            final String tile4 = b4.getTitle();
            b4.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (body.getValueAsString() == null) {
                        body.setValue("" + tile4);
                    } else {
                        body.setValue(body.getValueAsString() + " " + tile4);
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

            Canvas[] children = getChildren();
            removeMembers(children);
            l.addMember(b0);
            l.addMember(b);
            l.addMember(b1);
            l.addMember(b2);
            l.addMember(b3);
            l.addMember(b4);

            l.setMembersMargin(5);
            addMember(label);
            addMember(l);
            addMember(FieldUtil.getFormOneColumn(body));
            addMember(hlayoutbuttons);

        }


    }

    public QueryExplorerTab getQueryExplorerTb() {

        QueryExplorerTab queryExplorerTab = (QueryExplorerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYEXPLORER);
        return queryExplorerTab;
    }
}
