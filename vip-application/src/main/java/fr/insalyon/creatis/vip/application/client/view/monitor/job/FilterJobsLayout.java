/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class FilterJobsLayout extends VLayout {

    private static FilterJobsLayout instance;
    private SelectItem statusItem;

    public static FilterJobsLayout getInstance(int x, int y, CommandLayout commandLayout) {

        if (instance == null) {
            instance = new FilterJobsLayout(x, y, commandLayout);
        }
        return instance;
    }

    private FilterJobsLayout(int x, int y, final CommandLayout commandLayout) {

        this.setBorder("1px solid #CCCCCC");
        this.setBackgroundColor("#FFFFFF");
        this.setWidth(200);
        this.setHeight(90);
        this.setPadding(5);
        this.moveTo(x - getWidth(), y + 18);

        HLayout titleLayout = new HLayout(5);
        titleLayout.setWidth100();
        titleLayout.setHeight(25);

        titleLayout.addMember(WidgetUtil.getLabel("<b>Filter</b>", ApplicationConstants.ICON_MONITOR_SEARCH, 16));
        titleLayout.addMember(WidgetUtil.getSpaceLabel(16));
        titleLayout.addMember(WidgetUtil.getIconLabel(CoreConstants.ICON_CLOSE, "Close", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                destroy();
                instance = null;
            }
        }));
        this.addMember(titleLayout);

        List<String> values = new ArrayList<String>();
        values.add("All");
        values.addAll(JobStatus.valuesAsList());

        this.statusItem = new SelectItem();
        this.statusItem.setShowTitle(false);
        this.statusItem.setValueMap(values.toArray(new String[]{}));
        this.statusItem.setValue("All");
        this.statusItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                commandLayout.filter(statusItem.getValueAsString());
            }
        });

        this.addMember(WidgetUtil.getLabel("<b>Status</b>", 16));
        this.addMember(FieldUtil.getForm(statusItem));
    }
}
