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
package fr.insalyon.creatis.vip.application.client;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationConstants {
    
    // Tabs
    public final static String TAB_APPLICATION = "applications-tab";
    public final static String TAB_MANAGE_APPLICATION = "manage-application-tab";
    public final static String TAB_MANAGE_CLASSES = "manage-classes-tab";
    public final static String TAB_MONITOR = "monitor-tab";
    public final static String TAB_STATS = "stats-tab";
    // Icons
    private static final String IMG_FOLDER = "application/";
    public static final String ICON_APPLICATION = IMG_FOLDER + "icon-application.png";
    public static final String ICON_CHART = IMG_FOLDER + "icon-chart.png";
    public static final String ICON_CLASSES = IMG_FOLDER + "icon-classes.png";
    public static final String ICON_CLEAN = IMG_FOLDER + "icon-clean.png";
    public static final String ICON_GENERAL = IMG_FOLDER + "icon-general.png";
    public static final String ICON_KILL = IMG_FOLDER + "icon-kill.png";
    public static final String ICON_LOG = IMG_FOLDER + "icon-log.png";
    public static final String ICON_MONITOR = IMG_FOLDER + "icon-monitor.png";
    public static final String ICON_PICKER_MORE = IMG_FOLDER + "icon-picker-more.png";
    public static final String ICON_PICKER_LESS = IMG_FOLDER + "icon-picker-less.png";
    public static final String ICON_PREVIEW = IMG_FOLDER + "icon-preview.png";
    public static final String ICON_SEARCH = IMG_FOLDER + "icon-search.png";
    public static final String ICON_SIMULATION_VIEW = IMG_FOLDER + "icon-simulation-view.png";
    public static final String ICON_SUMMARY = IMG_FOLDER + "icon-summary.png";
    public static final String ICON_TREE_INPUT = IMG_FOLDER + "tree/icon-tree-input.png";
    public static final String ICON_TREE_OUTPUT = IMG_FOLDER + "tree/icon-tree-output.png";
    public static final String ICON_TREE_SERVICE = IMG_FOLDER + "tree/icon-tree-service.png";
    public static final String ICON_TREE_SIMULATION = IMG_FOLDER + "tree/icon-tree-simulation.png";
    // Application Names
    public static final String APP_APPLICATION = "Applications";
    public static final String APP_CLASSES = "Classes";
    public static final String APP_MONITOR = "Simulation Monitor";
    public static final String APP_SIMULATION_ERROR = "Error File";
    public static final String APP_SIMULATION_OUT = "Output File";
    // Application Images
    public static final String APP_IMG_APPLICATION = IMG_FOLDER + "app-application.png";
    public static final String APP_IMG_CLASSES = IMG_FOLDER + "app-classes.png";
    public static final String APP_IMG_MONITOR = IMG_FOLDER + "app-monitor.png";
    public static final String APP_IMG_SIMULATION_ERROR = IMG_FOLDER + "app-simulation-error.png";
    public static final String APP_IMG_SIMULATION_OUT = IMG_FOLDER + "app-simulation-out.png";
    // Configuration
    public final static String SESSION_CLASSES = "vip-classes";
    // Enums
    public static enum JobStatus {
        KILL, RESCHEDULE, ERROR, COMPLETED, RUNNING, STALLED, CANCELLED, QUEUED,
        SUCCESSFULLY_SUBMITTED
    };
    
    public static enum SimulationStatus {
        Running, Completed, Killed, Cleaned
    };
    
    public static enum MoteurStatus {
        RUNNING, COMPLETE, TERMINATED, UNKNOWN
    };
    
    public static String getLaunchTabID(String applicationName) {
        
        return "launch-" + applicationName.replaceAll(" ", "-").toLowerCase() 
                + "-tab";
    }
}
