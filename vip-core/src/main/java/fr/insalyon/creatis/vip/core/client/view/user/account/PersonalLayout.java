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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

/**
 *
 * @author Rafael Silva
 */
public class PersonalLayout extends AbstractFormLayout {

    private TextItem levelField;
    private TextItem firstNameField;
    private TextItem lastNameField;
    private TextItem emailField;
    private TextItem institutionField;
    private TextItem phoneField;
    private SelectItem countryField;
    private String folder;
    private IButton saveButton;

    public PersonalLayout() {

        super(330, 430);
        addTitle("Account Settings", CoreConstants.ICON_PERSONAL);

        configure();
        loadData();
    }

    private void configure() {

        levelField = FieldUtil.getTextItem(300, null, true);
        emailField = FieldUtil.getTextItem(300, null, true);
        firstNameField = FieldUtil.getTextItem(300, null);
        lastNameField = FieldUtil.getTextItem(300, null);
        institutionField = FieldUtil.getTextItem(300, null);
        phoneField = FieldUtil.getTextItem(150, "[0-9\\(\\)\\-+. ]");

        countryField = new SelectItem();
        countryField.setShowTitle(false);
        countryField.setValueMap(CountryCode.getCountriesMap());
        countryField.setValueIcons(CountryCode.getCodesMap());
        countryField.setImageURLPrefix(CoreConstants.FOLDER_FLAGS);
        countryField.setImageURLSuffix(".png");
        countryField.setRequired(true);

        saveButton = new IButton("Save Changes");
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (firstNameField.validate() & lastNameField.validate()
                        & institutionField.validate() & phoneField.validate()
                        & countryField.validate()) {

                    User user = new User(
                            firstNameField.getValueAsString().trim(),
                            lastNameField.getValueAsString().trim(),
                            emailField.getValueAsString(),
                            institutionField.getValueAsString().trim(),
                            phoneField.getValueAsString().trim(),
                            UserLevel.valueOf(levelField.getValueAsString()),
                            CountryCode.valueOf(countryField.getValueAsString()));
                    user.setFolder(folder);

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to save changes:<br />" + caught.getMessage());
                        }

                        @Override
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

        addField("Level", levelField);
        addField("E-mail", emailField);
        addField("First Name", firstNameField);
        addField("Last Name", lastNameField);
        addField("Institution", institutionField);
        addField("Phone", phoneField);
        addField("Country", countryField);
        this.addMember(saveButton);
    }

    private void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get user data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(User result) {

                levelField.setValue(result.getLevel().name());
                emailField.setValue(result.getEmail());
                firstNameField.setValue(result.getFirstName());
                lastNameField.setValue(result.getLastName());
                institutionField.setValue(result.getInstitution());
                phoneField.setValue(result.getPhone());
                countryField.setValue(result.getCountryCode().name());
                folder = result.getFolder();
                modal.hide();
            }
        };
        modal.show("Loading user data...", true);
        service.getUserData(callback);
    }
}
