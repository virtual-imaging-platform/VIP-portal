/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.server.business;

import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseDataSQL;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cervenansky
 */
public class XnatBusiness extends MedImgWarehouseBusiness{


    public XnatBusiness()
   {
      
   }
    
  
    
    public Boolean isSessionAlive(String nick, String site) throws DAOException, IOException
    {
        boolean bres = false;
        String jsession = midb.getSession(nick, site);
        String url = midb.getURL(nick, site);
        if (testConnection(jsession, url))
        {
                bres = true;
        }
        return bres;
    }
    
   // test if jsession is still available if not generate another one
   public Boolean getConnection(String nickname, String pwd, String url) throws IOException, DAOException
   {
       boolean bres = false;
      // String url = midb.getURL(nickname, url);
       String jsession = getConnectionID(nickname,pwd,url);
        if(!jsession.isEmpty())
        {
            midb.setSession(nickname, url, jsession);
            bres = true;
        }
        return bres;
   }
   
   public boolean testConnection(String jsession, String url) throws IOException
   { 
       boolean bres = false;
       HttpURLConnection connect = (HttpURLConnection) new URL(createURL(url)).openConnection();
        connect.setRequestMethod("GET");
        connect.setDoOutput(true);
        connect.setRequestProperty("Cookie", "JSESSIONID=" + jsession);
        connect.connect();
        if (connect.getResponseMessage().equals("OK"))
            bres = true;
        connect.disconnect();
        return  bres;
        
       
   }
   

   
   private String connectXnat(URL url, String jsID) throws MalformedURLException, IOException
   {
       HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET");
        connect.setDoOutput(true);
        connect.setRequestProperty("Cookie", "JSESSIONID=" + jsID);
        connect.connect();
        System.out.println(connect.getResponseMessage()+ "  " + connect.getResponseCode());
        InputStream is2 = connect.getInputStream();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is2, "UTF-8")); 
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        connect.disconnect();
        return  responseStrBuilder.toString();
    }
   
   
   public String getProjects(String nick, String site) throws DAOException, MalformedURLException, IOException
   {
       return connectXnat(new URL(createURLprojects(midb.getURL(nick, site))), midb.getSession(nick, site));
   }
   
//    public String getProjects( String url, String jsID) throws MalformedURLException, IOException, JSONException
//    {
//        return connectXnat(new URL(createURLprojects(url)), jsID);
//    }
    
    public String getProject( String nick, String site, String projectID) throws MalformedURLException, IOException, JSONException, DAOException
    {
         return connectXnat(new URL(createURLproject(midb.getURL(nick, site), projectID)), midb.getSession(nick, site));
     }
    
   
//    public String getProject( String url, String jsID, String projectID) throws MalformedURLException, IOException, JSONException
//    {
//         return connectXnat(new URL(createURLproject(url, projectID)), jsID);
//     }
    
    
     public String getSubject(String nick, String site, String projectID, String subjectID) throws MalformedURLException, IOException, JSONException, DAOException
    {
           return connectXnat(new URL(createURLSubject(midb.getURL(nick, site), projectID, subjectID)), midb.getSession(nick, site));
    }
   
    
//    public String getSubject(String url, String jsID, String projectID, String subjectID) throws MalformedURLException, IOException, JSONException
//    {
//           return connectXnat(new URL(createURLSubject(url, projectID, subjectID)), jsID);
//    }
//    
    
//    public JSONObject getData(String project)
//    {
//        JSONObject jso = new JSONObject();
//        return jso;
//    }
    
    // get the jsessionid
    private String getConnectionID(String user, String pwd, String url) throws IOException
    {
        String jsessionid = "";
        HttpURLConnection con = (HttpURLConnection) new URL(createURL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Authorization", buildAuthorization(user, pwd).toString());
        con.connect();
        Map<String, List<String> > maps =  con.getHeaderFields();
        List<String> jses = maps.get("Set-Cookie");
        for(String value : jses)
        {
            if (value.contains("JSESSIONID="))
                jsessionid = value.split(";")[0].replace("JSESSIONID=", "");
        }
        con.disconnect();
        return jsessionid;
    }
    
    
    private String createURLprojects(String url)
    {
        return url + "/data/archive/projects?format=json";
    }
     private String createURLproject(String url, String projectID)
    {
        return url + "/data/archive/projects/" + projectID + "/subjects?format=json";
    }
     
      private String createURLSubject(String url, String projectID, String subjectID)
    {
        return url + "/data/archive/projects/" + projectID + "/subjects/"+ subjectID +"?format=json";
        ///scans?columns=ID,xnat:xnat:gender/parameters/te&format=html
    }
    
    private String createURL(String url)
    {
        return url + "/data/JSESSION";
        
    }
    
    
    private StringBuilder buildAuthorization(String user, String pwd)
    {
        final StringBuilder up = new StringBuilder(user);
        up.append(':');
        up.append(pwd);
    
        final StringBuilder auth = new StringBuilder("Basic ");
        auth.append(Base64.encode(up.toString().getBytes()).trim());
        return auth;
    }
}
