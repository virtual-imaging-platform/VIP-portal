package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.shiwapool.client.ClientConfiguration;
import fr.insalyon.creatis.shiwapool.common.Connection;
import org.apache.log4j.Logger;

/**
 *
 * @author javier
 */
public class ShiwaPoolXMPPConnection {

    private static final Logger logger = Logger.getLogger(ShiwaPoolXMPPConnection.class);
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
