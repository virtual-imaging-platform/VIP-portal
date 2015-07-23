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

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class InputLayout extends InputOutputLayout {

    public InputLayout(String width, String height) {
        super("Application Inputs", Constants.ICON_INPUT, width, height);
    }

    public void setInputs(List<BoutiquesInput> inputs) {
        stackSection.clear();
        for (BoutiquesInput bi : inputs) {
            SectionStackSection section = new SectionStackSection();
            String title = bi.getId();
            if (bi.isList()) {
                title += "[]";
            }
            if (!bi.isOptional()) {
                title += "<font color=\"red\">(*)</font>";
            }
            section.setTitle("<strong>" + title + "</strong>");

            HLayout generalLayout = new HLayout();
            generalLayout.addMember(new LocalTextField("Name", false, false, bi.getName()));
            generalLayout.addMember(new LocalTextField("Type", false, false, bi.getType()));
            generalLayout.addMember(new LocalTextField("List?", false, false, bi.isList() ? "True" : "False"));
            generalLayout.addMember(new LocalTextField("Optional?", false, false, bi.isOptional() ? "True" : "False"));
            generalLayout.setMembersMargin(membersMargin);

            HLayout commandLineLayout = new HLayout();
            commandLineLayout.addMember(new LocalTextField("Command-line key", false, false, bi.getCommandLineKey()));
            commandLineLayout.addMember(new LocalTextField("Command-line flag", false, false, bi.getCommandLineFlag()));
            commandLineLayout.addMember(new LocalTextField("Default value", false, false, bi.getDefaultValue()));
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
