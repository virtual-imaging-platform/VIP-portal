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
package fr.insalyon.creatis.vip.provenance.server.business;

import fr.cnrs.i3s.neusemstore.core.utils.SemanticDataItem;
import fr.cnrs.i3s.neusemstore.core.utils.Util;
import fr.cnrs.i3s.neusemstore.provenance.ProvenanceManager;
import fr.cnrs.i3s.neusemstore.provenance.ProvenanceManagerFactory;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.provenance.client.bean.ProvenanceData;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ProvenanceManagerBusiness {

    private static Logger logger = Logger.getLogger(ProvenanceManagerBusiness.class);
    private static ProvenanceManagerBusiness instance;
    private ProvenanceManager provenanceManager;

    public synchronized static ProvenanceManagerBusiness getInstance() {
        if (instance == null) {
            instance = new ProvenanceManagerBusiness();
        }
        return instance;
    }

    private ProvenanceManagerBusiness() {
        try {
            ServerConfiguration conf = ServerConfiguration.getInstance();
            provenanceManager = ProvenanceManagerFactory.createOpmProvenanceManager(
                    conf.getProvenanceDBUser(),
                    conf.getProvenanceDBPass(),
                    conf.getProvenanceDBURL(),
                    Util.createJenaOPMOntology(), null);

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws BusinessException 
     */
    public List<ProvenanceData> getSimulationInputsAndOutputs(String simulationID) throws BusinessException {

        try {
            URI uri = null;
            for (SemanticDataItem s : provenanceManager.listWorkflowInvocations()) {
                if (s.getLabel().contains(simulationID)) {
                    uri = new URI(s.getUri());
                    break;
                }
            }
            if (uri != null) {
                List<ProvenanceData> list = new ArrayList<ProvenanceData>();

                for (SemanticDataItem s : provenanceManager.listInputForWorkflow(uri)) {
                    String name = s.getLabel().substring(s.getLabel().indexOf("#") + 1);
                    name = DataManagerUtil.parseRealDir(name);
                    list.add(new ProvenanceData(name, s.getUri(),
                            s.getTip(), ProvenanceData.Type.Input));
                }

                for (SemanticDataItem s : provenanceManager.listOutputForWorkflow(uri)) {
                    String name = s.getLabel().substring(s.getLabel().indexOf("#") + 1);
                    name = DataManagerUtil.parseRealDir(name);
                    list.add(new ProvenanceData(name, s.getUri(),
                            s.getTip(), ProvenanceData.Type.Output));
                }

                return list;

            } else {
                throw new BusinessException("Simulation '" + simulationID
                        + "' not found in provenance database.");
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (URISyntaxException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param inputUri
     * @return
     * @throws BusinessException 
     */
    public List<ProvenanceData> getDerivedData(String inputUri) throws BusinessException {

        try {
            List<ProvenanceData> list = new ArrayList<ProvenanceData>();
            URI uri = new URI(inputUri);

            for (SemanticDataItem s : provenanceManager.listDerivedData(uri)) {
                String name = s.getLabel().substring(s.getLabel().indexOf("#") + 1);
                name = DataManagerUtil.parseRealDir(name);
                list.add(new ProvenanceData(name, s.getUri(),
                        s.getTip(), ProvenanceData.Type.Derived));
            }

            return list;

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (URISyntaxException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param outputUri
     * @return
     * @throws BusinessException 
     */
    public List<ProvenanceData> getDataOrigins(String outputUri) throws BusinessException {

        try {
            List<ProvenanceData> list = new ArrayList<ProvenanceData>();
            URI uri = new URI(outputUri);

            for (SemanticDataItem s : provenanceManager.listOriginatingData(uri)) {
                String name = s.getLabel().substring(s.getLabel().indexOf("#") + 1);
                name = DataManagerUtil.parseRealDir(name);
                list.add(new ProvenanceData(name, s.getUri(),
                        s.getTip(), ProvenanceData.Type.Origin));
            }

            return list;

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (URISyntaxException ex) {
            throw new BusinessException(ex);
        }
    }
}
