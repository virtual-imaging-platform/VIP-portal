/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author gaignard
 */
public class SemEntity implements IsSerializable {
    private String type;
    private String value;

    private String name;
    
    public SemEntity(){}
    
    public  SemEntity(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getLabel() {
        return value;
    }

    public String getUri() {
        return type;
    }
    
    

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final SemEntity other = (SemEntity) obj;
//        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
//            return false;
//        }
//        return true;
//    }
//    
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 41 * hash + (this.value != null ? this.value.hashCode() : 0);
//        return hash;
//}
//
//    @Override
//    public String toString() {
//        return "{" + value + "->" + type + '}';
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
