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
package fr.insalyon.creatis.vip.social.client.view.message.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupsLayout extends AbstractMainLayout {

    private DynamicForm form;
    private SelectItem groupsItem;
    private VLayout groupLayout;
    
    public GroupsLayout() {

        super(SocialConstants.MENU_GROUP, SocialConstants.ICON_GROUP);

        configureGroupsList();
        
        groupLayout = new VLayout();
        groupLayout.setWidth100();
        groupLayout.setHeight100();
        this.addMember(groupLayout);
        
        loadData();
    }

    @Override
    public void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get user's groups:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                groupsItem.setValueMap(result.toArray(new String[]{}));
            }
        };
        service.getUserGroups(callback);
    }

    private void configureGroupsList() {

        form = new DynamicForm();
        form.setMargin(5);
        form.setNumCols(2);
        form.setHeight(20);
        form.setWidth(400);
        
        groupsItem = new SelectItem("groupsItem", "Your Groups");
        groupsItem.setEmptyPickListMessage("You have no groups");
        groupsItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                
                groupLayout.removeMembers(groupLayout.getMembers());
                groupLayout.addMember(new GroupLayout(groupsItem.getValueAsString()));
            }
        });
        
        form.setFields(groupsItem);
        
        HLayout groupsLayout = new HLayout(5);       
        groupsLayout.addMember(form);
        this.addMember(groupsLayout);
    }
}
