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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.common.MessageWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.ParameterRecord;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryRecord;
import fr.insalyon.creatis.vip.query.client.bean.Value;
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
   private RichTextEditor executionDescription;
   DynamicForm execution;
   private List<TextItem> arrList;
  
   int i=0;
   private Long id;

   public parameterTab(Long queryVersionID) {
       
      this.queryVersionID=queryVersionID;
      
      mainLayout = new VLayout();
      Label title = new Label("<strong>Execution Name</strong>");
      title.setHeight(20);
     
      executionName=new TextItem();
      executionName.setWidth(800);
      executionName.setShowTitle(false);
      executionName.setTitleOrientation(TitleOrientation.TOP);
      
      execution=new DynamicForm();
      execution.setFields(executionName);
      
      executionDescription = new RichTextEditor();
      executionDescription.setHeight(100);
      executionDescription.setWidth100();
      executionDescription.setOverflow(Overflow.HIDDEN);
      executionDescription.setShowEdges(true);
      executionDescription.setControlGroups("styleControls", "editControls",
                "colorControls");
      executionDescription.setTitle("Query Execution Description");
        
        
      Label executiondes = new Label("<strong>Execution Description</strong>");
      executiondes.setHeight(20);
      
      
      
      mainLayout.setPadding(5);
      mainLayout.addMember(title,0);
      mainLayout.addMember(execution,1);
      mainLayout.addMember(executiondes,2);
      mainLayout.addMember(executionDescription,3);
      
      Label parameterLab = new Label("<strong>Parameters</strong>");
      parameterLab.setHeight(20);
      
      mainLayout.addMember(parameterLab,4);
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
               i++;
            dataList=  new ArrayList<ParameterRecord>();
            dynamicForm= new DynamicForm();
            printViewer = new DetailViewer();
            printViewer.setWidth("600");  
            printViewer.setMargin(15);  
            DetailViewerField name=new DetailViewerField("name", "Name");
            DetailViewerField type=new DetailViewerField("type", "type");
            DetailViewerField description=new DetailViewerField("description", "description");
            DetailViewerField example=new DetailViewerField("example", "example");
            printViewer.setFields(name,type,description,example);
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

            mainLayout.addMember(hlayout,5);
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
                    saveQueryExecution(new QueryExecution(queryVersionID,"admin@vip.creatis.insa-lyon.fr","statusn",executionName.getValueAsString(),executionDescription.getValue(),"url"));
                     
                     
                     for(TextItem t : arrList){
                           
                     saveValue(new Value(t.getValueAsString(),Long.parseLong(t.getName()),id)) ;
                     t.setValue("");
                        }
                        
    }
});
        
      
        mainLayout.addMember(launchButton,6);
                }
    
    
    
    
    
     private void saveValue(Value value)  {
         final AsyncCallback <Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to save Value " + caught.getMessage());
            }
 
            @Override
            public void onSuccess(Long result) {
                
                
               
               
        }
        };
        QueryService.Util.getInstance().addValue(value, callback);
 
    }
     
     public void saveQueryExecution(QueryExecution queryExecution)  {
         
          final AsyncCallback <Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                
                Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
            }
 
            @Override
            public void onSuccess(Long result) {
                
                 id=new Long(result);
                 
              
        }
        };
        QueryService.Util.getInstance().addQueryExecution(queryExecution, callback);
        
        
    }
    
 
}
     

     

    
    
    
    
    
    
    
   