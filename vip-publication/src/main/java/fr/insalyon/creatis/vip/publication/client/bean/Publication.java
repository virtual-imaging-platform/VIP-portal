/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
