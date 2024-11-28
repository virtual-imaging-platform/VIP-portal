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
package fr.insalyon.creatis.vip.gatelab.server.business;

import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import fr.insalyon.creatis.vip.gatelab.client.view.GateLabException;
import fr.insalyon.creatis.vip.gatelab.server.dao.GateLabDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva, Ibrahim Kallel
 */
@Service
@Transactional
public class GateLabBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationBusiness applicationBusiness;
    private UserDAO userDAO;
    private EmailBusiness emailBusiness;

    @Autowired
    public GateLabBusiness(ApplicationBusiness applicationBusiness, UserDAO userDAO, EmailBusiness emailBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.userDAO = userDAO;
        this.emailBusiness = emailBusiness;
    }
    /*
        GateLabInputs is prototyped scoped as a new instance is needed
        at each use because each instance is linked to the parsing of a
        single gatelab simulation. It must be obtained through a lookup method.
    */
    @Lookup
    protected GateLabInputs getGateLabInputs(String workflowID) {
        // will be generated by spring to return a new instance each time
        return null;
    }

    /*
        GetGateLabDAO is prototyped scoped as a new instance is needed
        at each use because each instance is linked to a specific
        simulation/dbPath. It must be obtained through a lookup method
     */
    @Lookup
    protected GateLabDAO getGateLabDAO(String dbPath) {
        // will be generated by spring to return a new instance each time
        return null;
    }

    public List<String[]> getApplications() throws BusinessException {
        // try {
        //     classDAO.add(new AppClass(
        //             GateLabConstants.GATELAB_CLASS, new ArrayList<String>()));
        // } catch (DAOException ex) {
        //     if (!ex.getMessage().contains("A class named \"" + GateLabConstants.GATELAB_CLASS + "\" already exists")) {
        //         throw new BusinessException(ex);
        //     }
        // }
        // return applicationBusiness.getApplications(
        //     GateLabConstants.GATELAB_CLASS);
        return new ArrayList<>();
    }

    public Map<String, String> getGatelabWorkflowInputs(
            String workflowID, String currentUserFolder)
            throws BusinessException {

        try {
            GateLabInputs gateinputs = getGateLabInputs(workflowID);
            Map<String, String> inputMap =
                gateinputs.getWorkflowInputs(currentUserFolder);

            long nb = getGateLabDAO(workflowID).getNumberParticles();
            inputMap.put("runnedparticles", "" + nb);

            return inputMap;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public long getNumberParticles(String workflowID) throws BusinessException {

        try {
            return getGateLabDAO(workflowID).getNumberParticles();

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
            getGateLabDAO(workflowID).StopWorkflowSimulation();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @param message
     * @throws BusinessException
     */
    public void reportProblem(String email, String message) throws BusinessException {

        try {
            User user = userDAO.getUser(email);

            String adminsEmailContents = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear Supporters,</p>"
                    + "<p>The following user tried to submit a GATE-Lab simulation:</p>"
                    + "<p><b>First Name:</b> " + user.getFirstName() + "</p>"
                    + "<p><b>Last Name:</b> " + user.getLastName() + "</p>"
                    + "<p><b>Email:</b> " + user.getEmail() + "</p>"
                    + "<p>&nbsp;</p>"
                    + "<p><b>Error Message:</b></p>"
                    + "<p style=\"background-color: #F6F6F6\">" + message + "</p>"
                    + "<p>&nbsp;</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            for (String adminEmail : emailBusiness.getAdministratorsEmails()) {
                emailBusiness.sendEmail("[VIP Contact] GATE-Lab Error", adminsEmailContents,
                        new String[]{adminEmail}, false, user.getEmail());
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
