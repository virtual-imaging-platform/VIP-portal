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
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Instant;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParameter;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParametersLayer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.PhysicalParametersLayer.PhysicalParameterType;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import fr.insalyon.creatis.vip.models.client.ModelConstants;

/**
 *
 * @author Tristan Glatard
 */
public class ModelTreeGrid extends TreeGrid {

    private SimulationObjectModel model = null;

    public SimulationObjectModel getModel() {
        return model;
    }

    public ModelTreeGrid(final SimulationObjectModel model) {
        super();
        this.model = model;
        //init the tree grid
        setLoadDataOnDemand(false);
        setWidth(700);
        setHeight(600);
        setCanEdit(true);
        setShowOpenIcons(false);
        setShowDropIcons(false);
        setAutoFetchData(true);
        setCanFreezeFields(true);
        setCanReparentNodes(false);
        
        if (model != null) {
            setFields(new TreeGridField(model.getModelName()));
            load();
        } 
        
          addLeafClickHandler(new LeafClickHandler() {

            public void onLeafClick(LeafClickEvent event) {
                SC.say("click");
            }
        });

        addLeafContextClickHandler(new LeafContextClickHandler() {

            public void onLeafContextClick(LeafContextClickEvent event) {
                new AnnotationsContextMenu(model,event.getLeaf().getName()).showContextMenu();
            }

       });

    }
    private void load(){
         //model timepoints
        int id = 0;
        int ntp = 0;

        ModelTreeNode[] timepoints = new ModelTreeNode[model.getTimepoints().size()];
        for (Timepoint tp : model.getTimepoints()) {
            ModelTreeNode[] instants = new ModelTreeNode[tp.getInstants().size()];
            int nit = 0;
            for (Instant it : tp.getInstants()) {
                int nol = 0;
                ModelTreeNode[] instantLayers = new ModelTreeNode[it.getObjectLayers().size() + 1];

                ModelTreeNode[] physicalLayers = new ModelTreeNode[it.getPhysicalParametersLayers().size()];



                int nopl = 0;

                for (PhysicalParametersLayer pl : it.getPhysicalParametersLayers()) {
                    String icon = ModelConstants.APP_IMG_MAGNETIC;
                     if (pl.getType() == PhysicalParameterType.T1 || pl.getType() == PhysicalParameterType.T2 || pl.getType() == PhysicalParameterType.T2s || pl.getType() == PhysicalParameterType.protonDensity || pl.getType() == PhysicalParameterType.susceptibility) {
                            icon = ModelConstants.APP_IMG_MAGNETIC;
                        }
                        if (pl.getType() == PhysicalParameterType.chemicalBlend) {
                            icon = ModelConstants.APP_IMG_CHEMICAL;
                        }
                        if (pl.getType() == PhysicalParameterType.radioactiviy) {
                            icon = ModelConstants.APP_IMG_RADIO;
                        }
                         if (pl.getType() == PhysicalParameterType.scatterers) {
                            icon = ModelConstants.APP_IMG_ECHO;
                        }
                    physicalLayers[nopl++] = new ModelTreeNode("" + (2 + id++), pl.toString(), false);
                    physicalLayers[nopl - 1].setIcon(icon);
                }
                if (nopl != 0) {
                    instantLayers[nol++] = new ModelTreeNode("" + (2 + id++), "Physical parameter maps", false, physicalLayers);
                    instantLayers[nol - 1].setIcon(ModelConstants.APP_IMG_MAP);
                }


                for (ObjectLayer ol : it.getObjectLayers()) {
                    int nolp = 0;
                    ModelTreeNode[] objectLayerParts = new ModelTreeNode[2];

                    ModelTreeNode[] objectLayerPhysParams = new ModelTreeNode[2];

                    int olppl = 0;
                    ModelTreeNode[] objectLayerPhysParamsLUT = new ModelTreeNode[ol.getPhysicalParameters().size()];
                    for (PhysicalParameter pp : ol.getPhysicalParameters()) {
                          String description = pp.toString();
                        String icon = ModelConstants.APP_IMG_MAGNETIC;
                        if (pp.getType() == PhysicalParameterType.T1 || pp.getType() == PhysicalParameterType.T2 || pp.getType() == PhysicalParameterType.T2s || pp.getType() == PhysicalParameterType.protonDensity || pp.getType() == PhysicalParameterType.susceptibility) {
                            icon = ModelConstants.APP_IMG_MAGNETIC;
                        }
                        if (pp.getType() == PhysicalParameterType.chemicalBlend) {
                            icon = ModelConstants.APP_IMG_CHEMICAL;
                        }
                        if (pp.getType() == PhysicalParameterType.radioactiviy) {
                            icon = ModelConstants.APP_IMG_RADIO;
                        }
                         if (pp.getType() == PhysicalParameterType.scatterers) {
                            icon = ModelConstants.APP_IMG_ECHO;
                        }
                        objectLayerPhysParamsLUT[olppl++] = new ModelTreeNode("" + (2 + id++), description, false);
                        objectLayerPhysParamsLUT[olppl - 1].setIcon(icon);
                    }
                    if (olppl != 0) {
                        objectLayerPhysParams[0] = new ModelTreeNode("" + (2 + id++), "Look-up tables", true, objectLayerPhysParamsLUT);
                        objectLayerPhysParams[0].setIcon(ModelConstants.APP_IMG_LUT);
                    }

                    ModelTreeNode[] objectLayerPhysParamsLayer = new ModelTreeNode[ol.getPhysicalParametersLayers().size()];
                    int olppla = 0;
                    for (PhysicalParametersLayer ppl : ol.getPhysicalParametersLayers()) {
                        String description = ppl.toString();
                        objectLayerPhysParamsLayer[olppla++] = new ModelTreeNode("" + (2 + id++), description, false);
                        if (ppl.getType() == PhysicalParameterType.T1 || ppl.getType() == PhysicalParameterType.T2 || ppl.getType() == PhysicalParameterType.T2s || ppl.getType() == PhysicalParameterType.protonDensity || ppl.getType() == PhysicalParameterType.susceptibility) {
                            objectLayerPhysParamsLayer[olppla - 1].setIcon(ModelConstants.APP_IMG_MAGNETIC);
                        }
                        if (ppl.getType() == PhysicalParameterType.chemicalBlend) {
                            objectLayerPhysParamsLayer[olppla - 1].setIcon(ModelConstants.APP_IMG_CHEMICAL);
                        }
                        if (ppl.getType() == PhysicalParameterType.radioactiviy) {
                            objectLayerPhysParamsLayer[olppla - 1].setIcon(ModelConstants.APP_IMG_RADIO);
                        }
                         if (ppl.getType() == PhysicalParameterType.scatterers) {
                            objectLayerPhysParamsLayer[olppla - 1].setIcon(ModelConstants.APP_IMG_ECHO);
                        }
                    }

                    if (olppla != 0) {
                        objectLayerPhysParams[1] = new ModelTreeNode("" + (2 + id++), "Maps", true, objectLayerPhysParamsLayer);
                        objectLayerPhysParams[1].setIcon(ModelConstants.APP_IMG_MAP);
                    }
                    if (olppla != 0 || olppl!=0) {
                        objectLayerParts[nolp++] = new ModelTreeNode("" + (2 + id++), "Physical parameters", false, objectLayerPhysParams);
                        objectLayerParts[nolp - 1].setIcon(ModelConstants.APP_IMG_PHYSICAL_PARAMS);
                    }
                    ModelTreeNode[] objects = new ModelTreeNode[ol.getLayerParts().size()];
                    int no=0;
                    for (ObjectLayerPart olp : ol.getLayerParts()) {
                        String description = olp.getReferredObject().getObjectName().replace("_", " ") + " (" + olp.getFormat() + ": ";
                        if (olp.getFormat() == ObjectLayerPart.Format.voxel) {
                            description += "label " + olp.getLabel() + " in ";
                        }
                        description += olp.getFileNames().toString().replace("[", "").replace("]", "") + ")";
                        objects[no++] = new ModelTreeNode("" + (2 + id++), description, false);
                        objects[no - 1].setIcon(ModelConstants.APP_IMG_OBJECT);
                    }
                    objectLayerParts[nolp++] = new ModelTreeNode("" + (2 + id++), "Objects", false, objects);
                    objectLayerParts[nolp - 1].setIcon(ModelConstants.APP_IMG_OBJECT);
                    
                    String description = "";
                    String icon=ModelConstants.APP_IMG_ANATOMY;
                    if(ol.getType() == SimulationObjectModel.ObjectType.anatomical){
                        description = "Anatomy";
                        icon = ModelConstants.APP_IMG_ANATOMY;
                    }
                    if(ol.getType() ==  SimulationObjectModel.ObjectType.external_agent){
                        description = "External agent";
                        icon = ModelConstants.APP_IMG_EXTERNAL;
                    }
                    if(ol.getType() ==  SimulationObjectModel.ObjectType.foreign_body){
                        description = "Foreign body";
                        icon = ModelConstants.APP_IMG_FOREIGN;
                    }
                    if(ol.getType() ==  SimulationObjectModel.ObjectType.pathological){
                        description = "Pathology";
                        icon = ModelConstants.APP_IMG_PATHOLOGY;
                    }
                    if(ol.getType() ==  SimulationObjectModel.ObjectType.geometrical){
                        description = "Geometry";
                        icon = ModelConstants.APP_IMG_GEOMETRY;
                    }
                    if (!ol.getResolution().equals(ObjectLayer.Resolution.none)) {
                        description += " (" + ol.getResolution().toString() + " resolution) ";
                    }
                    instantLayers[nol++] = new ModelTreeNode("" + (2 + id++), description, false, objectLayerParts);
                    
                    instantLayers[nol - 1].setIcon(icon);
                }
                instants[nit++] = new ModelTreeNode("" + (2 + id++), "Instant (" + it.getDuration() + ")", true, instantLayers);
                instants[nit - 1].setIcon(ModelConstants.APP_IMG_INSTANT);
            }
            timepoints[ntp++] = new ModelTreeNode("" + (2 + id++), "Timepoint (" + tp.getStartingDate() + ")", true, instants);
            timepoints[ntp - 1].setIcon(ModelConstants.APP_IMG_TIMEPOINT);
        }
        TreeNode root = new ModelTreeNode("1", "Root", true, timepoints);

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

        
        
        public ModelTreeNode(String entityId, String entityName, boolean display) {
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
