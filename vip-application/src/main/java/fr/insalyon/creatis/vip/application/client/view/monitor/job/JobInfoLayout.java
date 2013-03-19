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
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobInfoLayout extends VLayout {

    private HLayout infoLayout;
    private Img img;
    private Label messageLabel;

    public JobInfoLayout() {

        this.setWidth100();
        this.setHeight100();

        infoLayout = new HLayout(10);
        infoLayout.setWidth(300);
        infoLayout.setHeight(50);
        infoLayout.setBorder("1px solid #CCCCCC");
        infoLayout.setPadding(10);
        infoLayout.setCursor(Cursor.HAND);
        this.addMember(infoLayout);

        img = new Img(ApplicationConstants.ICON_MONITOR_TASK_OK);
        img.setWidth(32);
        img.setHeight(32);
        infoLayout.addMember(img);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight(40);
        vLayout.setCursor(Cursor.HAND);
        infoLayout.addMember(vLayout);

        messageLabel = WidgetUtil.getLabel("<strong>No errors detected.</strong>", 40);
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
            default:
                messageLabel.setContents("<strong>No errors detected.</strong>");
                img.setSrc(ApplicationConstants.ICON_MONITOR_TASK_OK);
                break;
        }
    }

    public HLayout getInfoLayout() {
        
        return infoLayout;
    }
}
