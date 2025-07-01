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
package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;

import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
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
