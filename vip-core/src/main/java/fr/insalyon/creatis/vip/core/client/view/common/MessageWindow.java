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
    private Canvas canvas;

    public MessageWindow(final Canvas canvas) {

        this.canvas = canvas;

        panel = new HLayout();
        panel.hide();

        panel.setOpacity(75);
        panel.setBackgroundColor("#FFFFFF");
        panel.setBorder("1px solid #CCCCCC");
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

    /**
     * Removes all components from the panel.
     */
    private void clear() {

        for (Canvas c : panel.getMembers()) {
            panel.removeMember(c);
            c.destroy();
        }
    }

    /**
     * Creates a label and adds it to the message panel.
     *
     * @param title Label title
     * @param icon Label icon
     */
    private void createLabel(String title, String icon) {

        messageLabel = new Label();
        messageLabel.setWrap(false);
        messageLabel.setWidth(1);
        messageLabel.setHeight(1);
        messageLabel.setContents(title);
        messageLabel.setCanSelectText(true);
        messageLabel.setIcon(icon);
        messageLabel.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {
                messageLabel.setWidth(messageLabel.getVisibleWidth());
                messageLabel.setMargin(5);
                panel.setWidth(messageLabel.getWidth());
                panel.setHeight(messageLabel.getHeight());
                panel.moveTo(getPanelXPosition(canvas), 2);
            }
        });
        panel.addMember(messageLabel);
    }

    /**
     * Creates a close image button to close the panel.
     */
    private void createCloseImg() {

        Img closeImg = new Img(CoreConstants.ICON_CLOSE, 16, 16);
        closeImg.setCursor(Cursor.HAND);
        closeImg.setPrompt("Close");
        closeImg.setMargin(2);
        closeImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                panel.animateFade(0);
            }
        });
        panel.addMember(closeImg);
    }

    /**
     * Displays a message in the panel.
     *
     * @param message Message to be displayed
     * @param bgColor Background color in hexadecimal
     * @param icon Message icon
     * @param delay Time in seconds the panel will appear
     */
    public void setMessage(String message, String bgColor, String icon, int delay) {

        clear();
        createLabel(message, icon);
        createCloseImg();
        if (delay > 0) {
            new Timer() {
                @Override
                public void run() {
                    hideMessage();
                }
            }.schedule(delay * 1000);
        }
        panel.setBackgroundColor(bgColor);
        panel.show();
        panel.animateFade(100);
    }

    public void hideMessage() {
        panel.animateFade(0);
    }
}
