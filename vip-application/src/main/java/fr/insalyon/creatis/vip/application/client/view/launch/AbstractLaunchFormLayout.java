package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.Map;

public abstract class AbstractLaunchFormLayout extends AbstractFormLayout {
    public AbstractLaunchFormLayout(String width, String height) {
        super(width, height);
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
                if (result != null && !result.isEmpty()) {
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

    /**
     * Return a layout containing given buttons
     *
     * @param margin  int margin of the buttons layout
     * @param buttons IButton... to put in returned layout
     * @return        HLayout containing added buttons
     */
    protected static HLayout getButtonLayout(int margin, IButton... buttons) {
        HLayout buttonsLayout = new HLayout(5);
        buttonsLayout.setAlign(VerticalAlignment.CENTER);
        buttonsLayout.setMargin(margin);

        for (IButton button : buttons) {
            buttonsLayout.addMember(button);
        }
        return buttonsLayout;
    }

    public abstract void loadInputs(String simulationName, Map<String, String> values);

    public abstract Map<String, String> getParametersMap();

    public abstract String getSimulationName();

    public abstract boolean validate();
}
