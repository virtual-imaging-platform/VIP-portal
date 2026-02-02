package fr.insalyon.creatis.vip.application.client.view.system;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.insalyon.creatis.vip.core.client.bean.Group;

public class SystemUtils {
    
    public static List<String> formatGroups(Set<Group> groups) {
        return groups.stream().map(
            g -> g.isAuto() ? g.getName() + " (Auto, " + formatIsPublic(g.isPublicGroup()) + ")" : g.getName() + " (" + formatIsPublic(g.isPublicGroup()) + ")"
        ).collect(Collectors.toList());
    }

    public static String formatIsPublic(boolean isPublic) {
        return isPublic ? "Public" : "Private";
    }
}
