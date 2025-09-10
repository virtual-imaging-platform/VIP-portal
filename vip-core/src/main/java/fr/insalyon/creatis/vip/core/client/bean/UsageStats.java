package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class UsageStats implements IsSerializable {

    private Integer nUsers;
    private Integer nCountries;
    
    public UsageStats() {
    }

    public UsageStats(Integer nUsers, Integer nCountries) {
        this.nUsers = nUsers;
        this.nCountries = nCountries;
    }

    public Integer getnCountries() {
        return nCountries;
    }

    public void setnCountries(Integer nCountries) {
        this.nCountries = nCountries;
    }

    public Integer getnUsers() {
        return nUsers;
    }

    public void setnUsers(Integer nUsers) {
        this.nUsers = nUsers;
    }

    
}
