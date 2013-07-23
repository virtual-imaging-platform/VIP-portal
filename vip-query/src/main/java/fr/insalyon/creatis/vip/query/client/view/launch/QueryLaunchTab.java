/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.launch;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
/**
 *
 * @author Boujelben
 */
public class QueryLaunchTab extends Tab {
    
    
    protected HLayout layout;
    protected String queryName;
    protected String queryVersion;
    protected String titleName ;
    protected Long queryVersionID  ;
    protected ModalWindow modal;
   // protected LaunchFormLayout launchFormLayout;
    //protected InputsLayout inputsLayout;
   
    protected IButton saveInputsButton;
    protected IButton saveAsExampleButton;
    private parameterTab param;
   

    public QueryLaunchTab(String queryName,String queryVersion) {
       this.queryName = queryName;
       this.queryVersion = queryVersion;
       param=new parameterTab(Long.parseLong(queryVersion));
       this.setTitle(Canvas.imgHTML(QueryConstants.ICON_EXECUTE_VERSION) + " "
                + queryName + " " + queryVersion);
 
       this.setCanClose(true);
       this.setAttribute("paneMargin", 0);
       
       layout = new HLayout();
       layout.setWidth100();
       layout.setHeight100();
       layout.setMargin(5);
   
       layout.addMember(param);
       
        
     
       this.setPane(layout);
        
        
    
    }
    

    
       

        
}