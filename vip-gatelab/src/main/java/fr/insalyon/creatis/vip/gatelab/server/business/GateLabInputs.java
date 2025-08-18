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

import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
@Component
@Scope("prototype")
public class GateLabInputs {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, String> inputsMap;
    private String inputfile;

    private Server server;
    private LfcPathsBusiness lfcPathsBusiness;
    private String workflowID;
    private GateLabInputsParser gateLabInputsParser;

    @Autowired
    public final void setServer(Server server) {
        this.server = server;
    }

    @Autowired
    public final void setLfcPathsBusiness(fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness lfcPathsBusiness) {
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    /* GateLabInputsParser is also prototype based */
    @Autowired
    public void setGateLabInputsParser(GateLabInputsParser gateLabInputsParser) {
        this.gateLabInputsParser = gateLabInputsParser;
    }

    public GateLabInputs(String workflowID) {
        this.workflowID = workflowID;
    }

    @PostConstruct
    public final void init() {
        inputsMap = new HashMap<String, String>();
        inputfile = server.getWorkflowsPath() + "/" + workflowID + "/inputs.json";
        inputsMap = gateLabInputsParser.parse(inputfile);
    }

    /**
     *
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public Map<String, String> getWorkflowInputs(String currentUserFolder)
            throws BusinessException {

        String gateInput = inputsMap.get("gateInput");
        String numberOfJobs = inputsMap.get("numberOfJobs");
        String macfileName = inputsMap.get("macfileName");
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("gateInput", gateInput);
        inputMap.put("numberOfJobs", numberOfJobs);
        inputMap.put("macfileName", macfileName);

        return inputMap;

    }
}
