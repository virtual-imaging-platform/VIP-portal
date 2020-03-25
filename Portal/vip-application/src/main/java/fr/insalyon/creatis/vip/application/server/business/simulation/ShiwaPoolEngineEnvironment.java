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
import fr.insalyon.creatis.shiwapool.client.ClientMessageProcessor;
import fr.insalyon.creatis.shiwapool.common.Connection;
import fr.insalyon.creatis.shiwapool.common.FileTransfer;
import fr.insalyon.creatis.shiwapool.common.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author javier
 */
public class ShiwaPoolEngineEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(ShiwaPoolEngineEnvironment.class);

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
