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
package fr.insalyon.creatis.vip.core.client.view.portlet;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.LayoutPolicy;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Window;

/**
 *
 * @author Rafael Silva
 */
public class Portlet extends Window {

    public Portlet() {

        this.setShowShadow(false);

        // enable predefined component animation  
        this.setAnimateMinimize(true);

        // Window is draggable with "outline" appearance by default.  
        // "target" is the solid appearance.  
        this.setDragAppearance(DragAppearance.OUTLINE);
        this.setCanDrop(true);

        // customize the appearance and order of the controls in the window header  
        this.setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.MINIMIZE_BUTTON, HeaderControls.CLOSE_BUTTON);

        // show either a shadow, or translucency, when dragging a portlet  
        // (could do both at the same time, but these are not visually compatible effects)  
        // setShowDragShadow(true);  
        this.setDragOpacity(30);

        // these settings enable the portlet to autosize its height only to fit its contents  
        // (since width is determined from the containing layout, not the portlet contents)  
        this.setVPolicy(LayoutPolicy.FILL);
//        this.setOverflow(Overflow.VISIBLE);
    }
}
