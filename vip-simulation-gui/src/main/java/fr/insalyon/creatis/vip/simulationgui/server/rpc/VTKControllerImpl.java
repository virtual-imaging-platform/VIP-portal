/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulationgui.server.rpc;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;



import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKController;
import fr.insalyon.creatis.vip.simulationgui.server.DownloadService;
import fr.insalyon.creatis.vip.simulationgui.server.ObjectFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author moulin
 */
public class VTKControllerImpl extends RemoteServiceServlet implements VTKController {
   
  
	public Data3D[][] downloadAndUnzipExample(String path){
         
           
           String [][]entry=new String[3][3];
           entry[0][0]="[right_lung.vtp]";
           entry[0][1]="[internal_thorax.vtp]";
           entry[0][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
                         
           entry[1][0]="[spine.vtp]";
           entry[1][1]="[aorta.vtp]";
           entry[1][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
           
           entry[2][0]="[myocardium.vtp]";
           entry[2][1]="[external_thorax.vtp]";
           entry[2][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
           
           String type[]= new String[]{"external_agent","anatomical","geometrical"};
           Data3D [][]object=ObjectFactory.buildMulti(path,entry,type);
            
          // Data3D object =ObjectFactory.build(path+"/myocardium.vtp");
          // String s="";
          // for(int i: object[0][1].getSupIndices())s+=" "+i+" ";
          // System.out.println(s);
            return object;

            // list tous les modeles 
    //             ModelServiceAsync ms = ModelService.Util.getInstance();
    //        ms.listAllModels(callback);
            //peupler  le combo
            // check
            //à la selection d'un élément du combo
            //ModelServiceAsync ms = ModelService.Util.getInstance();  
            //ms.rebuildObjectModelFromTripleStore(uri, callback);
            //donne un SimulationObjectModel
            // url = SimulationObjectModel.getStorageURL
            
            //get storage url
            
            // telechargement 
//               VletAgentClient client = new VletAgentClient(
//                    ServerConfiguration.getInstance().getVletagentHost(),
//                    ServerConfiguration.getInstance().getVletagentPort(),
//                    proxyFileName);
//
//            String workflowPath = client.getRemoteFile(url.getPath(),
//                    System.getenv("HOME") + "/.platform/models/" + 
//                    url.getPath().substring(0, url.getPath().lastIndexOf("/")));
            
            //dezipper
            // voir fr.insalyon.creatis.objectpreparationworkflowsbeanshells.CopyAndUnzip
            
            //afficher 
            //lister les fichiers du premier instant du premier timepoint
            //voir public ModelTreeGrid(SimulationObjectModel model) 
            //les afficher (bounding box pour les mhd + tous les vtp)

        }

    public Data3D[][] downloadAndUnzipModel(String url, String proxyFileName ,String user) {
       DownloadService service = null;        
       service = new DownloadService(url, proxyFileName, user);   
       return service.getObject();
    }


}
