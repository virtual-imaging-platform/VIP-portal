/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author Nouha boujelben
 */
public class QueryExecution implements IsSerializable {

    private Long queryExecutionID;
    private Long queryVersionID;
    private Timestamp dateExecution;
    private String executer;
    private String status;
    private String name;
    private Timestamp dateEndExecution;
    private String bodyResult;

    public QueryExecution() {
    }

    public QueryExecution(Long queryVersionID, String status, String name, String bodyResult) {
        this.queryVersionID = queryVersionID;
        this.status = status;
        this.name = name;
        this.bodyResult = bodyResult;
    } 
    public QueryExecution(String status, String name, String bodyResult) { 
        this.status = status;
        this.name = name;
        this.bodyResult = bodyResult;
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

    public Timestamp getDateEndExecution() {
        return dateEndExecution;
    }

    public void setDateEndExecution(Timestamp dateEndExecution) {
        this.dateEndExecution = dateEndExecution;
    }

    public String getBodyResult() {
        return bodyResult;
    }

    public void setBodyResult(String bodyResult) {
        this.bodyResult = bodyResult;
    }
}
