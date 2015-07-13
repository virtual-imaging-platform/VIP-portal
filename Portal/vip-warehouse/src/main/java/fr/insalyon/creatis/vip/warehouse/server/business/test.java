/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.server.business;



import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class test {
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
    public static void main(String args[]) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, JSONException {
         String name = "fcervenansky";
            String pwd ="oeufs_sont_cuits";
        String url  = " https://central.xnat.org";
            XnatBusiness xbun = new XnatBusiness();
   //      JSONObject jso =   new JSONObject(xbun.getProjects(xbun.getConnectionID(name, pwd, url),url));
         String projectID="100RunsPerSubj";
    //      JSONObject jso2 =   new JSONObject(xbun.getProject(xbun.getConnectionID(name, pwd, url),url,projectID));
           String subjectID="CENTRAL_S01683";
        //   JSONObject jso3 =   new JSONObject(xbun.getSubject(xbun.getConnectionID(name, pwd, url),url,projectID,subjectID));
           String test ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<xnat:Subject ID=\"CENTRAL_S01683\" project=\"100RunsPerSubj\" label=\"Subject01\" xmlns:sapssans=\"http://nrg.wustl.edu/sapssans\" xmlns:cnda=\"http://nrg.wustl.edu/cnda\" xmlns:arc=\"http://nrg.wustl.edu/arc\" xmlns:val=\"http://nrg.wustl.edu/val\" xmlns:pipe=\"http://nrg.wustl.edu/pipe\" xmlns:fs=\"http://nrg.wustl.edu/fs\" xmlns:sf=\"http://nrg.wustl.edu/sf\" xmlns:wrk=\"http://nrg.wustl.edu/workflow\" xmlns:xdat=\"http://nrg.wustl.edu/security\" xmlns:cat=\"http://nrg.wustl.edu/catalog\" xmlns:nunda=\"http://nrg.wustl.edu/nunda\" xmlns:prov=\"http://www.nbirn.net/prov\" xmlns:xnat=\"http://nrg.wustl.edu/xnat\" xmlns:xnat_a=\"http://nrg.wustl.edu/xnat_assessments\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://nrg.wustl.edu/fs https://central.xnat.org/schemas/fs/fs.xsd http://nrg.wustl.edu/workflow https://central.xnat.org/schemas/pipeline/workflow.xsd http://nrg.wustl.edu/catalog https://central.xnat.org/schemas/catalog/catalog.xsd http://nrg.wustl.edu/pipe https://central.xnat.org/schemas/pipeline/repository.xsd http://nrg.wustl.edu/nunda https://central.xnat.org/schemas/nunda/nunda.xsd http://nrg.wustl.edu/arc https://central.xnat.org/schemas/project/project.xsd http://nrg.wustl.edu/cnda https://central.xnat.org/schemas/cnda_xnat/cnda_xnat.xsd http://nrg.wustl.edu/val https://central.xnat.org/schemas/validation/protocolValidation.xsd http://nrg.wustl.edu/sf https://central.xnat.org/schemas/subjforms/subjforms.xsd http://nrg.wustl.edu/sapssans https://central.xnat.org/schemas/sapssans/sapssans.xsd http://nrg.wustl.edu/xnat https://central.xnat.org/schemas/xnat/xnat.xsd http://nrg.wustl.edu/xnat_assessments https://central.xnat.org/schemas/assessments/assessments.xsd http://www.nbirn.net/prov https://central.xnat.org/schemas/birn/birnprov.xsd http://nrg.wustl.edu/security https://central.xnat.org/schemas/security/security.xsd\"><xnat:demographics xsi:type=\"xnat:demographicData\"><!--hidden_fields[xnat_abstractDemographicData_id=\"3181\"]--><xnat:age>25</xnat:age>" +
"<xnat:gender>female</xnat:gender></xnat:demographics><xnat:experiments><xnat:experiment ID=\"CENTRAL_E03906\" project=\"100RunsPerSubj\" label=\"ResultsSbj01\" xsi:type=\"xnat:mrSessionData\">" +
"<xnat:sharing><xnat:share label=\"ResultsSbj01\" project=\"project\">" +
"<!--hidden_fields[xnat_experimentData_share_id=\"11376\",sharing_share_xnat_experimentDa_id=\"CENTRAL_E03906\"]--></xnat:share></xnat:sharing><xnat:resources>" +
"<xnat:resource label=\"DATA\" URI=\"/data/xnat_central/archive/100RunsPerSubj/arc001/ResultsSbj01/RESOURCES/DATA/DATA_catalog.xml\" format=\"NIFTI\" xsi:type=\"xnat:resourceCatalog\">" +
"<!--hidden_fields[xnat_abstractResource_id=\"123221436\",xnat_imageScanData_xnat_imagescandata_id=\"null\"]-->" +
"</xnat:resource></xnat:resources><xnat:acquisition_site>Bethesda, MD</xnat:acquisition_site>" +
"<xnat:subject_ID>CENTRAL_S01683</xnat:subject_ID>" +
"<xnat:scanner>3.0 Tesla</xnat:scanner>" +
"</xnat:experiment></xnat:experiments></xnat:Subject>";
       DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
   DocumentBuilder bldr = fctr.newDocumentBuilder();
   InputSource insrc = new InputSource(new StringReader(test));
   Document doc= bldr.parse(insrc);
     //      JSONValue jsonValue = JSONParser.parse(xbun.getProjects(xbun.getConnectionID(name, pwd, url), url));
     //      String key = jse.keys().toString();
         Element nom = (Element)doc.getElementsByTagName("xnat:gender").item(0);
           String validator = nom.getAttribute("validator");
			System.out.println(validator);

      
          // loadTreeNodes((com.google.gwt.json.client.JSONObject)(jse),tree);
           
    }
           
           
           
//           
//           
//           
//           
//    final StringBuilder up = new StringBuilder(name);
//    up.append(':');
//    up.append(pwd);
//String jsessionid ="";
//    final StringBuilder auth = new StringBuilder("Basic ");
//    auth.append(Base64.encode(up.toString().getBytes()).trim());
//                HttpURLConnection con = (HttpURLConnection) new URL("https://central.xnat.org/data/JSESSION").openConnection();
//                   
//con.setRequestMethod("POST");
//                        con.setDoOutput(true);
//                        con.setRequestProperty("Authorization", auth.toString());
//                        con.connect();
////                         InputStream is2 = con.getInputStream();
////                         String temp = new String(con.getPermission().toString());
//                        Map<String, List<String> > maps =  con.getHeaderFields();
//                        List<String> jses = maps.get("Set-Cookie");
//                        for(String value : jses)
//                        {
//                            if (value.contains("JSESSIONID="))
//                                //jsessionid = value;
//                                jsessionid = value.split(";")[0].replace("JSESSIONID=", "");
//                        }
//                        con.disconnect();
//                        HttpURLConnection con2 = (HttpURLConnection) new URL("https://central.xnat.org/data/archive/projects?format=json").openConnection();
//                   
//con2.setRequestMethod("GET");
//                        con2.setDoOutput(true);
//                        con2.setRequestProperty("Cookie", "JSESSIONID="+jsessionid);
//                        //con2.setRequestProperty("Authorization", auth.toString());
//                        con2.connect();
//                        
//                          System.out.println(con2.getResponseMessage()+ "  " + con2.getResponseCode());
//                        InputStream is2 = con2.getInputStream();
//                          System.out.println(is2.toString());
//                       
//                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is2, "UTF-8")); 
//    StringBuilder responseStrBuilder = new StringBuilder();
//
//    String inputStr;
//    while ((inputStr = streamReader.readLine()) != null)
//        responseStrBuilder.append(inputStr);
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
