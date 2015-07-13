package fr.insalyon.creatis.vip.application.server.business.simulation;

import fr.insalyon.creatis.shiwapool.client.ClientConfiguration;
import fr.insalyon.creatis.shiwapool.client.ClientMessageProcessor;
import fr.insalyon.creatis.shiwapool.common.Connection;
import fr.insalyon.creatis.shiwapool.common.FileTransfer;
import fr.insalyon.creatis.shiwapool.common.Receiver;
import org.apache.log4j.Logger;

/**
 *
 * @author javier
 */
public class ShiwaPoolEngineEnvironment {

    private static Logger logger = Logger.getLogger(ShiwaPoolEngineEnvironment.class);
    private static volatile ShiwaPoolEngineEnvironment instance;
    private Thread rthread;
    private Thread cthread;

    /**
     * Setup the SHIWA Pool Execution environment by launching the carrier and
     * receiver threads if they are not active
     *
     * @return
     * @throws fr.insalyon.creatis.vip.core.server.business.BusinessException
     */
    public static ShiwaPoolEngineEnvironment getInstance()
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        ShiwaPoolEngineEnvironment _instance = instance;
        if (_instance == null) {
            _instance = createInstance();
        }

        Thread _rthread = _instance.rthread;
        if (!_instance.rthread.isAlive()) {

            logger.info("launching receiver thread");
            _rthread.start();
        } else {
            logger.info("receiver is running");
        }

        Thread _cthread = _instance.cthread;
        if (!_cthread.isAlive()) {

            logger.info("launching carrier thread");
            _cthread.start();
        } else {
            logger.info("carrier is running");
        }

        return _instance;
    }

    private static synchronized ShiwaPoolEngineEnvironment createInstance()
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        if (instance == null) {
            instance = new ShiwaPoolEngineEnvironment();
        }

        return instance;
    }

    private ShiwaPoolEngineEnvironment()
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        Connection connection = null;
        try {
            connection = ShiwaPoolXMPPConnection.getInstance();
        } catch (org.jivesoftware.smack.XMPPException ex) {
            throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Cannot connect to the XMPP server: " + ex.getMessage());
        }

        Receiver receiver = new Receiver(connection, ClientMessageProcessor.getInstance());
        rthread = new Thread(receiver);

        FileTransfer carrier = new FileTransfer(connection, ClientConfiguration.getInstance().getBundleRepository(), "CLIENT");
        cthread = new Thread(carrier);
    }
}
