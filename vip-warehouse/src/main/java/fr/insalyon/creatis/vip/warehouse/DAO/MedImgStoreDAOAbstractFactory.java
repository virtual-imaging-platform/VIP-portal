/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.DAO;

import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

/**
 *
 * @author cervenansky
 */
public abstract class MedImgStoreDAOAbstractFactory {
  public static MedImgStoreDAOAbstractFactory getDAOFactory(){
        return MedImgStoreDAOFactory.getInstance();
    }
    
    public abstract MedImgWarehouseData getMedicalImageStorage() throws DAOException;
    
}
  

