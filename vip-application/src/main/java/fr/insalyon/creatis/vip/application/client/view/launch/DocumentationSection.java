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
