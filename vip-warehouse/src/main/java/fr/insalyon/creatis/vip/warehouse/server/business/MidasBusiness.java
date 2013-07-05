/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.server.business;

import fr.insalyon.creatis.vip.warehouse.client.WarehouseConstants;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseDataSQL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cervenansky
 */
public class MidasBusiness extends MedImgWarehouseBusiness{
    
 
    public MidasBusiness()
   {

   }
    
   // test if jsession is still available if not generate another one
   public Boolean getConnection(String nickname, String pwd, String url) throws IOException, DAOException, MalformedURLException, JSONException
   {
       boolean bres = false;
       System.out.println("ok c'est bon");
    //   email= "frederic.cervenansky@creatis.insa-lyon.fr";
      // pwd="oeufs_sont_cuits";
       //String url = "http://ofsep.creatis.insa-lyon.fr/midas";//midb.getURL(email, site);
       String url2 = getApiKeyURL(nickname,pwd,url);
       System.out.println(url2);
       String apikey = getToken(url2,"apikey");
       String jsession = getToken(getTokenURL(nickname,apikey,url),"token");
        if(!jsession.isEmpty())
        {
               System.out.println("là par contre");
            midb.setSession(nickname, url, jsession);
            bres = true;
        }
        return bres;
   }
   
  public HashMap<String, String> getTopFolder(String nickname, String url) throws DAOException, IOException, JSONException
    //public String getTopFolder(String nickname, String url) throws DAOException, IOException, JSONException
   {
       HashMap<String, String> folders = new HashMap<String, String>();
       String token = midb.getSession(nickname, url);
       System.out.println("token :" +token);
       ArrayList<ItemDB> items =  getFolders(getTopFolderURL(url,token));
       System.out.println("on a peche un truc?");
       for(ItemDB item : items)
       {
           System.out.println("id :" +item.id +" name :" + item.name);
           folders.put(item.id, item.name);
       }
       return folders;
   }
   
   public ArrayList<ItemDB> getChildren(String email, String site, String id) throws DAOException, IOException, JSONException
   {
       String token = midb.getSession(email, site);
       ArrayList<ItemDB> result = getChildren(getChildrenURL(site,id,token),"f");
       if (result.isEmpty())
           result  = getChildren(getChildrenURL(site,id,token),"c");
       return result;
   }
   
   private JSONObject getConnection(String url) throws IOException, UnsupportedEncodingException, JSONException
   {
       HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();
        System.out.println(con.getResponseMessage()+ "  " + con.getResponseCode());
        JSONObject jso = getjson(con.getInputStream());
        con.disconnect();
        return jso;
   }
  
   private ArrayList<ItemDB> getFolders(String url) throws IOException, JSONException 
   {
          ArrayList<ItemDB> result = new ArrayList<ItemDB>();
          JSONObject jso2 =    getConnection(url);
          JSONArray jsona = jso2.getJSONArray("data");
          for(int i = 0 ; i < jsona.length(); i++ )
          {
              ItemDB idb = new ItemDB();
              idb.id = jsona.getJSONObject(i).getString("folder_id");
              idb.description = jsona.getJSONObject(i).getString("description");
              idb.name  = jsona.getJSONObject(i).getString("name");
              result.add( idb);
          }
          return result;
   }
   
    private ArrayList<ItemDB> getChildren(String url, String type) throws IOException, JSONException 
   {
        String type_fol = "folders";
         String type_id = "folder_id";
          if(type.equals("c"))
          {
              type_fol = "items";
              type_id = "item_id";
          }
         
       
          ArrayList<ItemDB> result = new ArrayList<ItemDB>();
          JSONObject jso2 =    getConnection(url);
           JSONArray jsona = jso2.getJSONObject("data").getJSONArray(type_fol);
          for(int i = 0 ; i < jsona.length(); i++ )
          {
              ItemDB idb = new ItemDB();
              idb.id = jsona.getJSONObject(i).getString(type_id);
              idb.description = jsona.getJSONObject(i).getString("description");
          
              idb.name  = jsona.getJSONObject(i).getString("name");
              result.add( idb);
          }
          return result;
   }
   
   private static JSONObject getjson(InputStream is) throws UnsupportedEncodingException, IOException, JSONException
    {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return   new JSONObject(responseStrBuilder.toString());
    }
   

   // getToken can be use to get the apikey and a token associated to the apikey
   // url can contains the method: midas.user.apikey.default or midas.user.dafaultmidas.login for the token
   public String getToken( String url,String key) throws MalformedURLException, IOException, JSONException
   {
       HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
       con.setRequestMethod("POST");
       con.setDoOutput(true);
       con.connect();
       JSONObject jso2 =   getjson(con.getInputStream());
       JSONObject js= jso2.getJSONObject("data");
       con.disconnect();
       return js.get(key).toString();
   }
   
    private String getChildrenURL(String site,String id, String token)
   {
       String method = "midas.folder.children";
       return site+"/api/json?method="+method+"&token="+token+"&id="+id;
   }
   
   private String getTopFolderURL(String site,String token)
   {
       String method = "midas.user.folders";
       return site+"/api/json?method="+method+"&token="+token;
   }
 
   private String getApiKeyURL(String usr, String pwd, String site)
   {
       String method = "midas.user.apikey.default";
       return site+"/api/json?method="+method+"&email="+usr+"&password="+pwd;
   }
   
   private String getTokenURL(String usr, String key, String site)
   {
       String method = "midas.login";
       return site+"/api/json?method="+method+"&appname=Default"+"&email="+usr+"&apikey="+key;
   }
}
