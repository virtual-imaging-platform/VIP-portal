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
package fr.insalyon.creatis.vip.models.client;

import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Tristan Glatard
 */
public class ModelConstants {

    // Tabs
    public static final String TAB_MODEL_BROWSER = "model-browse-tab";
 
    // Icons
    private static final String IMG_FOLDER = "model/";
    public static final String ICON_MODEL = IMG_FOLDER + "icon-model.png";
    // Names
    public static final String APP_MODEL = "Models";
    // Images
    public static final String APP_IMG_MODEL = IMG_FOLDER + "app-model.png";
    public static final String APP_IMG_ANATOMY = IMG_FOLDER + "icon-anatomy.png";
    public static final String APP_IMG_GEOMETRY = IMG_FOLDER + "icon-geometry.png";
    public static final String APP_IMG_EXTERNAL = IMG_FOLDER + "icon-external.png";
    public static final String APP_IMG_FOREIGN = IMG_FOLDER + "icon-foreign.png";
    public static final String APP_IMG_INSTANT = IMG_FOLDER + "icon-instant.png";
    public static final String APP_IMG_OBJECT = IMG_FOLDER + "icon-object.png";
    public static final String APP_IMG_RADIO = IMG_FOLDER + "icon-radio.png";
    public static final String APP_IMG_ECHO = IMG_FOLDER + "icon-echo.png";
    public static final String APP_IMG_MAGNETIC = IMG_FOLDER + "icon-magnetic.png";
    public static final String APP_IMG_CHEMICAL = IMG_FOLDER + "icon-chemical.png";
    public static final String APP_IMG_PATHOLOGY = IMG_FOLDER + "icon-pathology.png";
    public static final String APP_IMG_LUT = IMG_FOLDER + "icon-physical-lut.png";
    public static final String APP_IMG_MAP = IMG_FOLDER + "icon-physical-map.png";
    public static final String APP_IMG_TIMEPOINT = IMG_FOLDER + "icon-timepoint.png";
    public static final String APP_IMG_PHYSICAL_PARAMS = IMG_FOLDER + "icon-physparams.png";
    public static final String APP_IMG_OK = IMG_FOLDER + "icon-ok.png";
    public static final String APP_IMG_KO = IMG_FOLDER + "icon-ko.png";
    // Configuration Constants
    public static final String GROUP_VIP = "VIP";
    public static final String MODEL_HOME = DataManagerConstants.ROOT + "/VIP/Models";
}
