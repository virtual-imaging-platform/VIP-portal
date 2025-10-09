package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.server.dao.EngineDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

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

    public void add(Engine engine) throws VipException {
        try {
            engineDAO.add(engine);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void update(Engine engine) throws VipException {
        try {
            engineDAO.update(engine);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void remove(String name) throws VipException {
        try {
            engineDAO.remove(name);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Engine> get() throws VipException {
        try {
            return engineDAO.get();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<Engine> getByResource(Resource resource) throws VipException {
        try {
            return engineDAO.getByResource(resource);
        } catch (DAOException e) {
        throw new VipException(e);
        }
    }

    public List<Engine> getUsableEngines(Resource resource) throws VipException {
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

    public Engine selectEngine(List<Engine> availableEngines) throws VipException {
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
            throw new VipException("No available engines !");
        } else {
            return selectEngine;
        }
    }
}
