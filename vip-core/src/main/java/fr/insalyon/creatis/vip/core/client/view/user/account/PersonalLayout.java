package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
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
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.UpgradeLevelLayout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class PersonalLayout extends AbstractFormLayout {

    private Label levelLabel;
    private Label maxSimLabel;
    private Label firstNameField;
    private Label lastNameField;
    private TextItem institutionField;
    private SelectItem countryField;
    private IButton saveButton;

    public PersonalLayout() {

        super("100%", "275");
        addTitle("Account Information", CoreConstants.ICON_PERSONAL);

        configure();
        loadData();
    }

    private void configure() {
        levelLabel = WidgetUtil.getLabel("", 15);
        maxSimLabel = WidgetUtil.getLabel("", 15);
        firstNameField = WidgetUtil.getLabel("", 15);
        lastNameField = WidgetUtil.getLabel("", 15);
        institutionField = FieldUtil.getTextItem(200, null);

        countryField = new SelectItem();
        countryField.setShowTitle(false);
        countryField.setValueMap(CountryCode.getCountriesMap());
        countryField.setValueIcons(CountryCode.getCodesMap());
        countryField.setImageURLPrefix(CoreConstants.FOLDER_FLAGS);
        countryField.setImageURLSuffix(".png");
        countryField.setRequired(true);
        countryField.setWidth("150");
        saveButton = WidgetUtil.getIButton("Save Changes", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        if (institutionField.validate() & countryField.validate()) {

                            User user = new User(
                                    CoreModule.user.getFirstName(),
                                    CoreModule.user.getLastName(),
                                    CoreModule.user.getEmail(),
                                    institutionField.getValueAsString().trim(),
                                    UserLevel.valueOf(levelLabel.getContents()),
                                    CountryCode.valueOf(countryField.getValueAsString()));
                            user.setFolder(CoreModule.user.getFolder());

                            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                            final AsyncCallback<User> callback = new AsyncCallback<User>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    WidgetUtil.resetIButton(saveButton, "Save Changes", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setWarningMessage("Unable to save changes:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(User result) {
                                    Modules.getInstance().userUpdated(CoreModule.user, result);
                                    CoreModule.user = result;
                                    WidgetUtil.resetIButton(saveButton, "Save Changes", CoreConstants.ICON_SAVED);
                                    Layout.getInstance().setNoticeMessage("User information successfully updated.");
                                }
                            };
                            WidgetUtil.setLoadingIButton(saveButton, "Saving...");
                            service.updateUser(user, callback);
                        }
                    }
                });
        saveButton.setWidth(150);
        this.addMember(WidgetUtil.getLabel("<b>Level</b>", 15));
        this.addMember(levelLabel);
        this.addMember(WidgetUtil.getLabel("<b>Maximum simulatenous simulations</b>", 15));
        this.addMember(maxSimLabel);
        this.addMember(WidgetUtil.getLabel("<b>First Name</b>", 15));
        this.addMember(firstNameField);
        this.addMember(WidgetUtil.getLabel("<b>Last Name</b>", 15));
        this.addMember(lastNameField);
        addField("Institution", institutionField);
        addField("Country", countryField);
        this.addMember(saveButton);
    }

    private void loadData() {
        User user = CoreModule.user;

        levelLabel.setContents(user.getLevel().name());
        if (user.getLevel() == UserLevel.Beginner) {
            levelLabel.addClickHandler(
                    new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            new UpgradeLevelLayout(event.getX(), event.getY()).show();
                        }
                    });
            levelLabel.setPrompt("Upgrade your Account!");
            levelLabel.setCursor(Cursor.HAND);
        }
        maxSimLabel.setContents(String.valueOf(user.getMaxRunningSimulations()));
        firstNameField.setContents(user.getFirstName());
        lastNameField.setContents(user.getLastName());
        institutionField.setValue(user.getInstitution());
        countryField.setValue(user.getCountryCode().name());

        if(institutionField.getDisplayValue().equals("Unknown")){
            Layout.getInstance().setWarningMessage("Please review your account information (Institution)",0);
        }
    }
}
