/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.LinkedHashMap;

/**
 *
 * @author Rafael Silva
 */
public class ContactTab extends Tab {

    private ModalWindow modal;
    private DynamicForm form;
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

        modal = new ModalWindow(vLayout);

        configureForm();
        configureButton();

        vLayout.addMember(form);
        vLayout.addMember(submitButton);

        this.setPane(vLayout);
    }

    private void configureForm() {

        nameField = FieldUtil.getTextItem(300, true, "Name", null);
        nameField.setDisabled(true);
        nameField.setValue(CoreModule.user.getFullName());

        emailField = FieldUtil.getTextItem(300, true, "Email", null);
        emailField.setDisabled(true);
        emailField.setValue(CoreModule.user.getEmail());

        categoryItem = new SelectItem("category", "Category");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("General Contact", "General Contact");
        map.put("Report Bug", "Report Bug");
        categoryItem.setValueMap(map);
        categoryItem.setValue("General Contact");

        subjectField = FieldUtil.getTextItem(300, true, "Subject", null);

        commentItem = new TextAreaItem("comment", "Comments");
        commentItem.setWidth(300);

        form = FieldUtil.getForm(nameField, emailField, categoryItem,
                subjectField, commentItem);
        form.setWidth(500);
        form.setTitleWidth(150);
    }

    private void configureButton() {

        submitButton = new IButton("Submit");
        submitButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to send contact email:<br />" + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            modal.hide();
                            SC.say("Contact successfully sent.");
                        }
                    };
                    modal.show("Sending contact messsage...", true);
                    service.sendContactMail(
                            categoryItem.getValueAsString(), 
                            subjectField.getValueAsString(), 
                            commentItem.getValueAsString(), callback);
                }
            }
        });
    }
}
