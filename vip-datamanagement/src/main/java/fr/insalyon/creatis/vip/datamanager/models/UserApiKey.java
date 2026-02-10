package fr.insalyon.creatis.vip.datamanager.models;

public class UserApiKey {

    private String storageIdentifier;
    private String userEmail;
    private String apiKey;

    public UserApiKey() {}

    public UserApiKey(
        String storageIdentifier, String userEmail, String apiKey) {
        this.storageIdentifier = storageIdentifier;
        this.userEmail = userEmail;
        this.apiKey = apiKey;
    }

    public String getStorageIdentifier() {
        return storageIdentifier;
    }

    public void setStorageIdentifier(String storageIdentifier) {
        this.storageIdentifier = storageIdentifier;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "UserApiKey@" + Integer.toHexString(hashCode())
            + "[storageIdentifier=" + storageIdentifier
            + ",userEmail=" + userEmail
            + ",apiKey=" + apiKey
            + "]";
    }
}
