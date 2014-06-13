/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.DateItemSelectorFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.PublicationTab;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Nouha Boujelben
 */
public class EditPublicationLayout extends AbstractFormLayout {

    private TextItem titleField;
    private TextItem doiField;
    private TextItem authorsField;
    private ComboBoxItem publicationDate;
    private IButton saveButton;
    private ComboBoxItem publicationType;
    private TextItem publicationTypeName;
    private boolean newPublication;
    private Long idPub;

    public EditPublicationLayout() {

        super("50%", "100%");
        addTitle("Add/Edit Publications", CoreConstants.ICON_PUBLICATION);
        configure();
    }

    private void configure() {


        publicationDate = new ComboBoxItem();
        publicationDate.setWidth("60");
        publicationDate.setValueMap("1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015");
        publicationDate.setDefaultValue("2014");
        publicationDate.setShowTitle(false);

        titleField = FieldUtil.getTextItem(500, null);
        doiField = FieldUtil.getTextItem(500, null);
        authorsField = FieldUtil.getTextItem(500, null);


        publicationType = new ComboBoxItem();
        publicationType.setTitle("<b>" + "Type" + "</b>");
        publicationType.setWidth("250");
        publicationType.setValueMap("Article In Conference Proceedings", "Journal Article", "Book Chapter", "Other");
        publicationType.setDefaultValue("Article In Conference Proceedings");
        publicationType.setTitleOrientation(TitleOrientation.TOP);

        publicationTypeName = new TextItem();
        publicationTypeName.setTitle("Journal, Conference or Book Name");
        publicationTypeName.setRequired(true);
        publicationTypeName.setTitleOrientation(TitleOrientation.TOP);
        publicationTypeName.setWidth("250");


        addMember(WidgetUtil.getLabel("Please list here the references of the publications that you made using VIP. These references may"
                + " be used by the VIP team to justify the use of computing and storage resources to the European Grid Infrastructure.", 20));
        addField("<font color=red>*</font> Title", titleField);
        addMember(FieldUtil.getForm(publicationType, publicationTypeName));
        addField("Authors", authorsField);
        addField("Date", publicationDate);
        addField("Doi", doiField);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_ADD,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                save(new Publication(idPub, titleField.getValueAsString(), publicationDate.getValueAsString().toString().substring(0, 4), doiField.getValueAsString(), authorsField.getValueAsString(), publicationType.getValueAsString(), publicationTypeName.getValueAsString()));
            }
        });



        addButtons(saveButton);

    }

    public void setPublication(String id, String title, String type, String nameType, String authors, String date, String doi) {

        if (title != null) {
            idPub = Long.valueOf(id);
            this.newPublication = false;
            this.titleField.setValue(title);
            this.publicationType.setValue(type);
            this.publicationTypeName.setValue(nameType);
            this.publicationDate.setValue(date);
            this.doiField.setValue(doi);
            this.authorsField.setValue(authors);

        } else {
            this.newPublication = true;
            this.titleField.setValue("");
            this.publicationType.setValue("");
            this.publicationTypeName.setValue("");
            this.publicationDate.setValue("");
            this.doiField.setValue("");
            this.authorsField.setValue("");

        }
    }

    private void save(Publication pub) {

        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newPublication) {
            ConfigurationService.Util.getInstance().addPublication(pub, getCallback("add"));
        } else {
            ConfigurationService.Util.getInstance().updatePublication(pub, getCallback("update"));
        }
    }

    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                Layout.getInstance().setWarningMessage("Unable to " + text + " publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                setPublication(null, null, null, null, null, null, null);
                PublicationTab pubTab = (PublicationTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_PUBLICATION);
                pubTab.loadPublication();
            }
        };
    }
}
