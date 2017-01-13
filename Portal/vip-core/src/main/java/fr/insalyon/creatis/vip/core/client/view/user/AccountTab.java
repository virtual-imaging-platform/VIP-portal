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
        VLayout leftLayout = new VLayout(14);
        leftLayout.setWidth(350);
        leftLayout.setHeight100();

        leftLayout.addMember(new PersonalLayout());
        leftLayout.addMember(new PasswordLayout());
        leftLayout.addMember(new ApikeyLayout());

        // Right column
        VLayout rightLayout = new VLayout(14);
        rightLayout.setWidth("*");
        rightLayout.setHeight100();

        rightLayout.addMember(new GroupLayout());

        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(CoreConstants.GROUP_DROPBOX)) {
            rightLayout.addMember(new DropboxLayout());
        }
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
