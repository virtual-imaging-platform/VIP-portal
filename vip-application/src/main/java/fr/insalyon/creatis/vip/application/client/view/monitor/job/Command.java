/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Command extends Label {

    private EnumMap<JobStatus, Set<Integer>> jobStatus;
    private EnumMap<JobStatus, JobStatusLayout> jobStatusLayout;
    private boolean selected;
    private Map<String, Command> commandsMap;
    private VLayout displayLayout;

    public Command(String simulationID, String command,
            Map<String, Command> commandsMap, VLayout displayLayout) {

        this.setContents("Debug: " + command);
        this.setIcon(ApplicationConstants.ICON_MONITOR_DEBUG);
        this.setAutoWidth();
        this.setOverflow(Overflow.VISIBLE);
        this.commandsMap = commandsMap;
        this.displayLayout = displayLayout;
        this.selected = false;
        this.setCursor(Cursor.HAND);
        this.setBorder("1px solid #CCCCCC");
        this.setBackgroundColor("#FFFFFF");
        this.setPadding(3);

        jobStatus = new EnumMap<JobStatus, Set<Integer>>(JobStatus.class);
        jobStatusLayout = new EnumMap<JobStatus, JobStatusLayout>(JobStatus.class);
        for (JobStatus j : JobStatus.values()) {
            jobStatus.put(j, new HashSet<Integer>());
            jobStatusLayout.put(j, new JobStatusLayout(simulationID,
                    j, jobStatus.get(j)));
        }

        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setSelected(true);
            }
        });
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#DEDEDE");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (!selected) {
                    setBackgroundColor("#FFFFFF");
                    setBorder("1px solid #CCCCCC");
                } else {
                    setBackgroundColor("#DEDEDE");
                    setBorder("1px solid #ADADAD");
                }
            }
        });
    }

    /**
     *
     * @param job
     */
    public void addJob(Job job) {

        Set<Integer> set = jobStatus.get(job.getStatus());
        if (!set.contains(job.getId())) {
            for (JobStatus status : jobStatus.keySet()) {
                if (status != job.getStatus()) {
                    Set<Integer> s = jobStatus.get(status);
                    if (s.contains(job.getId())) {
                        s.remove(job.getId());
                        jobStatusLayout.get(status).remove(job.getId(), status);
                        break;
                    }
                }
            }
            set.add(job.getId());
            JobStatusLayout layout = jobStatusLayout.get(job.getStatus());
            layout.add(job.getId());
        }
    }

    /**
     *
     * @param selected
     */
    public void setSelected(boolean selected) {

        if (this.selected) {
            this.selected = false;
            setBackgroundColor("#FFFFFF");
            displayLayout.removeMembers(displayLayout.getMembers());
            
        } else {
            this.selected = selected;

            if (selected) {
                setBackgroundColor("#F5F5F5");

                for (String command : commandsMap.keySet()) {
                    if (!getContents().equals("Debug: " + command)) {
                        commandsMap.get(command).setSelected(false);
                    }
                }
                displayLayout.removeMembers(displayLayout.getMembers());

                for (JobStatus status : JobStatus.values()) {
                    displayLayout.addMember(jobStatusLayout.get(status));
                }
                displayLayout.addMember(WidgetUtil.getBlankLayout());

            } else {
                setBackgroundColor("#FFFFFF");
            }
        }
    }
}
