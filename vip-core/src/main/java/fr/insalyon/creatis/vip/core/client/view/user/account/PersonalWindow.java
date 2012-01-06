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
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

/**
 *
 * @author Rafael Silva
 */
public class PersonalWindow extends Window {

    private ModalWindow modal;
    private DynamicForm form;
    private TextItem firstNameField;
    private TextItem lastNameField;
    private TextItem emailField;
    private TextItem institutionField;
    private TextItem phoneField;
    private String folder;
    private IButton saveButton;

    public PersonalWindow() {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_PERSONAL) + " Account Settings");
        this.setWidth100();
        this.setHeight(230);
        this.setShowCloseButton(false);

        VLayout vLayout = new VLayout(15);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setPadding(10);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureForm();
        configureButton();

        vLayout.addMember(form);
        vLayout.addMember(saveButton);

        this.addItem(vLayout);

        loadData();
    }

    private void configureForm() {

        emailField = FieldUtil.getTextItem(300, true, "Your Email", null);
        emailField.setDisabled(true);
        firstNameField = FieldUtil.getTextItem(300, true, "First Name", null);
        lastNameField = FieldUtil.getTextItem(300, true, "Last Name", null);
        institutionField = FieldUtil.getTextItem(300, true, "Institution", null);
        phoneField = FieldUtil.getTextItem(150, true, "Phone", "[0-9\\(\\)\\-+. ]");

        form = FieldUtil.getForm(emailField, firstNameField, lastNameField,
                institutionField, phoneField);
        
        form.setWidth(400);
    }

    private void configureButton() {

        saveButton = new IButton("Save Changes");
        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (form.validate()) {
                    User user = new User(
                            firstNameField.getValueAsString().trim(), 
                            lastNameField.getValueAsString().trim(), 
                            emailField.getValueAsString().trim(), 
                            institutionField.getValueAsString().trim(), 
                            phoneField.getValueAsString().trim());
                    user.setFolder(folder);
                    
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to save changes:<br />" + caught.getMessage());
                        }

                        public void onSuccess(User result) {
                            Modules.getInstance().userUpdated(CoreModule.user, result);
                            CoreModule.user = result;
                            modal.hide();
                        }
                    };
                    modal.show("Saving changes...", true);
                    service.updateUser(user, callback);
                }
            }
        });
    }

    private void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get user data:<br />" + caught.getMessage());
            }

            public void onSuccess(User result) {
                emailField.setValue(result.getEmail());
                firstNameField.setValue(result.getFirstName());
                lastNameField.setValue(result.getLastName());
                institutionField.setValue(result.getInstitution());
                phoneField.setValue(result.getPhone());
                folder = result.getFolder();
                modal.hide();
            }
        };
        modal.show("Loading user data...", true);
        service.getUserData(callback);
    }
}
