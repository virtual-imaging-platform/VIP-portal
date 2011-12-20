/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view;

import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class CoreConstants {

    public static final String VERSION = "v0.5.4";
    public static final String FOLDER_TRASH = "Trash";
    // Tabs
    public static final String TAB_ACCOUNT = "account-tab";
    public static final String TAB_ACTIVATION = "activation-tab";
    public static final String TAB_CONTACT = "contact-tab";
    public static final String TAB_HOME = "home-tab";
    public static final String TAB_SIGNIN = "signin-tab";
    public static final String TAB_SIGNUP = "signup-tab";
    public static final String TAB_SYSTEM = "system-tab";
    public static final String TAB_MANAGE_USERS = "manage-users-tab";
    public static final String TAB_MANAGE_GROUPS = "manage-groups-tab";
    // Icons
    private static final String IMG_FOLDER = "core/";
    public static final String ICON_ACCOUNT = IMG_FOLDER + "icon-account.png";
    public static final String ICON_ACTIVATE = IMG_FOLDER + "icon-select.png";
    public static final String ICON_ADD = IMG_FOLDER + "icon-add.png";
    public static final String ICON_CLEAR = IMG_FOLDER + "icon-clear.png";
    public static final String ICON_DELETE = IMG_FOLDER + "icon-delete.png";
    public static final String ICON_EDIT = IMG_FOLDER + "icon-edit.png";
    public static final String ICON_HELP = IMG_FOLDER + "icon-help.png";
    public static final String ICON_HOME = IMG_FOLDER + "icon-home.png";
    public static final String ICON_LOADING = IMG_FOLDER + "icon-loading.gif";
    public static final String ICON_PASSWORD = IMG_FOLDER + "icon-password.png";
    public static final String ICON_PERSONAL = IMG_FOLDER + "icon-personal.png";
    public static final String ICON_REFRESH = IMG_FOLDER + "icon-refresh.png";
    public static final String ICON_SAVE = IMG_FOLDER + "icon-save.png";
    public static final String ICON_SIGNOUT = IMG_FOLDER + "icon-signout.png";
    public static final String ICON_SYSTEM = IMG_FOLDER + "system/icon-system.png";
    public static final String ICON_USER = IMG_FOLDER + "system/icon-user.png";
    public static final String ICON_GROUP = IMG_FOLDER + "system/icon-group.png";
    // Application Names
    public static final String APP_ACCOUNT = "My Account";
    public static final String APP_GROUP = "Groups";
    public static final String APP_USER = "Users";
    // Application Images
    public static final String APP_IMG_ACCOUNT = IMG_FOLDER + "app-account.png";
    public static final String APP_IMG_GROUP = IMG_FOLDER + "system/app-group.png";
    public static final String APP_IMG_NEWS = IMG_FOLDER + "system/app-news.png";
    public static final String APP_IMG_USER = IMG_FOLDER + "system/app-user.png";
    // Session Attributes
    public static final String COOKIES_USER = "vip-cookie-user";
    public static final String COOKIES_SESSION = "vip-cookie-session";
    public static final String SESSION_USER = "vip-user";
    public static final String SESSION_GROUPS = "vip-groups";
    public static final Date COOKIES_EXPIRATION_DATE = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 7));
    // Roles and Groups
    public static final String GROUP_ADMIN = "Administrator";
    public static final String GROUP_SUPPORT = "Support";
    // Account Type
    public static final String ACCOUNT_OTHER = "Other";
    public static enum ROLE { Admin, User };
}
