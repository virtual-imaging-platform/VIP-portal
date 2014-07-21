/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.PublicationTab;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class BibtexLayout extends AbstractFormLayout {

    TextAreaItem textAreaItem;
    private IButton saveButton;

    public BibtexLayout() {
        super("100%", "50%");
        addTitle("Add Publications From Bibtex ", CoreConstants.ICON_PUBLICATION);
        configure();
    }

    private void configure() {

        textAreaItem = new TextAreaItem();
        textAreaItem.setShowTitle(false);
        textAreaItem.setWidth(500);
        textAreaItem.setHeight("100%");

        saveButton = WidgetUtil.getIButton("Add", CoreConstants.ICON_ADD,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            
                final AsyncCallback<List<Publication>> callback = new AsyncCallback<List<Publication>>() {
            @Override
            public void onFailure(Throwable caught) {
               
                Layout.getInstance().setWarningMessage("Unable to parse publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Publication> result) {
               for(Publication p:result){
               save(p);
               }
                
              
            }
        };
     
        ConfigurationService.Util.getInstance().parseBibtexText(textAreaItem.getValueAsString(), callback);
                
              
            }
        });

        addFieldResponsiveHeight("Bibtex text", textAreaItem);
        addButtons(saveButton);
        
        

    }
     private void save(Publication pub) {

            ConfigurationService.Util.getInstance().addPublication(pub, getCallback("add"));
       
    }
    
    
    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to " + text + " publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {

                
                PublicationTab pubTab = (PublicationTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_PUBLICATION);
                pubTab.loadPublication();
            }
        };
    }
}
