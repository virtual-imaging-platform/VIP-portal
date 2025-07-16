package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.smartgwt.client.types.Overflow;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

public class GeneralLayout extends AbstractFormLayout {

    private final LocalTextField name,
            commandLine,
            dockerImage,
            version,
            description,
            vipContainer,
            dotInputs;

    public GeneralLayout(String width, String height) {
        super(width, height);

        addTitle("General Information", Constants.ICON_INFORMATION);
        setMembersMargin(2);
        setOverflow(Overflow.AUTO);

        name = new LocalTextField("Application Name", false, false);
        commandLine = new LocalTextField("Command Line", false, false);
        dockerImage = new LocalTextField("Docker Image", false, false);
        version = new LocalTextField("Version", false, false);
        description = new LocalTextField("Description", false, false);
        vipContainer = new LocalTextField("VIP Container", false, false);
        dotInputs = new LocalTextField("DOT Inputs", false, false);

        this.addMembers(name, version, description, commandLine, dockerImage, vipContainer, dotInputs);
    }

    public void setTool(BoutiquesApplication bt) {
        name.setValue(bt.getName());
        version.setValue(bt.getToolVersion());
        description.setValue(bt.getDescription());
        commandLine.setValue(bt.getCommandLine());
        dockerImage.setValue(bt.getContainerImage());
        vipContainer.setValue(bt.getVipContainer());
        String dotInputsValue = String.join(", ", bt.getVipDotInputIds());
        dotInputs.setValue(dotInputsValue + (bt.getVipDotIncludesResultsDir() ? (dotInputsValue.isEmpty() ? "results-directory" : ", results-directory") : ""));
    }
}
