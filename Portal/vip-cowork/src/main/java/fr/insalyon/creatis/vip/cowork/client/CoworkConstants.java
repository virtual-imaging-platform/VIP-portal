/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cowork.client;

import fr.insalyon.creatis.vip.core.server.business.Server;

/**
 *
 * @author glatard
 */
public class CoworkConstants {
    public static final String APP_SD = "Workflow Designer";
    private static final String IMG_FOLDER = "cowork/";
    public static final String APP_IMG_SD = IMG_FOLDER + "app-cowork.png";
    public static final String ICON_SD = IMG_FOLDER + "icon-cowork.png";
    public static final String TAB_SD = "cowork-tab";
    public static final String COWORK_GROUP ="Cowork";
    public static final String GWENDIA_HOME = Server.getInstance().getDataManagerGroupsHome()+"/"+COWORK_GROUP;
    public static final String VIP_APPLICATION_CLASS="Cowork";
    
}
