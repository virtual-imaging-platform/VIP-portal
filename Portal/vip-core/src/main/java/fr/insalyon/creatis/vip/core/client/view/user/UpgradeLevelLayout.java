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
package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Silva
 */
public class UpgradeLevelLayout extends VLayout {

    public UpgradeLevelLayout(int x, int y) {
        
        this.setWidth(500);
        this.setHeight(300);
        this.setMembersMargin(5);
        this.setPadding(5);
        this.setBackgroundColor("#FFFFFF");
        this.setBorder("1px solid #CCCCCC");
        this.setOpacity(85);
        this.moveTo(x, y + 20);
        
        Label titleLabel = new Label("<b>User Levels:</b>");
        titleLabel.setIcon(CoreConstants.ICON_USER_INFO);
        titleLabel.setHeight(30);
        this.addMember(titleLabel);
        
        HTMLPane pane = new HTMLPane();
        pane.setWidth100();
        pane.setHeight100();
        pane.setOverflow(Overflow.AUTO);
        pane.setContentsURL("./documentation/user/user_levels.html");  
        pane.setContentsType(ContentsType.PAGE);        
        this.addMember(pane);
        
        Label closeLabel = new Label("Close");
        closeLabel.setIcon(CoreConstants.ICON_CLOSE);
        closeLabel.setHeight(25);
        closeLabel.setWidth100();
        closeLabel.setAlign(Alignment.RIGHT);
        closeLabel.setCursor(Cursor.HAND);
        closeLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                destroy();
            }
        });

        this.addMember(closeLabel);
    }
}
