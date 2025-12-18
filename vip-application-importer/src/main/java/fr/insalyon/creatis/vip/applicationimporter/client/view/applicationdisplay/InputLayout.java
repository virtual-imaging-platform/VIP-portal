package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import java.util.Set;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;

import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;

/**
 *
 * @author Nouha Boujelben
 */
public class InputLayout extends InputOutputLayout {

    public InputLayout(String width, String height) {
        super("Application Inputs", Constants.ICON_INPUT, width, height);
    }

    public void setInputs(Set<BoutiquesInput> inputs) {
        stackSection.clear();
        for (BoutiquesInput bi : inputs) {
            SectionStackSection section = new SectionStackSection();
            String title = bi.getId();
            if (bi.isList()) {
                title += " []";
            }
            if (!bi.isOptional()) {
                title += " <font color=\"red\">(*)</font>";
            }
            section.setTitle("<strong>" + title + "</strong>");

            HLayout generalLayout = new HLayout();
            generalLayout.addMember(new LocalTextField("Name", false, false, bi.getName()));
            generalLayout.addMember(new LocalTextField("Type", false, false,  bi.getTypeString()));
            generalLayout.addMember(new LocalTextField("List?", false, false, bi.isList() ? "True" : "False"));
            generalLayout.addMember(new LocalTextField("Optional?", false, false, bi.isOptional() ? "True" : "False"));
            generalLayout.setMembersMargin(membersMargin);

            HLayout commandLineLayout = new HLayout();
            if (bi.getValueKey() != null) {
                commandLineLayout.addMember(new LocalTextField("Value key", false, false, bi.getValueKey()));
            }
            if (bi.getCommandLineFlag() != null && !bi.getCommandLineFlag().equals("")) {
                commandLineLayout.addMember(new LocalTextField("Command-line flag", false, false, bi.getCommandLineFlag()));
            }
            if (bi.getDefaultValue() != null) {
                commandLineLayout.addMember(new LocalTextField("Default value", false, false, bi.getDefaultValue().toString()));
            }
            commandLineLayout.setMembersMargin(membersMargin);

            HLayout descriptionLayout = new HLayout();
            descriptionLayout.addMember(new LocalTextField("Description", false, false, bi.getDescription()));
            descriptionLayout.setMembersMargin(membersMargin);

            section.setItems(generalLayout, descriptionLayout, commandLineLayout);
            section.setExpanded(false);
            section.setCanCollapse(true);
            stackSection.addSection(section);
        }
    }

}
