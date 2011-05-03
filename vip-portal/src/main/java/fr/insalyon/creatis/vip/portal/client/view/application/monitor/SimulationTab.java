/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import fr.insalyon.creatis.vip.common.client.view.Context;

/**
 *
 * @author Rafael Silva
 */
public class SimulationTab extends Tab {

    private boolean completed;
    private Timer timer;
    private SimulationToolStrip simulationToolStrip;
    private SummaryStackSection summaryStackSection;
    private JobsStackSection jobsStackSection;
    private ChartsStackSection chartsStackSection;
    private DiagramStackSection diagramStackSection;
    private LogsStackSection logsStackSection;

    public SimulationTab(String simulationID, String status) {
        this.setTitle(simulationID);
        this.setID(simulationID + "-tab");
        this.setCanClose(true);

        VLayout vLayout = new VLayout();
        simulationToolStrip = new SimulationToolStrip(simulationID);
        vLayout.addMember(simulationToolStrip);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);

        this.completed = status.equals("Running") ? false : true;
        summaryStackSection = new SummaryStackSection(simulationID, completed);
        jobsStackSection = new JobsStackSection(simulationID);
        chartsStackSection = new ChartsStackSection(simulationID);
        diagramStackSection = new DiagramStackSection(simulationID, completed);

        sectionStack.setSections(
                summaryStackSection,
                jobsStackSection,
                chartsStackSection,
                diagramStackSection);

        if (Context.getInstance().isSystemAdmin()) {
            logsStackSection = new LogsStackSection(simulationID);
            sectionStack.addSection(logsStackSection);
        }

        vLayout.addMember(sectionStack);

        this.setPane(vLayout);

        if (!completed) {
            timer = new Timer() {

                public void run() {
                    updateData();
                }
            };
            timer.scheduleRepeating(30000);
        }

        this.addTabDeselectedHandler(new TabDeselectedHandler() {

            public void onTabDeselected(TabDeselectedEvent event) {
                if (!completed) {
                    timer.cancel();
                }
            }
        });
        this.addTabSelectedHandler(new TabSelectedHandler() {

            public void onTabSelected(TabSelectedEvent event) {
                if (!completed) {
                    updateData();
                    timer.scheduleRepeating(30000);
                }
            }
        });
    }

    private void updateData() {
        summaryStackSection.loadData();
        jobsStackSection.loadData();
        diagramStackSection.loadImage();
        simulationToolStrip.updateDate();
    }

    public void destroy() {
        if (!completed) {
            timer.cancel();
        }
    }
}
