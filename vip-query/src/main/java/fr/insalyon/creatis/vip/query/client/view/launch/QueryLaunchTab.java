/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.launch;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
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
    protected String queryVersionID;
    protected String titleName ;
   
    protected ModalWindow modal;
   // protected LaunchFormLayout launchFormLayout;
    //protected InputsLayout inputsLayout;
   
    protected IButton saveInputsButton;
    protected IButton saveAsExampleButton;
    private ParameterTab param;
   

    public QueryLaunchTab(String queryName,String queryVersionID,String queryVersion) {
       this.queryName = queryName;
       this.queryVersionID = queryVersionID;
       param=new ParameterTab(Long.parseLong(queryVersionID));
       this.setTitle(Canvas.imgHTML(QueryConstants.ICON_EXECUTE_VERSION) + " "
                + queryName + " "+queryVersion);
 
       this.setCanClose(true);
       this.setAttribute("paneMargin", 0);
       
       layout = new HLayout();
       layout.setWidth100();
       layout.setHeight100();
       layout.setMargin(5);
   
       layout.addMember(param);
       
        
     
       this.setPane(layout);
      
    
           
        
        
    
    }

    @Override
    public void setCanClose(boolean canClose) {
        super.setCanClose(canClose); //To change body of generated methods, choose Tools | Templates.
    }
    

    
       

        
}