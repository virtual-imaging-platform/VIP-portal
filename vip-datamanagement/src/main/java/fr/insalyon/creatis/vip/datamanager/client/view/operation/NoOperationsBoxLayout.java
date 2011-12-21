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
package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class NoOperationsBoxLayout extends HLayout {

    public NoOperationsBoxLayout() {

        this.setMembersMargin(2);
        this.setWidth100();
        this.setHeight(40);
        this.setBackgroundColor("#E2E2E2");
        this.setBorder("1px solid #D2D2D2");
        this.setAlign(Alignment.CENTER);

        configureImageLayout();
        configureMainLayout();
    }

    /**
     * Configures the image layout.
     */
    private void configureImageLayout() {

        VLayout imgLayout = new VLayout();
        imgLayout.setPadding(2);
        imgLayout.setWidth(40);
        imgLayout.setHeight(45);
        imgLayout.setAlign(Alignment.CENTER);

        Img icon = new Img(DataManagerConstants.IMG_WARNING, 32, 32);
        icon.setPrompt("No more operations");

        imgLayout.addMember(icon);
        this.addMember(imgLayout);
    }

    /**
     * Configures the main layout of an operation.
     */
    private void configureMainLayout() {

        VLayout mainLayout = new VLayout(2);
        mainLayout.setWidth(120);
        mainLayout.setHeight(45);
        mainLayout.setAlign(Alignment.CENTER);

        Label messageLabel = new Label("No more operations");
        messageLabel.setHeight(15);
        mainLayout.addMember(messageLabel);

        this.addMember(mainLayout);
    }
}
