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
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class GroupLayout extends AbstractFormLayout {

    private ListGrid grid;

    public GroupLayout() {

        super(350, 150);
        addTitle("Groups", CoreConstants.ICON_GROUP);

        configureGrid();
        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField nameField = new ListGridField("groupName", "Group");
        ListGridField roleField = new ListGridField("role", "Role");

        grid.setFields(nameField, roleField);
        grid.setSortField("groupName");
        grid.setSortDirection(SortDirection.ASCENDING);

        modal = new ModalWindow(grid);
        this.addMember(grid);
    }

    private void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Map<String, GROUP_ROLE>> callback = new AsyncCallback<Map<String, GROUP_ROLE>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.say("Unable to load groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, GROUP_ROLE> result) {
                List<GroupRecord> groups = new ArrayList<GroupRecord>();

                for (String groupName : result.keySet()) {
                    groups.add(new GroupRecord(groupName, result.get(groupName)));
                }
                grid.setData(groups.toArray(new GroupRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading groups...", true);
        service.getUserGroups(null, callback);
    }
}
