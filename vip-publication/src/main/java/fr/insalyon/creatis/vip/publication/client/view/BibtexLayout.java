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
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.client.rpc.PublicationService;

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
