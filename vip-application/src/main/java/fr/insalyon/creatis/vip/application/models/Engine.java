package fr.insalyon.creatis.vip.application.models;

import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Engine implements IsSerializable {

    private String name;
    private String endpoint;
    private String status;

    public Engine() {}

    public Engine(String name) {
        this.name = name;
    }

    public Engine(String name, String endpoint, String status) {
        this.name = name;
        this.endpoint = endpoint;
        this.status = status ;
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
    
        Engine other = (Engine) obj;
        return Objects.equals(name, other.name) &&
               Objects.equals(endpoint, other.endpoint) &&
               Objects.equals(status, other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, endpoint, status);
    }
}
