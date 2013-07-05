/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author Boujelben
 */
public class QueryVersion implements IsSerializable{
    
private Long  queryVersionID ;
private String queryVersion;
private Long queryID;
private String body;
private  Timestamp dateCreation;

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public QueryVersion() {
    }
    public QueryVersion(String queryVersion, Long queryID, String body) {
        this.queryVersion = queryVersion;
        this.queryID = queryID;
        this.body = body;
    }

    public QueryVersion(String queryVersion, String body) {
        this.queryVersion = queryVersion;
        this.body = body;
    }
   

    

    public QueryVersion(Long queryVersionID, String queryVersion, Long queryID, String body) {
        this.queryVersionID = queryVersionID;
        this.queryVersion = queryVersion;
        this.queryID = queryID;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getQueryID() {
        return queryID;
    }

    public void setQueryID(Long queryID) {
        this.queryID = queryID;
    }

    public String getQueryVersion() {
        return queryVersion;
    }

    public void setQueryVersion(String queryVersion) {
        this.queryVersion = queryVersion;
    }

    public Long getQueryVersionID() {
        return queryVersionID;
    }

    public void setQueryVersionID(Long queryVersionID) {
        this.queryVersionID = queryVersionID;
    }



    
    
}
