/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.view.ParameterRecord;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class parameterTab extends VLayout {
   private Long queryVersionID;
   private VLayout mainLayout;
   private IButton launchButton;
   private DetailViewer printViewer ;
   private TextItem executionName;
 
   DynamicForm execution;
   private List<TextItem> arrList;
   private Long queryExecutionID;
  
   
  

   public parameterTab(Long queryVersionID) {
       
      this.queryVersionID=queryVersionID;
      
      mainLayout = new VLayout();
      Label title = new Label("<strong>Execution Name</strong>");
      title.setHeight(20);
     
      executionName=new TextItem();
      
      executionName.setShowTitle(false);
      executionName.setTitleOrientation(TitleOrientation.TOP);
      
      execution=new DynamicForm();
      execution.setFields(executionName);
      
   
        
        
     
      
      
      mainLayout.setPadding(5);
      mainLayout.addMember(title,0);
      mainLayout.addMember(execution,1);
     
      
      Label parameterLab = new Label("<strong>Parameters</strong>");
      parameterLab.setHeight(20);
      
      mainLayout.addMember(parameterLab,2);
      loadParameter();
      configure();
      this.addMember(mainLayout);    
    }

   
   
    private void loadParameter() {
         
      final AsyncCallback<List<Parameter>> callback;
      callback = new AsyncCallback<List<Parameter>>() {
         @Override
          public void onFailure(Throwable caught) {

            Layout.getInstance().setWarningMessage("Unable to Load parameter" + caught.getMessage());
          }

         @Override
          public void onSuccess(List<Parameter>result) {
              

           List<ParameterRecord> dataList ;
          
          arrList = new ArrayList<TextItem>();
           DynamicForm dynamicForm ;
           
           for (Parameter q : result) {
               TextItem value;  
               
            dataList=  new ArrayList<ParameterRecord>();
            dynamicForm= new DynamicForm();
            printViewer = new DetailViewer();
            printViewer.setWidth("600");  
            printViewer.setMargin(15);  
            DetailViewerField name=new DetailViewerField("name(type)", "Name");
            //DetailViewerField type=new DetailViewerField("type", "type");
            DetailViewerField description=new DetailViewerField("description(example)", "description");
           // DetailViewerField example=new DetailViewerField("example", "example");
            printViewer.setFields(name,description);
            dataList.add(new ParameterRecord(q.getName(),q.getType(),q.getDescription(),q.getExample()));
            
            printViewer.setData(dataList.toArray(new ParameterRecord[]{}));
            HLayout hlayout=new  HLayout(15);
 
            hlayout.setBorder("1px solid #C0C0C0");
            hlayout.setBackgroundColor("#F5F5F5");
            hlayout.setPadding(10);
            hlayout.setMembersMargin(10);
            

            value=new TextItem();
            value.setWidth(600);
            value.setTitle(q.getName());
           
            value.setTitleOrientation(TitleOrientation.TOP);
            value.setName(String.valueOf(q.getParameterID()));
            arrList.add(value);
            dynamicForm.setFields(value);

            hlayout.addMember(printViewer);
            hlayout.addMember(dynamicForm);

            mainLayout.addMember(hlayout,3);
            mainLayout.setMembersMargin(10);

            }


         }


    };
    
      QueryService.Util.getInstance().getParameter(queryVersionID,callback);
    

                }

    private void configure() {
       
       launchButton=new IButton();
    
       launchButton= WidgetUtil.getIButton("Execute", QueryConstants.ICON_LAUNCH,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                  
                     final AsyncCallback <Long> callback = new AsyncCallback<Long>() {
                     @Override
                     public void onFailure(Throwable caught) {
                
                     Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                     }
                    
 
                    @Override
                    public void onSuccess(Long result) {
                   update("","waiting",result);
                  for(TextItem t : arrList){
                           
                     saveValue(new Value(t.getValueAsString(),Long.parseLong(t.getName()),result)) ;
                     
                     t.setValue("");
                        }
                   queryExecutionID=result;
                  
                 
                
                
              
        }
        };
        QueryService.Util.getInstance().addQueryExecution(new QueryExecution(queryVersionID,"Waiting",executionName.getValueAsString()," "), callback);
                     
                    
                        
    }
});
        
      
        mainLayout.addMember(launchButton,4);
                }
    
    
    
    
    
     private void saveValue(Value value)  {
         final AsyncCallback <Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to save Value " + caught.getMessage());
            }
 
            @Override
            public void onSuccess(Long result) {
            getBody(queryVersionID,queryExecutionID);
                
               
               
        }
        };
        QueryService.Util.getInstance().addValue(value, callback);
 
    }
     
    
    
 private  void getBody(Long queryVersionID,Long executonID )  {
         final AsyncCallback <String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to get Body" + caught.getMessage());
            }
 
            @Override
            public void onSuccess(String result) {
                
              getUrlResult(result,"json");
              
               
        }
        };
       
  QueryService.Util.getInstance().getBody(queryVersionID, executonID, callback);
  
 
 }
 
 
 private  void update(String urlResult,String status,Long executonID )  {
         final AsyncCallback <Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to update" + caught.getMessage());
            }
 
            @Override
            public void onSuccess(Void result) {     
        }
        };
       
  QueryService.Util.getInstance().updateQueryExecution(urlResult, status, executonID, callback); 
  
 
 }
 
 
 
 
 private void getUrlResult(String query, String format)
 {
                 
           final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());
                update("","failed",queryExecutionID);
            }
 
            @Override
            public void onSuccess(String result) {
                
                 
                update(result,"completed",queryExecutionID);
               // com.google.gwt.user.client.Window.open(result,"_self","");    
               
        }
        };
      
         EndPointSparqlService.Util.getInstance().getUrlResult(query, format, callback);
                                
                }

 
 
}
     

     

    
    
    
    
    
    
    
   