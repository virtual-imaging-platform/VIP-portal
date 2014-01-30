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
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupLayout extends AbstractFormLayout {

    private VLayout vLayout;
    private Label messageLabel;

    public GroupLayout() {

        super(350, 430);
        addTitle("Groups", CoreConstants.ICON_GROUP);

        messageLabel = WidgetUtil.getLabel("", 15);
        messageLabel.setBackgroundColor("#F79191");
        this.addMember(messageLabel);
        
        vLayout = new VLayout(5);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        this.addMember(vLayout);

        this.addMember(WidgetUtil.getLabel("<font color=\"#666666\"><b>Note</b>: "
                + "modifications will only take effect once you reload the portal.</font>", 30));

        loadData();
    }

    private void loadData() {

        final ArrayList<String> groups = new ArrayList<String>();
        
        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        
        final AsyncCallback<List<Group>> cbPublicGroups = new AsyncCallback<List<Group>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load public groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                for (Group group : result) {
                    if(!groups.contains(group.getName()))
                        vLayout.addMember(new GroupBoxLayout(group.getName(),
                                group.isPublicGroup(), GROUP_ROLE.None));
                }
            }
        };
        final AsyncCallback<Map<Group, GROUP_ROLE>> callback = new AsyncCallback<Map<Group, GROUP_ROLE>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Group, GROUP_ROLE> result) {
                
                for (Group group : result.keySet()) {
                    vLayout.addMember(new GroupBoxLayout(group.getName(),
                            group.isPublicGroup(), result.get(group)));
                    groups.add(group.getName());
                }
                if(result.isEmpty()){
                    messageLabel.setVisible(true);
                    messageLabel.setContents("You are not a member of any group. The following public groups will grant you access to applications.");
                } else
                    messageLabel.setVisible(false);
                service.getPublicGroups(cbPublicGroups);
            }
        };
        service.getUserGroups(null, callback);
        
        
        
    }
}
