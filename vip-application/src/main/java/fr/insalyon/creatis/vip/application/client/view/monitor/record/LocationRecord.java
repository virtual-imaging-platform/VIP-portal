/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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

        setAttribute(ATT_ICON, CoreConstants.FOLDER_FLAGS + icon);
        setAttribute(ATT_JOBS, jobs);
        
        try {
            setAttribute(ATT_COUNTRY, CountryCode.valueOf(icon).getCountryName());
        } catch (IllegalArgumentException ex) {
            setAttribute(ATT_COUNTRY, "Unknown");
        }
    }

    public String getCountry() {
        return getAttributeAsString(ATT_COUNTRY);
    }

    public int getJobs() {
        return getAttributeAsInt(ATT_JOBS);
    }
}
