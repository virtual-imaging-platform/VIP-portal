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
package fr.insalyon.creatis.vip.applicationimporter.client.view;

/**
 *
 * @author Nouha boujelben
 */
public class Constants {

    // Tab IDs
    public static final String TAB_ID_BOUTIQUES = "ApplicationImporter_boutiques_tab";
    public static final String TAB_ID_BOUTIQUES_APPLICATION = "ApplicationImporter_boutiques_application_tab";

    // Tab names
    public static final String TAB_NAME_BOUTIQUES = "Boutiques Importer";

    // Icons
    private static final String IMG_FOLDER = "importer/";
    public static final String ICON_BOUTIQUES = IMG_FOLDER + "icon-boutiques.png";
    public static final String ICON_IMPORTER = IMG_FOLDER + "icon-importer.png";
    public static final String ICON_PICKER_LESS = IMG_FOLDER + "icon-picker-less.png";
    public static final String ICON_PICKER_MORE = IMG_FOLDER + "icon-picker-more.png";
    public static final String ICON_PICKER_EDIT = IMG_FOLDER + "icon-picker-edit.jpg";
    public static final String ICON_LAUNCH = IMG_FOLDER + "icon-launch.png";
    public static final String ICON_IMPORT = IMG_FOLDER + "icon-import.png";
    public static final String ICON_FILE = IMG_FOLDER + "icon-file.png";
    public static final String ICON_FILES = IMG_FOLDER + "icon-files.png";

    //Icons confirm
    public static final String ICON_ADD = IMG_FOLDER + "add.png";
    public static final String ICON_CANCEL = IMG_FOLDER + "cancel.png";
    public static final String ICON_OVERWRITE = IMG_FOLDER + "overwrite.png";
    public static final String ICON_INPUT = IMG_FOLDER + "icon-input.png";
    public static final String ICON_OUTPUT = IMG_FOLDER + "icon-output.png";
    public static final String ICON_INFORMATION = IMG_FOLDER + "icon-information.png";
    public static final String ICON_EXECUTABLE = IMG_FOLDER + "icon-executable.png";
    // Application Names
    public static final String APP_APPLICATION_IMPORTER = "Application Importer";
    // Application Images
    public static final String APP_IMG_IMPORTER = IMG_FOLDER + "importer.png";
    //Group
    public static final String APPLICATION_IMPORTER_GROUP = "Application Importer";
    //vm files
    public static final String VM_GASW = "gasw.vm";
    //LFN access to SegPerfAnalyzer
    public static final String LNF_PATH = "";
    // Application Importer types
    public static final String APP_IMPORTER_STANDALONE_TYPE = "app-importer-standalone-type";
    public static final String APP_IMPORTER_DOT_INPUTS_TYPE = "app-importer-dot-inputs-type";
    //Execution type
    public static final String APP_EXECUTION_LOCAL = "file";
    public static final String APP_EXECUTION_CLOUD = "lfn";

}
