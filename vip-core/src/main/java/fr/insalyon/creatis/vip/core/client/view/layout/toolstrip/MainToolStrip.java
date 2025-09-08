package fr.insalyon.creatis.vip.core.client.view.layout.toolstrip;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MainToolStrip extends ToolStrip {

    private static MainToolStrip instance;

    public static MainToolStrip getInstance() {
        if (instance == null) {
            instance = new MainToolStrip();
        }
        return instance;
    }

    private MainToolStrip() {

        this.setWidth100();
        this.setPadding(2);

        setUpLogoAndVersion();
    }

    public void reset() {

        for (Canvas c : getMembers()) {
            this.removeMember(c);
        }
        setUpLogoAndVersion();
    }

    private void setUpLogoAndVersion() {
        this.addMember(getVipLogo());
        this.addSeparator();
        this.addMember(getVipLabel());
        this.addSeparator();
    }

    private Img getVipLogo() {
        Img img = new Img(CoreConstants.ICON_VIP_LOGO_WITHOUT_TEXT);
        img.setWidth(25);
        img.setHeight(25);
        return img;
    }

    private Label getVipLabel() {

        Label vipLabel = new Label("VIP " + CoreConstants.VERSION);
        vipLabel.setAlign(Alignment.CENTER);
        vipLabel.setWidth(60);

        return vipLabel;
    }
}
