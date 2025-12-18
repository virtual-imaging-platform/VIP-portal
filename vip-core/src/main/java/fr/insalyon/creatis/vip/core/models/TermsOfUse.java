package fr.insalyon.creatis.vip.core.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.sql.Timestamp;

/**
 *
 * @author Nouha Boujelben
 */
public class TermsOfUse implements IsSerializable {

    private int id;
    private Timestamp date;

    public TermsOfUse() {
    }

    public TermsOfUse(int id, Timestamp date) {
        this.id = id;
        this.date = date;
    }

    public TermsOfUse(Timestamp date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
