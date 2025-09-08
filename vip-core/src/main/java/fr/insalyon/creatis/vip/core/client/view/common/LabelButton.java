package fr.insalyon.creatis.vip.core.client.view.common;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LabelButton extends Label {

    private String label;
    private boolean selected;

    public LabelButton(String label, String icon) {

        this.label = label;
        this.selected = false;
        this.setIcon(icon);
        this.setHeight(16);
        this.setPadding(3);
        this.setBorder("1px solid #E2E2E2");
        this.setAlign(Alignment.CENTER);
        this.setDisabled(false);
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                if (!isDisabled()) {
                    setBackgroundColor("#DEDEDE");
                }
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (!isDisabled()) {
                    if (selected) {
                        setBackgroundColor("#DEDEDE");
                    } else {
                        setBackgroundColor("#F2F2F2");
                    }
                }
            }
        });
    }

    @Override
    public Canvas setDisabled(boolean disabled) {

        super.setDisabled(disabled);
        if (disabled) {
            this.setContents("<font color=\"#CCCCCC\">" + label + "</font>");
            this.setBackgroundColor("#F9F9F9");
            this.setCursor(Cursor.AUTO);
        } else {
            this.setContents("<font color=\"#666666\">" + label + "</font>");
            this.setBackgroundColor("#F2F2F2");
            this.setCursor(Cursor.HAND);
        }
        return (this);
    }

    public void setSelected(boolean selected) {

        this.selected = selected;
        if (selected) {
            this.setBackgroundColor("#DEDEDE");
        } else {
            if (isDisabled()) {
                this.setBackgroundColor("#F9F9F9");
            } else {
                this.setBackgroundColor("#F2F2F2");
            }
        }
    }
}
