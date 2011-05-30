/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

    
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;

/**
 *
 * @author glatard
 */
public class ModelDisplay extends TreeGrid {

   private SimulationObjectModel model = null;

    public SimulationObjectModel getModel() {
        return model;
    }
   

    public ModelDisplay(SimulationObjectModel model){
        this.model = model;
        
        //init the tree grid
        setLoadDataOnDemand(false);  
        setWidth(700);  
        setHeight(800);  
        setCanEdit(true);  
        setShowOpenIcons(false);  
        setShowDropIcons(false); 
        setAutoFetchData(true);  
        setCanFreezeFields(true);  
        setCanReparentNodes(false);          
        setFields(new TreeGridField(model.getModelName())); 
        
        //test timepoints
        //TreeNode modelRoot = new ModelTreeNode("1","Root",new ModelTreeNode("2","TP1"),new ModelTreeNode("3","TP2", new ModelTreeNode("4","I1"), new ModelTreeNode("5","I2")));
        
  
        
        //model timepoints
        int id = 0;
        int ntp=0;
        ModelTreeNode[] timepoints = new ModelTreeNode[model.getTimepoints().size()];
        for (Timepoint tp : model.getTimepoints()) {
            ModelTreeNode[] instants = new ModelTreeNode[tp.getInstants().size()];
            int nit = 0;
            for(Instant it : tp.getInstants()){
                int nol = 0;
                ModelTreeNode[] objectLayers = new ModelTreeNode[it.getObjectLayers().size()];
                for(ObjectLayer ol : it.getObjectLayers()){
                    int nolp = 0;
                    ModelTreeNode[] objectLayerParts = new ModelTreeNode[ol.getLayerParts().size()];
                    for(ObjectLayerPart olp : ol.getLayerParts()){
                        String description = olp.getReferredObject().getObjectName().replace("_", " ") +" ("+olp.getFormat()+": ";
                        if(olp.getFormat() == ObjectLayerPart.Format.voxel)
                            description+="label "+olp.getLabel()+" in ";
                        description+=olp.getFileNames().toString().replace("[", "").replace("]", "") +")";
                        objectLayerParts[nolp++] = new ModelTreeNode(""+(2+id++),description,false);
                                            }
                    String description =ol.getType().toString().replace("_", " ");
                    if(!ol.getResolution().equals(ObjectLayer.Resolution.none))
                            description+=" ("+ol.getResolution().toString()+" resolution) ";
                    description+=" layer";
                    objectLayers[nol++]=new ModelTreeNode(""+(2+id++),description,false, objectLayerParts);
                }
                instants[nit++] = new ModelTreeNode(""+(2+id++),"Instant ("+it.getDuration()+")",true, objectLayers);
            }
            timepoints[ntp++] = new ModelTreeNode(""+(2+id++),"Timepoint ("+tp.getStartingDate()+")",true,instants);
        }
        TreeNode root = new ModelTreeNode("1","Root",true,timepoints);
        
        //create tree with model timepoints
        Tree modelTree = new Tree();
        modelTree.setModelType(TreeModelType.CHILDREN);
        modelTree.setNameProperty(model.getModelName());
        modelTree.setIdField("EntityId");
        modelTree.setChildrenProperty("Children");
        modelTree.setOpenProperty("isOpen");
        modelTree.setRoot(root);
        
           
        setData(modelTree);
    


    }
      public class ModelTreeNode extends TreeNode {

        public ModelTreeNode(String entityId, String entityName,boolean display) {
            this(entityId, entityName, display, new ModelTreeNode[]{});
        }

        public ModelTreeNode(String entityId, String entityName, boolean display, ModelTreeNode... children) {
            setAttribute(model.getModelName(), entityName);
            setAttribute("EntityId", entityId);
            setAttribute("Children", children);
            setAttribute("isOpen", display);
        }
    }
  
}
