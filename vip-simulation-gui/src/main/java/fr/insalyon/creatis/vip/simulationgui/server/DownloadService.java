
package fr.insalyon.creatis.vip.simulationgui.server;


import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



/**
 *
 * @author moulin
 */
public class DownloadService {
    
  
      VletAgentClient client=null;
      String zipPath="null";
      String finalPath="null";
      String rdfPath="null";
      String returnPath="null";
      Data3D object[][];
      public DownloadService (String url, String proxyFileName, String user) 
       {   
           
         zipPath="null";
         finalPath="null";
         File fb = new File(System.getenv("HOME")+"/.platform/models"); 
         fb.mkdirs();
        try {
            download(url,proxyFileName,user);
        } catch (URISyntaxException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
        }
        rebuildObject(finalPath,rdfPath);
       }
       public String getPath()
       {
         return returnPath;
       }
        public Data3D[][] getObject()
        {
          return object;
        }
       
       private void download(String url, String proxyFileName, String user) throws URISyntaxException
       {
             try {
            zipPath=vletAgentConnexion(url,proxyFileName,user);
            
            } catch (DataManagerException ex) {
                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            zipPath="null";
        } catch (VletAgentClientException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            zipPath="null";
        }
         
       try {
            finalPath=unzip(zipPath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            finalPath="null2";
        } catch (IOException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            finalPath="null2";
        }
       
       }
       
       
       
       
        private String vletAgentConnexion(String url, String proxyFileName, String user)throws VletAgentClientException, URISyntaxException, DataManagerException{
                   
                   URI URL = new URI(url);
                   String path="null2";
                   client = new VletAgentClient(
                   ServerConfiguration.getInstance().getVletagentHost(),
                   ServerConfiguration.getInstance().getVletagentPort(),proxyFileName);//  ProxyUtil.readAsString(Context.getInstance().getUserDN()) Context.getInstance().getProxyFileName()
                   path= client.getRemoteFile(DataManagerUtil.parseBaseDir(user, URL.getPath()),
                   System.getenv("HOME") + "/.platform/models");
                   return path;
       }
        
        
        
        
        
        
       private String unzip (String path) throws FileNotFoundException, IOException, URISyntaxException {

        String parent= new File(path).getParent();
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new FileInputStream(path));
        zipentry = zipinputstream.getNextEntry();
         FileOutputStream fileoutputstream;
         
         
         
         
           String entryName = zipentry.getName();
           File newFile = new File(entryName);
           String directory = newFile.getParent();
           if(!entryName.endsWith("/"))
           {
               parent=parent+"/"+entryName.substring(0,(entryName.length()-4));           
               new File(parent).mkdir();
           }
         
         
         
         
        byte[] buf = new byte[1024];
        while (zipentry != null) {
//                for each entry to be extracted
             entryName = zipentry.getName();
             newFile = new File(entryName);
             directory = newFile.getParent();
    
             if(directory!=null)
             {
                 new File (parent+"/"+directory+"/").mkdir();
               
             }
             if(!entryName.endsWith("/"))
             {
       
                int n;
            fileoutputstream = new FileOutputStream(parent+"/"+entryName);
           
  
            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                fileoutputstream.write(buf, 0, n);
            }
            fileoutputstream.close();
            zipinputstream.closeEntry();
           
           }
             if(entryName.endsWith(".rdf"))rdfPath=parent+"/"+entryName;
           zipentry = zipinputstream.getNextEntry();
      }

       return parent;
      }
      private void rebuildObject(String path, String rdfPath)
      {
           SimulationObjectModel inModel = SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile(rdfPath, true);
           
            int index=0;
            int nb_tp = inModel.getTimepoints().size();
          
    
            Timepoint tp = inModel.getTimepoints().get(0);
            Instant it = tp.getInstants().get(0);
            String[][] entry =new String [it.getObjectLayers().size()][1];
           String[] type = new String[it.getObjectLayers().size()];
            for (ObjectLayer ol : it.getObjectLayers()) {                                            
                         entry[index]=new String[ol.getLayerParts().size()];
                         type[index]=ol.getType().toString();
                         //index++;
                         int i=0;
                         for (ObjectLayerPart olp : ol.getLayerParts()) {
                         entry[index][i]=olp.getFileNames().toString();
                         i++;
                        }
                         index++;
            } 
            returnPath=" 0 ";
            for(int i=0;i<entry[0].length;i++)returnPath+=entry[0][i]+" "; 
            returnPath+=" 1 ";
            for(int i=0;i<entry[1].length;i++)returnPath+=entry[1][i]+" "; 
            returnPath+=" 2 ";
            for(int i=0;i<entry[2].length;i++)returnPath+=entry[2][i]+" "; 
            returnPath+="  objectListSize :     "+index+ " compare to "+it.getObjectLayers().size();
            System.out.println(returnPath);
            
            
            ObjectFactory objFact=ObjectFactory.getInstance();
            objFact.addPath(path,entry,type);
            object=objFact.GetObjectTab();
      }

       
}
