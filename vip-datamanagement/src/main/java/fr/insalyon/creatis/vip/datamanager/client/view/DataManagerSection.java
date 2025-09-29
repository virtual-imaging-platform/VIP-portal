package fr.insalyon.creatis.vip.datamanager.client.view;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerSection extends SectionStackSection {
    
    public DataManagerSection() {

        this.setTitle(Canvas.imgHTML(DataManagerConstants.ICON_FILE_TRANSFER) 
                + " " + DataManagerConstants.APP_FILE_TRANSFER);
        this.setCanCollapse(true);
        this.setExpanded(false);
        this.setResizeable(true);
        this.setID(DataManagerConstants.SECTION_FILE_TRANSFER);
        
        HLayout hLayout = new HLayout();
        hLayout.setHeight100();
                
        hLayout.addMember(BrowserLayout.getInstance());
        hLayout.addMember(OperationLayout.getInstance());
        
        this.addItem(hLayout);
    }
    
    public void expand() {
        this.setExpanded(true);
    }
}
