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
package fr.insalyon.creatis.vip.datamanager.client.view.system.zombie;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ManageZombieFilesTab extends AbstractManageTab {

    private ListGrid grid;
    private ModalWindow modal;
    
    public ManageZombieFilesTab() {

        super(DataManagerConstants.ICON_DOWNLOAD, DataManagerConstants.APP_ZOMBIE_FILES,
                DataManagerConstants.TAB_MANAGE_ZOMBIE_FILES);
        
        configureGrid();
        modal = new ModalWindow(grid);
        toolStrip = new ManageZombieFilesToolStrip(modal);
        
        vLayout.addMember(toolStrip);
        vLayout.addMember(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        ListGridField pathField = new ListGridField("surl", "SURL");
        ListGridField registrationField = FieldUtil.getDateField();

        grid.setFields(pathField, registrationField);
    }
    
    public void loadData() {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<List<DMZombieFile>> callback = new AsyncCallback<List<DMZombieFile>>() {

            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Unable to get files list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<DMZombieFile> result) {
                
                List<ZombieFileRecord> dataList = new ArrayList<ZombieFileRecord>();
                for (DMZombieFile zf : result) {
                    dataList.add(new ZombieFileRecord(zf.getSurl(), zf.getRegistration()));
                }
                grid.setData(dataList.toArray(new ZombieFileRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading zombie files...", true);
        service.getZombieFiles(callback);
    }
    
    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();
    }
}
