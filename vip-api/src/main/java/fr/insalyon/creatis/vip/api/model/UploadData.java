package fr.insalyon.creatis.vip.api.model;

public class UploadData {

    private String base64Content;
    private UploadDataType type;
    private String md5;

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    public UploadDataType getType() {
        return type;
    }

    public void setType(UploadDataType type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
