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

package fr.insalyon.creatis.platform.main.client.view.main;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 *
 * @author Rafael Silva
 */
public class MainCenterPanel extends Panel {

    private static MainCenterPanel instance;

    public static MainCenterPanel getInstance() {
        if (instance == null) {
            instance = new MainCenterPanel();
        }
        return instance;
    }

    private MainCenterPanel() {
        this.setId("main-center-panel");
        this.setTitle("Home");
        this.setPaddings(5, 5, 5, 5);
        this.setLayout(new FitLayout());

        this.setHtml("<html>" +
                "<body>" +
                "<p>&nbsp;</p>" +
                "<h2>Disclaimer</h2><p>This portal is exclusively dedicated to academic use. "
                +"It shows some functionalities of the VIP platform but it is not in a stable release yet. "
                + "Please acknowledge the Virtual Imaging Platform (VIP) ANR project ANR-09-COSI-03 wherever appropriate.</p><br/>"
                + "<p style=\"padding-left: 10 px\">The Virtual Imaging Platform (VIP) project targets multi-modality, "
                + "multi-organ and dynamic (4D) medical image simulation. Integrating proven simulation software "
                +"of the four main imaging modalities (MRI, US, PET and CT), the platform will cope with "
                +"interoperability challenges among simulators, will address compatibility issues between organ"+
                "models and will provide transparent access to computing and storage resources.</p><p>"
                + "<img src=\"http://www.agence-nationale-recherche.fr/fileadmin/tpl/img/logo.gif\"/><p/>" +
                "</body>" +
                "</html>");
    }

}
