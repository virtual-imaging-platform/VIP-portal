/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.simulationgui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.MenuItemStringFunction;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;


/**
 *
 * @author glatard
 */
class SimulationGUITab extends Tab{
    private final ToolStrip toolStrip;
    private final DefineSceneSection defineSceneSection;
    private final DefineParamsSection defineParamsSection;
   
    public SimulationGUITab() {
        this.setTitle("New Simulation");
        this.setID("new-simulation-tab");
        this.setCanClose(true);
        VLayout vLayout = new VLayout();
        final SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);
        sectionStack.showSection(0);
        sectionStack.hideSection(1);
        defineSceneSection = new DefineSceneSection();
        defineParamsSection = new DefineParamsSection();
        
        sectionStack.setSections(defineSceneSection,defineParamsSection);
        
        toolStrip = new ToolStrip();
        ToolStripMenuButton menuButton = getToolStripMenuButton();  
        toolStrip.addMenuButton(menuButton);
        toolStrip.addSeparator();
        
        ToolStripButton runButton = new ToolStripButton("Run");  
        runButton.setIcon("icon-refresh.gif");  
        runButton.setActionType(SelectionType.CHECKBOX);  
        toolStrip.addButton(runButton);
        runButton.addClickHandler( new ClickHandler(){
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
            //sectionStack.showSection(1);
            //sectionStack.hideSection(0);
                throw new UnsupportedOperationException("Not supported yet.");
            }
       });
        /*ToolStripButton backButton = new ToolStripButton("Back");  
        backButton.setIcon("icon-clear.png");  
        backButton.setActionType(SelectionType.CHECKBOX); 
        backButton.addClickHandler( new ClickHandler(){
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
               sectionStack.showSection(0);
               sectionStack.hideSection(1);
                throw new UnsupportedOperationException("Not supported yet.");
            }
       });*/
     
        //toolStrip.addButton(backButton);
        toolStrip.addSeparator();
        vLayout.addMember(toolStrip);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
        
        
    }

   private ToolStripMenuButton getToolStripMenuButton() {  
        Menu menu = new Menu();  
        menu.setShowShadow(true);  
        menu.setShadowDepth(3);  
  
        MenuItem newItem = new MenuItem("New", "icons/16/document_plain_new.png", "Ctrl+N");  
        MenuItem openItem = new MenuItem("Open", "icon-folder.png", "Ctrl+O");  
        MenuItem saveItem = new MenuItem("Save", "icon-save.png", "Ctrl+S");  
        MenuItem saveAsItem = new MenuItem("Save As", "icon-save-as.png");  
  
        MenuItem recentDocItem = new MenuItem("Recent Documents", "icon-action.png");  
  
        Menu recentDocSubMenu = new Menu();  
        /*MenuItem dataSM = new MenuItem("data.xml");  
        dataSM.setChecked(true);  
        MenuItem componentSM = new MenuItem("Component Guide.doc");  
        MenuItem ajaxSM = new MenuItem("AJAX.doc");  
        recentDocSubMenu.setItems(dataSM, componentSM, ajaxSM);  
       */
        recentDocItem.setSubmenu(recentDocSubMenu);  
  
        MenuItem exportItem = new MenuItem("Export as...", "icon-simulation-manage.png");  
        Menu exportSM = new Menu();  
        exportSM.setItems(  
                new MenuItem("XML"),  
                new MenuItem("CSV"),  
                new MenuItem("Plain text"));  
        exportItem.setSubmenu(exportSM);  
  
       // MenuItem printItem = new MenuItem("Print", "icons/16/printer3.png", "Ctrl+P");  
        //printItem.setEnabled(false);  
  
        MenuItemSeparator separator = new MenuItemSeparator();  
  
        final MenuItem activateMenu = new MenuItem("Activate");  
        activateMenu.setDynamicTitleFunction(new MenuItemStringFunction() {  
  
            public String execute(final Canvas aTarget, final Menu aMenu, final MenuItem aItem) {  
                if (Math.random() > 0.5) {  
                    return "De-Activate Blacklist";  
                } else {  
                    return "Activate Blacklist";  
                }  
            }  
        });  
  
        menu.setItems(activateMenu, newItem, openItem, separator, saveItem, saveAsItem,  
                separator, recentDocItem, separator, exportItem, separator);  
  
        ToolStripMenuButton menuButton = new ToolStripMenuButton("File", menu);  
        menuButton.setWidth(100);  
        return menuButton;  
    }  

}
