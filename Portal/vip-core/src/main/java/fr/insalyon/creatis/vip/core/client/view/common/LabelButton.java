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
package fr.insalyon.creatis.vip.core.client.view.common;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
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
    public void setDisabled(boolean disabled) {

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
