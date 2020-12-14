package fr.insalyon.creatis.vip.api.model.stats;

import java.util.List;

public class UsersList {
    private Integer total;
    private List<StatUser> users;

    public UsersList() {}

    public UsersList(List<StatUser> users) {
        this.total = users.size();
        this.users = users;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<StatUser> getUsers() {
        return users;
    }

    public void setUsers(List<StatUser> users) {
        this.users = users;
    }
}
