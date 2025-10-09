package fr.insalyon.creatis.vip.core.server.business.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;

@Service
public class CorePermissions {

    private Supplier<User> uSupplier;

    @Autowired
    public CorePermissions(Supplier<User> uSupplier) {
        this.uSupplier = uSupplier;
    }

    public void checkLevel(UserLevel... authorizedLevels) throws VipException {
        User user = uSupplier.get();

        for (UserLevel level: authorizedLevels) {
            if (level.equals(user.getLevel())) {
                return;
            }
        }
        throw new VipException(DefaultError.ACCESS_DENIED);
    }

    public void checkOnlyUserPrivateGroups(List<Group> groupsToCheck) throws VipException {
        User user = uSupplier.get();
        Set<Group> userGroups = user.getGroups();

        if (groupsToCheck == null) {
            return;
        }
        for (Group group : groupsToCheck) {
            // check ONLY user groups and ONLY privates groups
            if ( ! userGroups.contains(group) || group.isPublicGroup()) {
                throw new VipException(DefaultError.ACCESS_DENIED);
            }
        }
    }

    public List<Group> filterOnlyUserGroups(List<Group> toFilter) {
        User user = uSupplier.get();
        List<Group> result = new ArrayList<>();
        Set<Group> userGroups = user.getGroups();

        if (user.isSystemAdministrator()) {
            result.addAll(toFilter);
        } else {
            result = toFilter.stream().filter((g) -> userGroups.contains(g)).toList();
        }
        return result;
    }

    public <T> void checkItemInList(T item, List<T> list) throws VipException {
        if ( ! list.contains(item)) {
            throw new VipException(DefaultError.ACCESS_DENIED);
        }
    }

    public <T> void checkUnchanged(T a, T b) throws VipException {
        if (a != b) {
            throw new VipException(DefaultError.ACCESS_DENIED); 
        }
    }
}
