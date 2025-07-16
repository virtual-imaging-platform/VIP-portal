package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;

/**
 *
 * @author Rafael Silva
 */
public class LocationRecord extends ListGridRecord {

    private final String ATT_ICON = "icon";
    private final String ATT_COUNTRY = "country";
    private final String ATT_JOBS = "jobs";

    public LocationRecord() {
    }

    public LocationRecord(String icon, int jobs) {

        setAttribute(ATT_JOBS, jobs);
        
        try {
            setAttribute(ATT_COUNTRY, CountryCode.valueOf(icon).getCountryName());
            setAttribute(ATT_ICON, CoreConstants.FOLDER_FLAGS + icon);
        } catch (IllegalArgumentException ex) {
            setAttribute(ATT_COUNTRY, "Unknown");
            setAttribute(ATT_ICON, CoreConstants.FOLDER_FLAGS + "_world");
        }
    }

    public String getCountry() {
        return getAttributeAsString(ATT_COUNTRY);
    }

    public int getJobs() {
        return getAttributeAsInt(ATT_JOBS);
    }
}
