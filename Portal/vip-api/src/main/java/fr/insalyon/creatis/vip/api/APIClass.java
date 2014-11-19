/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.core.server.rpc.ConfigurationServiceImpl;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
class APIClass {

    private ConfigurationServiceImpl csi;
    private static Logger logger = Logger.getLogger(APIClass.class);
    
    public APIClass() {
        csi = new ConfigurationServiceImpl();
    }
    
    public ConfigurationServiceImpl getConfigurationServiceImpl(){
        return csi;
    }
    
    public Logger getLogger(){
        return logger;
    }
}
