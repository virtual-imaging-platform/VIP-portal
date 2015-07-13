/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.server.business;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseDataSQL;
import fr.insalyon.creatis.vip.warehouse.DAO.MedImgStoreDAOAbstractFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author cervenansky
 */
// Medical Images DataBase interface
public class MedImgWarehouseBusiness {
    
    protected MedImgWarehouseDataSQL midb = null;
    public MedImgWarehouseBusiness()
   {
       midb = new MedImgWarehouseDataSQL();
   }
    
    public ArrayList<String> getSites(String email) throws DAOException
    {
        System.out.println("I was here");
        ArrayList<String> sites= new ArrayList<String>();
        return MedImgStoreDAOAbstractFactory.getDAOFactory().getMedicalImageStorage().getSites(email);
     }
}
