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

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesOutputFile;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;

import java.util.Set;

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
