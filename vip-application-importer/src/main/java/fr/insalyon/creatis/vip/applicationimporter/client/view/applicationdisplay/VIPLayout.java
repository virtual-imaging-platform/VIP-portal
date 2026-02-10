package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.Arrays;
import java.util.List;

public class VIPLayout extends AbstractFormLayout {

    private final CheckboxItem overwriteIfexists;
    private final SelectItem resourcesList;

    public VIPLayout(String width, String height) {
        super(width, height);
        addTitle("Executable", Constants.ICON_EXECUTABLE);
        setOverflow(Overflow.AUTO);
        setMembersMargin(2);

        overwriteIfexists = new CheckboxItem("ckbox_over", "Overwrite application version if it exists");

        // Resources allowed
        resourcesList = new SelectItem();
        resourcesList.setTitle("Resource(s) on which the application is authorized to execute");
        resourcesList.setMultiple(true);

        this.addMember(FieldUtil.getForm(resourcesList));
        this.addMember(FieldUtil.getForm(overwriteIfexists));

        loadResources();
    }

    public boolean getOverwrite() {
        return this.overwriteIfexists.getValueAsBoolean();
    }

    public List<String> getSelectedResources() {
        return Arrays.asList(resourcesList.getValues());
    }

    private void loadResources() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Resource>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load resources:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Resource> result) {
                String[] data = result.stream().map((e) -> e.getName()).toArray(String[]::new);
                resourcesList.setValueMap(data);
            }
        };
        service.getResources(callback);
    }
}
