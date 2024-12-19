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
package fr.insalyon.creatis.vip.application.server.dao.hibernate;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Repository
public class SimulationStatsData implements SimulationStatsDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionFactory sessionFactory;

    @Autowired
    public SimulationStatsData(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<String> getBySimulationID(List<String> simulationID) throws DAOException {
        List<String> result = new ArrayList<String>();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
            Root<Workflow> root = criteriaQuery.from(Workflow.class);
            List<Selection<?>> selections = new ArrayList<>();

            criteriaQuery.where(root.get("id").in(simulationID));

            selections.add(criteriaBuilder.sum(root.get("completed")).alias("sumCompleted"));
            selections.add(criteriaBuilder.sum(root.get("completedWaitingTime")).alias("sumCompletedWaitingTime"));
            selections.add(criteriaBuilder.sum(root.get("completedExecutionTime")).alias("sumCompletedExecutionTime"));
            selections.add(criteriaBuilder.sum(root.get("completedInputTime")).alias("sumCompletedInputTime"));
            selections.add(criteriaBuilder.sum(root.get("completedOutputTime")).alias("sumCompletedOutputTime"));
            selections.add(criteriaBuilder.sum(root.get("cancelled")));
            selections.add(criteriaBuilder.sum(root.get("cancelled")));
            selections.add(criteriaBuilder.sum(root.get("cancelledWaitingTime")));
            selections.add(criteriaBuilder.sum(root.get("cancelledExecutionTime")));
            selections.add(criteriaBuilder.sum(root.get("cancelledInputTime")));
            selections.add(criteriaBuilder.sum(root.get("cancelledOutputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedApplication")));
            selections.add(criteriaBuilder.sum(root.get("failedApplicationWaitingTime")));
            selections.add(criteriaBuilder.sum(root.get("failedApplicationExecutionTime")));
            selections.add(criteriaBuilder.sum(root.get("failedApplicationInputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedApplicationOutputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedInput")));
            selections.add(criteriaBuilder.sum(root.get("failedInputWaitingTime")));
            selections.add(criteriaBuilder.sum(root.get("failedInputExecutionTime")));
            selections.add(criteriaBuilder.sum(root.get("failedInputInputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedInputOutputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedOutput")));
            selections.add(criteriaBuilder.sum(root.get("failedOutputWaitingTime")));
            selections.add(criteriaBuilder.sum(root.get("failedOutputExecutionTime")));
            selections.add(criteriaBuilder.sum(root.get("failedOutputInputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedOutputOutputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedStalled")));
            selections.add(criteriaBuilder.sum(root.get("failedStalledWaitingTime")));
            selections.add(criteriaBuilder.sum(root.get("failedStalledExecutionTime")));
            selections.add(criteriaBuilder.sum(root.get("failedStalledInputTime")));
            selections.add(criteriaBuilder.sum(root.get("failedStalledOutputTime")));
            criteriaQuery.multiselect(selections);

            session.getTransaction().commit();

            List<Object[]> results = session.createQuery(criteriaQuery).getResultList();

            for (Object[] row : results) {
                if (row.length >= 30) {
                    result.add("Completed Jobs##" + String.valueOf(row[0]) + "");
                    result.add("CompletedJobs WaitingTime##" + String.valueOf(row[1]) + "");
                    result.add("CompletedJobs ExecutionTime##" + String.valueOf(row[2]) + "");
                    result.add("CompletedJobs InputTime##" + String.valueOf(row[3]) + "");
                    result.add("CompletedJobs OutputTime##" + String.valueOf(row[4]) + "");
                    result.add("Cancelled Jobs##" + String.valueOf(row[5]) + "");
                    result.add("CancelledJobs WaitingTime##" + String.valueOf(row[6]) + "");
                    result.add("CancelledJobs ExecutionTime##" + String.valueOf(row[7]) + "");
                    result.add("CancelledJobs InputTime##" + String.valueOf(row[8]) + "");
                    result.add("CancelledJobs OutputTime##" + String.valueOf(row[9]) + "");
                    result.add("failedApplication Jobs##" + String.valueOf(row[10]) + "");
                    result.add("failedApplicationJobs WaitingTime##" + String.valueOf(row[11]) + "");
                    result.add("failedApplicationJobs ExecutionTime##" + String.valueOf(row[12]) + "");
                    result.add("failedApplicationJobs InputTime##" + String.valueOf(row[13]) + "");
                    result.add("failedApplicationJobs OutputTime##" + String.valueOf(row[14]) + "");
                    result.add("failedInput Jobs##" + String.valueOf(row[15]) + "");
                    result.add("failedInputJobs WaitingTime##" + String.valueOf(row[16]) + "");
                    result.add("failedInputJobs ExecutionTime##" + String.valueOf(row[17]) + "");
                    result.add("failedInputJobs InputTime##" + String.valueOf(row[18]) + "");
                    result.add("failedInputJobs OutputTime##" + String.valueOf(row[19]) + "");
                    result.add("failedInput Jobs##" + String.valueOf(row[15]) + "");
                    result.add("failedInputJobs WaitingTime##" + String.valueOf(row[16]) + "");
                    result.add("failedInputJobs ExecutionTime##" + String.valueOf(row[17]) + "");
                    result.add("failedInputJpbs InputTime##" + String.valueOf(row[18]) + "");
                    result.add("failedInputJobs OutputTime##" + String.valueOf(row[19]) + "");
                    result.add("failedOutput Jobs##" + String.valueOf(row[20]) + "");
                    result.add("failedOutputJobs WaitingTime##" + String.valueOf(row[21]) + "");
                    result.add("failedOutputJobs ExecutionTime##" + String.valueOf(row[22]) + "");
                    result.add("failedOutputJobs InputTime##" + String.valueOf(row[23]) + "");
                    result.add("failedOutputJobs OutputTime##" + String.valueOf(row[24]) + "");
                    result.add("failedStalled Jobs##" + String.valueOf(row[25]) + "");
                    result.add("failedStalledJobs WaitingTime##" + String.valueOf(row[26]) + "");
                    result.add("failedStalledJobs ExecutionTime##" + String.valueOf(row[27]) + "");
                    result.add("failedStalledJobs InputTime##" + String.valueOf(row[28]) + "");
                    result.add("failedStalledJobs OutputTime##" + String.valueOf(row[29]) + "");
                } else {
                    logger.error("getBySimulationID: Not enough data : {}", row.length);
                    throw new DAOException("getBySimulationID: Not enough data");
                }
            }

            return result;
        } catch (HibernateException ex) {
            logger.error("Error getting stats for {}", simulationID, ex);
            throw new DAOException(ex);

        }
    }

    @Override
    public List<String> getWorkflowsPerUser(List<String> workflowsId) throws WorkflowsDBDAOException {
        List<String> result = new ArrayList<String>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
            Root<Workflow> root = criteriaQuery.from(Workflow.class);
            List<Selection<?>> selections = new ArrayList<>();
            Expression<?> expression = criteriaBuilder.count(root.get("status"));

            criteriaQuery.where(root.get("id").in(workflowsId));

            selections.add(root.get("username"));
            selections.add(expression.alias("nbWfls"));
            criteriaQuery.multiselect(selections)
                .groupBy(root.get("username"));

            criteriaQuery.orderBy(criteriaBuilder.desc(expression));

            List<Object[]> results = session.createQuery(criteriaQuery).getResultList();

            for (Object[] row : results) {
                String username = (String) row[0];
                Long nbWfls = (Long) row[1];

                result.add(String.valueOf(username) + "##" + String.valueOf(nbWfls));
            }

            return result;
        } catch (HibernateException ex) {
            logger.error("Error getting workflows for {}", workflowsId, ex);
            throw new WorkflowsDBDAOException(ex);
        }
    }

    @Override
    public List<String> getApplications(List<String> workflowsId) throws WorkflowsDBDAOException {
        List<String> result = new ArrayList<String>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
            Root<Workflow> root = criteriaQuery.from(Workflow.class);
            List<Selection<?>> selections = new ArrayList<>();
            Expression<?> expression = criteriaBuilder.count(root.get("status"));

            criteriaQuery.where(root.get("id").in(workflowsId));

            selections.add(root.get("application"));
            selections.add(expression.alias("nbWfls"));
            criteriaQuery.multiselect(selections)
                .groupBy(root.get("application"));

            criteriaQuery.orderBy(criteriaBuilder.desc(expression));
            
            List<Object[]> results = session.createQuery(criteriaQuery).getResultList();

            for (Object[] row : results) {
                if (row[0] != null && row[1] != null) {
                    result.add(String.valueOf(row[0]) + "##" + String.valueOf(row[1]));
                }
            }
            return result;
        } catch (HibernateException ex) {
            logger.error("Error getting applications for {}", workflowsId, ex);
            throw new WorkflowsDBDAOException(ex);
        }
    }

    @Override
    public List<String> getClasses(List<String> workflowsId) throws WorkflowsDBDAOException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
            Root<Workflow> root = criteriaQuery.from(Workflow.class);

            criteriaQuery.where(root.get("id").in(workflowsId))
                .select(root.get("applicationClass")).distinct(true);

            List<String> results = session.createQuery(criteriaQuery).getResultList();

            return results;
        } catch (HibernateException ex) {
            logger.error("Error getting classes for {}", workflowsId, ex);
            throw new WorkflowsDBDAOException(ex);
        }

    }
}
