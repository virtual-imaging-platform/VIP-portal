package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.account.*;

/**
 *
 * @author Rafael Silva
 */
public class AccountTab extends Tab {

    private TermsOfUseLayout layouttermsOfUse;

    public AccountTab() {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_ACCOUNT) + " "
                + CoreConstants.APP_ACCOUNT);
        this.setID(CoreConstants.TAB_ACCOUNT);
        this.setCanClose(true);

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.setPadding(10);

        // Left column
        VLayout leftLayout = new VLayout(15);
        leftLayout.setWidth(350);
        leftLayout.setHeight100();

        leftLayout.addMember(new PersonalLayout());
        if (CoreModule.user.getNextEmail() != null) {
            leftLayout.addMember(new ConfirmNewEmailLayout());
        } else {
            leftLayout.addMember(new RequestNewEmailLayout());
        }
        leftLayout.addMember(new PasswordLayout());
        leftLayout.addMember(new ApikeyLayout());

        // Right column
        VLayout rightLayout = new VLayout(15);
        rightLayout.setWidth("*");
        rightLayout.setHeight100();

        rightLayout.addMember(new GroupLayout());

        layouttermsOfUse = new TermsOfUseLayout();
        rightLayout.addMember(new RemoveAccountLayout());
        rightLayout.addMember(layouttermsOfUse);
        hLayout.addMember(leftLayout);
        hLayout.addMember(rightLayout);

        this.setPane(hLayout);
    }

    public TermsOfUseLayout getLayouttermsOfUse() {
        return layouttermsOfUse;
    }
}
