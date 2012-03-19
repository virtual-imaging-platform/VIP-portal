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
package fr.insalyon.creatis.vip.gatelab.server.business;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import fr.insalyon.creatis.vip.gatelab.server.dao.DAOFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class GateLabBusiness {

    private final static Logger logger = Logger.getLogger(GateLabBusiness.class);

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<Application> getApplications() throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getClassDAO().add(new AppClass(
                    GateLabConstants.GATELAB_CLASS, new ArrayList<String>()));

        } catch (DAOException ex) {
            if (!ex.getMessage().contains("A class named \"" + GateLabConstants.GATELAB_CLASS + "\" already exists")) {
                logger.error(ex);
                throw new BusinessException(ex);
            }
        }
        
        ApplicationBusiness applicationBusiness = new ApplicationBusiness();
        List<String> classes = new ArrayList<String>();
        classes.add(GateLabConstants.GATELAB_CLASS);

        return applicationBusiness.getApplications(classes);
    }

    /**
     * 
     * @param workflowID
     * @param currentUserFolder
     * @return
     * @throws BusinessException 
     */
    public Map<String, String> getGatelabWorkflowInputs(String workflowID, 
            String currentUserFolder) throws BusinessException {

        try {
            GateLabInputs gateinputs = new GateLabInputs(workflowID);
            Map<String, String> inputMap = gateinputs.getWorkflowInputs(currentUserFolder);

            long nb = DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();
            inputMap.put("runnedparticles", "" + nb);

            return inputMap;

        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param workflowID
     * @return
     * @throws BusinessException 
     */
    public long getNumberParticles(String workflowID) throws BusinessException {

        try {
            return DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();

        } catch (DAOException ex) {
            return 0;
        }
    }

    /**
     * 
     * @param workflowID
     * @throws GateLabException 
     */
    public void StopWorkflowSimulation(String workflowID) throws BusinessException {

        try {
            DAOFactory.getDAOFactory().getGatelabDAO(workflowID).StopWorkflowSimulation();

        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
}
