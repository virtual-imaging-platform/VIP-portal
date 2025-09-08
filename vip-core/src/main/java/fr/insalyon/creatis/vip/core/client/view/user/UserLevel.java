package fr.insalyon.creatis.vip.core.client.view.user;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public enum UserLevel {

    Beginner,
    Advanced,
    Developer,
    Administrator;

    public static String[] toStringArray() {

        List<String> list = new ArrayList<String>();
        for (UserLevel level : values()) {
            list.add(level.name());
        }
        return list.toArray(new String[]{});
    }
}
