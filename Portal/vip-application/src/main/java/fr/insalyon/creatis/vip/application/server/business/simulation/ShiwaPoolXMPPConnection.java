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
package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.shiwapool.client.ClientConfiguration;
import fr.insalyon.creatis.shiwapool.common.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author javier
 */
public class ShiwaPoolXMPPConnection {

    private static final Logger logger = LoggerFactory.getLogger(ShiwaPoolXMPPConnection.class);
    private static volatile ShiwaPoolXMPPConnection instance;
    private Connection connection;

    public static Connection getInstance()
            throws org.jivesoftware.smack.XMPPException {

        ShiwaPoolXMPPConnection _instance = instance;
        if (_instance == null) {
            _instance = createInstance();
        }

        Connection _connection = _instance.connection;
        if (!_connection.isConnected()) {

            logger.info("Connecting to the XMPP server...");
            ClientConfiguration configuration = ClientConfiguration.getInstance();
            _connection.connect(configuration.getClientName(), configuration.getClientPassword());
        } else {
            logger.info("XMPP Connection is active");
        }

        return _connection;
    }

    private static synchronized ShiwaPoolXMPPConnection createInstance() {

        if (instance == null) {
            instance = new ShiwaPoolXMPPConnection();
        }

        return instance;
    }

    private ShiwaPoolXMPPConnection() {

        ClientConfiguration configuration = ClientConfiguration.getInstance();
        connection = new Connection(configuration.getServerHost(), configuration.getServerPort());
    }
}
