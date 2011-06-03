/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Tristan Glatard
 */
public class ModelButton extends ToolStripButton {

    public ModelButton() {
        
        this.setTitle("Models");
        this.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new ModelListTab());
            }
        });

//        Menu menu = new Menu();
//        menu.setShowShadow(true);
//        menu.setShadowDepth(3);
//
//        MenuItem imp = new MenuItem("Upload");
//        imp.setIcon("icon-add.png");
//        imp.addClickHandler(new ClickHandler(){
//
//            public void onClick(MenuItemClickEvent event) {
//                Layout.getInstance().addTab(new ModelImportTab());
//            }
//        });
//
//        MenuItem browse = new MenuItem("List");
//        browse.setIcon("icon-list.png");
//        browse.addClickHandler(new ClickHandler(){
//
//            public void onClick(MenuItemClickEvent event) {
//                Layout.getInstance().addTab(new ModelListTab());
//            }
//        });
//        browse.setEnabled(true);
//        
//        menu.setItems(imp,browse);
//        this.setMenu(menu);
    }
}
