package fr.insalyon.creatis.vip.applicationimporter.client.view;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;

/**
 *
 * @author Nouha Boujelben
 */
public class ImportTab extends Tab {

    public ImportTab() {           
        this.setTitle(Canvas.imgHTML(Constants.ICON_BOUTIQUES) + " Boutiques Application Importer");
        this.setID(Constants.TAB_ID_BOUTIQUES);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        setPane(new BoutiquesImportLayout("100%", "100%"));
    }
}