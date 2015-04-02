/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server;

import fr.insalyon.creatis.vip.n4u.client.rpc.N4uException;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import fr.insalyon.creatis.vip.n4u.client.EnumInputTypes;
import fr.insalyon.creatis.vip.n4u.client.EnumTypes;
import fr.insalyon.creatis.vip.n4u.server.velocity.Velocity;
import fr.insalyon.creatis.vip.n4u.server.velocity.VelocityException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Nouha Boujelben
 */
public class FileProcessServiceImpl extends fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet
        implements FileProcessService {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileProcessServiceImpl.class);
    //get the value from first appel to generateScriptFile
    private String generateTime = null;
    Velocity ve;
    List<String> mandatoryDir=null;

    /**
     *
     * @param jobFile the jobFIle path
     * @param expressFile the expressFile path
     * @see FileProcessService
     * @return table of int first value is the number of inputs et the second
     * value is the number of input file.
     * @throws N4uException
     */
    @Override
    public int[] fileJobProcess(String jobFile, String expressFile) throws N4uException {
        int[] result = new int[2];
        int nb;
        String localJobFilePath = null;
        String localExpressFilePath = null;
        try {
            localJobFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), jobFile), Server.getInstance().getN4uApplicationFilesRepository());
            localExpressFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), expressFile), Server.getInstance().getN4uApplicationFilesRepository());
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }

        try {
            Scanner scanner = new Scanner(new FileInputStream(localJobFilePath));
            String ligne = scanner.nextLine();
            while (ligne.substring(0, 1).equals("#")) {
                ligne = scanner.nextLine();
            }
            StringTokenizer strok = new StringTokenizer(ligne, "\t");
            nb = strok.countTokens();
            result[0] = nb;
            Scanner scanner2 = new Scanner(new FileInputStream(localExpressFilePath));
            mandatoryDir = new ArrayList<String>();
            while (scanner2.hasNext()) {
                String readLigne = scanner2.nextLine();
                if (readLigne.contains("commonDir")) {
                    mandatoryDir.add(readLigne);
                }
                if (readLigne.contains("ExplicitFiles")) {
                    int fileNumber = Integer.parseInt(readLigne.substring(14));
                    result[1] = fileNumber;
                }
            }
        } catch (IOException ex) {

            throw new N4uException(ex);
        }
        return result;
    }
    //name,type,description,requiredField

    /**
     *
     * @param xmlFile path of xml file
     * @return list of inputs with values (name,required,description)
     * @throws N4uException
     */
    @Override
    public List<String[]> parseXmlFile(String xmlFile) throws N4uException {

        List<String[]> listInputs = new ArrayList<String[]>();

        String localXmlFilePath = null;
        try {
            localXmlFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), xmlFile), Server.getInstance().getN4uApplicationFilesRepository());
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }

        try {

            File fXmlFile = new File(localXmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            String[] nameDesc = new String[3];
            nameDesc[0] = doc.getDocumentElement().getAttribute("name");
            nameDesc[1] = doc.getDocumentElement().getAttribute("version");
            nameDesc[2] = doc.getElementsByTagName("description").item(0).getTextContent();

            listInputs.add(nameDesc);
            //commonDir 
            NodeList mandatoryCommonDirs = doc.getElementsByTagName("mandatoryCommonDirs");
            mandatoryDir = new ArrayList<String>();
            if (mandatoryCommonDirs.getLength() != 0) {
                NodeList commonDirs = mandatoryCommonDirs.item(0).getChildNodes();
                if (commonDirs.getLength() != 0) {
                    for (int j = 0; j < commonDirs.getLength(); j++) {
                        Node nNode = commonDirs.item(j);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE & nNode.getNodeName().equals("mandatoryCommonDir")) {
                            Element eElement = (Element) nNode;
                            String value = eElement.getAttribute("value");
                            mandatoryDir.add(value);

                        }
                    }
                }
            }

            List<NodeList> l = new ArrayList<NodeList>();
            NodeList e = doc.getElementsByTagName("inputs");
            //All nodes in the inputs
            NodeList nList = e.item(0).getChildNodes();

            for (int tempp = 0; tempp < nList.getLength(); tempp++) {

                Node nNode = nList.item(tempp);

                if (nNode.getNodeName().equals("entries")) {

                    NodeList nList2 = nNode.getChildNodes();

                    l.add(nList2);
                }

            }

            for (NodeList entriesList : l) {
                for (int temp = 0; temp < entriesList.getLength(); temp++) {

                    Node nNode = entriesList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE & nNode.getNodeName().equals("entry")) {
                        String[] value = new String[4];
                        Element eElement = (Element) nNode;
                        value[0] = eElement.getAttribute("name");//NAME
                        value[1] = parseTypeSupported(eElement.getAttribute("type"));//test if the type is supported
                        if (eElement.getAttribute("type").equalsIgnoreCase(EnumTypes.Combo.toString()) || eElement.getAttribute("type").equalsIgnoreCase(EnumTypes.CheckBox.toString()) || eElement.getAttribute("type").equalsIgnoreCase(EnumTypes.Radio.toString())) {
                            String vals = "";
                            //add value of combo to description 
                            //default value
                            String defaulVal = eElement.getAttribute("default");
                            //values of type combo                  
                            NodeList el = eElement.getChildNodes();
                            for (int i = 0; i < el.getLength(); i++) {
                                Node eNode = el.item(i);
                                if (eNode.getNodeName().equals("value")) {
                                    Element eElementNode = (Element) eNode;
                                    vals = vals + " " + eElementNode.getAttribute("val");
                                }
                            }
                            value[3] = eElement.getElementsByTagName("description").item(0).getTextContent() + "  " + "Default value is: " + defaulVal + "  " + "Values:" + vals;
                        } else if (eElement.getAttribute("type").equalsIgnoreCase(EnumTypes.File.toString())) {

                            NodeList el = eElement.getChildNodes();
                            String extensions = "";
                            for (int i = 0; i < el.getLength(); i++) {
                                Node eNode = el.item(i);
                                if (eNode.getNodeName().equals("ext")) {
                                    Element eElementNode = (Element) eNode;
                                    extensions = extensions + " " + eElementNode.getTextContent();
                                }
                            }

                            value[3] = eElement.getElementsByTagName("description").item(0).getTextContent() + "  " + "Possible extension values " + extensions;

                        } else {
                            value[3] = eElement.getElementsByTagName("description").item(0).getTextContent();
                        }
                        value[2] = eElement.getAttribute("required");//required
                        listInputs.add(value);
                    }
                }
            }

            return listInputs;

        } catch (ParserConfigurationException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (SAXException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
    }

    /**
     * method to generate the script file of the application
     *
     * @param listInput
     * @param listOutput
     * @param wrapperScriptPath
     * @param scriptFile
     * @param applicationName
     * @param applicationLocation
     * @param description
     * @throws N4uException
     */
    @Override
    public void generateScriptFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String environementFile, String description,String dockerImage,String commandLine) throws N4uException {
        ve = new Velocity();
        String applicationRealLocation;
        try {

            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            generateTime = getCurrentTimeStamp().toString().replaceAll(":", "_").replaceAll(" ", "");
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
            theDir.mkdirs();
            String dir = theDir.getAbsolutePath();
            if(scriptFile!=null){
            if (!scriptFile.isEmpty()) {
                scriptFile = DataManagerUtil.parseBaseDir(getSessionUser(), scriptFile);
            }
            }
            ve.wrapperScriptFile(templateFolder,listInput, listOutput, applicationName, scriptFile, applicationRealLocation, environementFile, dir, generateTime, mandatoryDir,dockerImage,commandLine);
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
    }

    /**
     * method to generate the Gwendia file of the application
     *
     * @param listInput
     * @param listOutput
     * @param wrapperScriptPath
     * @param scriptFile
     * @param applicationName
     * @param applicationLocation
     * @param description
     * @return
     * @throws N4uException
     */
    @Override
    public String generateGwendiaFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description,String vo) throws N4uException {
        String applicationRealLocation;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
            theDir.mkdirs();
            String dir = theDir.getAbsolutePath();
            return ve.gwendiaFile(templateFolder,listInput, listOutput, applicationName, description, applicationRealLocation, dir,new File(homeDir, applicationName), generateTime, mandatoryDir,vo);
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }

    }

    /**
     * method to generate the Gasw file of the application
     *
     * @param listInput
     * @param listOutput
     * @param wrapperScriptPath
     * @param scriptFile
     * @param applicationName
     * @param applicationLocation
     * @param description
     * @throws N4uException
     */
    @Override
    public void generateGaswFile(String templateFolder,HashMap<Integer, HashMap<String, String>> listInput, HashMap<Integer, HashMap<String, String>> listOutput, String wrapperScriptPath, String scriptFile, String applicationName, String applicationLocation, String description, String sandboxFile, String environementFile, String extensionFile) throws N4uException {
        String applicationRealLocation = null;
        try {
            applicationRealLocation = DataManagerUtil.parseBaseDir(getSessionUser(), applicationLocation);
            //create folder to genrate file
            final File homeDir = new File(Server.getInstance().getN4uApplicationFilesRepository());
            File theDir = new File(homeDir, applicationName + "/" + getSessionUser().getFolder() + "/" + generateTime);
            theDir.mkdirs();

            // if the directory does not exist, create it
            String dir = theDir.getAbsolutePath();
            String executableSandbox =null;
            String envF = null;
            String sandboxF =null;
            List<String> extensionFValue = null;
            String extensionF = "";
            if(extensionFile!=null){
                extensionFValue = new ArrayList<String>();
            if (!extensionFile.isEmpty()) {
                try {
                    extensionF = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), extensionFile), Server.getInstance().getN4uApplicationFilesRepository());
                } catch (GRIDAClientException ex) {
                    logger.error(ex);
                    throw new N4uException(ex);
                }

                try {

                    Scanner scanner = new Scanner(new FileInputStream(extensionF));
                    List<String> requirements = new ArrayList<String>();
                    String ligne = null;
                    while (scanner.hasNextLine()) {
                        ligne = scanner.nextLine();
                        if (ligne.startsWith("requirements=")) {
                            requirements.add(ligne.substring(14));
                        }
                    }
                    scanner.close();

                    for (String requirement : requirements) {

                        extensionFValue.add(StringEscapeUtils.escapeXml(requirement.substring(0, requirement.length() - 1)));

                    }

                } catch (FileNotFoundException exc) {
                    logger.error(exc);
                    throw new N4uException("Can't find the extension file :" + extensionF);

                }
            }
            }
            if(scriptFile!=null){
            if (!scriptFile.isEmpty()) {
                executableSandbox = DataManagerUtil.parseBaseDir(getSessionUser(), scriptFile);

            }
            }
            if(environementFile!=null){
            if (!environementFile.isEmpty()) {
                envF = DataManagerUtil.parseBaseDir(getSessionUser(), environementFile);

            }
            }
            if(sandboxFile!=null){
            if (!sandboxFile.isEmpty()) {
                sandboxF = DataManagerUtil.parseBaseDir(getSessionUser(), sandboxFile);
            }
            }
            ve.gaswFile(templateFolder,listInput, listOutput, applicationName, wrapperScriptPath, applicationRealLocation, dir, generateTime, sandboxF, envF, extensionFValue, executableSandbox);
        } catch (CoreException e) {
            logger.error(e);
            throw new N4uException(e);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (VelocityException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
    }

    private java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    /**
     *
     * @param val
     * @return
     * @throws N4uException
     */
    private String parseTypeSupported(String val) throws N4uException {
        if (val.equalsIgnoreCase(EnumTypes.Text.toString())||val.equalsIgnoreCase(EnumTypes.String.toString()) || val.equalsIgnoreCase(EnumTypes.Integer.toString()) || val.equalsIgnoreCase(EnumTypes.Float.toString()) || val.equalsIgnoreCase(EnumTypes.Combo.toString()) || val.equalsIgnoreCase(EnumTypes.CheckBox.toString()) || val.equalsIgnoreCase(EnumTypes.Radio.toString())) {
            return EnumInputTypes.Parameter.toString();
        } else if (val.equalsIgnoreCase(EnumTypes.File.toString())) {
            return EnumInputTypes.File.toString();
        } else {
            throw new N4uException("type " + val + " " + "not supported by the application ");
        }
    }

    @Override
    public List<List<HashMap<String,String>>> parseJsonFile(String jsonFile) throws N4uException {
       String localJsonFilePath = null;
        try {
            localJsonFilePath = CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(getSessionUser(), jsonFile), Server.getInstance().getN4uApplicationFilesRepository());
        } catch (CoreException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new N4uException(ex);
        }
 
           JSONParser parser = new JSONParser();
           List<List<HashMap<String,String>>> allValues=null;
 
        try {
            allValues=new ArrayList<List<HashMap<String,String>>>();
            Object obj = parser.parse(new FileReader(localJsonFilePath));
            List<HashMap<String,String>> val=new ArrayList<HashMap<String,String>>();
            HashMap<String,String> values=new HashMap<String,String>();
            JSONObject jsonObject = (JSONObject) obj;
            String name = (String) jsonObject.get("name");
            values.put("name",name);
            String description = (String) jsonObject.get("description");
            values.put("description",description);
            String commandLine = (String) jsonObject.get("command-line");
            values.put("command-line",commandLine);
            String dockerImage = (String) jsonObject.get("docker-image");
            values.put("docker-image",dockerImage);
            String dockerIndex = (String) jsonObject.get("docker-index");
            values.put("docker-index",dockerIndex);
            String shemaVersion = (String) jsonObject.get("schema-version");
            values.put("schema-version",shemaVersion);
            val.add(values);
            allValues.add(val);
            JSONArray inputList = (JSONArray) jsonObject.get("inputs");
            
            List<HashMap<String,String>> inputs=new ArrayList<HashMap<String,String>>();
            Iterator i = inputList.iterator();
            while (i.hasNext()) {
                     HashMap<String,String> inputt=new HashMap<String,String>();
	             JSONObject input = (JSONObject) i.next();
	             String inputName=input.get("name").toString();
                     inputt.put("name",inputName);
                     String inputType=input.get("type").toString();
                     inputt.put("type",parseTypeSupported(inputType));
                     String inputDescription=input.get("description").toString();
                     inputt.put("description",inputDescription);
                     String inputCommandLine=input.get("command-line-key").toString();
                     inputt.put("command-line-key",inputCommandLine);
                     String inputCardinality=input.get("cardinality").toString();
                     inputt.put("cardinality",inputCardinality);
                     inputs.add(inputt);
                    
	            }
            
            allValues.add(inputs);          
            JSONArray outputList = (JSONArray) jsonObject.get("outputs");
             List<HashMap<String,String>> outputs=new ArrayList<HashMap<String,String>>();
             Iterator j = outputList.iterator();
              while (j.hasNext()) {
	             JSONObject input = (JSONObject) j.next();
                      HashMap<String,String> outputt=new HashMap<String,String>();
	             String outputName=input.get("name").toString();
                     outputt.put("name",outputName);
                     String outputType=input.get("type").toString();
                     outputt.put("type",parseTypeSupported(outputType));
                     String outputDescription=input.get("description").toString();
                     outputt.put("description",outputDescription);
                     String outputCommandLine=input.get("command-line-key").toString();
                     outputt.put("command-line-key",outputCommandLine);
                     String outputCardinality=input.get("cardinality").toString();
                     outputt.put("cardinality",outputCardinality);
                     String outputTemplate=input.get("value-template").toString();
                     outputt.put("value-template",outputTemplate);
                     outputs.add(outputt);
	            }
              
              allValues.add(outputs);
             } catch (Exception e) {
            e.printStackTrace();
        }

          return allValues;
    

}
}
