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
package fr.insalyon.creatis.vip.core.client.view.util;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class WidgetUtil {

    /**
     *
     * @param contents
     * @param height
     * @return
     */
    public static Label getLabel(String contents, int height) {
        return getLabel(contents, null, height);
    }

    /**
     *
     * @param contents
     * @param icon
     * @param height
     * @return
     */
    public static Label getLabel(String contents, String icon, int height) {
        return getLabel(contents, icon, height, Cursor.ARROW);
    }

    /**
     *
     * @param contents
     * @param height
     * @param cursor
     * @return
     */
    public static Label getLabel(String contents, int height, Cursor cursor) {
        return getLabel(contents, null, height, cursor);
    }

    /**
     *
     * @param contents
     * @param icon
     * @param height
     * @param cursor
     * @return
     */
    public static Label getLabel(String contents, String icon, int height, Cursor cursor) {

        Label label = new Label(contents);
        label.setIcon(icon);
        label.setHeight(height);
        label.setCursor(cursor);
        return label;
    }

    /**
     *
     * @param height
     * @return
     */
    public static Label getSpaceLabel(int height) {

        Label label = new Label();
        label.setWidth100();
        label.setContents("");
        return label;
    }

    /**
     *
     * @param icon
     * @param prompt
     * @param height
     * @param clickHandler
     * @return
     */
    public static Label getIconLabel(String icon, String prompt, int height, ClickHandler clickHandler) {

        Label label = getLabel("", icon, height, Cursor.HAND);
        label.setWidth(height);
        label.setPrompt(prompt);
        label.addClickHandler(clickHandler);
        return label;
    }

    /**
     *
     * @param width
     * @return
     */
    public static VLayout getVIPLayout(int width) {

        return getVIPLayout(width, "100%", true);
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public static VLayout getVIPLayout(int width, int height) {

        return getVIPLayout(width, Integer.toString(height), true);
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public static VLayout getVIPLayout(int width, String height) {

        return getVIPLayout(width, height, true);
    }

    /**
     *
     * @param width
     * @param height
     * @param showTitle
     * @return
     */
    public static VLayout getVIPLayout(int width, int height, boolean showTitle) {

        return getVIPLayout(width, Integer.toString(height), showTitle);
    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public static VLayout getVIPLayout(int width, String height, boolean showTitle) {

        VLayout layout = new VLayout(5);
        layout.setWidth(width);
        layout.setHeight(height);
        layout.setBorder("1px solid #C0C0C0");
        layout.setBackgroundColor("#F5F5F5");
        layout.setPadding(10);

        if (showTitle) {
            Label vipLabel = getLabel("<font color=\"#C0C0C0\"><b>Virtual Imaging Platform</b></font>", 15);
            vipLabel.setWidth100();
            vipLabel.setAlign(Alignment.RIGHT);
            layout.addMember(vipLabel);
        }

        return layout;
    }

    /**
     * Adds a FormItem to a VIP Layout.
     *
     * @param layout VIP Layout
     * @param title Field title
     * @param item Field object
     */
    public static void addFieldToVIPLayout(Layout layout, String title, FormItem item) {

        layout.addMember(getLabel("<b>" + title + "</b>", 15));
        layout.addMember(FieldUtil.getForm(item));
    }

    /**
     * Gets a ToolStrip configured to display an icon and a prompt, and to
     * perform an action.
     *
     * @param icon Icon to be displayed
     * @param prompt Prompt message
     * @param clickHandler Action to be performed
     * @return
     */
    public static ToolStripButton getToolStripButton(String icon, String prompt,
            ClickHandler clickHandler) {

        return getToolStripButton(null, icon, prompt, clickHandler);
    }

    /**
     * Gets a ToolStrip configured to display a title, an icon and a prompt, and
     * to perform an action.
     *
     * @param title Button title
     * @param icon Button icon
     * @param prompt Prompt message
     * @param clickHandler Action to be performed
     * @return
     */
    public static ToolStripButton getToolStripButton(String title, String icon, String prompt,
            ClickHandler clickHandler) {

        ToolStripButton button = title == null ? new ToolStripButton() : new ToolStripButton(title);
        button.setIcon(icon);
        button.setPrompt(prompt);
        button.addClickHandler(clickHandler);
        return button;
    }

    /**
     * Sets a toolstrip button to a loading state.
     *
     * @param button Button object
     * @param title Button title
     */
    public static void setLoadingToolStripButton(ToolStripButton button, String title) {

        button.setTitle(title);
        button.setIcon(CoreConstants.ICON_LOADING);
        button.setDisabled(true);
    }

    /**
     * Resets a toolstrip button to its initial state.
     *
     * @param button Button object
     * @param title Button title
     * @param icon Button icon
     */
    public static void resetToolStripButton(ToolStripButton button, String title, String icon) {

        button.setTitle(title);
        button.setIcon(icon);
        button.setDisabled(false);
    }

    /**
     * Gets an IButton configured to display a title, an icon and perform an
     * action.
     *
     * @param title Button title
     * @param icon Button icon
     * @param clickHandler Action to be performed
     * @return
     */
    public static IButton getIButton(String title, String icon,
            ClickHandler clickHandler) {

        IButton button = new IButton(title);
        button.setIcon(icon);
        button.setShowDisabledIcon(false);
        button.addClickHandler(clickHandler);
        return button;
    }

    /**
     * Sets a button to a loading state.
     *
     * @param button Button object
     * @param title Button title
     */
    public static void setLoadingIButton(IButton button, String title) {

        button.setTitle(title);
        button.setIcon(CoreConstants.ICON_LOADING);
        button.setDisabled(true);
    }

    /**
     * Resets a button to its initial state.
     *
     * @param button Button object
     * @param title Button title
     * @param icon Button icon
     */
    public static void resetIButton(IButton button, String title, String icon) {

        button.setTitle(title);
        button.setIcon(icon);
        button.setDisabled(false);
    }

    /**
     * Gets a blank layout.
     * 
     * @return 
     */
    public static VLayout getBlankLayout() {
        
        VLayout blankLayout = new VLayout();
        blankLayout.setWidth100();
        blankLayout.setHeight100();
        return blankLayout;
    }
}
