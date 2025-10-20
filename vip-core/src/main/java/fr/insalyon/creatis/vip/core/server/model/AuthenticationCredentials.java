package fr.insalyon.creatis.vip.core.server.model;

import jakarta.validation.constraints.NotNull;

public class AuthenticationCredentials {

    private String username;
    private String password;

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
