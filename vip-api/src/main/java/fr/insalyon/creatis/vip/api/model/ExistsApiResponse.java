package fr.insalyon.creatis.vip.api.model;

public class ExistsApiResponse {

    private Boolean exists;

    public ExistsApiResponse() {
    }

    public ExistsApiResponse(Boolean exists) {
        this.exists = exists;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }
}
