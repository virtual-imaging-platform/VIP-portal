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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSourceLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LaunchFormLayout extends AbstractFormLayout {

    private static Logger logger = Logger.getLogger(LaunchFormLayout.class.getName());

    private TextItem simulationNameItem;
    private VLayout sourcesLayout;
    private VLayout executionNameLayout;

    public LaunchFormLayout(String title, String icon, final String description, boolean executionNamePadding) {
        super("600", "*");
        addTitle(title, icon);

        Label docLabel = WidgetUtil.getLabel("Documentation and Terms of Use",
                                             CoreConstants.ICON_INFORMATION, 30, Cursor.HAND);
        docLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new DocumentationLayout(event.getX(), event.getY(), description).show();
            }
        });
        this.addMember(docLabel);

        simulationNameItem = FieldUtil.getTextItem(400, "[0-9A-Za-z-_ ]");
        simulationNameItem.setValidators(ValidatorUtil.getStringValidator());
        simulationNameItem.setEditPendingCSSText("padding-left: 30px");

        if (executionNamePadding) { // To align horizontaly "Execution name" and the text field on the others comboBoxes (inputs comboBoxes) of the screen
                executionNameLayout = new VLayout(0);
                executionNameLayout.setLayoutLeftMargin(25);
                executionNameLayout.setWidth(300);
                executionNameLayout.addMember(WidgetUtil.getLabel("<b>Execution Name<font color=\"red\">*</font></b>", 15));
                executionNameLayout.addMember(FieldUtil.getForm(simulationNameItem));
                this.addMember(executionNameLayout);
        }
        else {
             addField("Execution Name<font color=\"red\">*</font>", simulationNameItem);
        }

        sourcesLayout = new VLayout(5);
        sourcesLayout.setAutoHeight();
        this.addMember(sourcesLayout);
    }

    // Constructor called by GateLab application
    public LaunchFormLayout(String title, String icon, final String description) {
        this(title, icon, description, false);
    }

    public void addSource(AbstractSourceLayout sourceLayout) {
        addSource(sourceLayout, false);
    }

    /**
     *
     * @param sourceLayout
     */
    public void addSource(AbstractSourceLayout sourceLayout, boolean disabled) {
        if (disabled) {
            sourceLayout.setDisabled(true);
        }
        sourcesLayout.addMember(sourceLayout);
    }

    /**
     *
     * @param buttons
     */
    @Override
    public void addButtons(IButton... buttons) {

        addButtons(20, buttons);
    }

    /**
     *
     * @param margin
     * @param buttons
     */
    public void addButtons(int margin, IButton... buttons) {

        HLayout buttonsLayout = new HLayout(5);
        buttonsLayout.setAlign(VerticalAlignment.CENTER);
        buttonsLayout.setMargin(margin);

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
            if (canvas instanceof AbstractSourceLayout) {
                AbstractSourceLayout source = (AbstractSourceLayout) canvas;
                   if (!source.validate()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    /**
     * @param simulationName
     * @param valuesMap
     */
    public void loadInputs(
        String simulationName, Map<String, String> valuesMap) {

        this.simulationNameItem.setValue(simulationName);

        List<AbstractSourceLayout> asls =
            Arrays.stream(sourcesLayout.getMembers())
                .filter(c -> c instanceof AbstractSourceLayout)
                .map(c -> (AbstractSourceLayout) c)
                .collect(Collectors.toList());

        String conflicts = asls.stream()
            .filter(
                asl -> {
                    String curValue = asl.getValue();
                    String newValue = valuesMap.get(asl.getName());
                    return newValue != null
                        && curValue != null
                        && !curValue.isEmpty()
                        && !curValue.equals(newValue)
                        && !asl.isValueModifiable();
                })
            .map(AbstractSourceLayout::getName)
            .collect(Collectors.joining(", "));

        if (conflicts.length() > 0) {
            SC.ask("The following fields already have a value.<br />"
                   + "Do you want to replace them?<br />"
                   + "Fields: " + conflicts,
                   b -> { if (b) updateInputs(asls, valuesMap); });
        } else {
            updateInputs(asls, valuesMap);
        }
    }

    private void updateInputs(
        List<AbstractSourceLayout> asls, Map<String, String> valuesMap) {

        asls.stream().forEach(
            asl -> {
                if (asl.isOptional()) {
                    asl.enableInput();
                }
                String newValue = valuesMap.get(asl.getName());
                asl.setValue(newValue);
            });
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
            if (canvas instanceof AbstractSourceLayout) {
                AbstractSourceLayout source = (AbstractSourceLayout) canvas;
                if (source.getName().equals(inputName)) {
                    source.setValue(value);
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
            if (canvas instanceof AbstractSourceLayout) {
                AbstractSourceLayout source = (AbstractSourceLayout) canvas;
                 if (source.isOptional()) {
                    if (source instanceof InputLayout) {
                        InputLayout inputLayoutSource = (InputLayout) source;
                        if (inputLayoutSource.isOptionalChecked()) {
                             paramsMap.put(source.getName(), source.getValue());
                        } else {
                             paramsMap.put(source.getName(), ApplicationConstants.INPUT_WITHOUT_VALUE);
                        }
                    } else {
                       paramsMap.put(source.getName(), source.getValue());
                    }
                 } else {
                     paramsMap.put(source.getName(), source.getValue());
                 }
            }
        }
        return paramsMap;
    }

    /**
     *
     * @param visible
     */
    public void setSourcesLayoutVisibible(boolean visible) {

        sourcesLayout.setVisible(visible);
    }

    /**
     *
     * @param applicationName
     */
    public void configureCitation(String applicationName) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load citation:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                if (result != null || !result.isEmpty() || !result.equals("")) {

                    VLayout citationLayout = new VLayout(5);
                    citationLayout.addMember(WidgetUtil.getLabel("<b>Please refer to the following publication:</b>", 20));

                    Label citation = new Label(result);
                    citation.setWidth100();
                    citation.setAutoHeight();
                    citation.setCanSelectText(true);
                    citation.setPadding(5);
                    citation.setBackgroundColor("#FFFFFF");
                    citation.setBorder("1px solid #CCCCCC");
                    citationLayout.addMember(citation);

                    addMember(citationLayout);
                }
            }
        };
        ApplicationService.Util.getInstance().getCitation(applicationName, callback);
    }
}
