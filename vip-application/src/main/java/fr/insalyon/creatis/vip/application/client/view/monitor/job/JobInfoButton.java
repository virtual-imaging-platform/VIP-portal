/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobInfoButton extends HLayout {

    private boolean isSelected;
    private Img img;
    private Label messageLabel;

    public JobInfoButton() {

        this.isSelected = false;

        this.setMembersMargin(10);
        this.setWidth(300);
        this.setHeight(50);
        this.setBorder("1px solid #CCCCCC");
        this.setPadding(10);
        this.setCursor(Cursor.HAND);
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#F7F7F7");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (isSelected) {
                    setBackgroundColor("#F7F7F7");
                } else {
                    setBackgroundColor("#FFFFFF");
                }
            }
        });

        img = new Img(ApplicationConstants.ICON_MONITOR_TASK_OK);
        img.setWidth(32);
        img.setHeight(32);
        this.addMember(img);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight(40);
        vLayout.setCursor(Cursor.HAND);
        this.addMember(vLayout);

        messageLabel = WidgetUtil.getLabel("<strong>Loading...</strong>", 40);
        vLayout.addMember(messageLabel);
    }

    public void setStatus(int status) {

        switch (status) {
            case 1:
                messageLabel.setContents("<strong>Non-critical errors detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_NONCRITICAL);
                break;
            case 2:
                messageLabel.setContents("<strong>Critical errors detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_CRITICAL);
                break;
            case 3:
                messageLabel.setContents("<strong>Held jobs detected.</strong><br />"
                        + "<font color=\"#666666\"><em>Click to debug.</em></font>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_NONCRITICAL);
                break;
            default:
                messageLabel.setContents("<strong>No errors detected.</strong>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_OK);
                break;
        }
    }

    public void setSelected(boolean selected) {

        this.isSelected = selected;
        if (selected) {
            this.setBackgroundColor("#F7F7F7");
        } else {
            this.setBackgroundColor("#FFFFFF");
        }
    }
}
