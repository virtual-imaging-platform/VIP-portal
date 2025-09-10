package fr.insalyon.creatis.vip.core.client.view.main;

import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationTileRecord;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SystemTileGrid extends ApplicationsTileGrid {

    private List<ApplicationParser> parsers;
    
    public SystemTileGrid() {

        super("System");
        parsers = new ArrayList<ApplicationParser>();
    }

    @Override
    public void parse(String applicationName, String applicationVersion) {
        
        for (ApplicationParser parser : parsers) {
            if (parser.parse(applicationName, applicationVersion)) {
                return;
            }
        }
    }
    
    /**
     * Adds a parser to the system tile grid.
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
