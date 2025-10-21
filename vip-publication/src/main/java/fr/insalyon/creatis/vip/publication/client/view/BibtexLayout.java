package fr.insalyon.creatis.vip.publication.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.publication.client.rpc.PublicationService;
import fr.insalyon.creatis.vip.publication.models.Publication;

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
        addTitle("Add Publications From Bibtex Format", PublicationConstants.ICON_PUBLICATION);
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
     
        PublicationService.Util.getInstance().parseBibtexText(textAreaItem.getValueAsString(), callback);
                
              
            }
        });

        addFieldResponsiveHeight("Add your bibtex base here:", textAreaItem);
        addButtons(saveButton);
        
        

    }
     private void save(Publication pub) {

            PublicationService.Util.getInstance().addPublication(pub, getCallback("add"));
       
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
                        getTab(PublicationConstants.TAB_PUBLICATION);
                pubTab.loadPublication();
            }
        };
    }
}
