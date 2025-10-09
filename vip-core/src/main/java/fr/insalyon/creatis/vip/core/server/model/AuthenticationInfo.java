package fr.insalyon.creatis.vip.core.server.model;

public class AuthenticationInfo {

    private String httpHeader;
    private String httpHeaderValue;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getHttpHeaderValue() {
        return httpHeaderValue;
    }

    public void setHttpHeaderValue(String httpHeaderValue) {
        this.httpHeaderValue = httpHeaderValue;
    }
}
