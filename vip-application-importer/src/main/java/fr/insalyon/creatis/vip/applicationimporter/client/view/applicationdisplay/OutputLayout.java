package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import java.util.Set;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesOutputFile;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;

public class OutputLayout extends InputOutputLayout {

    public OutputLayout(String width, String height) {
        super("Application Outputs", Constants.ICON_OUTPUT, width, height);
    }

    public void setOutputFiles(Set<BoutiquesOutputFile> outputFiles) {

        stackSection.clear();

        for (BoutiquesOutputFile bof : outputFiles) {
            SectionStackSection section = new SectionStackSection();

            String title = bof.getName();
            if (bof.isList()) {
                title += " []";
            }
            if (!bof.isOptional()) {
                title += " <font color=\"red\">(*)</font>";
            }
            section.setTitle("<strong>" + title + "</strong>");

            HLayout generalLayout = new HLayout();
            generalLayout.addMember(new LocalTextField("Name", false, false, bof.getName()));
            generalLayout.addMember(new LocalTextField("List?", false, false, bof.isList() ? "True" : "False"));
            generalLayout.addMember(new LocalTextField("Optional?", false, false, bof.isOptional() ? "True" : "False"));
            generalLayout.setMembersMargin(membersMargin);

            HLayout commandLayout = new HLayout();
            if (bof.getValueKey() != null) {
                commandLayout.addMember(new LocalTextField("Value key", false, false, bof.getValueKey()));
            }
            if (bof.getCommandLineFlag() != null && !bof.getCommandLineFlag().equals("")) {
                commandLayout.addMember(new LocalTextField("Command-line Flag", false, false, bof.getCommandLineFlag()));
            }
            commandLayout.setMembersMargin(membersMargin);

            VLayout largeLayout = new VLayout();
            largeLayout.addMember(new LocalTextField("Path template", false, false, bof.getPathTemplate()));
            if (bof.getDescription() != null) {
                largeLayout.addMember(new LocalTextField("Description", false, false, bof.getDescription()));
            }
            largeLayout.setMembersMargin(membersMargin);

            if (commandLayout.getMembers().length > 0) {
                section.setItems(generalLayout, commandLayout, largeLayout);
            } else {
                section.setItems(generalLayout, largeLayout);
            }
            section.setExpanded(false);
            section.setCanCollapse(true);
            stackSection.addSection(section);
        }
    }
}
