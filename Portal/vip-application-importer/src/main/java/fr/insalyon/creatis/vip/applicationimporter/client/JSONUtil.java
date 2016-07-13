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
package fr.insalyon.creatis.vip.applicationimporter.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesInput;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesOutputFile;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesTool;

/**
 *
 * @author Tristan Glatard
 */
public class JSONUtil {

    public static BoutiquesTool parseBoutiquesTool(JSONObject jsonObject) throws ApplicationImporterException {

        BoutiquesTool bt = new BoutiquesTool();
        bt.setName(getPropertyAsString(jsonObject, "name"));
        bt.setToolVersion(getPropertyAsString(jsonObject, "tool-version"));
        bt.setDescription(getPropertyAsString(jsonObject, "description"));
        bt.setCommandLine(getPropertyAsString(jsonObject, "command-line"));
        bt.setDockerImage(getPropertyAsString(jsonObject, "docker-image"));
        bt.setDockerIndex(getPropertyAsString(jsonObject, "docker-index"));
        bt.setSchemaVersion(getPropertyAsString(jsonObject, "schema-version"));
        bt.setChallengerEmail(getPropertyAsString(jsonObject, "vip:miccai-challenger-email"));
        bt.setChallengerTeam(getPropertyAsString(jsonObject, "vip:miccai-challenge-team-name"));
        
        JSONArray inputJSONArray = getPropertyAsArray(jsonObject, "inputs");
        if (inputJSONArray != null) {
            for (int i = 0; i < inputJSONArray.size(); i++) {
                bt.getInputs().add(parseBoutiquesInput(inputJSONArray.get(i).isObject()));
            }
        }

        JSONArray outputJSONArray = JSONUtil.getPropertyAsArray(jsonObject, "output-files");
        if (outputJSONArray != null) {
            for (int i = 0; i < outputJSONArray.size(); i++) {
                bt.getOutputFiles().add(parseBoutiquesOutputFile(outputJSONArray.get(i).isObject()));
            }
        }
        return bt;
    }

    public static BoutiquesInput parseBoutiquesInput(JSONObject input) throws ApplicationImporterException {

        BoutiquesInput bi = new BoutiquesInput();
        bi.setId(getPropertyAsString(input, "id"));
        bi.setName(getPropertyAsString(input, "name"));
        bi.setType(getPropertyAsString(input, "type"));
        bi.setDescription(getPropertyAsString(input, "description", ""));
        bi.setCommandLineKey(getPropertyAsString(input, "command-line-key"));
        bi.setList(getPropertyAsBoolean(input, "list"));
        bi.setOptional(getPropertyAsBoolean(input, "optional"));
        bi.setCommandLineFlag(getPropertyAsString(input, "command-line-flag", ""));
        bi.setDefaultValue(getPropertyAsString(input, "default-value", ""));

        return bi;
    }

    public static BoutiquesOutputFile parseBoutiquesOutputFile(JSONObject outputFile) throws ApplicationImporterException {

        BoutiquesOutputFile bof = new BoutiquesOutputFile();

        bof.setId(getPropertyAsString(outputFile, "id"));
        bof.setName(getPropertyAsString(outputFile, "name"));
        bof.setDescription(getPropertyAsString(outputFile, "description"));
        bof.setCommandLineKey(getPropertyAsString(outputFile, "command-line-key"));
        bof.setPathTemplate(getPropertyAsString(outputFile, "path-template"));
        bof.setList(getPropertyAsBoolean(outputFile, "list"));
        bof.setOptional(getPropertyAsBoolean(outputFile, "optional"));

        String commandLineFlag = getPropertyAsString(outputFile, "command-line-flag");
        commandLineFlag = commandLineFlag == null ? "" : commandLineFlag;
        bof.setCommandLineFlag(commandLineFlag);

        return bof;
    }

    public static boolean getPropertyAsBoolean(JSONObject jo, String property) throws ApplicationImporterException {
        JSONValue value = jo.get(property);
        if (value == null) {
            return false;
        }
        if (value.isBoolean() == null) {
            throw new ApplicationImporterException(value.toString() + " is not a boolean value!");
        }
        return value.isBoolean().booleanValue();
    }

    public static JSONArray getPropertyAsArray(JSONObject jo, String property) throws ApplicationImporterException {
        JSONValue value = jo.get(property);
        if (value == null) {
            return null;
        }
        if (value.isArray() == null) {
            throw new ApplicationImporterException(value.toString() + " is not an array!");
        }
        return value.isArray();
    }

    public static String getPropertyAsString(JSONObject jo, String property, String valueIfAbsent) throws ApplicationImporterException {
        JSONValue value = jo.get(property);
        if (value == null) {
            return valueIfAbsent;
        }
        if (value.isString() == null) {
            return value.toString();
        }
        return value.isString().stringValue();
    }

    public static String getPropertyAsString(JSONObject jo, String property) throws ApplicationImporterException {
        return getPropertyAsString(jo, property, null);
    }

}
