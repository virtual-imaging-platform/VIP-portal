/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.warehouse.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.warehouse.client.view.WareHouseException;
import fr.insalyon.creatis.vip.warehouse.server.business.ItemDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Frederic Cervenansky
 */
public interface WarehouseServiceAsync {
    
    
    public void getSites(AsyncCallback< List<String> > asyncCallback);
    
    public void isSessionAlive(String nick, String site, AsyncCallback<Boolean> asyncCallback);
    
    public void getConnection(String nickname, String pwd, String site, String type, AsyncCallback<Void> asyncCallback);

    public void getProjects(String nick, String site, String type, AsyncCallback< ArrayList<String> > asyncCallback);
    
    public void getProject(String nick, String site, String type, String projectid,  String itemtype, AsyncCallback< ArrayList<String> > asyncCallback);

    public void getData(String nick, String site, String waretype, String name, 
                    String id,  String itemtype, String path, AsyncCallback< Void > asyncCallback);
   
    /**
     *
     * @param asyncCallback
     */
    public void test( AsyncCallback<String> asyncCallback);
    //public void getData(String project, AsyncCallback<JSONObject> asyncCallback);
}

