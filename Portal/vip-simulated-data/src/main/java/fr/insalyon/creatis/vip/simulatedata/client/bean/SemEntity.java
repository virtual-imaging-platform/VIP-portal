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
