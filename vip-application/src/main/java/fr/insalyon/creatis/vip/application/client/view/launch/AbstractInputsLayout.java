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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractInputsLayout extends VLayout {

    protected ModalWindow modal;
    protected String tabID;

    public AbstractInputsLayout(String tabID, String title, String icon) {

        this.tabID = tabID;

        this.setWidth100();
        this.setHeight100();
        this.setBorder("1px solid #C0C0C0");
        this.setBackgroundColor("#F5F5F5");
        this.setPadding(10);
        this.setMembersMargin(5);

        Label titleLabel = WidgetUtil.getLabel("<b>" + title + "</b>", icon, 15);
        titleLabel.setWidth100();
        titleLabel.setAlign(Alignment.LEFT);

        Label closeLabel = WidgetUtil.getLabel("<font color=\"#B0B0B0\">Close</font>", CoreConstants.ICON_CLOSE, 15, Cursor.HAND);
        closeLabel.setWidth(50);
        closeLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                destroy();
            }
        });

        HLayout titleLayout = new HLayout(5);
        titleLayout.addMember(titleLabel);
        titleLayout.addMember(closeLabel);
        this.addMember(titleLayout);
    }

    public abstract void loadData();
}
