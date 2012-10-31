/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        MenuItem newInstant = new MenuItem("Add instat");
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
