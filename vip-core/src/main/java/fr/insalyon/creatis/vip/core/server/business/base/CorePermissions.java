package fr.insalyon.creatis.vip.core.server.business.base;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

@Service
public class CorePermissions {

    private Supplier<User> uSupplier;

    @Autowired
    public CorePermissions(Supplier<User> uSupplier) {
        this.uSupplier = uSupplier;
    }

    public void checkLevel(UserLevel... authorizedLevels) throws BusinessException {
        User user = uSupplier.get();

        for (UserLevel level: authorizedLevels) {
            if (level.equals(user.getLevel())) {
                return;
            }
        }
        throw new BusinessException("You do not have the right to do that!");
    }

    public void checkOnlyUserPrivateGroups(List<Group> groupsToCheck) throws BusinessException {
        User user = uSupplier.get();
        Set<Group> userGroups = user.getGroups();

        if (user.getLevel().equals(UserLevel.Administrator) || groupsToCheck == null) {
            return;
        }
        for (Group group : groupsToCheck) {
            // check ONLY user groups and ONLY privates groups
            if ( ! userGroups.contains(group) || group.isPublicGroup()) {
                throw new BusinessException("You do not have the right to do that!");
            }
        }
    }
}
