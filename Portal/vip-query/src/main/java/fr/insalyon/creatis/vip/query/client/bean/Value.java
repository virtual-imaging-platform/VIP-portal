/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha boujelben
 */
public class Value implements IsSerializable {

    private Long valueID;
    private String value;
    private Long parameterID;
    private Long queryExecutionID;

    public Value() {
    }

    public Value(String value, Long parameterID, Long queryExecutionID) {
        this.value = value;
        this.parameterID = parameterID;
        this.queryExecutionID = queryExecutionID;
    }

    public Long getValueID() {
        return valueID;
    }

    public void setValueID(Long valueID) {
        this.valueID = valueID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getParameterID() {
        return parameterID;
    }

    public void setParameterID(Long parameterID) {
        this.parameterID = parameterID;
    }

    public Long getQueryExecutionID() {
        return queryExecutionID;
    }

    public void setQueryExecutionID(Long queryExecutionID) {
        this.queryExecutionID = queryExecutionID;
    }
}
