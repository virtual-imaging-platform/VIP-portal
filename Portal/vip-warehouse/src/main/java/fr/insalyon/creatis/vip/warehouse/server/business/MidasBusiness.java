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
package fr.insalyon.creatis.vip.warehouse.server.business;

import fr.insalyon.creatis.devtools.zip.UnZipper;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.warehouse.client.WarehouseConstants;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseDataSQL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cervenansky
 */
public class MidasBusiness extends MedImgWarehouseBusiness {

    GRIDAPoolClient poolClient;

    public MidasBusiness() {
    }

    public Boolean getData(User user, String site, String name, String id, String type, String path) throws DAOException, IOException, JSONException, MalformedURLException, DataManagerException, GRIDAClientException {

        boolean btest = false;
        System.out.println("download start");
        String token = midb.getSession(user.getEmail(), site);
        poolClient = CoreUtil.getGRIDAPoolClient();
        // test if id is folder;
        if (type.equals("folder")) {
            downloadFolder(getDownloadFolderUrl(site, id, token), name, path, user);
        } else {
            downloadFile(getDownloadDataUrl(site, id, token), name, user, path);
        }
        return btest;
    }

    // test if jsession is still available if not generate another one
    public Boolean getConnection(String nickname, String pwd, String url) throws IOException, DAOException, MalformedURLException, JSONException {
        boolean bres = false;
        String url2 = getApiKeyURL(nickname, pwd, url);
        String apikey = getToken(url2, "apikey");
        String jsession = getToken(getTokenURL(nickname, apikey, url), "token");
        if (!jsession.isEmpty()) {
            midb.setSession(nickname, url, jsession);
            bres = true;
        }
        return bres;
    }

    public ArrayList<String> getTopFolder(String nickname, String url) throws DAOException, IOException, JSONException //public String getTopFolder(String nickname, String url) throws DAOException, IOException, JSONException
    {
        ArrayList<String> result = new ArrayList<String>();
        HashMap<String, String> folders = new HashMap<String, String>();
        String token = midb.getSession(nickname, url);
        ArrayList<ItemDB> items = getFolders(getTopFolderURL(url, token));
        for (ItemDB item : items) {
            System.out.println("id :" + item.id + " name :" + item.name);
            result.add(String.valueOf(item.id) + "#" + String.valueOf(item.name) + "#"
                    + String.valueOf(item.description) + "#" + "folder");
            folders.put(item.id, item.name);
        }
        return result;
    }

    public ArrayList<String> getChildren(String email, String site, String id, String type) throws DAOException, IOException, JSONException {

        String token = midb.getSession(email, site);
        ArrayList<String> result = getChildren(getChildrenURL(site, id, token), "folder");
        if (result.isEmpty()) {
            result = getChildren(getChildrenURL(site, id, token), "item");
        }
        return result;


    }

    private JSONObject getConnection(String url) throws IOException, UnsupportedEncodingException, JSONException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();
        System.out.println(con.getResponseMessage() + "  " + con.getResponseCode());
        JSONObject jso = getjson(con.getInputStream());
        con.disconnect();
        return jso;
    }

    private void downloadFolder(String url, String id, String path, User user) throws MalformedURLException, IOException, DataManagerException, GRIDAClientException {
        String rootDirectory = Server.getInstance().getDataManagerPath() + "/uploads/";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.connect();
        System.out.println(con.getResponseMessage() + "  " + con.getResponseCode());
        InputStream input = con.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[8 * 1024]; // Or whatever
        int bytesRead;
        while ((bytesRead = input.read(buffer)) > 0) {
            bos.write(buffer, 0, bytesRead);
        }


        bos.flush();
        byte[] result = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(rootDirectory + id + ".zip");
        fos.write(result);
        fos.close();
        UnZipper.unzip(rootDirectory + id + ".zip");
        processDir(rootDirectory + String.valueOf(id), path + "/" + id, user);
    }

    private void downloadFile(String url, String id, User user, String path) throws MalformedURLException, IOException, GRIDAClientException, DataManagerException {

        String rootDirectory = Server.getInstance().getDataManagerPath() + "/uploads/";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.connect();

        System.out.println(con.getResponseMessage() + "  " + con.getResponseCode());
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int next = streamReader.read();
        while (next > -1) {
            bos.write(next);
            next = streamReader.read();
        }
        bos.flush();
        byte[] result = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(rootDirectory + id);
        fos.write(result);
        fos.close();
        poolClient.uploadFile(rootDirectory + id, DataManagerUtil.parseBaseDir(user, path), user.getEmail());

    }

    private ArrayList<ItemDB> getFolders(String url) throws IOException, JSONException {
        ArrayList<ItemDB> result = new ArrayList<ItemDB>();
        JSONObject jso2 = getConnection(url);
        JSONArray jsona = jso2.getJSONArray("data");
        for (int i = 0; i < jsona.length(); i++) {
            ItemDB idb = new ItemDB();
            idb.id = jsona.getJSONObject(i).getString("folder_id");
            idb.description = jsona.getJSONObject(i).getString("description");
            idb.name = jsona.getJSONObject(i).getString("name");
            result.add(idb);
        }
        return result;
    }

    private ArrayList<String> getChildren(String url, String type) throws IOException, JSONException {
        String type_fol = "folders";
        String type_id = "folder_id";
        if (type.equals("item")) {
            type_fol = "items";
            type_id = "item_id";
        }


        ArrayList<String> result = new ArrayList<String>();
        JSONObject jso2 = getConnection(url);
        JSONArray jsona = jso2.getJSONObject("data").getJSONArray(type_fol);
        for (int i = 0; i < jsona.length(); i++) {
            ItemDB idb = new ItemDB();
            idb.id = jsona.getJSONObject(i).getString(type_id);
            idb.description = jsona.getJSONObject(i).getString("description");

            idb.name = jsona.getJSONObject(i).getString("name");
            result.add(String.valueOf(idb.id) + "#" + String.valueOf(idb.name) + "#"
                    + String.valueOf(idb.description) + "#" + type);
            System.out.println(String.valueOf(idb.id) + "#" + String.valueOf(idb.name) + "#"
                    + String.valueOf(idb.description) + "#" + type);
        }
        return result;
    }

    private static JSONObject getjson(InputStream is) throws UnsupportedEncodingException, IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return new JSONObject(responseStrBuilder.toString());
    }

    // getToken can be use to get the apikey and a token associated to the apikey
    // url can contains the method: midas.user.apikey.default or midas.user.dafaultmidas.login for the token
    public String getToken(String url, String key) throws MalformedURLException, IOException, JSONException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();
        JSONObject jso2 = getjson(con.getInputStream());
        JSONObject js = jso2.getJSONObject("data");
        con.disconnect();
        return js.get(key).toString();
    }

    private String getChildrenURL(String site, String id, String token) {
        String method = "midas.folder.children";
        return site + "/api/json?method=" + method + "&token=" + token + "&id=" + id;
    }

    private String getTopFolderURL(String site, String token) {
        String method = "midas.user.folders";
        return site + "/api/json?method=" + method + "&token=" + token;
    }

    private String getApiKeyURL(String usr, String pwd, String site) {
        String method = "midas.user.apikey.default";
        return site + "/api/json?method=" + method + "&email=" + usr + "&password=" + pwd;
    }

    private String getTokenURL(String usr, String key, String site) {
        String method = "midas.login";
        return site + "/api/json?method=" + method + "&appname=Default" + "&email=" + usr + "&apikey=" + key;
    }

    private String getDownloadFolderUrl(String site, String id, String token) {
        String method = "midas.folder.download";
        return site + "/api/json?method=" + method + "&token=" + token + "&id=" + id;
    }

    private String getDownloadDataUrl(String site, String id, String token) {
        String method = "midas.item.download";
        return site + "/api/json?method=" + method + "&token=" + token + "&id=" + id;
    }

    private void processDir(String dir, String path, User user) throws DataManagerException, GRIDAClientException {

        for (File f : new File(dir).listFiles()) {

            if (f.isDirectory()) {
                processDir(f.getAbsolutePath(), dir + "/" + f.getName(), user);
            } else {
                poolClient.uploadFile(f.getAbsolutePath(), DataManagerUtil.parseBaseDir(user, path), user.getEmail());
            }
        }
    }
}
