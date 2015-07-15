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



import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author cervenansky
 */
public class testMidas {
//    private void loadTreeNodes(JSONObject jsonObject, TreeItem parent) {        
//        Iterator it = jsonObject.keys();
//    for (I key: jsonObject.keys()) {                
//        JSONValue jsonValue = jsonObject.get(key);    
//        TreeItem item = new TreeItem("<b>" + key + "</b> ");
//        parent.addItem(item);
//
//        if (jsonValue != null) {
//            if (jsonValue.isObject() != null) {
//                loadTreeNodes(jsonValue.isObject(), item);
//            } else if (jsonValue.isArray() != null) {
//
//            } else {
//                if ("oldValue".equals(key)) {
//                    item.setHTML("<b>before:</b> " + jsonValue.toString());
//                } else {
//                    item.setHTML("<b>after:</b> " + jsonValue.toString());
//                }                    
//            }
//        } else {
//            item.setText("<b>" + item.getText() + ":</b> null");
//        }
//    }        
//}
       private static  ArrayList<String> getFolders(String url, String ty) throws IOException, UnsupportedEncodingException, JSONException
      {
          String type = "folder_id";
          if(ty.equals("c"))
              type = "item_id";
          ArrayList<String> result = new ArrayList<String>();
          HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
          con.setRequestMethod("POST");
          con.setDoOutput(true);
          con.connect();
          System.out.println(con.getResponseMessage()+ "  " + con.getResponseCode());
          JSONObject jso2 =   getjson(con.getInputStream());
          con.disconnect();
          JSONArray jsona = jso2.getJSONArray("data");
          for(int i = 0 ; i < jsona.length(); i++ )
          {
                  result.add( jsona.getJSONObject(i).getString("folder_id"));
          }
          return result;
      }
    
    
    
        private static  ArrayList<String> getChildren(String url, String ty) throws IOException, UnsupportedEncodingException, JSONException
      {
          String type = "folders";
          String type_id = "folder_id";
          if(ty.equals("c"))
          {
              type = "items";
              type_id = "item_id";
          }
              ArrayList<String> result = new ArrayList<String>();
          HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
          con.setRequestMethod("POST");
          con.setDoOutput(true);
          con.connect();
          System.out.println(con.getResponseMessage()+ "  " + con.getResponseCode());
          JSONObject jso2 =   getjson(con.getInputStream());
          con.disconnect();
          JSONArray jsona = jso2.getJSONObject("data").getJSONArray(type);
          for(int i = 0 ; i < jsona.length(); i++ )
          {
                  result.add( jsona.getJSONObject(i).getString(type_id));
          }
          return result;
      }
    
    
    private static JSONObject getjson(InputStream is) throws UnsupportedEncodingException, IOException, JSONException
    {
        
                          System.out.println(is.toString());
                     
                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
    StringBuilder responseStrBuilder = new StringBuilder();

    String inputStr;
    while ((inputStr = streamReader.readLine()) != null)
        responseStrBuilder.append(inputStr);
      System.out.println(responseStrBuilder.toString());
      return   new JSONObject(responseStrBuilder.toString());
    }
    public static void main(String args[]) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, JSONException {
//         String name = "fcervenansky";
//            String pwd ="oeufs_sont_cuits";
//        String url  = " https://central.xnat.org";
//            XnatBusiness xbun = new XnatBusiness();
//   //      JSONObject jso =   new JSONObject(xbun.getProjects(xbun.getConnectionID(name, pwd, url),url));
//         String projectID="100RunsPerSubj";
//    //      JSONObject jso2 =   new JSONObject(xbun.getProject(xbun.getConnectionID(name, pwd, url),url,projectID));
//           String subjectID="CENTRAL_S01683";
//        //   JSONObject jso3 =   new JSONObject(xbun.getSubject(xbun.getConnectionID(name, pwd, url),url,projectID,subjectID));
//           String test ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//"<xnat:Subject ID=\"CENTRAL_S01683\" project=\"100RunsPerSubj\" label=\"Subject01\" xmlns:sapssans=\"http://nrg.wustl.edu/sapssans\" xmlns:cnda=\"http://nrg.wustl.edu/cnda\" xmlns:arc=\"http://nrg.wustl.edu/arc\" xmlns:val=\"http://nrg.wustl.edu/val\" xmlns:pipe=\"http://nrg.wustl.edu/pipe\" xmlns:fs=\"http://nrg.wustl.edu/fs\" xmlns:sf=\"http://nrg.wustl.edu/sf\" xmlns:wrk=\"http://nrg.wustl.edu/workflow\" xmlns:xdat=\"http://nrg.wustl.edu/security\" xmlns:cat=\"http://nrg.wustl.edu/catalog\" xmlns:nunda=\"http://nrg.wustl.edu/nunda\" xmlns:prov=\"http://www.nbirn.net/prov\" xmlns:xnat=\"http://nrg.wustl.edu/xnat\" xmlns:xnat_a=\"http://nrg.wustl.edu/xnat_assessments\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://nrg.wustl.edu/fs https://central.xnat.org/schemas/fs/fs.xsd http://nrg.wustl.edu/workflow https://central.xnat.org/schemas/pipeline/workflow.xsd http://nrg.wustl.edu/catalog https://central.xnat.org/schemas/catalog/catalog.xsd http://nrg.wustl.edu/pipe https://central.xnat.org/schemas/pipeline/repository.xsd http://nrg.wustl.edu/nunda https://central.xnat.org/schemas/nunda/nunda.xsd http://nrg.wustl.edu/arc https://central.xnat.org/schemas/project/project.xsd http://nrg.wustl.edu/cnda https://central.xnat.org/schemas/cnda_xnat/cnda_xnat.xsd http://nrg.wustl.edu/val https://central.xnat.org/schemas/validation/protocolValidation.xsd http://nrg.wustl.edu/sf https://central.xnat.org/schemas/subjforms/subjforms.xsd http://nrg.wustl.edu/sapssans https://central.xnat.org/schemas/sapssans/sapssans.xsd http://nrg.wustl.edu/xnat https://central.xnat.org/schemas/xnat/xnat.xsd http://nrg.wustl.edu/xnat_assessments https://central.xnat.org/schemas/assessments/assessments.xsd http://www.nbirn.net/prov https://central.xnat.org/schemas/birn/birnprov.xsd http://nrg.wustl.edu/security https://central.xnat.org/schemas/security/security.xsd\"><xnat:demographics xsi:type=\"xnat:demographicData\"><!--hidden_fields[xnat_abstractDemographicData_id=\"3181\"]--><xnat:age>25</xnat:age>" +
//"<xnat:gender>female</xnat:gender></xnat:demographics><xnat:experiments><xnat:experiment ID=\"CENTRAL_E03906\" project=\"100RunsPerSubj\" label=\"ResultsSbj01\" xsi:type=\"xnat:mrSessionData\">" +
//"<xnat:sharing><xnat:share label=\"ResultsSbj01\" project=\"project\">" +
//"<!--hidden_fields[xnat_experimentData_share_id=\"11376\",sharing_share_xnat_experimentDa_id=\"CENTRAL_E03906\"]--></xnat:share></xnat:sharing><xnat:resources>" +
//"<xnat:resource label=\"DATA\" URI=\"/data/xnat_central/archive/100RunsPerSubj/arc001/ResultsSbj01/RESOURCES/DATA/DATA_catalog.xml\" format=\"NIFTI\" xsi:type=\"xnat:resourceCatalog\">" +
//"<!--hidden_fields[xnat_abstractResource_id=\"123221436\",xnat_imageScanData_xnat_imagescandata_id=\"null\"]-->" +
//"</xnat:resource></xnat:resources><xnat:acquisition_site>Bethesda, MD</xnat:acquisition_site>" +
//"<xnat:subject_ID>CENTRAL_S01683</xnat:subject_ID>" +
//"<xnat:scanner>3.0 Tesla</xnat:scanner>" +
//"</xnat:experiment></xnat:experiments></xnat:Subject>";
//       DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
//   DocumentBuilder bldr = fctr.newDocumentBuilder();
//   InputSource insrc = new InputSource(new StringReader(test));
//   Document doc= bldr.parse(insrc);
//     //      JSONValue jsonValue = JSONParser.parse(xbun.getProjects(xbun.getConnectionID(name, pwd, url), url));
//     //      String key = jse.keys().toString();
//         Element nom = (Element)doc.getElementsByTagName("xnat:gender").item(0);
//           String validator = nom.getAttribute("validator");
//			System.out.println(validator);
//
//      
//          // loadTreeNodes((com.google.gwt.json.client.JSONObject)(jse),tree);
//           
//    }
//           
//           
      String name = "frederic.cervenansky@creatis.insa-lyon.fr";
      String pwd ="oeufs_sont_cuits";
      String site = "http://ofsep.creatis.insa-lyon.fr/midas/api/json?method=";
      String method = "midas.user.apikey.default";
      String type ="&format=json";
      String jsessionid ="";
      
      HttpURLConnection con = (HttpURLConnection) new URL(site+method+"&email="+name+"&password="+pwd).openConnection();
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.connect();
      JSONObject jso2 =   getjson(con.getInputStream());
      JSONObject js= jso2.getJSONObject("data");
      
      jsessionid = js.get("apikey").toString();
      con.disconnect();
      
      method = "midas.login";
      HttpURLConnection con2 = (HttpURLConnection) new URL(site+method+"&appname=Default"+"&email="+name+"&apikey="+jsessionid).openConnection();
      con2.setRequestMethod("POST");
      con2.setDoOutput(true);
      con2.connect();
      System.out.println(con2.getResponseMessage()+ "  " + con2.getResponseCode());
      jso2 =   getjson(con2.getInputStream());
      js= jso2.getJSONObject("data");
      String token =   js.get("token").toString();
//                        InputStream is2 = con2.getInputStream();
//                          System.out.println(is2.toString());
//                     
//                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is2, "UTF-8")); 
//    StringBuilder responseStrBuilder = new StringBuilder();
//
//    String inputStr;
//    while ((inputStr = streamReader.readLine()) != null)
//        responseStrBuilder.append(inputStr);
//      System.out.println(responseStrBuilder.toString());
//      JSONObject jso =   new JSONObject(responseStrBuilder.toString());
   
  method = "midas.folder.download";
      String id="165";
      HttpURLConnection con8 = (HttpURLConnection) new URL(site+method+"&token="+token+"&id="+id).openConnection();
      con8.setRequestMethod("GET");
      con8.setDoOutput(true);
      con8.connect();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(con8.getInputStream(), "UTF-8")); 
    StringBuilder responseStrBuilder = new StringBuilder();

    String inputStr;
    while ((inputStr = streamReader.readLine()) != null)
        responseStrBuilder.append(inputStr);
      
  File file = new File("D:\\" + id + ".zip");
BufferedWriter writer = new BufferedWriter(new FileWriter(file));
writer.write(responseStrBuilder.toString());
  writer.close();    
      
      
      
      
      method = "midas.folder.download";
      id="165";
      HttpURLConnection con7 = (HttpURLConnection) new URL(site+method+"&token="+token+"&id="+id).openConnection();
      con7.setRequestMethod("GET");
      con7.setDoOutput(true);
      con7.connect();
         streamReader = new BufferedReader(new InputStreamReader(con7.getInputStream())); 
         
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
int next = streamReader.read();
while (next > -1) {
    bos.write(next);
    next = streamReader.read();
}
bos.flush();
byte[] result = bos.toByteArray();
         
//     responseStrBuilder = new StringBuilder();
//    while ((inputStr = streamReader.readLine()) != null)
//     responseStrBuilder.append(inputStr);
//    

FileOutputStream fos = new FileOutputStream("D:\\"+id+".zip");
fos.write(result);
fos.close();

 

















           method = "midas.user.folders";
        ArrayList<String> folders = getFolders(site+method+"&token="+token,"f");
        for(String fols : folders)
        {
             method = "midas.folder.children";
            ArrayList<String> fold = getChildren(site+method+"&token="+token+"&id="+fols,"f");
            if(fold.size() != 0)
                 for(String fl : fold)
                {
                    ArrayList<String> child = getChildren(site+method+"&token="+token+"&id="+fl,"f");
                    if(child.size() != 0)
                    {
                        
                    }
                    else
                    {
                        child = getChildren(site+method+"&token="+token+"&id="+fl,"c");
                    }
                }
        }
        
      method = "midas.user.folders";
      HttpURLConnection con3 = (HttpURLConnection) new URL(site+method+"&token="+token).openConnection();
      con3.setRequestMethod("POST");
      con3.setDoOutput(true);
      con3.connect();
      System.out.println(con3.getResponseMessage()+ "  " + con3.getResponseCode());
      jso2 =   getjson(con3.getInputStream());
      System.out.println(jso2.toString());
      
      method = "midas.folder.children";
      ArrayList<String> child = getChildren(site+method+"&token="+token+"&id=165","f");
      
      HttpURLConnection con4 = (HttpURLConnection) new URL(site+method+"&token="+token+"&id=165").openConnection();
      con4.setRequestMethod("POST");
      con4.setDoOutput(true);
      con4.connect();
      System.out.println(con4.getResponseMessage()+ "  " + con4.getResponseCode());
      jso2 =   getjson(con4.getInputStream());
      System.out.println(jso2.toString());
    }
//  JSONObject jso =   new JSONObject(responseStrBuilder.toString());
//                        
//                        Document document = null;
//                         System.out.println(con2.getResponseMessage()+ "  " + con2.getResponseCode());
//                // maps =  con2.getHeaderFields();
//                          DocumentBuilderFactory factory =
//DocumentBuilderFactory
//                                                .newInstance();
//                                DocumentBuilder builder = factory.newDocumentBuilder();
//                                InputStream is = con2.getInputStream();
//                                 document = builder.parse(is);
//                                
//                                is.close();
//    }                               
////    }
////      private void loadTreeNodes(JSONObject jsonObject, TreeItem parent) throws JSONException {        
////    for (String key: jsonObject.keys()) {                
////        JSONValue jsonValue = (JSONValue) jsonObject.get(key);    
////        TreeItem item = new TreeItem("<b>" + key + "</b> ");
////        parent.addItem(item);
////
////        if (jsonValue != null) {
////            if (jsonValue.isObject() != null) {
////                loadTreeNodes(jsonValue.isObject(), item);
////            } else if (jsonValue.isArray() != null) {
////
////            } else {
////                if ("oldValue".equals(key)) {
////                    item.setHTML("<b>before:</b> " + jsonValue.toString());
////                } else {
////                    item.setHTML("<b>after:</b> " + jsonValue.toString());
////                }                    
////            }
////        } else {
////            item.setText("<b>" + item.getText() + ":</b> null");
////        }
////    }
////}
////
////    private void loadTreeNodes(com.google.gwt.json.client.JSONObject object, TreeItem item) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////    }
}
