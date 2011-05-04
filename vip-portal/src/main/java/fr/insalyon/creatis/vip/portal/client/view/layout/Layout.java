/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.layout;

import fr.insalyon.creatis.vip.portal.client.view.common.toolbar.ToolbarPanel;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.portal.client.view.layout.toolstrip.BottomToolStrip;
import fr.insalyon.creatis.vip.portal.client.view.layout.toolstrip.MainToolStrip;

/**
 *
 * @author Rafael Silva
 */
public class Layout {

    private static Layout instance;
    private CenterTabSet centerTabSet;
    private Panel leftPanel;
    private ToolbarPanel toolbarPanel;
    private TabPanel centerPanel;

    public static Layout getInstance() {
        if (instance == null) {
            instance = new Layout();
        }
        return instance;
    }

    private Layout() {

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();

        vLayout.addMember(new MainToolStrip());

        HLayout hLayout = new HLayout();
        hLayout.setWidth100();
        hLayout.setHeight100();

        centerTabSet = CenterTabSet.getInstance();
        hLayout.addMember(centerTabSet);

        vLayout.addMember(hLayout);
        vLayout.addMember(new BottomToolStrip());

        vLayout.draw();
    }

    public void addTab(Tab tab) {
        if (centerTabSet.getTab(tab.getID()) == null) {
            centerTabSet.addTab(tab);
        }
        centerTabSet.selectTab(tab.getID());
    }
    
    public void setActiveCenterTab(String id) {
        centerTabSet.selectTab(id);
    }
    
    public Tab getTab(String id) {
        return centerTabSet.getTab(id);
    }

    public void setLeftPanel(AbstractLeftPanel panel) {
        leftPanel.removeAll();
        leftPanel.setTitle(panel.getTitle());
        leftPanel.setCollapsed(panel.isCollapsed());
        leftPanel.add(panel.getPanel());
        leftPanel.doLayout();
    }

    public void setCenterPanel(Panel panel) {
        if (centerPanel.getComponent(panel.getId()) == null) {
            centerPanel.removeAll();
            centerPanel.add(panel);
            centerPanel.setActiveTab(0);
            centerPanel.doLayout();
        } else {
            centerPanel.setActiveTab(panel.getId());
        }
    }

    public void addCenterPanel(Panel panel) {
        if (centerPanel.getComponent(panel.getId()) == null) {
            centerPanel.add(panel);
            centerPanel.setActiveTab(panel.getId());
            centerPanel.doLayout();
        } else {
            centerPanel.setActiveTab(panel.getId());
        }
    }

    public void setActiveCenterPanel(String panelID) {
        centerPanel.setActiveTab(panelID);
    }

    public boolean hasCenterPanelTab(String panelID) {
        return centerPanel.hasItem(panelID);
    }

    public Component getCenterPanelTab(String panelID) {
        return centerPanel.getComponent(panelID);
    }

    public void addClassButton(String className) {
        toolbarPanel.addClassButton(className);
    }

    public Panel getLeftPanel() {
        return leftPanel;
    }
}
