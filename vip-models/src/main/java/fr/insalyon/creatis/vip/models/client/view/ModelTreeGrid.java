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
 * @author Tristan Glatard
 */
public class ModelTreeGrid extends TreeGrid {

    private SimulationObjectModel model = null;

    public SimulationObjectModel getModel() {
        return model;
    }

    public ModelTreeGrid(SimulationObjectModel model) {
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
        int ntp = 0;
        ModelTreeNode[] timepoints = new ModelTreeNode[model.getTimepoints().size()];
        for (Timepoint tp : model.getTimepoints()) {
            ModelTreeNode[] instants = new ModelTreeNode[tp.getInstants().size()];
            int nit = 0;
            for (Instant it : tp.getInstants()) {
                int nol = 0;
                ModelTreeNode[] objectLayers = new ModelTreeNode[it.getObjectLayers().size()];
                for (ObjectLayer ol : it.getObjectLayers()) {
                    int nolp = 0;
                    ModelTreeNode[] objectLayerParts = new ModelTreeNode[ol.getLayerParts().size()];
                    for (ObjectLayerPart olp : ol.getLayerParts()) {
                        String description = olp.getReferredObject().getObjectName().replace("_", " ") + " (" + olp.getFormat() + ": ";
                        if (olp.getFormat() == ObjectLayerPart.Format.voxel) {
                            description += "label " + olp.getLabel() + " in ";
                        }
                        description += olp.getFileNames().toString().replace("[", "").replace("]", "") + ")";
                        objectLayerParts[nolp++] = new ModelTreeNode("" + (2 + id++), description, false);
                    }
                    String description = ol.getType().toString().replace("_", " ");
                    if (!ol.getResolution().equals(ObjectLayer.Resolution.none)) {
                        description += " (" + ol.getResolution().toString() + " resolution) ";
                    }
                    description += " layer";
                    objectLayers[nol++] = new ModelTreeNode("" + (2 + id++), description, false, objectLayerParts);
                }
                instants[nit++] = new ModelTreeNode("" + (2 + id++), "Instant (" + it.getDuration() + ")", true, objectLayers);
            }
            timepoints[ntp++] = new ModelTreeNode("" + (2 + id++), "Timepoint (" + tp.getStartingDate() + ")", true, instants);
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
