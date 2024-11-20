package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LFCPermissionBusinessTest {

    @AfterEach
    protected void tearDown() {
        // to reset user groups
        UserTestUtils.reset();
    }

    @Test
    public void testGroupAccess() throws BusinessException {
        String groupName="EGI tutorial";
        String path = "/vip/EGI tutorial (group)/inputs";
        Map<Group, CoreConstants.GROUP_ROLE> groups = new HashMap<>();
        UserTestUtils.baseUser1.setGroups(groups);
        UserTestUtils.baseUser2.setGroups(groups);
        LFCPermissionBusiness sut = new LFCPermissionBusiness(null, null);

        // First, test users does not belong to the group
        Assertions.assertFalse(sut.isLFCPathAllowed(UserTestUtils.baseUser1, path, LFCPermissionBusiness.LFCAccessType.UPLOAD, false));
        Assertions.assertFalse(sut.isLFCPathAllowed(UserTestUtils.baseUser2, path, LFCPermissionBusiness.LFCAccessType.UPLOAD, false));

        // Then, they belong to the group, but baseUser1 is beginner although baseUser2 is advanced
        groups.put(new Group(groupName, true, GroupType.getDefault()), CoreConstants.GROUP_ROLE.User);
        Assertions.assertFalse(sut.isLFCPathAllowed(UserTestUtils.baseUser1, path, LFCPermissionBusiness.LFCAccessType.UPLOAD, false));
        Assertions.assertTrue(sut.isLFCPathAllowed(UserTestUtils.baseUser2, path, LFCPermissionBusiness.LFCAccessType.UPLOAD, false));
    }
}
