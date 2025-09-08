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
public class GalleryTab extends Tab {

    public GalleryTab() {
        
        this.setTitle(Canvas.imgHTML(DocsConstants.ICON_GALLERY) + " " + 
                DocsConstants.APP_GALLERY);
        this.setID(DocsConstants.TAB_GALLERY);
        this.setCanClose(true);
        
        HTMLPane pane = new HTMLPane();
        pane.setOverflow(Overflow.AUTO);
        pane.setStyleName("defaultBorder");
        pane.setContentsURL("gallery/index.html");
        pane.setContentsType(ContentsType.PAGE);
        
        this.setPane(pane);
    }
}
