/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import java.sql.Date;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.List;

/**
 *
 * @author Boujelben
 */


 public class Query implements IsSerializable {
    public Query() {}

            
    private String name;
    private Long queryID;
    private Date dateCreation;
    private String description;
    private String queryMaker ;
    private String queryversions;

    public Query(String description,String name,String queryMaker ) {
        this.name = name;
       
        this.description = description;
        this.queryMaker = queryMaker;
    }

   

  

    public Query( String name, Date dateCreation,String queryversions ) {
        this.name = name;
        this.dateCreation = dateCreation;
        this.queryversions=queryversions;
        
    }
    

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQueryID() {
        return queryID;
    }

    public void setQueryID(Long queryID) {
        this.queryID = queryID;
    }

    public String getQueryMaker() {
        return queryMaker;
    }

    public void setQueryMaker(String queryMaker) {
        this.queryMaker = queryMaker;
    }
     
        public String getQueryversions() {
        return queryversions;
    }

    public void setQueryversions(String queryversions) {
        this.queryversions = queryversions;
    }
    
    

  

  
    

    
   
   
   
    }
     

    
