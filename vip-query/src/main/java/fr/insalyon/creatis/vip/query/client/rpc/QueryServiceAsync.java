/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;


import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public interface QueryServiceAsync {
     public void getQureies(AsyncCallback<List<Query>> asyncCallback);
     public void  getVersion(AsyncCallback <List<String[]>> asyncCallback) ;
     public void add(Query query,AsyncCallback <Void> asyncCallback)throws QueryException;
     public void addVersion(QueryVersion version,Query query,AsyncCallback <Void> asyncCallback)throws QueryException;
}
