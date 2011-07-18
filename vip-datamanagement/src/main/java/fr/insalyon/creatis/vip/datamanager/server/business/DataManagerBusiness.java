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
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerBusiness {
    
    private final static Logger logger = Logger.getLogger(DataManagerBusiness.class);
    
    public void configureDataManager(String user, String proxyFileName) throws BusinessException {
        
        VletAgentClient client = new VletAgentClient(
                ServerConfiguration.getInstance().getVletagentHost(),
                ServerConfiguration.getInstance().getVletagentPort(),
                proxyFileName);

        String errorMessage = "ERROR: File/Directory exists or "
                    + "Directory is not empty";
        
        try {
            client.createDirectory(ServerConfiguration.getInstance().getDataManagerUsersHome(),
                    user.replaceAll(" ", "_").toLowerCase());
            
        } catch (VletAgentClientException ex) {
            if (!ex.getMessage().contains(errorMessage)) {
                logger.error(ex);
                throw new BusinessException(ex);
            }
        }
        try {
            client.createDirectory(ServerConfiguration.getInstance().getDataManagerUsersHome(),
                    user.replace(" ", "_").toLowerCase()
                    + "_" + DataManagerConstants.TRASH_HOME);
            
        } catch (VletAgentClientException ex) {
            if (!ex.getMessage().contains(errorMessage)) {
                logger.error(ex);
                throw new BusinessException(ex);
            }
        }
    }
    
    public void deleteLocalFile(String fileName) throws BusinessException {
        
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        } else {
            logger.error("File '" + fileName + "' does not exist.");
        }
    }
}
