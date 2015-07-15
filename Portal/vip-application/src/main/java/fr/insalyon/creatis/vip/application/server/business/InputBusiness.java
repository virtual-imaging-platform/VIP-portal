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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class InputBusiness {

    /**
     *
     * @param email
     * @param name
     * @param appName
     * @return
     * @throws BusinessException
     */
    public SimulationInput getInputByUserAndName(String email, String name,
            String appName) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().getInputByNameUserApp(email, name, appName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param simulationInput
     * @throws BusinessException
     */
    public void addSimulationInput(String email, SimulationInput simulationInput)
            throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().addSimulationInput(email, simulationInput);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param simulationInput
     * @throws BusinessException
     */
    public void updateSimulationInput(String email, SimulationInput simulationInput)
            throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().updateSimulationInput(email, simulationInput);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationInput
     * @throws BusinessException
     */
    public void saveSimulationInputAsExample(SimulationInput simulationInput)
            throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().saveSimulationInputAsExample(simulationInput);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param fileName
     * @return
     * @throws BusinessException
     */
    public String loadSimulationInput(String fileName) throws BusinessException {

        fileName = DataManagerUtil.getUploadRootDirectory(true) + fileName;
        String inputs = new InputParser().parse(fileName);
        new DataManagerBusiness().deleteLocalFile(fileName);
        return inputs;
    }

    /**
     *
     * @param email
     * @param inputName
     * @param applicationName
     * @throws BusinessException
     */
    public void removeSimulationInput(String email, String inputName,
            String applicationName) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().removeSimulationInput(email, inputName, applicationName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param inputName
     * @param applicationName
     * @throws BusinessException 
     */
    public void removeSimulationInputExample(String inputName, 
            String applicationName) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().removeSimulationInputExample(inputName, applicationName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws BusinessException
     */
    public List<SimulationInput> getSimulationInputByUser(String email)
            throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().getSimulationInputByUser(email);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param applicationName
     * @return
     * @throws BusinessException 
     */
    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().getSimulationInputExamples(applicationName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
