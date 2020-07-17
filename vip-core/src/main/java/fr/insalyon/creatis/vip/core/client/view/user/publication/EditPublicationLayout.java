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
package fr.insalyon.creatis.vip.core.client.view.user.publication;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.PublicationTab;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private boolean newPublication = true;
    private Long idPub;
    private ComboBoxItem vipApplication;

    public EditPublicationLayout() {

        super("100%", "50%");
        addTitle("Add/Edit Publications", CoreConstants.ICON_PUBLICATION);
        configure();
    }

    private void configure() {


        publicationDate = new ComboBoxItem();
        publicationDate.setWidth(80);
        publicationDate.setValueMap(configureYear());
        publicationDate.setDefaultValue("2014");
        publicationDate.setShowTitle(false);

        vipApplication = new ComboBoxItem();
        vipApplication.setWidth(250);
        vipApplication.setValueMap(getApplicationsList());
        vipApplication.setDefaultValue("");
        vipApplication.setShowTitle(false);

        titleField = FieldUtil.getTextItem(500, null);
        doiField = FieldUtil.getTextItem(500, null);
        authorsField = FieldUtil.getTextItem(500, null);


        publicationType = new ComboBoxItem();
        publicationType.setWidth(250);
        publicationType.setValueMap("Article In Conference Proceedings", "Journal Article", "Book Chapter", "Other");
        publicationType.setDefaultValue("Article In Conference Proceedings");
        publicationType.setTitleOrientation(TitleOrientation.TOP);
        publicationType.setShowTitle(false);

        publicationTypeName = FieldUtil.getTextItem(250, null);
        addField("<font color=red>*</font> Title", titleField);
        VLayout v = new VLayout(5);
        v.setWidth(250);
        v.addMember(WidgetUtil.getLabel("<b>" + "<font color=red>*</font>" + " Type" + "</b>", 15));
        v.addMember(FieldUtil.getForm(publicationType));
        VLayout v2 = new VLayout(5);
        v2.setWidth(250);
        v2.addMember(WidgetUtil.getLabel("<b>" + "<font color=red>*</font>" + " Journal, Conference or Book Name" + "</b>", 15));
        v2.addMember(FieldUtil.getForm(publicationTypeName));
        HLayout h = new HLayout(5);
        h.addMember(v);
        h.addMember(v2);
        h.setHeight(50);
        addMember(h);
        addField("<font color=red>*</font>" + " Authors", authorsField);
        addField("<font color=red>*</font>" + " Date", publicationDate);
        addField("<font color=red>*</font>" + " VIP Application used", vipApplication);
        addField("Doi", doiField);

        saveButton = WidgetUtil.getIButton("Add", CoreConstants.ICON_ADD,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                save(new Publication(idPub, titleField.getValueAsString(), publicationDate.getValueAsString().toString().substring(0, 4), doiField.getValueAsString(), authorsField.getValueAsString(), publicationType.getValueAsString(), publicationTypeName.getValueAsString(), vipApplication.getValueAsString()));
            }
        });



        addButtons(saveButton);

    }

    public void setPublication(String id, String title, String type, String nameType, String authors, String date, String doi, String vipApplication) {

        if (title != null) {
            idPub = Long.valueOf(id);
            this.newPublication = false;
            WidgetUtil.resetIButton(saveButton, "Update", CoreConstants.ICON_EDIT);
            this.titleField.setValue(title);
            this.publicationType.setValue(type);
            this.publicationTypeName.setValue(nameType);
            this.publicationDate.setValue(date);
            this.doiField.setValue(doi);
            this.authorsField.setValue(authors);
            this.vipApplication.setValue(vipApplication);


        } else {
            this.newPublication = true;
            WidgetUtil.resetIButton(saveButton, "Add", CoreConstants.ICON_ADD);
            this.titleField.setValue("");
            this.publicationType.setValue("");
            this.publicationTypeName.setValue("");
            this.publicationDate.setValue("");
            this.doiField.setValue("");
            this.authorsField.setValue("");
            this.vipApplication.setValue(vipApplication);

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
                Layout.getInstance().setWarningMessage("Unable to " + text + " publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {

                setPublication(null, null, null, null, null, null, null, null);
                PublicationTab pubTab = (PublicationTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_PUBLICATION);
                pubTab.loadPublication();
            }
        };
    }

    private String[] configureYear() {
        String year = DateTimeFormat.getFormat("d-M-yyyy").format(new Date()).split("-")[2];
        List<String> values = new ArrayList<String>();
        for (int i = 2008; i < Integer.valueOf(year) + 2; i++) {
            values.add(String.valueOf(i));
        }
        String[] yearTable = new String[values.size()];
        return values.toArray(yearTable);
    }

    public String[] getApplicationsList() {
        //TODO have a callback retrieving the application list from the VIP database
        List<String> values = new ArrayList<String>();
        values.add("GATE");
        values.add("Freesurfer");
        String[] appliTable = new String[values.size()];
        return values.toArray(appliTable);
    }
}
