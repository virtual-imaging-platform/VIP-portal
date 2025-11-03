package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class InputsLayout extends VLayout {

    private AbstractInputsLayout mainLayout;
    private String tabID;
    private String applicationName;

    public InputsLayout(String tabID, String applicationName, boolean showExamples) {

        this.tabID = tabID;
        this.applicationName = applicationName;
        this.setMembersMargin(10);

        configureButtons(showExamples);
    }

    private void configureButtons(boolean showExamples) {

        HLayout buttonsLayout = new HLayout(5);
        buttonsLayout.setWidth100();

        Label savedInputsLabel = WidgetUtil.getLabel("Saved Inputs", CoreConstants.ICON_SAVED, 15, Cursor.HAND);
        savedInputsLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (mainLayout != null) {
                    mainLayout.destroy();
                }
                mainLayout = new SavedInputsLayout(tabID, applicationName);
                addMember(mainLayout);
            }
        });
        buttonsLayout.addMember(savedInputsLabel);

        if (showExamples) {
            Label examplesLabel = WidgetUtil.getLabel("Examples", CoreConstants.ICON_EXAMPLE, 15, Cursor.HAND);
            examplesLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (mainLayout != null) {
                        mainLayout.destroy();
                    }
                    mainLayout = new ExampleInputsLayout(tabID, applicationName);
                    addMember(mainLayout);
                }
            });
            buttonsLayout.addMember(examplesLabel);
        }

        this.addMember(buttonsLayout);
    }

    public void loadData() {

        if (mainLayout != null) {
            mainLayout.loadData();
        }
    }
}
