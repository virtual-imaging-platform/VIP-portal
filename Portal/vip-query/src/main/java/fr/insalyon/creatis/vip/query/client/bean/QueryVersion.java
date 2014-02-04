/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author Nouha Boujelben
 */
public class QueryVersion implements IsSerializable {

    private Long queryVersionID;
    private Long queryVersion;
    private Long queryID;
    private String body;
    private Timestamp dateCreation;
    private String description;
    private boolean isPublic;

 
   

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public QueryVersion() {
    }

    public QueryVersion(Long queryVersion, Long queryID,String description, String body,boolean isPublic) {
        this.queryVersion = queryVersion;
        this.queryID = queryID;
        this.description=description;
        this.body = body;
        this.isPublic=isPublic;
    }

    public QueryVersion(Long queryVersion, String body) {
        this.queryVersion = queryVersion;
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

    public Long getQueryVersion() {
        return queryVersion;
    }

    public void setQueryVersion(Long queryVersion) {
        this.queryVersion = queryVersion;
    }

    public Long getQueryVersionID() {
        return queryVersionID;
    }

    public void setQueryVersionID(Long queryVersionID) {
        this.queryVersionID = queryVersionID;
    }
    
     public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
