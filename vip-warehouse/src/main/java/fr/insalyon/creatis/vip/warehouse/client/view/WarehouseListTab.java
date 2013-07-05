/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.warehouse.client.view;


import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.TreeModelType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.application.client.view.launch.ListHLayout;



import fr.insalyon.creatis.vip.warehouse.client.rpc.WarehouseService;
import fr.insalyon.creatis.vip.warehouse.client.rpc.WarehouseServiceAsync;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederic Cervenansky
 */
public class WarehouseListTab extends Tab {

    private TreeGrid warehouseTree;
    private ListGrid dbGrid;
    //private ListGrid progrid;
    private SelectItem DBCombo = null;
    private ArrayList<String> sites;
    private String mnickname;
    private String murl;
    private String mwarehouse;
    private String mtype;
       private Logger logger = null;

    private String addProject(String jslist)
    {
        String result;
      logger = Logger.getLogger("warehouse");
        logger.log(Level.SEVERE, "warehouse access");
        JSONValue value = JSONParser.parseStrict(jslist);
        JSONObject jobj = value.isObject();
        //HashMap<String, String> maps = new HashMap<String, String>();
        result = "<b>" + jobj.get("name") + "</b> <br>";
        if (jobj.get("ID") != null)
        {
            result += "<b> projet ID: " + jobj.get("ID") + "</b>";
        }
        if (jobj.get("pi_firstname") != null)
        {
            result += "<b> PI: " + jobj.get("pi_firstname") + " " + jobj.get("pi_lastname")+ "</b> <br>";
        }
        if (jobj.get("description") != null)
        {
            result +=  jobj.get("description") +"<br>";
        }
        return result;
    }
    
    private String addSubjects(String jslist)
    {
        String result;
    
        JSONValue value = JSONParser.parseStrict(jslist);
        JSONObject jobj = value.isObject();
        //HashMap<String, String> maps = new HashMap<String, String>();
        result = "<b>" + jobj.get("name") + "</b> <br>";
        if (jobj.get("label") != null)
        {
            result += "<b> projet ID: " + jobj.get("ID") + "</b>";
        }
        if (jobj.get("pi_firstname") != null)
        {
            result += "<b> PI: " + jobj.get("pi_firstname") + " " + jobj.get("pi_lastname")+ "</b> <br>";
        }
        if (jobj.get("description") != null)
        {
            result +=  jobj.get("description") +"<br>";
        }
        return result;
    }
    
    
    private String getProjectID(String jslist)
    {
        String res = "";
        JSONValue value = JSONParser.parseStrict(jslist);
        JSONObject jobj = value.isObject();
         if (jobj.get("ID") != null)
        {
            res = jobj.get("ID").isObject().toString();
        }
         return res;
    }
    
    private void readJSonProjects(String jslist)
    {
        JSONValue value = JSONParser.parseStrict(jslist);
        JSONObject jobj = value.isObject();
        JSONValue jsonValue = jobj.get("ResultSet").isObject().get("Result");
        int id = 0;
         for (String key: jsonValue.isObject().keySet()) { 
             Record rc = new Record();
             rc.setAttribute("subject", id);
             JSONValue subjs = jsonValue.isObject().get(key).isObject();
             rc.setAttribute("label",subjs.isObject().get("label").toString());
             rc.setAttribute("date",subjs.isObject().get("insert_date").toString());
             rc.setAttribute("user",subjs.isObject().get("insert_user").toString());
             rc.setAttribute("subjectid", subjs.isObject().get("ID").toString());
             dbGrid.addData(rc); 
             id++;
         }
    }
    
    private void readJSON(String jslist, TreeItem parent) {
        JSONValue value = JSONParser.parseStrict(jslist);
        JSONObject jobj = value.isObject();
        for (String key : jobj.keySet()) {
            JSONValue jsonValue = jobj.get(key);
            TreeItem item = new TreeItem("<b>" + key + "</b> ");
            parent.addItem(item);

            if (jsonValue != null) {
                if (jsonValue.isObject() != null) {
                    readJSON(jsonValue.isObject().toString(), item);
                } else if (jsonValue.isArray() != null) {
                } else {
                    if ("oldValue".equals(key)) {
                        item.setHTML("<b>before:</b> " + jsonValue.toString());
                    } else {
                        item.setHTML("<b>after:</b> " + jsonValue.toString());
                    }
                }
            } else {
                item.setText("<b>" + item.getText() + ":</b> null");
            }
        }

    }
    
    
    private void extractSubjects(String project)
    {
        JSONValue value = JSONParser.parseStrict(project);
        JSONObject jobj = value.isObject();
        JSONArray subArray = jobj.get("ResultSet").isObject().get("Result").isArray();
        for(int i = 0; i < subArray.size(); i++)
        {
             Record rc = new Record();
             rc.setAttribute("ID", i);
             rc.setAttribute("Projects",addSubjects(subArray.get(i).isObject().toString()) );
             //rc.setAttribute("projectid",getProjectID(jsonValue.isObject().get(key).isObject().toString()));
             dbGrid.addData(rc); 
        }
    }
    
    private void configureDBGrid()
    {
        dbGrid = new ListGrid();
          ListGridField idField = new ListGridField("id", "ID", 120);  
        ListGridField nameField = new ListGridField("project", "Project");  
        ListGridField projectidField = new ListGridField("projectid", "projectid", 50); 
        projectidField.setHidden(true);
        dbGrid.setFields(idField, nameField, projectidField);
        
        dbGrid.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String projectid = event.getRecord().getAttribute("projectid");
                showProject(projectid);
            }
        });
    }
    
   private void configureTree()
    {
        /* It's working
         warehouseTree = new TreeGrid();
        warehouseTree.setWidth100();
        warehouseTree.setHeight100();
        warehouseTree.setShowOpenIcons(false);
        warehouseTree.setShowDropIcons(false);
       
       
        warehouseTree.setCanHover(true);
        warehouseTree.setClosedIconSuffix("");
        warehouseTree.setLoadDataOnDemand(true);
        warehouseTree.setFields(new TreeGridField("name", "test"));

      Tree tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setNameProperty("name");

        TreeNode root = new TreeNode("Root");
        tree.setRoot(root);

        TreeNode treeNode = new TreeNode( "Simulation");
        tree.add(treeNode, root);
        
        warehouseTree.setData(tree);
        */
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        warehouseTree = new TreeGrid();
      //  TreeGridField idField = new TreeGridField("id", "ID");  
        TreeGridField nameField = new TreeGridField("name");  
        //TreeGridField descriptionField = new TreeGridField("description", "description"); 
        //idField.setHidden(true);
        //descriptionField.setHidden(true);
       // warehouseTree.setFields(idField, nameField, descriptionField);
        warehouseTree.setWidth100();
        warehouseTree.setHeight100();
        warehouseTree.setShowOpenIcons(false);
        warehouseTree.setShowDropIcons(false);
       
       
        warehouseTree.setCanHover(true);
        warehouseTree.setClosedIconSuffix("");
        warehouseTree.setLoadDataOnDemand(true);
          warehouseTree.setFields( nameField);
           // MedImgWarehouseTreeNode[] child = new MedImgWarehouseTreeNode[3];
           
           
        warehouseTree.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String description = event.getRecord().getAttribute("description");
                showProject(description);
            }
        });
    }
    
    
    
    
    
    private void showProject(String id)
    {
         WarehouseServiceAsync ms = WarehouseService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot find the project");
            }

            @Override
            public void onSuccess(String result) {
               configureSubjectsGrid(result); 
            }
        };
         //  ms.getProject(id, callback);
    }
    

        private void configureSubjectsGrid(String project)
    {
        dbGrid = new ListGrid();
          ListGridField subField = new ListGridField("subject", "Subject", 20); 
          ListGridField labelField = new ListGridField("label", "Label", 120);  
          ListGridField dateField = new ListGridField("date", "Date of insertion", 120);  
          ListGridField userField = new ListGridField("user", "Owner", 120); 
          ListGridField subjectidField = new ListGridField("subjectid", "subjectid", 50); 
          subjectidField.setHidden(true);
        dbGrid.setFields(subField, labelField, dateField, userField, subjectidField);
        
        dbGrid.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String projectid = event.getRecord().getAttribute("projectid");
                showProject(projectid);
            }
        });
    }

   public void connect(String pwd)
   {
       WarehouseServiceAsync ms = WarehouseService.Util.getInstance();
       final AsyncCallback< Void > callback = new AsyncCallback< Void >() {
       @Override
       public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot reach the selected warehouse.");
            }

       @Override
            public void onSuccess(Void result) {
                 WarehouseServiceAsync ms2 = WarehouseService.Util.getInstance();
                final AsyncCallback< HashMap<String, String> > callback = new AsyncCallback< HashMap<String, String> >() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Cannot find the database");
                    }


                    @Override
                    // id, name
                    public void onSuccess(HashMap<String, String> results) {
                        Layout.getInstance().setNoticeMessage("successful connection to the warehouse.");
                        //create tree with model timepoints
                      
                  //  wareTree.setModelType(TreeModelType.CHILDREN);
                    //wareTree.setChildrenProperty("Children");
                 
                    
                                           TreeNode root = new TreeNode("root");
                                   //("1","root","root");
       Tree wareTree = new Tree();
       
                            wareTree.setRoot(root);
            TreeNode[] child = new TreeNode[results.size()];
            int i=0;
                         for(String key : results.keySet())
                        {
                            child[i] = new TreeNode(results.get(key));
                              //child[i] = new MedImgWarehouseTreeNode("1","test","test");
                              
                              wareTree.add(child[i], root);
                              i++;
                      
                        }
             
       
              
                            warehouseTree.setData(wareTree);
                        
                        
                        
 logger.log(Level.SEVERE, " tree creation");
//                    MedImgWarehouseTreeNode[] child = new MedImgWarehouseTreeNode[results.size()];
//                    int i =0;
// logger.log(Level.SEVERE, " treechild creation");
//                        for(String key : results.keySet())
//                        {
//                              child[i] = new MedImgWarehouseTreeNode(key,results.get(key),results.get(key));
//                              i++;
//                        }
//                           logger.log(Level.SEVERE, " tp size :" + child.length);
//                           MedImgWarehouseTreeNode root = new MedImgWarehouseTreeNode("1","root","root",child);
//                           
//                            wareTree.setRoot(root);
//                            warehouseTree.setData(wareTree);
//                            warehouseTree.redraw();
                              logger.log(Level.SEVERE, " root creation  " + root.toString());
                     }

                };
                ms2.getProjects(mnickname,murl, mtype,callback);
            }
            
        };
         
        ms.getConnection(mnickname, pwd, murl, mtype, callback);
   }
        
        
   private void getDB()
   {
      
        WarehouseServiceAsync ms = WarehouseService.Util.getInstance();
        final AsyncCallback< List<String> > callback = new AsyncCallback< List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot find the database");
            }

            // a result is composed of nickanme#url#site
            @Override
            public void onSuccess(List<String> result) {
               sites = (ArrayList)result;
                             LinkedHashMap<String, String> lutMap = new LinkedHashMap<String, String>();
        for(String key : result) {
            String splits[] = key.split("#");
            //Map : nickanme#url#type, name
            lutMap.put(splits[0]+"#"+splits[1]+"#"+splits[3], splits[2]);
        }
                    DBCombo.setValueMap(lutMap);
                }
            
        };
        ms.getSites(callback);
   }
    
   private void getDB2()
   {
      
           WarehouseServiceAsync ms = WarehouseService.Util.getInstance();
        final AsyncCallback< String > callback = new AsyncCallback< String >() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot find the project");
            }

            @Override
            public void onSuccess(String result) {
              
            }
        };
       
       ms.test(callback);
   } 
   
   
    public WarehouseListTab() {
       logger = Logger.getLogger("warehouse");
        logger.log(Level.SEVERE, "warehouse access");
        this.setTitle("Medical Images Database");
       this.setCanClose(true);

        // Layout for  selection to database
        final DynamicForm manageForm = new DynamicForm();  
        manageForm.setIsGroup(true);  
        manageForm.setGroupTitle("Manage connections");  
        manageForm.setAutoFocus(false);  
        manageForm.setID("manageconnections");  
        manageForm.setNumCols(2);  
        manageForm.setHeight100();
        //manageForm.setWidth100();
        ButtonItem submitButton = new ButtonItem("OK");
        final pwdDialog pDial = new pwdDialog(this);
        submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick( com.smartgwt.client.widgets.form.fields.events.ClickEvent event) 
            {
              
                String[] values = DBCombo.getValue().toString().split("#");
                mnickname = values[0];
                murl = values[1];
                mwarehouse = DBCombo.getDisplayValue();
                mtype = values[2];
                pDial.configure(mnickname,murl, mwarehouse, mtype);
                pDial.show();
            }
        });
        DBCombo = new SelectItem();
        DBCombo.setTitle("Database");
        DBCombo.setMultiple(false);
        DBCombo.setPrompt("select the database where to download the data.");
        DBCombo.enable(); 
        getDB();
        manageForm.setFields(DBCombo,submitButton);
        
        HLayout hconlay = new HLayout();
        //hconlay.setWidth100();
        hconlay.setHeight(100);
        hconlay.setMembersMargin(10);
        hconlay.addMember(manageForm);
        
        //Layout for browse database
        VLayout dblayout = new VLayout();
        
        final DynamicForm exploreForm = new DynamicForm();
        exploreForm.setIsGroup(true);  
        exploreForm.setGroupTitle("Explore DB");  
        exploreForm.setAutoFocus(false);  
        exploreForm.setID("exploredb");  
        exploreForm.setNumCols(2); 
        //database grid
        //configureDBGrid();
        configureTree();
        CanvasItem item = new CanvasItem();
        item.setName("");
        item.setTitle("");
        item.setCanvas(warehouseTree);
        exploreForm.setFields(item);
        // result path
        
        ListHLayout lhlayout = new ListHLayout(dblayout,false);
        //download button
        final ButtonItem downBut = new ButtonItem("download");
        downBut.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick( com.smartgwt.client.widgets.form.fields.events.ClickEvent event) 
            {}
        });
        final DynamicForm downForm = new DynamicForm();
        downForm.setFields(downBut);
        exploreForm.setIsGroup(true); 
        dblayout.addMember(warehouseTree);
//        dblayout.addMember(exploreForm);
//        dblayout.addMember(lhlayout);
        dblayout.addMember(downForm);
        hconlay.addMember(dblayout);

        VLayout vlay = new VLayout();
       vlay.addMember(hconlay);
        
        this.setPane(vlay);
    }
}
