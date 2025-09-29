package fr.insalyon.creatis.vip.docs.client.view;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.tab.Tab;

/**
 *
 * @author Rafael Silva
 */
public class DocumentationTab extends Tab {

    public DocumentationTab() {
        
        this.setTitle(Canvas.imgHTML(DocsConstants.ICON_DOCUMENTATION) + " " +
                DocsConstants.APP_DOCUMENTATION);
        this.setID(DocsConstants.TAB_DOCUMENTATION);
        this.setCanClose(true);
        
        HTMLPane pane = new HTMLPane();
        pane.setOverflow(Overflow.AUTO);
        pane.setStyleName("defaultBorder");
        pane.setContentsURL("documentation/index.html");
        pane.setContentsType(ContentsType.PAGE);
        
        this.setPane(pane);
    }
}
