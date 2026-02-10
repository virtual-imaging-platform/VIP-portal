package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.SectionStack;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author Tristan Glatard
 */
public class InputOutputLayout extends AbstractFormLayout {
 
    protected SectionStack stackSection;
    protected int membersMargin = 5;
    
    public InputOutputLayout(String title, String icon, String width, String height) {
        super(width, height);
        addTitle(title, icon);
        setOverflow(Overflow.AUTO);
        stackSection = new SectionStack();
        stackSection.setTitle("");
        stackSection.setShowHover(false);
        stackSection.setOverflow(Overflow.VISIBLE);
        addMember(stackSection);
    }
}
