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
package fr.insalyon.creatis.vip.core.client.view.util;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class FieldUtil {

    /**
     * Gets a ListGridField configured to display an icon.
     *
     * @param name Field name
     * @return List grid field
     */
    public static ListGridField getIconGridField(String name) {

        ListGridField iconField = new ListGridField(name, " ", 30);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLSuffix(".png");
        iconField.setImageWidth(12);
        iconField.setImageHeight(12);
        return iconField;

    }

    /**
     * Gets a ListGridField configured to display dates.
     *
     * @return List grid field
     */
    public static ListGridField getDateField() {

        return getDateField("date", "Date");
    }

    /**
     * Gets a ListGridField configured to display dates.
     *
     * @param name
     * @param title
     * @return
     */
    public static ListGridField getDateField(String name, String title) {

        ListGridField dateField = new ListGridField(name, title, 120);
        dateField.setType(ListGridFieldType.DATE);
        dateField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);
        return dateField;
    }

    /**
     * Gets a DynamicForm with the specified list of items set.
     *
     * @param items List of form items
     * @return
     */
    public static DynamicForm getForm(FormItem... items) {

        DynamicForm form = new DynamicForm();
        form.setWidth100();

        form.setFields(items);
        return form;
    }
    public static DynamicForm getFormOneColumnResponsiveHeight(FormItem... items) {

        DynamicForm form = new DynamicForm();
        form.setWidth100();
        form.setHeight("*");
        form.setNumCols(1);
        form.setFields(items);
        return form;
    }

    public static DynamicForm getFormOneColumn(FormItem... items) {

        DynamicForm form = new DynamicForm();
        form.setWidth100();
        form.setNumCols(1);
        form.setFields(items);
        return form;
    }

    /**
     * Gets a TextItem configured according to the provided parameters.
     *
     * @param size Field size
     * @param keyPressFilter Regular expression filter
     * @return
     */
    public static TextItem getTextItem(int size, String keyPressFilter) {

        return getTextItem(size, false, "", keyPressFilter, false);
    }

    public static TextItem getTextItem(String size, String keyPressFilter) {

        return getTextItem(size, false, "", keyPressFilter, false);
    }
    /**
     * Gets a TextItem configured according to the provided parameters.
     *
     * @param size Field size
     * @param keyPressFilter Regular expression filter
     * @param disabled Whether the text item is disabled
     * @return
     */
    public static TextItem getTextItem(int size, String keyPressFilter,
            boolean disabled) {

        return getTextItem(size, false, "", keyPressFilter, disabled);
    }

    /**
     * Gets a TextItem configured according to the provided parameters.
     *
     * @param size Field size
     * @param showTitle If title should be displayed
     * @param title Title to be displayed
     * @param keyPressFilter Regular expression filter
     * @return
     */
    public static TextItem getTextItem(int size, boolean showTitle, String title,
            String keyPressFilter) {

        return getTextItem(size, showTitle, title, keyPressFilter, false);
    }

    /**
     * Gets a TextItem configured according to the provided parameters.
     *
     * @param size Field size
     * @param showTitle If title should be displayed
     * @param title Title to be displayed
     * @param keyPressFilter Regular expression filter
     * @param disabled Whether the text item is disabled
     * @return
     */
    public static TextItem getTextItem(int size, boolean showTitle, String title,
            String keyPressFilter, boolean disabled) {

        TextItem textItem = new TextItem();
        textItem.setTitle(title);
        textItem.setShowTitle(showTitle);
        textItem.setWidth(size);
        textItem.setKeyPressFilter(keyPressFilter);
        textItem.setAlign(Alignment.LEFT);
        textItem.setRequired(true);
        textItem.setDisabled(disabled);
        textItem.addChangedHandler(new ChangedHandler() {
            
            @Override
            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });

        return textItem;
    }

    public static TextItem getTextItem(String size, boolean showTitle, String title,
            String keyPressFilter, boolean disabled) {

        TextItem textItem = new TextItem();
        textItem.setTitle(title);
        textItem.setShowTitle(showTitle);
        textItem.setWidth(size);
        textItem.setKeyPressFilter(keyPressFilter);
        textItem.setAlign(Alignment.LEFT);
        textItem.setRequired(true);
        textItem.setDisabled(disabled);
        textItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });

        return textItem;
    }

    public static TextItem getTextItem(String size, boolean showTitle, String title,
            String keyPressFilter, boolean disabled, boolean required) {

        TextItem textItem = new TextItem();
        textItem.setTitle(title);
        textItem.setShowTitle(showTitle);
        textItem.setWidth(size);
        textItem.setKeyPressFilter(keyPressFilter);
        textItem.setAlign(Alignment.LEFT);
        textItem.setRequired(required);
        textItem.setDisabled(disabled);
        textItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });

        return textItem;
    }

    /**
     * Gets a PasswordItem configured according to the provided parameters.
     *
     * @param name Field name
     * @param title Title to be displayed
     * @param width Field size
     * @param length Field maximum length
     * @return
     */
    public static PasswordItem getPasswordItem(int width, int length) {

        PasswordItem passwordItem = new PasswordItem();
        passwordItem.setWidth(width);
        passwordItem.setLength(length);
        passwordItem.setShowTitle(false);
        passwordItem.setRequired(true);

        return passwordItem;
    }

    /**
     * Gets a CellFormatter to parse file sizes.
     *
     * @return
     */
    public static CellFormatter getSizeCellFormatter() {

        return new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {

                if (value == null) {
                    return null;
                }

                long length = ((Number) value).longValue();
                if (length > 0) {
                    String size = length + " B";
                    NumberFormat nf = NumberFormat.getFormat("#.##");
                    if (length / 1024 > 0) {
                        if (length / (1024 * 1024) > 0) {
                            if (length / (1024 * 1024 * 1024) > 0) {
                                size = nf.format(length / (double) (1024 * 1024 * 1024)) + " GB";
                            } else {
                                size = nf.format(length / (double) (1024 * 1024)) + " MB";
                            }
                        } else {
                            size = nf.format(length / (double) 1024) + " KB";
                        }
                    }
                    return size;

                } else {
                    return "";
                }
            }
        };
    }

    /**
     * Gets an ImgButton for RollOverCanvas
     *
     * @param imgSrc
     * @param prompt
     * @param clickHandler
     * @return
     */
    public static ImgButton getImgButton(String imgSrc, String prompt,
            ClickHandler clickHandler) {

        ImgButton button = new ImgButton();
        button.setShowDown(false);
        button.setShowRollOver(false);
        button.setLayoutAlign(Alignment.CENTER);
        button.setSrc(imgSrc);
        button.setPrompt(prompt);
        button.setHeight(16);
        button.setWidth(16);
        button.addClickHandler(clickHandler);

        return button;
    }

    /**
     * Gets a Link Item according to the provided parameters.
     *
     * @param name
     * @param title
     * @param clickHandler
     * @return
     */
    public static LinkItem getLinkItem(String name, String title,
            com.smartgwt.client.widgets.form.fields.events.ClickHandler clickHandler) {

        LinkItem link = new LinkItem(name);
        link.setShowTitle(false);
        link.setLinkTitle(title);
        link.addClickHandler(clickHandler);
        return link;
    }
}
