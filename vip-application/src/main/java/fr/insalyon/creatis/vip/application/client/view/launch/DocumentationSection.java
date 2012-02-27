/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Tristan Glatard
 */
public class DocumentationSection extends SectionStackSection {

    private Label descriptionPane;

    public DocumentationSection(String applicationName) {
        
        this.setTitle("Documentation and terms of use");
        this.setCanCollapse(true);
        this.setExpanded(false);
        this.setResizeable(true);

        VLayout vLayout = new VLayout();
        vLayout.setMaxHeight(200);
        vLayout.setHeight100();
        vLayout.setWidth100();
        vLayout.setOverflow(Overflow.AUTO);

        descriptionPane = new Label();
        descriptionPane.setWidth100();
        descriptionPane.setHeight100();
        descriptionPane.setShowEdges(true);
        descriptionPane.setEdgeSize(5);
        descriptionPane.setPadding(5);
        descriptionPane.setValign(VerticalAlignment.TOP);

        vLayout.addMember(descriptionPane);
        this.addItem(vLayout);

        this.setExpanded(false);
        ImgButton docButton = new ImgButton();
        docButton.setSrc("docs/icon-information.png");
        docButton.setSize(16);
        docButton.setShowFocused(false);
        docButton.setShowRollOver(false);
        docButton.setShowDown(false);
        docButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                setExpanded(true);
            }
        });
        this.setControls(docButton);
    }

    public void setContents(String content) {
        descriptionPane.setContents(content);
    }
}
