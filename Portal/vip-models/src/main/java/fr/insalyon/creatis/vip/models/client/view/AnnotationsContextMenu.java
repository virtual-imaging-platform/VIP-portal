/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.models.client.ModelConstants;

/**
 *
 * @author glatard
 */
public class AnnotationsContextMenu extends Menu {

    private ModalWindow modal;
    private String displayId;
    private SimulationObjectModel model = null;

    public AnnotationsContextMenu(final SimulationObjectModel model, final String name) {
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

//        MenuItem newTimePoint = new MenuItem("Add timepoint");
//        newTimePoint.setIcon(ModelConstants.APP_IMG_TIMEPOINT);
//        newTimePoint.addClickHandler(new ClickHandler() {
//
//            public void onClick(MenuItemClickEvent event) {
//                addTimepoint();
//            }
//        });
        //   MenuItem newInstant = new MenuItem("Add timepoint");
//        MenuItem newTimePoint = new MenuItem("Add timepoint");
//        newTimePoint.setIcon(ModelConstants.APP_IMG_TIMEPOINT);
//        newTimePoint.addClickHandler(new ClickHandler() {
//
//            public void onClick(MenuItemClickEvent event) {
//                addTimepoint();
//            }
//        });
        MenuItem newInstant = new MenuItem("Add instant");
        newInstant.setIcon(ModelConstants.APP_IMG_INSTANT);
        newInstant.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                //addInstant(model,event.);
//                ModelTreeNode node = null;
//                node = (ModelTreeNode)(event.getSource());//.getTarget().getParent().);
//                if (node.getIcon() != ModelConstants.APP_IMG_TIMEPOINT)
//                {
//                    node.
//                }
//               addInstant(model,node.getParent()));
            }
        });

        MenuItem viewItem = new MenuItem("View");
        viewItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                view();
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon(ApplicationConstants.ICON_KILL);
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to delete "
                        + name + "?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            deleteTreeNode();
                        }
                    }
                });

            }
        });

        this.setItems(newInstant);

    }

    private void view() {
        SC.warn("not implemented");
    }

    private void addInstant(SimulationObjectModel model) {
        //  model.getTimepoint(timePointIndex).addInstant(new Instant());
    }

    private void addTimepoint() {
        SC.warn("not implemented");

    }

    private void deleteTreeNode() {
        SC.warn("not implemented");
    }
}
