/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractCornerTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.GeneralLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.InputTreeGrid;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.LogsLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.OutputTreeGrid;
import fr.insalyon.creatis.vip.core.client.CoreModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralTab extends AbstractCornerTab {

    private GeneralLayout generalLayout;
    private InputTreeGrid inputTreeGrid;
    private OutputTreeGrid outputTreeGrid;
    
    public GeneralTab(String simulationID, SimulationStatus status) {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL));
        this.setPrompt("General Information");

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        generalLayout = new GeneralLayout(simulationID, status);
        vLayout.addMember(generalLayout);

        HLayout hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);

        // Left column
        VLayout leftLayout = new VLayout(10);
        leftLayout.setWidth("50%");
        leftLayout.setHeight100();
        leftLayout.setOverflow(Overflow.AUTO);

        inputTreeGrid = new InputTreeGrid(simulationID);
        leftLayout.addMember(inputTreeGrid);
        
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.isGroupAdmin()) {
            leftLayout.addMember(new LogsLayout(simulationID));
        }

        // Right column
        VLayout rightLayout = new VLayout(10);
        rightLayout.setWidth("50%");
        rightLayout.setHeight100();
        rightLayout.setOverflow(Overflow.AUTO);
        
        outputTreeGrid = new OutputTreeGrid(simulationID);
        rightLayout.addMember(outputTreeGrid);

        hLayout.addMember(leftLayout);
        hLayout.addMember(rightLayout);
        vLayout.addMember(hLayout);

        this.setPane(vLayout);
    }

    @Override
    public void update() {

        generalLayout.update();
        inputTreeGrid.update();
        outputTreeGrid.update();
    }
}
