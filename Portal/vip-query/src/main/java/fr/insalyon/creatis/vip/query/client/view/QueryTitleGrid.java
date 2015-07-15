/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.launch.QueryLaunchTab;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class QueryTitleGrid extends ApplicationsTileGrid {

    private HashMap<QueryTitleGrid.Key, String> map;
    private static QueryTitleGrid instance;

    public static QueryTitleGrid getInstance() {
        if (instance == null) {
            instance = new QueryTitleGrid();
        }

        return instance;
    }

    public QueryTitleGrid() {
        super("My Queries");
        this.setID(id);
        loadApplications();
    }

    /**
     *
     * @param queryName
     * @param queryVersion
     */
    @Override
    public void parse(String queryName, String queryVersion) {
        String queryVersionID;
        queryVersionID = map.get(new QueryTitleGrid.Key(queryName, queryVersion));
        Layout.getInstance().addTab(new QueryLaunchTab(queryName, queryVersionID, queryVersion));

    }

    public void loadApplications() {

        final AsyncCallback<List<String[]>> callback;
        callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load queries:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String[]> result) {
                map = new HashMap<QueryTitleGrid.Key, String>();
                for (String[] q : result) {
                    //name,version,image
                    addApplication(q[0], "v." + q[2], QueryConstants.APP_IMG_QUERY);
                    map.put(new QueryTitleGrid.Key(q[0], "v." + q[2]), q[3]);
                }
            }
        };
        QueryService.Util.getInstance().getQueries(callback);

    }

    public class Key {

        private String name;
        private String version;

        /**
         *
         * @param name
         * @param version
         */
        public Key(String name, String version) {
            this.name = name;
            this.version = version;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof QueryTitleGrid.Key) {
                QueryTitleGrid.Key s = (QueryTitleGrid.Key) obj;
                return name.equals(s.name) && version.equals(s.version);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (name + version).hashCode();
        }
    }

    public void addQuery() {
        loadApplications();
    }
}
