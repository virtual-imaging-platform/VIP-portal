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
           ObjectFactory objFact=ObjectFactory.getInstance();
           
           String [][]entry=new String[3][3];
           entry[0][0]="[tumor-HeartLungsThorax00.vtp]";
           entry[0][1]="[internal_thorax.vtp]";
           entry[0][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
                         
           entry[1][0]="[spine.vtp]";
           entry[1][1]="[aorta.vtp]";
           entry[1][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
           
           entry[2][0]="[myocardium.vtp]";
           entry[2][1]="[external_thorax.vtp]";
           entry[2][2]="[HeartLungsThorax00.mhd, blabla.zraw]";
           
           String type[]= new String[]{"external_agent","anatomical","geometrical"};
           objFact.addPath(path,entry,type);
           Data3D [][]object=objFact.GetObjectTab();
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
    public List<String> linkerSimulator (String modality)
    {
       
       
        
        List<String> listString=new ArrayList<String>();
		try{
                        boolean test=false;
			InputStream ips=this.getClass().getResourceAsStream("linker.txt"); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
                        String ligne;
			while ((ligne=br.readLine())!=null){
                              int j=0;
                                for (int i= 0 ; i < ligne.length() ;i++)
                                {
                                  if(ligne.charAt(i)==',')j++;
                                }                               
			      StringTokenizer st1dTokenize = new StringTokenizer(ligne);
                              if(st1dTokenize.nextToken(",").equals(modality))
                              {                                 
                                  for(int i=0;i<j;i++)
                                  {
                                      listString.add(st1dTokenize.nextToken(","));
                                      test=true;
                                  }                                 
                              }
                        }
			br.close();
                        if(test)return listString;
                        else return null;
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}		
        return null;
    }

    

   

   

}
