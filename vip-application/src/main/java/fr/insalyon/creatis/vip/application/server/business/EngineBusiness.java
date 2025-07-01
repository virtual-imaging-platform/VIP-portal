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

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.dao.EngineDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class EngineBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EngineDAO engineDAO;
    private WorkflowDAO workflowDAO;

    public EngineBusiness(EngineDAO engineDAO, WorkflowDAO workflowDAO) {
        this.engineDAO = engineDAO;
        this.workflowDAO = workflowDAO;
    }

    public void add(Engine engine) throws BusinessException {
        try {
            engineDAO.add(engine);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void update(Engine engine) throws BusinessException {
        try {
            engineDAO.update(engine);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void remove(String name) throws BusinessException {
        try {
            engineDAO.remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Engine> get() throws BusinessException {
        try {
            return engineDAO.get();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Engine> getByResource(Resource resource) throws BusinessException {
        try {
            return engineDAO.getByResource(resource);
        } catch (DAOException e) {
        throw new BusinessException(e);
        }
    }

    public List<Engine> getUsableEngines(Resource resource) throws BusinessException {
        List<Engine> engines = getByResource(resource);

        engines = getByResource(resource);

        if (engines.isEmpty()) {
            engines = get();
        }
        return engines
            .stream()
            .filter((e) -> e.getStatus().equals("enabled"))
            .collect(Collectors.toList());
    }

    public Engine selectEngine(List<Engine> availableEngines) throws BusinessException {
        long min = Long.MAX_VALUE;
        Engine selectEngine = null;

        try {
            for (Engine engine : availableEngines) {
                long runningWorkflows = workflowDAO.getNumberOfRunningPerEngine(engine.getEndpoint());
                if (runningWorkflows < min && ! engine.getEndpoint().isEmpty()) {
                    min = runningWorkflows;
                    selectEngine = engine;
                }
            }
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error selecting an engine !", ex);
        }
        if (selectEngine == null || availableEngines.isEmpty()) {
            logger.error("No available engines !");
            throw new BusinessException("No available engines !");
        } else {
            return selectEngine;
        }
    }
}
