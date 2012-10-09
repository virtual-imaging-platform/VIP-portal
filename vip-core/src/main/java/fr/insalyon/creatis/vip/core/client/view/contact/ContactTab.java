/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.contact;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.LinkedHashMap;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ContactTab extends Tab {

    private VLayout contactLayout;
    private TextItem nameField;
    private TextItem emailField;
    private SelectItem categoryItem;
    private TextItem subjectField;
    private TextAreaItem commentItem;
    private IButton submitButton;

    public ContactTab() {

        this.setID(CoreConstants.TAB_CONTACT);
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_HELP) + " Contact");
        this.setCanClose(true);

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        configure();
        vLayout.addMember(contactLayout);

        this.setPane(vLayout);
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(300, null, true);
        nameField.setValue(CoreModule.user.getFullName());

        emailField = FieldUtil.getTextItem(300, null, true);
        emailField.setValue(CoreModule.user.getEmail());

        categoryItem = new SelectItem("category", "Category");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("General question", "General question");
        map.put("Report bug", "Report bug");
        map.put("Report missing term in ontologies", "Report missing term in ontologies");
        categoryItem.setValueMap(map);
        categoryItem.setValue("General Contact");
        categoryItem.setShowTitle(false);

        subjectField = FieldUtil.getTextItem(300, null);

        commentItem = new TextAreaItem("comment");
        commentItem.setWidth(300);
        commentItem.setShowTitle(false);

        submitButton = new IButton("Submit");
        submitButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (subjectField.validate()) {

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Layout.getInstance().setWarningMessage("Unable to send contact email:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            Layout.getInstance().setNoticeMessage("Contact successfully sent.");
                        }
                    };
                    WidgetUtil.setLoadingIButton(submitButton, "Sending messsage...");
                    service.sendContactMail(
                            categoryItem.getValueAsString(),
                            subjectField.getValueAsString().trim(),
                            commentItem.getValueAsString(), callback);
                }
            }
        });

        contactLayout = WidgetUtil.getVIPLayout(330, 370);
        addField("Name", nameField);
        addField("E-mail", emailField);
        addField("Category", categoryItem);
        addField("Subject", subjectField);
        addField("Comments", commentItem);
        contactLayout.addMember(submitButton);
    }

    private void addField(String title, FormItem item) {

        contactLayout.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        contactLayout.addMember(FieldUtil.getForm(item));
    }
}
