package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author gaignard
 */
public class ExpeSummaryTriple implements IsSerializable {
    private ExpeSummaryEntity subject;
    private ExpeSummaryEntity predicate;
    private ExpeSummaryEntity object;
    
    public ExpeSummaryTriple(){}
    
    public ExpeSummaryTriple (ExpeSummaryEntity subj, ExpeSummaryEntity pred, ExpeSummaryEntity obj) {
        this.subject = subj;
        this.predicate = pred;
        this.object = obj;
    }

    public ExpeSummaryEntity getObject() {
        return object;
    }

    public ExpeSummaryEntity getPredicate() {
        return predicate;
    }

    public ExpeSummaryEntity getSubject() {
        return subject;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final ExpeSummaryTriple other = (ExpeSummaryTriple) obj;
//        if (this.subject != other.subject && (this.subject == null || !this.subject.equals(other.subject))) {
//            return false;
//        }
//        if (this.predicate != other.predicate && (this.predicate == null || !this.predicate.equals(other.predicate))) {
//            return false;
//        }
//        if (this.object != other.object && (this.object == null || !this.object.equals(other.object))) {
//            return false;
//        }
//        return true;
//    }
    
    
}
