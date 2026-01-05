package fr.insalyon.creatis.vip.application.server.rpc;

import com.google.gwt.logging.shared.RemoteLoggingService;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import java.util.logging.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minimalist Servlet allowing to write logs (from the client side) in vip.log file on server side.
 * 
 * Please see servlet configuration in Application.gwt.xml file from VIP-Application module, and in web.xml from VIP-portal module WEB-INF folder.
 * To use it, create a Logger object in a client side class : "private final Logger logger = LoggerFactory.getLogger(getClass());"
 * Then use it in methods. For instance : "logger.info("log_name : " + valueMapResult);
 */
public class LoggerServlet extends AbstractRemoteServiceServlet implements RemoteLoggingService  {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
     public String logOnServer(LogRecord record) {

         final String message = record.getMessage();
         logger.info(message);
         return null;
    }
        
}
