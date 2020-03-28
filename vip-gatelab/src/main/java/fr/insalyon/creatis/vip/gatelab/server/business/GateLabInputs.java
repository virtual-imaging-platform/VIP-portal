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

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class GateLabInputs {

    private final static Logger logger = Logger.getLogger(GateLabInputs.class);
    private Map<String, String> inputsMap;
    private String inputfile;

    public GateLabInputs(String workflowID) {

        inputsMap = new HashMap<String, String>();
        inputfile = Server.getInstance().getWorkflowsPath() + "/" + workflowID + "/input-m2.xml";
        GateLabInputsParser in = new GateLabInputsParser();
        inputsMap = in.parse(inputfile);
    }

    /**
     *
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public Map<String, String> getWorkflowInputs(
        String currentUserFolder, Connection connection)
        throws BusinessException {

        try {
            String input = inputsMap.get("GateInput");
            /*
             * int ind = input.lastIndexOf("/"); String inputlink =
             * input.substring(0, ind);
             *
             * String application_name = "unknown";
             *
             * if (input.indexOf(".zip") > 0) { application_name =
             * input.substring(ind + 1, input.indexOf(".zip"));
             *
             * } else if (input.indexOf(".tgz") > 0) { application_name =
             * input.substring(ind + 1, input.indexOf(".tgz"));
             *
             * } else if (input.indexOf(".tar.gz") > 0) { application_name =
             * input.substring(ind + 1, input.indexOf(".tar.gz"));
             *
             * } else if (input.indexOf(".zip") > 0) { application_name =
             * input.substring(ind + 1, input.indexOf(".zip")); }
             *
             * String outputlink = inputlink + "/output";
             */
            String release = inputsMap.get("GateRelease");
            String particles = inputsMap.get("NumberOfParticles");
            String simtype = inputsMap.get("ParallelizationType").equals("dyn") ?
                    "Dynamic" : "Static";
            String phaseSpace = inputsMap.get("phaseSpace");
            Map<String, String> inputMap = new HashMap<String, String>();
            inputMap.put(
                "inputlink",
                DataManagerUtil.parseRealDir(
                    input, currentUserFolder, connection));
            //inputMap.put("outputlink", DataManagerUtil.parseRealDir(outputlink));
            inputMap.put(
                "gate_version",
                DataManagerUtil.parseRealDir(
                    release, currentUserFolder, connection));
            inputMap.put("particles", particles);
            inputMap.put("simulation", simtype);
            inputMap.put("phaseSpace", phaseSpace);

            return inputMap;

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
}
