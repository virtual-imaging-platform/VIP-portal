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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.SelectItem;

import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

public class GeneralLayout extends AbstractFormLayout {

    private final LocalTextField name,
            commandLine,
            dockerImage,
            dockerIndex,
            version,
            schemaVersion,
            description,
            vipContainer,
            dotInputs;

    private final SelectItem tags;

    public GeneralLayout(String width, String height) {
        super(width, height);

        addTitle("General Information", Constants.ICON_INFORMATION);
        setMembersMargin(2);
        setOverflow(Overflow.AUTO);

        name = new LocalTextField("Application Name", false, false);
        commandLine = new LocalTextField("Command Line", false, false);
        dockerImage = new LocalTextField("Docker Image", false, false);
        dockerIndex = new LocalTextField("Docker Index", false, false);
        version = new LocalTextField("Version", false, false);
        schemaVersion = new LocalTextField("Schema Version", false, false);
        description = new LocalTextField("Description", false, false);
        vipContainer = new LocalTextField("VIP Container", false, false);
        dotInputs = new LocalTextField("DOT Inputs", false, false);

        tags = new SelectItem();
        tags.setTitle("Tags associated");
        tags.setMultiple(true);

        this.addMembers(name, commandLine, dockerImage, dockerIndex, version, schemaVersion, description, vipContainer, dotInputs);
        this.addMember(FieldUtil.getForm(tags));
    
        loadTags();
    }

    public void setTool(BoutiquesApplication bt) {
        name.setValue(bt.getName());
        version.setValue(bt.getToolVersion());
        description.setValue(bt.getDescription());
        commandLine.setValue(bt.getCommandLine());
        dockerImage.setValue(bt.getContainerImage());
        dockerIndex.setValue(bt.getContainerIndex());
        schemaVersion.setValue(bt.getSchemaVersion());
        vipContainer.setValue(bt.getVipContainer());
        String dotInputsValue = String.join(", ", bt.getVipDotInputIds());
        dotInputs.setValue(dotInputsValue + (bt.getVipDotIncludesResultsDir() ? (dotInputsValue.isEmpty() ? "results-directory" : ", results-directory") : ""));
    }

    private void loadTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                String[] data = result.stream().map((e) -> e.getName()).toArray(String[]::new);
                tags.setValueMap(data);
            }
        };
        service.getTags(callback);
    }

    public List<String> getSelectedTags() {
        return Arrays.asList(tags.getValues());
    }
}
