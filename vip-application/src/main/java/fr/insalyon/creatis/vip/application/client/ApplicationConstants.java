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
package fr.insalyon.creatis.vip.application.client;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationConstants {

    // Tabs
    public final static String TAB_APPLICATION = "applications_tab";
    public final static String TAB_MANAGE_APPLICATION = "manage_application_tab";
    public final static String TAB_MANAGE_CLASSES = "manage_classes_tab";
    public final static String TAB_MANAGE_ENGINE = "manage_engine_tab";
    public final static String TAB_MANAGE_RESOURCE = "manage_resource_tab";
    public final static String TAB_MONITOR = "monitor_tab";
    public final static String TAB_STATS = "stats_tab";
    public final static String TAB_REPROVIP = "reproVip_tab";
    public static final String TAB_MAKE_EXECUTION_PUBLIC = "make_execution_public_tab";
    // Icons
    private static final String IMG_FOLDER = "application/";
    public static final String ICON_APPLICATION = IMG_FOLDER + "icon-application.png";
    public static final String ICON_APPLICATION_MONITOR = IMG_FOLDER + "icon-application-monitor.png";
    public static final String ICON_CHART = IMG_FOLDER + "icon-chart.png";
    public static final String ICON_CLASSES = IMG_FOLDER + "icon-classes.png";
    public static final String ICON_CLEAN = IMG_FOLDER + "icon-clean.png";
    public static final String ICON_GENERAL = IMG_FOLDER + "icon-general.png";
    public static final String ICON_KILL = IMG_FOLDER + "icon-kill.png";
    public static final String ICON_KILLWR = IMG_FOLDER + "icon-killWR.png";
    public static final String ICON_REPORT_ISSUE = IMG_FOLDER + "icon-reportIssue.png";
    public static final String ICON_LAUNCH = IMG_FOLDER + "icon-launch.png";
    public static final String ICON_LOG = IMG_FOLDER + "icon-log.png";
    public static final String ICON_SEMANTICS = IMG_FOLDER + "icon-semantics.png";
    public static final String ICON_MONITOR = IMG_FOLDER + "icon-monitor.png";
    public static final String ICON_MONITOR_DEBUG = IMG_FOLDER + "monitor/icon-debug.png";
    public static final String ICON_MONITOR_DOWNLOAD = IMG_FOLDER + "monitor/icon-download.png";
    public static final String ICON_MONITOR_ERROR_FILE = IMG_FOLDER + "monitor/icon-error-file.png";
    public static final String ICON_MONITOR_JOB = IMG_FOLDER + "monitor/icon-job.png";
    public static final String ICON_MONITOR_OUTPUT_FILE = IMG_FOLDER + "monitor/icon-output-file.png";
    public static final String ICON_MONITOR_RELAUNCH = IMG_FOLDER + "monitor/icon-relaunch.png";
    public static final String ICON_MONITOR_SEARCH = IMG_FOLDER + "monitor/icon-search.png";
    public static final String ICON_MONITOR_SIMULATION_CLEANED = IMG_FOLDER + "monitor/icon-simulation-cleaned.png";
    public static final String ICON_MONITOR_SIMULATION_COMPLETED = IMG_FOLDER + "monitor/icon-simulation-completed.png";
    public static final String ICON_MARK_COMPLETED = IMG_FOLDER + "icon-mark-completed.png";
    public static final String ICON_USER = IMG_FOLDER + "icon-user.png";
    public static final String ICON_RESOURCE = IMG_FOLDER + "app-resource.png";
    ;
    public static final String ICON_MONITOR_SIMULATION_FAILED = IMG_FOLDER + "monitor/icon-simulation-failed.png";
    public static final String ICON_MONITOR_SIMULATION_KILLED = IMG_FOLDER + "monitor/icon-simulation-killed.png";
    public static final String ICON_MONITOR_SIMULATION_RUNNING = IMG_FOLDER + "monitor/icon-simulation-running.png";
    public static final String ICON_MONITOR_TIMELINE = IMG_FOLDER + "monitor/icon-timeline.png";
    public static final String ICON_MONITOR_TASK_CRITICAL = IMG_FOLDER + "monitor/icon-critical.png";
    public static final String ICON_MONITOR_TASK_NONCRITICAL = IMG_FOLDER + "monitor/icon-noncritical.png";
    public static final String ICON_MONITOR_TASK_OK = IMG_FOLDER + "monitor/icon-ok.png";
    public static final String ICON_MONITOR_TASKS = IMG_FOLDER + "monitor/icon-tasks.png";
    public static final String ICON_ENGINE = IMG_FOLDER + "icon-engines.png";
    public static final String ICON_PICKER_MORE = IMG_FOLDER + "icon-picker-more.png";
    public static final String ICON_PICKER_LESS = IMG_FOLDER + "icon-picker-less.png";
    public static final String ICON_PREVIEW = IMG_FOLDER + "icon-preview.png";
    public static final String ICON_PROCESSOR = IMG_FOLDER + "icon-processor.png";
    public static final String ICON_RELAUNCH = IMG_FOLDER + "icon-relaunch.png";
    public static final String ICON_SEARCH = IMG_FOLDER + "icon-search.png";
    public static final String ICON_SIMULATION_VIEW = IMG_FOLDER + "icon-simulation-view.png";
    public static final String ICON_STATUS = IMG_FOLDER + "icon-status.png";
    public static final String ICON_SUMMARY = IMG_FOLDER + "icon-summary.png";
    public static final String ICON_TASK_KILL = IMG_FOLDER + "icon-task-kill.png";
    public static final String ICON_TASK_REPLICATE = IMG_FOLDER + "icon-task-replicate.png";
    public static final String ICON_TASK_RESCHEDULE = IMG_FOLDER + "icon-task-reschedule.png";
    public static final String ICON_TASK_UNHOLD = IMG_FOLDER + "icon-task-unhold.png";
    public static final String ICON_TREE_INPUT = IMG_FOLDER + "tree/icon-tree-input.png";
    public static final String ICON_TREE_OUTPUT = IMG_FOLDER + "tree/icon-tree-output.png";
    public static final String ICON_TREE_SERVICE = IMG_FOLDER + "tree/icon-tree-service.png";
    public static final String ICON_TREE_SIMULATION = IMG_FOLDER + "tree/icon-tree-simulation.png";
    // Application Names
    public static final String APP_APPLICATION = "Applications";
    public static final String APP_CLASSES = "Classes";
    public static final String APP_MONITOR = "Execution Monitor";
    public static final String APP_ENGINE = "Engines";
    public static final String APP_SIMULATION_ERROR = "Error File";
    public static final String APP_SIMULATION_OUT = "Output File";
    public static final String APP_PUBLIC_APPLICATION = "Applications";
    public static final String APP_REPRO_VIP = "ReproVIP";
    public static final String APP_RESOURCE = "Resources";
    // Application Images
    public static final String APP_IMG_APPLICATION = IMG_FOLDER + "app-application.png";
    public static final String APP_IMG_CLASSES = IMG_FOLDER + "app-classes.png";
    public static final String APP_IMG_MONITOR = IMG_FOLDER + "app-monitor.png";
    public static final String APP_IMG_ENGINE = IMG_FOLDER + "app-engine.png";
    public static final String APP_IMG_RESOURCE = IMG_FOLDER + "app-resource.png";
    public static final String APP_IMG_SIMULATION_ERROR = IMG_FOLDER + "app-simulation-error.png";
    public static final String APP_IMG_SIMULATION_OUT = IMG_FOLDER + "app-simulation-out.png";
    // Application launch, input without value
    public static final String INPUT_WITHOUT_VALUE = "No_value_provided";
    public static final String INPUT_WITHOUT_VALUE_REQUIRED_MESSAGE = "Either you deselect the field or you fill it in";
    // Configuration
    public static final String WORKKFLOW_EXAMPLE_TAG = "example";
    public static final String SESSION_CLASSES = "vip-classes";
    public static final String SEPARATOR_INPUT = "##";
    public static final String SEPARATOR_LIST = "@@";
    public static final String INPUT_VALID_CHARS = "0-9.,A-Za-z-+@/_(): ";
    public static final String EXEC_NAME_VALID_CHARS = "0-9A-Za-z-_ ";

    public static String getLaunchTabID(String applicationName) {
        return "launch_"
            + CoreConstants.replaceSpacesInIds(applicationName)
            + "_tab";
    }
}
