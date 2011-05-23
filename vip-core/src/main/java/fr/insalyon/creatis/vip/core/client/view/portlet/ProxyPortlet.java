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

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import fr.insalyon.creatis.vip.common.client.view.Context;

/**
 *
 * @author Rafael Silva
 */
public class ProxyPortlet extends Portlet {

    public ProxyPortlet() {

        this.setTitle("Proxy Info");

        HTMLPane pane = new HTMLPane();
        pane.setHeight100();
        pane.setPadding(10);
        pane.setStyleName("defaultBorder");
        pane.setOverflow(Overflow.AUTO);

        if (Context.getInstance().hasValidProxy()) {
            pane.setContents("<html>"
                    + "<body>"
                    + "<p>You have valid grid credentials.<br />"
                    + "Proxy Validity: " + Context.getInstance().getProxyValidity() + "</p>"
                    + "</body>"
                    + "</html>");

        } else {
            if (Context.getInstance().getProxyValidity().equals("")) {
                pane.setContents("<html>"
                        + "<body>"
                        + "<p>Warning! You do not have valid credentials uploaded!<br />"
                        + "Please, go to <a href=\"http://vip.creatis.insa-lyon.fr/portal/web/vip/proxy-initialisation\">"
                        + "our portal</a> to upload your proxy.</p>"
                        + "</body>"
                        + "</html>");
            } else {
                pane.setContents("<html>"
                        + "<body>"
                        + "<p>Warning! You do not have valid credentials uploaded!<br />"
                        + "Please, go to <a href=\"http://vip.creatis.insa-lyon.fr/portal/web/vip/proxy-initialisation\">"
                        + "our portal</a> to upload your proxy.</p>"
                        + "</body>"
                        + "</html>");
            }
        }

        this.addItem(pane);
    }
}
