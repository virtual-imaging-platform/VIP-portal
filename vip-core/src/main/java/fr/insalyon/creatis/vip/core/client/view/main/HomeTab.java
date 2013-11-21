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
package fr.insalyon.creatis.vip.core.client.view.main;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;

/**
 *
 * @author Rafael Silva
 */
public class HomeTab extends Tab {

    private VLayout leftLayout;
    private VLayout rightLayout;
    private HLayout hLayout;

    public HomeTab() {

        this.setTitle("Home");
        this.setID(CoreConstants.TAB_HOME);
        this.setIcon(CoreConstants.ICON_HOME);
        

        hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);

        leftLayout = new VLayout();
        leftLayout.setWidth100();
        leftLayout.setHeight100();
        leftLayout.setOverflow(Overflow.AUTO);
        hLayout.addMember(leftLayout);

        rightLayout = new VLayout();
        rightLayout.setWidth(380);
        rightLayout.setHeight100();
        rightLayout.setOverflow(Overflow.VISIBLE);
       

        this.setPane(hLayout);
    }

    public void addTileGrid(ApplicationsTileGrid tileGrid) {

        Label tileName = new Label("<b>" + tileGrid.getTileName() + "</b>");
        tileName.setAlign(Alignment.LEFT);
        tileName.setWidth100();
        tileName.setHeight(20);
        tileName.setBackgroundColor("#F2F2F2");

        leftLayout.addMember(tileName);
        leftLayout.addMember(tileGrid);
    }

    public void addToRightLayout(Layout layout) {
        rightLayout.addMember(layout);
         hLayout.addMember(rightLayout);
    }
}
