package fr.insalyon.creatis.vip.core.server.model;

import com.fasterxml.jackson.annotation.JsonView;

import fr.insalyon.creatis.vip.core.server.inter.DataViews;
import jakarta.validation.constraints.NotBlank;

public class TwoFactorCodeRequest {

    @NotBlank
    @JsonView(DataViews.User.class)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
