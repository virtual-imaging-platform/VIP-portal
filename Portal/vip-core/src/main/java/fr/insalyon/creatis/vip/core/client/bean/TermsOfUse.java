/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author Nouha Boujelben
 */
public class TermsOfUse implements IsSerializable {

    private int id;
    private String text;
    private Timestamp date;

    public TermsOfUse() {
    }

    public TermsOfUse(int id, String text, Timestamp date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public TermsOfUse( String text, Timestamp date) {
        this.text = text;
        this.date = date;
    }
    
    
    public TermsOfUse( String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
