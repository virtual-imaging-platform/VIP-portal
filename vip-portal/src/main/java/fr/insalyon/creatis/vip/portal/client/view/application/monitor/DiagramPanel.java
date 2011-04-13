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
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import fr.insalyon.creatis.vip.common.client.view.Context;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class DiagramPanel extends Panel {

    private ToolbarTextItem lastUpdateItem;
    private Timer timer;
    private String workflowID;

    public DiagramPanel(String workflowID, boolean completed) {

        this.workflowID = workflowID;

        this.setTitle("Workflow diagram");
        this.setAutoScroll(true);
        this.setHtml("<img src=\"" + Context.getInstance().getMoteurServerHost() + "/workflows/" + workflowID + "/html/workflow-image.png\" />");

        Toolbar topToolbar = new Toolbar();
        topToolbar.addFill();
        lastUpdateItem = new ToolbarTextItem("Last updated on " + new Date());
        topToolbar.addItem(lastUpdateItem);
        this.setTopToolbar(topToolbar);

        timer = new Timer() {

            public void run() {
                updateImage();
            }
        };
        if (!completed) {
            timer.scheduleRepeating(30000);
        }
    }

    private void updateImage() {
        lastUpdateItem.setText("Last updated on " + new Date());
        this.setHtml("");
        this.doLayout();
        this.setHtml("<img src=\"" + Context.getInstance().getMoteurServerHost() + "/workflows/" + workflowID + "/html/workflow-image.png\" />");
        this.doLayout();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.destroy();
    }
}
