/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.server.business;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cervenansky
 */
public class ItemDB implements Serializable {
    public String description="";
    public String name = "";
  
    public ArrayList<ItemDB> items = new ArrayList<ItemDB>();
    public String id="";
    public String nb_child ="";
 
    
}
