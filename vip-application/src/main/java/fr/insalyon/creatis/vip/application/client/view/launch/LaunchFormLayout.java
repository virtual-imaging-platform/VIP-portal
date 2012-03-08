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

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchFormLayout extends AbstractFormLayout {

    private TextItem simulationNameItem;
    private VLayout sourcesLayout;

    public LaunchFormLayout(String title, String icon, final String description) {

        super("600", "*");
        addTitle(title, icon);

        Label docLabel = WidgetUtil.getLabel("Documentation and Terms of Use", 
                CoreConstants.ICON_INFORMATION, 30, Cursor.HAND);
        docLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                new DocumentationLayout(event.getX(), event.getY(), description).show();
            }
        });
        this.addMember(docLabel);

        simulationNameItem = FieldUtil.getTextItem(400, "[0-9A-Za-z-_ ]");
        addField("Simulation Name", simulationNameItem);

        sourcesLayout = new VLayout(5);
        sourcesLayout.setAutoHeight();
        this.addMember(sourcesLayout);

        modal = new ModalWindow(this);
    }

    /**
     *
     * @param inputHLayout
     */
    public void addSource(InputHLayout inputHLayout) {

        Label sourceLabel = WidgetUtil.getLabel("<b>" + inputHLayout.getName() + "</b>", 15);
        String comment = inputHLayout.getComment();
        if (comment != null) {
            sourceLabel.setTooltip(comment);
            sourceLabel.setHoverWidth(500);
        }
        sourcesLayout.addMember(sourceLabel);
        sourcesLayout.addMember(inputHLayout);
    }

    /**
     *
     * @param buttons
     */
    public void addButtons(IButton... buttons) {

        HLayout buttonsLayout = new HLayout(5);
        buttonsLayout.setAlign(VerticalAlignment.CENTER);
        buttonsLayout.setMargin(20);

        for (IButton button : buttons) {
            buttonsLayout.addMember(button);
        }

        this.addMember(buttonsLayout);
    }

    /**
     *
     * @return
     */
    public String getSimulationName() {

        return simulationNameItem.getValueAsString().trim();
    }

    /**
     *
     * @return
     */
    public boolean validate() {

        boolean valid = simulationNameItem.validate();
        for (Canvas canvas : sourcesLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                if (!input.validate()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    /**
     *
     * @param simulationName
     * @param valuesMap
     */
    public void loadInputs(String simulationName, Map<String, String> valuesMap) {

        this.simulationNameItem.setValue(simulationName);

        final Map<String, String> conflictMap = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();

        for (Canvas canvas : sourcesLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                final InputHLayout input = (InputHLayout) canvas;
                final String inputValue = valuesMap.get(input.getName());

                if (inputValue != null) {
                    if (input.getValue() == null || input.getValue().isEmpty()) {
                        input.setValue(inputValue);

                    } else {
                        conflictMap.put(input.getName(), inputValue);
                    }
                } else {
                    sb.append("Could not find value for parameter \"");
                    sb.append(input.getName()).append("\".<br />");
                }
            }
        }
        if (!conflictMap.isEmpty()) {
            SC.ask("The following fields already have a value.<br />"
                    + "Do you want to replace them?<br />"
                    + "Fields: " + conflictMap.keySet(), new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value) {
                        for (Canvas canvas : sourcesLayout.getMembers()) {
                            if (canvas instanceof InputHLayout) {
                                InputHLayout input = (InputHLayout) canvas;
                                input.setValue(conflictMap.get(input.getName()));
                            }
                        }
                    }
                }
            });
        }
        if (sb.length() > 0) {
            SC.warn(sb.toString());
        }
    }

    /**
     * Sets a value to an input name. The value should be in the following
     * forms:
     *
     * For single list field: a string For multiple list fields: strings
     * separated by '; ' For ranges: an string like 'Start: 0 - Stop: 0 - Step:
     * 0'
     *
     * @param inputName
     * @param value
     */
    public void setInputValue(String inputName, String value) {

        for (Canvas canvas : sourcesLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                if (input.getName().equals(inputName)) {
                    input.setValue(value);
                }
            }
        }
    }

    /**
     * Gets a map of parameters.
     *
     * @return Map of parameters
     */
    public Map<String, String> getParametersMap() {

        Map<String, String> paramsMap = new HashMap<String, String>();

        for (Canvas canvas : sourcesLayout.getMembers()) {
            if (canvas instanceof InputHLayout) {
                InputHLayout input = (InputHLayout) canvas;
                paramsMap.put(input.getName(), input.getValue());
            }
        }
        return paramsMap;
    }

    public ModalWindow getModal() {
        return modal;
    }
}
