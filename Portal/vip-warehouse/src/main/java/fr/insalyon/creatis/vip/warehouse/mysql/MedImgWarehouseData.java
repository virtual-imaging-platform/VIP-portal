/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.mysql;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.ArrayList;

/**
 *
 * @author cervenansky
 */
public interface MedImgWarehouseData {

    public void recordSite(String email, String label, String url) throws DAOException;

    public void recordNickname(String email, String label, String nickname) throws DAOException;

    public void setData(String jsession, String url, String suri, String furi) throws DAOException;

    public void setSession(String email, String label, String jsession) throws DAOException;

    public String getSession(String email, String label) throws DAOException;

    public String getURL(String email, String label) throws DAOException;

    public String getNickName(String email, String label) throws DAOException;

    public ArrayList<String> getSites(String email) throws DAOException;
    
    public void updateData(String jsession, String suri) throws DAOException;
}
