/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author nouha
 */
public class QueryExecution implements IsSerializable{
    private Long queryExecutionID;
    private Long queryVersionID;
    private Timestamp dateExecution;
    private String executer;
    private String status ;
    private String name;
    private String description;
    private Timestamp dateEndExecution;
    private String urlResult;

    public QueryExecution() {
    }
    

    public QueryExecution(Long queryVersionID, String executer, String status, String name, String description, String urlResult) {
        this.queryVersionID = queryVersionID;
        //this.dateExecution = dateExecution;
        this.executer = executer;
        this.status = status;
        this.name = name;
        this.description = description;
    
        this.urlResult = urlResult;
    }
    

    public Long getQueryExecutionID() {
        return queryExecutionID;
    }

    public void setQueryExecutionID(Long queryExecutionID) {
        this.queryExecutionID = queryExecutionID;
    }

    public Long getQueryVersionID() {
        return queryVersionID;
    }

    public void setQueryVersionID(Long queryVersionID) {
        this.queryVersionID = queryVersionID;
    }

    public Timestamp getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Timestamp dateExecution) {
        this.dateExecution = dateExecution;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateEndExecution() {
        return dateEndExecution;
    }

    public void setDateEndExecution(Timestamp dateEndExecution) {
        this.dateEndExecution = dateEndExecution;
    }

    public String getUrlResult() {
        return urlResult;
    }

    public void setUrlResult(String urlResult) {
        this.urlResult = urlResult;
    }
   
    
    
}
