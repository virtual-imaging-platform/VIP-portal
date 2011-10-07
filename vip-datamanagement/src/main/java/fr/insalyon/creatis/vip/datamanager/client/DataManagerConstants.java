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
package fr.insalyon.creatis.vip.datamanager.client;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerConstants {

    // Tabs
    public final static String TAB_MANAGE_OPERATIONS = "manage-operations-tab";
    public final static String TAB_MANAGE_CACHED_FILES = "manage-cached-files-tab";
    // Sections
    public final static String SECTION_FILE_TRANSFER = "file-transfer-section";
    // Layouts
    public final static String LAYOUT_BROWSER = "browser-layout";
    public final static String LAYOUT_OPERATION = "operation-layout";
    // Icons
    private static final String IMG_FOLDER = "datamanager/";
    public static final String ICON_CACHE = IMG_FOLDER + "icon-cache.png";
    public static final String ICON_CUT = IMG_FOLDER + "icon-cut.png";
    public static final String ICON_DOWNLOAD = IMG_FOLDER + "icon-download.png";
    public static final String ICON_EMPTY_TRASH = IMG_FOLDER + "icon-trash-empty.png";
    public static final String ICON_FILE_TRANSFER = IMG_FOLDER + "icon-file-transfer.png";
    public static final String ICON_FOLDER_UP = IMG_FOLDER + "icon-folderup.png";
    public static final String ICON_FOLDER_ADD = IMG_FOLDER + "icon-addfolder.png";
    public static final String ICON_JUMPTO = IMG_FOLDER + "icon-jumpto.png";
    public static final String ICON_OPERATION = IMG_FOLDER + "icon-operation.png";
    public static final String ICON_PASTE = IMG_FOLDER + "icon-paste.png";
    public static final String ICON_TRASH = IMG_FOLDER + "icon-trash.png";
    public static final String ICON_UPLOAD = IMG_FOLDER + "icon-upload.png";
    // Application Names
    public final static String APP_CACHED_FILES = "Cached Files";
    public final static String APP_FILE_TRANSFER = "File Transfer";
    public final static String APP_OPERATIONS = "Operations";
    // Application Images
    public static final String APP_IMG_CACHED_FILES = IMG_FOLDER + "app-cache.png";
    public static final String APP_IMG_FILE_TRANSFER = IMG_FOLDER + "app-file-transfer.png";
    public static final String APP_IMG_OPERATIONS = IMG_FOLDER + "app-operation.png";
    // Configuration Constants
    public final static String ROOT = "/vip";
    public final static String USERS_HOME = "Home";
    public final static String TRASH_HOME = "Trash";
    public final static String BIOMED_HOME = "Biomed";
    public final static String GROUP_APPEND = " (group)";
}
