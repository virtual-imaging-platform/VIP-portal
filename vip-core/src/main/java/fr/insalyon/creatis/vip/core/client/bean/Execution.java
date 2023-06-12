package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Execution implements IsSerializable {
    private int id;
    private String name;
    private String version;
    private String status;
    private String author;
    private String comments;
    public Execution() {}
    public Execution(String name, String version, String status, String author, String comments) {
        this.name = name;
        this.version = version;
        this.status = status;
        this.author = author;
        this.comments = comments;
    }

    public Execution(int id, String name, String version, String status, String author, String comments) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.status = status;
        this.author = author;
        this.comments = comments;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }
}

