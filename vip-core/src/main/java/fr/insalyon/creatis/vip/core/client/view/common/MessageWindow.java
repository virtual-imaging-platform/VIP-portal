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
package fr.insalyon.creatis.vip.core.client.view.common;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MessageWindow {

    private HLayout panel;
    private Label messageLabel;

    public MessageWindow(final Canvas canvas) {

        panel = new HLayout();
        panel.hide();

        panel.setOpacity(85);
        panel.setBackgroundColor("#FFFFFF");
        panel.setBorder("1px solid #CCCCCC");
        panel.setUseOpacityFilter(true);
        panel.setAnimateTime(1200);
        panel.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {
                panel.setWidth(messageLabel.getWidth());
                panel.setHeight(messageLabel.getHeight());
                panel.moveTo(getPanelXPosition(canvas), 2);
            }
        });

        canvas.addResizedHandler(new ResizedHandler() {
            @Override
            public void onResized(ResizedEvent event) {
                panel.moveTo(getPanelXPosition(canvas), 2);
            }
        });
    }

    /**
     * Gets the X axis position for the panel.
     * 
     * @param canvas
     * @return 
     */
    private int getPanelXPosition(Canvas canvas) {
        return (canvas.getVisibleWidth() / 2) - (panel.getWidth() / 2);
    }
    
    private void clear() {

        for (Canvas canvas : panel.getMembers()) {
            panel.removeMember(canvas);
            canvas.destroy();
        }
    }

    private void createLabel(String message) {

        messageLabel = new Label();
        messageLabel.setWrap(false);
        messageLabel.setWidth(1);
        messageLabel.setHeight(1);
        messageLabel.setContents(message);
        messageLabel.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {
                messageLabel.setWidth(messageLabel.getVisibleWidth());
                messageLabel.setMargin(5);
            }
        });
        panel.addMember(messageLabel);
    }

    private void createCloseImg() {

        Img closeImg = new Img(CoreConstants.ICON_CLOSE, 16, 16);
        closeImg.setCursor(Cursor.HAND);
        closeImg.setPrompt("Close");
        closeImg.setMargin(2);
        closeImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                panel.animateFade(0);
//                panel.hide();
            }
        });
        panel.addMember(closeImg);
    }

    /**
     * Displays a message in the panel.
     * 
     * @param message Message to be displayed
     * @param bgColor Background color in hexadecimal
     * @param delay Time in seconds the panel will appear
     */
    public void setMessage(String message, String bgColor, int delay) {

        clear();
        createLabel(message);
        createCloseImg();
        if (delay > 0) {
            new Timer() {
                @Override
                public void run() {
                    panel.animateFade(0);
//                    panel.hide();
                }
            }.schedule(delay * 1000);
        }
        panel.setBackgroundColor(bgColor);
        panel.show();
        panel.animateFade(100);
    }
}
