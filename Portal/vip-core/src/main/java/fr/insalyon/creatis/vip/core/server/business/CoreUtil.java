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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.sma.client.SMAClientException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class CoreUtil {

    private static final Logger logger = Logger.getLogger(CoreUtil.class);

    public static void sendEmail(String subject, String content, String[] recipients,
            boolean direct, String username) throws BusinessException {

        try {
            SMAClient client = new SMAClient(Server.getInstance().getSMAHost(), Server.getInstance().getSMAPort());
            client.sendEmail(subject, content, recipients, direct, username);

        } catch (SMAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return
     */
    public static GRIDAClient getGRIDAClient() {

        return new GRIDAClient(
                Server.getInstance().getGRIDAHost(),
                Server.getInstance().getGRIDAPort(),
                Server.getInstance().getServerProxy(CoreConstants.VO_BIOMED));
    }

    public static GRIDAClient getGRIDAN4uClient() {

        return new GRIDAClient(
                Server.getInstance().getN4uGridaHost(),
                Server.getInstance().getN4uGridaPort(),
                Server.getInstance().getServerProxy(CoreConstants.VO_NEUGRID));
    }

    /**
     *
     * @return
     */
    public static GRIDAPoolClient getGRIDAPoolClient() {

        return new GRIDAPoolClient(
                Server.getInstance().getGRIDAHost(),
                Server.getInstance().getGRIDAPort(),
                Server.getInstance().getServerProxy(CoreConstants.VO_BIOMED));
    }

    /**
     *
     * @return
     */
    public static GRIDACacheClient getGRIDACacheClient() {

        return new GRIDACacheClient(
                Server.getInstance().getGRIDAHost(),
                Server.getInstance().getGRIDAPort(),
                Server.getInstance().getServerProxy(CoreConstants.VO_BIOMED));
    }

    /**
     *
     * @return
     */
    public static GRIDAZombieClient getGRIDAZombieClient() {

        return new GRIDAZombieClient(
                Server.getInstance().getGRIDAHost(),
                Server.getInstance().getGRIDAPort(),
                Server.getInstance().getServerProxy(CoreConstants.VO_BIOMED));
    }
}
