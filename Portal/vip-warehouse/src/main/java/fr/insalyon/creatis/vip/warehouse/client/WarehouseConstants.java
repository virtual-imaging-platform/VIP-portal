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
package fr.insalyon.creatis.vip.warehouse.client;

/**
 *
 * @author cervenansky
 */
public class WarehouseConstants {
      // Icons
    private static final String IMG_FOLDER = "warehouse/";
    public static final String ICON_WAREHOUSE = IMG_FOLDER + "icon-warehouse.png";
    public static final String ICON_DUMP = IMG_FOLDER + "icon-dump.png";
     public static final String ICON_FOLDER = IMG_FOLDER + "icon-folder.png";
     public static final String ICON_FILE = IMG_FOLDER + "icon-file.png";
    // Names
    public static final String APP_WAREHOUSE = "WAREHOUSE";
    
    public static final String APP_IMG_WAREHOUSE = IMG_FOLDER + "icon-warehouse.png";
    
    // Midas Method
    public static final String APP_MIDAS_APIKEY = "midas.user.apikey.default";
    public static final String APP_MIDAS_LOGIN = "midas.login";
    public static final String APP_MIDAS_FOLDERS =  "midas.user.folders";
    public static final String APP_MIDAS_CHILDREN = "midas.folder.children";
    public static final String APP_MIDAS_DOWNLOAD =   "midas.bitstream.download";
}
