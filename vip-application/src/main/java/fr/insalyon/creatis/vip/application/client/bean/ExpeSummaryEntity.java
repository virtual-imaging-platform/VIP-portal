package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author gaignard
 */
public class ExpeSummaryEntity implements IsSerializable {
    private String uri;
    private String label;

    public ExpeSummaryEntity(){}
    
    public  ExpeSummaryEntity(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getUri() {
        return uri;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final ExpeSummaryEntity other = (ExpeSummaryEntity) obj;
//        if ((this.uri == null) ? (other.uri != null) : !this.uri.equals(other.uri)) {
//            return false;
//        }
//        return true;
//    }
    
}
