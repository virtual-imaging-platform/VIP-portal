/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.gatelab.server.rpc;

import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.view.GateLabException;
import fr.insalyon.creatis.vip.gatelab.server.business.GateLabBusiness;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva, Ibrahim Kallel
 */
public class GateLabServiceImpl extends AbstractRemoteServiceServlet implements GateLabService {

    private final static Logger logger = Logger.getLogger(GateLabServiceImpl.class);
    private GateLabBusiness gatelabBusiness;
    private ClassBusiness classBusiness;

    public GateLabServiceImpl() {

        gatelabBusiness = new GateLabBusiness();
        classBusiness = new ClassBusiness();
    }

    @Override
    public Map<String, String> getGatelabWorkflowInputs(String simulationID) throws GateLabException {

        try {
            return gatelabBusiness.getGatelabWorkflowInputs(simulationID, getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new GateLabException(ex);
        } catch (BusinessException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public long getNumberParticles(String simulationID) throws GateLabException {

        try {
            return gatelabBusiness.getNumberParticles(simulationID);

        } catch (BusinessException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public void StopWorkflowSimulation(String simulationID) throws GateLabException {

        try {
            trace(logger, "Stopping GateLab simulation: " + simulationID);
            gatelabBusiness.StopWorkflowSimulation(simulationID);

        } catch (CoreException ex) {
            throw new GateLabException(ex);
        } catch (BusinessException ex) {
            throw new GateLabException(ex);
        }
    }

    @Override
    public void reportProblem(String message) throws GateLabException {

        try {
            trace(logger, "Reporting simulation launch problem.");
            gatelabBusiness.reportProblem(getSessionUser().getEmail(), message);

        } catch (CoreException ex) {
            throw new GateLabException(ex);
        } catch (BusinessException ex) {
            throw new GateLabException(ex);
        }
    }
}
