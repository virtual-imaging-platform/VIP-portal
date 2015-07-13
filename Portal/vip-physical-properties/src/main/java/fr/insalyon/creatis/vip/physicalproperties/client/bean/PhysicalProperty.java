/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.physicalproperties.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author glatard
 */
public class PhysicalProperty implements IsSerializable {
    private int id;
    private String author;
    private String comment;
    private Date date;
    private String type;


    public PhysicalProperty(){}

    public PhysicalProperty(int id, String type){
        this.id = id;
        this.type=type;
        
    }

    public PhysicalProperty(int id, String author, String comment, Date date, String type) {
        this.id = id;
        this.author = author;
        this.comment = comment;
        this.date = date;
        this.type = type;
    }

    public PhysicalProperty(int propId) {
        this.id = propId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
}
