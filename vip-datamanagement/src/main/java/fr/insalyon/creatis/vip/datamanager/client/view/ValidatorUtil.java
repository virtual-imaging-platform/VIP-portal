package fr.insalyon.creatis.vip.datamanager.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ValidatorUtil {

    /**
     *
     * @param baseDir
     * @param errorMessage
     * @return
     */
    public static boolean validateRootPath(String baseDir, String errorMessage) {

        if (baseDir.equals(DataManagerConstants.ROOT)) {
            Layout.getInstance().setWarningMessage("You cannot " + errorMessage + " the root folder.");
            return false;
        }
        return true;
    }
    
    /**
     *
     * @param baseDir
     * @param errorMessage
     * @return
     */
    public static boolean validateUserLevel(String baseDir, String errorMessage) {

        if (baseDir.contains(DataManagerConstants.GROUP_APPEND)
                && CoreModule.user.getLevel() == UserLevel.Beginner) {
            Layout.getInstance().setWarningMessage("You cannot " + errorMessage
                    + " a group folder<br />"
                    + "since you have 'Beginner' level.");
            return false;
        }
        return true;
    }
}
