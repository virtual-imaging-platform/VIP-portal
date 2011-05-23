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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import fr.insalyon.creatis.vip.application.client.view.monitor.ChartsStackSection;
import fr.insalyon.creatis.vip.application.client.view.monitor.JobsStackSection;
import fr.insalyon.creatis.vip.application.client.view.monitor.SummaryStackSection;

/**
 *
 * @author Rafael Silva
 */
public class GateLabSimulationTab extends Tab {

    private boolean completed;
    private Timer timer;
    private GateLabSimulationToolStrip simulationToolStrip;
    private GeneralInformationStackSection informationStackSection;
    private SummaryStackSection summaryStackSection;
    private JobsStackSection jobsStackSection;
    private ChartsStackSection chartsStackSection;

    public GateLabSimulationTab(String simulationID, String status, String date) {

        this.setTitle(simulationID);
        this.setID(simulationID + "-gatetab");
        this.setIcon("icon-simulation-monitor.png");
        this.setCanClose(true);
        this.completed = status.equals("Running") ? false : true;

        VLayout vLayout = new VLayout();
        simulationToolStrip = new GateLabSimulationToolStrip(simulationID, completed);
        vLayout.addMember(simulationToolStrip);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);

        informationStackSection = new GeneralInformationStackSection(simulationID, status, date);
        summaryStackSection = new SummaryStackSection(simulationID, completed);
        jobsStackSection = new JobsStackSection(simulationID);
        chartsStackSection = new ChartsStackSection(simulationID);

        sectionStack.setSections(
                informationStackSection,
                summaryStackSection,
                jobsStackSection,
                chartsStackSection);

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
        informationStackSection.loadData();
        summaryStackSection.loadData();
        jobsStackSection.loadData();
        simulationToolStrip.updateDate();
    }

    public void destroy() {
        if (!completed) {
            timer.cancel();
        }
    }
}
