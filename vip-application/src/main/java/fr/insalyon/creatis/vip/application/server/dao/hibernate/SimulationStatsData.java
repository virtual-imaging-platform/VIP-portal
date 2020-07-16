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

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Stats;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
// configured by spring in SpringApplicationConfig
public class SimulationStatsData implements SimulationStatsDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionFactory sessionFactory;

    public SimulationStatsData(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<String> getBySimulationID(List<String> simulationID) throws DAOException {
        //System.out.println("Calling SimulationStatsData: getBySimuID");
        List<String> result = new ArrayList<String>();
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Stats.class);

            // 'workflowID' is the variable in the class Stats
            //criteria.add(Restrictions.eq("workflowID", simulationID));
            criteria.add(Restrictions.in("workflowID", simulationID));
            //criteria.addOrder(Order.desc("workflowID"));

            ProjectionList p = Projections.projectionList();
            //p.add(Projections.sum("completed"));
            p.add(Projections.alias(Projections.sum("completed"), "sumCompleted"));
            p.add(Projections.alias(Projections.sum("completedWaitingTime"), "sumCompletedWaitingTime"));
            p.add(Projections.alias(Projections.sum("completedExecutionTime"), "sumCompletedExecutionTime"));
            p.add(Projections.alias(Projections.sum("completedInputTime"), "sumCompletedInputTime"));
            p.add(Projections.alias(Projections.sum("completedOutputTime"), "sumCompletedOutputTime"));
            p.add(Projections.sum("cancelled"));
            p.add(Projections.sum("cancelledWaitingTime"));
            p.add(Projections.sum("cancelledExecutionTime"));
            p.add(Projections.sum("cancelledInputTime"));
            p.add(Projections.sum("cancelledOutputTime"));
            p.add(Projections.sum("failedApplication"));
            p.add(Projections.sum("failedApplicationWaitingTime"));
            p.add(Projections.sum("failedApplicationExecutionTime"));
            p.add(Projections.sum("failedApplicationInputTime"));
            p.add(Projections.sum("failedApplicationOutputTime"));
            p.add(Projections.sum("failedInput"));
            p.add(Projections.sum("failedInputWaitingTime"));
            p.add(Projections.sum("failedInputExecutionTime"));
            p.add(Projections.sum("failedInputInputTime"));
            p.add(Projections.sum("failedInputOutputTime"));
            p.add(Projections.sum("failedOutput"));
            p.add(Projections.sum("failedOutputWaitingTime"));
            p.add(Projections.sum("failedOutputExecutionTime"));
            p.add(Projections.sum("failedOutputInputTime"));
            p.add(Projections.sum("failedOutputOutputTime"));
            p.add(Projections.sum("failedStalled"));
            p.add(Projections.sum("failedStalledWaitingTime"));
            p.add(Projections.sum("failedStalledExecutionTime"));
            p.add(Projections.sum("failedStalledInputTime"));
            p.add(Projections.sum("failedStalledOutputTime"));

            criteria.setProjection(p);
            List l = criteria.list();
            session.getTransaction().commit();
            session.close();

            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object ob[] = (Object[]) it.next();
                /*
                for (int i = 0; i < ob.length; i++) {
                System.out.println("Object " + i + " is " + ob[i]);
                if (ob[i] != null) {
                result.add(String.valueOf(ob[i]));
                }
                }
                 * 
                 */
                if (ob.length >= 30) {
                    result.add("Completed Jobs##" + String.valueOf(ob[0]) + "");
                    result.add("CompletedJobs WaitingTime##" + String.valueOf(ob[1]) + "");
                    result.add("CompletedJobs ExecutionTime##" + String.valueOf(ob[2]) + "");
                    result.add("CompletedJobs InputTime##" + String.valueOf(ob[3]) + "");
                    result.add("CompletedJobs OutputTime##" + String.valueOf(ob[4]) + "");
                    result.add("Cancelled Jobs##" + String.valueOf(ob[5]) + "");
                    result.add("CancelledJobs WaitingTime##" + String.valueOf(ob[6]) + "");
                    result.add("CancelledJobs ExecutionTime##" + String.valueOf(ob[7]) + "");
                    result.add("CancelledJobs InputTime##" + String.valueOf(ob[8]) + "");
                    result.add("CancelledJobs OutputTime##" + String.valueOf(ob[9]) + "");
                    result.add("failedApplication Jobs##" + String.valueOf(ob[10]) + "");
                    result.add("failedApplicationJobs WaitingTime##" + String.valueOf(ob[11]) + "");
                    result.add("failedApplicationJobs ExecutionTime##" + String.valueOf(ob[12]) + "");
                    result.add("failedApplicationJobs InputTime##" + String.valueOf(ob[13]) + "");
                    result.add("failedApplicationJobs OutputTime##" + String.valueOf(ob[14]) + "");
                    result.add("failedInput Jobs##" + String.valueOf(ob[15]) + "");
                    result.add("failedInputJobs WaitingTime##" + String.valueOf(ob[16]) + "");
                    result.add("failedInputJobs ExecutionTime##" + String.valueOf(ob[17]) + "");
                    result.add("failedInputJobs InputTime##" + String.valueOf(ob[18]) + "");
                    result.add("failedInputJobs OutputTime##" + String.valueOf(ob[19]) + "");
                    result.add("failedInput Jobs##" + String.valueOf(ob[15]) + "");
                    result.add("failedInputJobs WaitingTime##" + String.valueOf(ob[16]) + "");
                    result.add("failedInputJobs ExecutionTime##" + String.valueOf(ob[17]) + "");
                    result.add("failedInputJpbs InputTime##" + String.valueOf(ob[18]) + "");
                    result.add("failedInputJobs OutputTime##" + String.valueOf(ob[19]) + "");
                    result.add("failedOutput Jobs##" + String.valueOf(ob[20]) + "");
                    result.add("failedOutputJobs WaitingTime##" + String.valueOf(ob[21]) + "");
                    result.add("failedOutputJobs ExecutionTime##" + String.valueOf(ob[22]) + "");
                    result.add("failedOutputJobs InputTime##" + String.valueOf(ob[23]) + "");
                    result.add("failedOutputJobs OutputTime##" + String.valueOf(ob[24]) + "");
                    result.add("failedStalled Jobs##" + String.valueOf(ob[25]) + "");
                    result.add("failedStalledJobs WaitingTime##" + String.valueOf(ob[26]) + "");
                    result.add("failedStalledJobs ExecutionTime##" + String.valueOf(ob[27]) + "");
                    result.add("failedStalledJobs InputTime##" + String.valueOf(ob[28]) + "");
                    result.add("failedStalledJobs OutputTime##" + String.valueOf(ob[29]) + "");
                }else{
                    logger.error("getBySimulationID: Not enough data : {}", ob.length);
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
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Workflow.class);
            criteria.add(Restrictions.in("id", workflowsId));

            ProjectionList p = Projections.projectionList();
            p.add(Projections.groupProperty("username"));
            p.add(Projections.property("username"));
            p.add(Projections.alias(Projections.count("status"), "nbWfls"));
   
            //p.add(Projections.count("status"));
            criteria.setProjection(p);
            criteria.addOrder(Order.desc("nbWfls"));
            List l = criteria.list();
            session.getTransaction().commit();
            session.close();

            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object ob[] = (Object[]) it.next();
                if (ob[0] != null && ob[1] != null) {
                    result.add(String.valueOf(ob[0]) + "##" + String.valueOf(ob[2]));
                }
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
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Workflow.class);
            criteria.add(Restrictions.in("id", workflowsId));

            ProjectionList p = Projections.projectionList();
            p.add(Projections.groupProperty("application"));
            p.add(Projections.property("application"));
            p.add(Projections.alias(Projections.count("status"), "nbWfls"));
            criteria.setProjection(p);
            criteria.setProjection(p);
            criteria.addOrder(Order.desc("nbWfls"));
            List l = criteria.list();

            session.getTransaction().commit();
            session.close();

            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object ob[] = (Object[]) it.next();
                if (ob[0] != null && ob[1] != null) {
                    result.add(String.valueOf(ob[0]) + "##" + String.valueOf(ob[2]));
                }

            }
            return result;
        } catch (HibernateException ex) {
            logger.error("Error getting applications for {}", workflowsId, ex);
            throw new WorkflowsDBDAOException(ex);
        }
        //System.out.println("getApplications, first result is " + result.get(0).toString());
    }

    @Override
    public List<String> getClasses(List<String> workflowsId) throws WorkflowsDBDAOException {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Workflow.class);
            criteria.add(Restrictions.in("id", workflowsId));
            criteria.setProjection(Projections.distinct(Projections.property("applicationClass")));
            @SuppressWarnings("unchecked")
            List<String> result = (List<String>) criteria.list();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (HibernateException ex) {
            logger.error("Error getting classes for {}", workflowsId, ex);
            throw new WorkflowsDBDAOException(ex);
        }

    }
}
