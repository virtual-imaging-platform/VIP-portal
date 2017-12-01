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
package fr.insalyon.creatis.vip.core.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class CoreConstants implements IsSerializable {

    public static final String VERSION = "v1.21";
    // Configuration Labels
    public static final String VO_NAME = "vo.name";
    public static final String VO_ROOT = "vo.root";
    public static final String LAB_DB_HOST = "database.server.host";
    public static final String LAB_DB_PORT = "database.server.port";
    public static final String LAB_ADMIN_FIRST_NAME = "admin.first.name";
    public static final String LAB_ADMIN_LAST_NAME = "admin.last.name";
    public static final String LAB_ADMIN_EMAIL = "admin.email";
    public static final String LAB_ADMIN_INSTITUTION = "admin.institution";
    public static final String LAB_ADMIN_PHONE = "admin.phone";
    public static final String LAB_ADMIN_PASS = "admin.pass";
    public static final String LAB_CAS_URL = "cas.url";
    public static final String LAB_MYPROXY_HOST = "myproxy.host";
    public static final String LAB_MYPROXY_PORT = "myproxy.port";
    public static final String LAB_MYPROXY_USER = "myproxy.user";
    public static final String LAB_MYPROXY_PASS = "myproxy.pass";
    public static final String LAB_MYPROXY_LIFETIME = "myproxy.lifetime";
    public static final String LAB_MYPROXY_MIN_HOURS = "myproxy.min.hours";
    public static final String LAB_GRIDA_HOST = "grida.server.host";
    public static final String LAB_GRIDA_PORT = "grida.server.port";
    public static final String LAB_N4U_GRIDA_HOST = "n4u.grida.server.host";
    public static final String LAB_N4U_GRIDA_PORT = "n4u.grida.server.port";
    public static final String LAB_DATA_USERS_HOME = "datamanager.users.home";
    public static final String LAB_DATA_GROUPS_HOME = "datamanager.groups.home";
    public static final String LAB_DATA_PATH = "datamanager.path";
    public static final String LAB_DATA_LFC_HOST = "datamanager.lfc.host";
    public static final String LAB_DATA_LFC_PORT = "datamanager.lfc.port";
    public static final String LAB_MOTEUR_HOST = "moteur.host";
    public static final String LAB_SMA_HOST = "sma.host";
    public static final String LAB_SMA_PORT = "sma.port";
    public static final String LAB_TRUSTSTORE_FILE = "truststore.file";
    public static final String LAB_TRUSTSTORE_PASS = "truststore.password";
    public static final String LAB_SIMULATION_ADVANCED_MAX = "simulation.max.advanced";
    public static final String LAB_SIMULATION_PLATFORM_MAX = "simulation.max.platfrom";
    public static final String LAB_SIMULATION_BEGINNER_MAX = "simulation.max.beginner";
    public static final String LAB_SIMULATION_DB_HOST = "workflows.db.host";
    public static final String LAB_SIMULATION_DB_PORT = "workflows.db.port";
    public static final String LAB_SIMULATION_FOLDER = "workflows.directory";
    public static final String LAB_SIMULATION_EXEC_MODE = "workflows.exec.mode";
    public static final String LAB_APACHE_HOST = "apache.host";
    public static final String LAB_APACHE_SSL_PORT = "apache.ssl.port";
    // Tabs
    public static final String TAB_ACCOUNT = "account-tab";
    public static final String TAB_PUBLICATION = "publications-tab";
    public static final String TAB_ACTIVATION = "activation-tab";
    public static final String TAB_CONTACT = "contact-tab";
    public static final String TAB_HOME = "home-tab";
    public static final String TAB_RECOVERY = "recovery-tab";
    public static final String TAB_SIGNIN = "signin-tab";
    public static final String TAB_SIGNUP = "signup-tab";
    public static final String TAB_SYSTEM = "system-tab";
    public static final String TAB_MANAGE_ACCOUNTS = "manage-accounts-tab";
    public static final String TAB_MANAGE_GROUPS = "manage-groups-tab";
    public static final String TAB_MANAGE_TIPS = "manage-tips-tab";
    public static final String TAB_MANAGE_USERS = "manage-users-tab";
    public static final String TAB_MANAGE_SETTING = "manage-setting-tab";
    // Icons
    private static final String IMG_FOLDER = "core/";
    public static final String ICON_SEARCH = "icon-search.png";
    public static final String ICON_ACCOUNT = IMG_FOLDER + "icon-account.png";
    public static final String ICON_ACCOUNT_REMOVE = IMG_FOLDER + "icon-account-remove.png";
    public static final String ICON_ACTIVE = IMG_FOLDER + "icon-select.png";
    public static final String ICON_ADD = IMG_FOLDER + "icon-add.png";
    public static final String ICON_CLEAR = IMG_FOLDER + "icon-clear.png";
    public static final String ICON_CLOSE = IMG_FOLDER + "icon-close.png";
    public static final String ICON_DELETE = IMG_FOLDER + "icon-delete.png";
    public static final String ICON_EDIT = IMG_FOLDER + "icon-edit.png";
    public static final String ICON_EXAMPLE = IMG_FOLDER + "icon-example.png";
    public static final String ICON_GROUP = IMG_FOLDER + "system/icon-group.png";
    public static final String ICON_HELP = IMG_FOLDER + "icon-help.png";
    public static final String ICON_HOME = IMG_FOLDER + "icon-home.png";
    public static final String ICON_INFORMATION = IMG_FOLDER + "icon-information.png";
    public static final String ICON_LOADING = IMG_FOLDER + "icon-loading.gif";
    public static final String ICON_LOCK = IMG_FOLDER + "icon-lock.png";
    public static final String ICON_PASSWORD = IMG_FOLDER + "icon-password.png";
    public static final String ICON_PUBLICATION = IMG_FOLDER + "icon-publication.png";
    public static final String ICON_INFO = IMG_FOLDER + "icon-info.png";
    public static final String ICON_PERSONAL = IMG_FOLDER + "icon-personal.png";
    public static final String ICON_PICKER_QUESTION = IMG_FOLDER + "icon-picker-question.png";
    public static final String ICON_REFRESH = IMG_FOLDER + "icon-refresh.png";
    public static final String ICON_RESET = IMG_FOLDER + "icon-reset.png";
    public static final String ICON_SAVE = IMG_FOLDER + "icon-save.png";
    public static final String ICON_SAVED = IMG_FOLDER + "icon-saved.png";
    public static final String ICON_SIGNOUT = IMG_FOLDER + "icon-signout.png";
    public static final String ICON_SUCCESS = IMG_FOLDER + "icon-success.png";
    public static final String ICON_SYSTEM = IMG_FOLDER + "system/icon-system.png";
    public static final String ICON_TIP = IMG_FOLDER + "icon-tip.png";
    public static final String ICON_USER = IMG_FOLDER + "system/icon-user.png";
    public static final String ICON_SETTING = IMG_FOLDER + "system/icon-setting.png";
    public static final String ICON_USER_INFO = IMG_FOLDER + "system/icon-user-info.png";
    public static final String ICON_WARNING = IMG_FOLDER + "icon-warning.png";
    public static final String ICON_WORLD = IMG_FOLDER + "flags/_world.png";
    public static final String ICON_DROPBOX = IMG_FOLDER + "icon-dropbox.png";
    public static final String ICON_TERMS_USE = IMG_FOLDER + "icon-terms_of_use.png";
    public static final String ICON_RUNNING_SIMULATIONS = IMG_FOLDER +"icon-runningSimulation.png";
    // Folders
    public static final String FOLDER_TRASH = "Trash";
    public static final String FOLDER_FLAGS = IMG_FOLDER + "flags/";
    // Application Names
    public static final String APP_ACCOUNT = "My Account";
    public static final String APP_PUBLICATIONS = "Publications";
    public static final String APP_ACCOUNT_MANAGER = "Accounts Type";
    public static final String APP_GROUP = "Groups";
    public static final String APP_TIP = "Tips";
    public static final String APP_USER = "Users";
    public static final String APP_SETTING = "Settings";
    // Application Images
    public static final String APP_IMG_ACCOUNT = IMG_FOLDER + "app-account.png";
    public static final String APP_IMG_PUBLICATIONS = IMG_FOLDER + "app-articles.png";
    public static final String APP_IMG_ACCOUNT_MANAGER = IMG_FOLDER + "system/app-account.png";
    public static final String APP_IMG_GROUP = IMG_FOLDER + "system/app-group.png";
    public static final String APP_IMG_TIP = IMG_FOLDER + "system/app-tip.png";
    public static final String APP_IMG_USER = IMG_FOLDER + "system/app-user.png";
    public static final String APP_IMG_SETTING = IMG_FOLDER + "system/app-setting.png";
    // Session Attributes
    public static final String COOKIES_USER = "vip-cookie-user";
    public static final String COOKIES_SESSION = "vip-cookie-session";
    public static final String SESSION_USER = "vip-user";
    public static final String SESSION_GROUPS = "vip-groups";
    public static final Date COOKIES_EXPIRATION_DATE = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 7)); //cookies are valid for a week
    // Account Type
    public static final String ACCOUNT_OTHER = "Other";
    // Roles and Groups
    public static final String GROUP_SUPPORT = "Support";
    public static final String GROUP_DROPBOX = "Dropbox";
    // ssh
    public static final String SSH_PUBLIC_KEY = "ssh.publickey";
    //application,Gatelab
    public static final String APPLET_GATELAB_CLASSES = "appletGatelab.classes";
    public static final String APPLET_GATELABTEST_CLASSES = "appletGatelabTest.classes";
    public static final String UNDESIRED_MAIL_DOMAINS = "account.undesiredEmailDomains";
    public static final String UNDESIRED_COUNTRIES = "account.undesiredCountries";
    public static final String SAML_TRUSTED_CERTIFICATE = "saml.trustedcertificate";
    public static final String SAML_ACCOUNT_TYPE = "saml.accounttype";
    //query
    public static String TreeQuery = "query.classes";
    //Boutiques
    public static String APP_CLASS = "boutiques.application.class";
    public static String APPLICATION_FILES_REPOSITORY = "boutiques.upload.repository";
    public static String APP_DELETE_FILES_AFTER_UPLOAD = "boutiques.upload.deleteFile";
    //Publiction
    public static final String  PUB_MONTHS_UPDATES = "last.publication.update";

    public static enum GROUP_ROLE implements IsSerializable {

        Admin, User, None
    };
}
