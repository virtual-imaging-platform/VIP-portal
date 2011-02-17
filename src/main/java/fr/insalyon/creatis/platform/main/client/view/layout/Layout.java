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
package fr.insalyon.creatis.platform.main.client.view.layout;

import fr.insalyon.creatis.platform.main.client.view.common.toolbar.ToolbarPanel;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 *
 * @author Rafael Silva
 */
public class Layout {

    private static Layout instance;
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

        Panel borderPanel = new Panel();
        borderPanel.setLayout(new BorderLayout());

        // Toolbar
        toolbarPanel = new ToolbarPanel();
        borderPanel.add(toolbarPanel, new BorderLayoutData(RegionPosition.NORTH));

        // Bottom Panel
        BorderLayoutData bottomData = new BorderLayoutData(RegionPosition.SOUTH);
        bottomData.setMinSize(20);
        bottomData.setMaxSize(20);
        bottomData.setMargins(new Margins(0, 0, 0, 0));
        bottomData.setSplit(true);
        borderPanel.add(new BottomPanel(), bottomData);

        // Center Panel
        centerPanel = new TabPanel();
        centerPanel.setEnableTabScroll(true);
        centerPanel.setMargins(0, 0, 0, 0);
        centerPanel.add(new Panel());
        borderPanel.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));

        // Left Panel
        BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
        westData.setSplit(true);
        westData.setMinSize(175);
        westData.setMaxSize(400);
        westData.setMinHeight(150);
        westData.setMargins(new Margins(0, 0, 0, 0));

        leftPanel = new Panel();
        leftPanel.setCollapsible(true);
        leftPanel.setCollapsed(true);
        leftPanel.setWidth(200);
        leftPanel.setMargins(0, 0, 0, 0);
        leftPanel.setTitle("VIP Platform");
        leftPanel.setLayout(new AccordionLayout(true));
        borderPanel.add(leftPanel, westData);

        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setPaddings(0);
        panel.setLayout(new FitLayout());
        panel.setHeight("100%");
        panel.add(borderPanel);
        new Viewport(panel);
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
