package fr.insalyon.creatis.vip.publication.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha Boujelben
 */
public class Publication implements IsSerializable {
    

    private Long id;
    private String title;
    private String date;
    private String doi;
    private String authors;
    private String type;
    private String typeName;
    private String vipAuthor;
    private String vipApplication;

    public Publication() {
    }
    

    public Publication(Long id, String title, String date, String doi, String authors, String type, String typeName, String vipAuthor, String vipApplication) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.doi = doi;
        this.authors = authors;
        this.type = type;
        this.typeName = typeName;
        this.vipAuthor = vipAuthor;
        this.vipApplication = vipApplication;
    }
    
    public Publication( Long id,String title, String date, String doi, String authors, String type, String typeName, String vipApplication) {
        this.id=id;
        this.title = title;
        this.date = date;
        this.doi = doi;
        this.authors = authors;
        this.type = type;
        this.typeName = typeName;
        this.vipApplication = vipApplication;
    }
     public Publication(String title, String date, String doi, String authors, String type, String typeName,String vipAuthor, String vipApplication) {
        
        this.title = title;
        this.date = date;
        this.doi = doi;
        this.authors = authors;
        this.type = type;
        this.typeName = typeName;
        this.vipAuthor=vipAuthor;
        this.vipApplication = vipApplication;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVipAuthor() {
        return vipAuthor;
    }

    public void setVipAuthor(String vipAuthor) {
        this.vipAuthor = vipAuthor;
    }

    public String getVipApplication() {
        return vipApplication;
    }

    public void setVipApplication(String vipApplication) {
        this.vipApplication = vipApplication;
    }



}
