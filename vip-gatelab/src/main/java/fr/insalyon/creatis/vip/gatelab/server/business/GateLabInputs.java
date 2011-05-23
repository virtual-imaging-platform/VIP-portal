/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.gatelab.server.business;

import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class GateLabInputs {

    private Map<String, String> inputsMap;
    private String inputfile;

    public GateLabInputs(String workflowID) {
        inputsMap = new HashMap<String, String>();
        inputfile = ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/input-m2.xml";
        GateLabInputsParser in = new GateLabInputsParser();
        inputsMap = in.parse(inputfile);
    }

    public Map<String, String> getWorkflowInputs() {

        String input = inputsMap.get("input_tgz");

        int ind = input.lastIndexOf("/");
        String inputlink = input.substring(0, ind);

        String application_name="unknown";

        if(input.indexOf(".zip")>0)
            application_name = input.substring(ind + 1, input.indexOf(".zip"));
        else
            if(input.indexOf(".tgz")>0)
                application_name = input.substring(ind + 1, input.indexOf(".tgz"));
            else
                if(input.indexOf(".tar.gz")>0)
                    application_name = input.substring(ind + 1, input.indexOf(".tar.gz"));
                else
                    if(input.indexOf(".zip")>0)
                        application_name = input.substring(ind + 1, input.indexOf(".zip"));


        String outputlink = inputlink + "/output";


        String release = inputsMap.get("fgate_release_tgz");
        ind = release.lastIndexOf("/") + 1;
        release = release.substring(ind, release.indexOf(".tar.gz"));


        String particles = inputsMap.get("nParticles");

        String simtype = inputsMap.get("wrapperType");
        if (simtype.equals("dyn")) {
            simtype = "Dynamic";
        } else {
            simtype = "Static";
        }

        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("application_name", application_name);
        inputMap.put("inputlink", DataManagerUtil.parseRealDir(inputlink));
        inputMap.put("outputlink", DataManagerUtil.parseRealDir(outputlink));
        inputMap.put("gate_version", release);
        inputMap.put("particles", particles);
        inputMap.put("simulation", simtype);

        return inputMap;

    }
}



         
        

    



    
    

  