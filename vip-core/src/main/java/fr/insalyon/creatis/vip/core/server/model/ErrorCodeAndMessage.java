package fr.insalyon.creatis.vip.core.server.model;

import jakarta.validation.constraints.NotNull;

public class ErrorCodeAndMessage {

    @NotNull private Integer errorCode;
    @NotNull private String errorMessage;

    public ErrorCodeAndMessage() {}

    public ErrorCodeAndMessage(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCodeAndMessage(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
