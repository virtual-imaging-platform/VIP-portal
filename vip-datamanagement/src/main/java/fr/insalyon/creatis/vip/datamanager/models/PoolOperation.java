package fr.insalyon.creatis.vip.datamanager.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class PoolOperation implements IsSerializable {

    public static enum Type {

        Download, Upload, Delete
    };

    public static enum Status {

        Queued, Running, Done, Failed, Rescheduled
    };
    private String id;
    private Date registration;
    private String parsedRegistration;
    private String source;
    private String dest;
    private Type type;
    private Status status;
    private String user;
    private int progress;

    public PoolOperation() {
    }

    /**
     * 
     * @param id
     * @param registration
     * @param source
     * @param dest
     * @param type
     * @param status
     * @param user
     */
    public PoolOperation(String id, Date registration, String parsedResgistration,
            String source, String dest, Type type, Status status, String user, 
            int progress) {

        this.id = id;
        this.registration = registration;
        this.parsedRegistration = parsedResgistration;
        this.source = source;
        this.dest = dest;
        this.type = type;
        this.status = status;
        this.user = user;
        this.progress = progress;
    }

    public String getDest() {
        return dest;
    }

    public String getId() {
        return id;
    }

    public Date getRegistration() {
        return registration;
    }

    public String getSource() {
        return source;
    }

    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getParsedRegistration() {
        return parsedRegistration;
    }

    public int getProgress() {
        return progress;
    }
}
