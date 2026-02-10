package fr.insalyon.creatis.vip.core.client.view.main;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationTileRecord;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.AccountTab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralTileGrid extends ApplicationsTileGrid {

    private List<ApplicationParser> parsers;

    public GeneralTileGrid() {

        super("General");
        addApplication(CoreConstants.APP_ACCOUNT, CoreConstants.APP_IMG_ACCOUNT);
        parsers = new ArrayList<ApplicationParser>();
    }

    @Override
    public void parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(CoreConstants.APP_ACCOUNT)) {
            Layout.getInstance().addTab(
                CoreConstants.TAB_ACCOUNT, AccountTab::new);
            return;
        }

        for (ApplicationParser parser : parsers) {
            if (parser.parse(applicationName, applicationVersion)) {
                return;
            }
        }
    }

    /**
     * Adds a parser to the general tile grid.
     *
     * @param parser
     */
    public void addParser(ApplicationParser parser) {

        parsers.add(parser);
        for (ApplicationTileRecord record : parser.getApplications()) {
            addApplication(record);
        }
    }
}
