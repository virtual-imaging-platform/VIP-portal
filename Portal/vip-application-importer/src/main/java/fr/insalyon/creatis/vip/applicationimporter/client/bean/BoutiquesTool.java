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
package fr.insalyon.creatis.vip.applicationimporter.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

/**
 *
 * @author Tristan Glatard
 */
public class BoutiquesTool implements IsSerializable {

    private String applicationLFN;

    // Fields parsed from the JSON object.
    private String name;
    private String author;
    private String toolVersion;
    private String description;
    private String commandLine;
    private String dockerImage;
    private String dockerIndex;
    private String schemaVersion;
    private String challengerEmail;
    private String challengerTeam;
    private List<BoutiquesInput> inputs;
    private List<BoutiquesOutputFile> outputFiles;
    private Map<String,String> tags;
    private String jsonFile;

    public BoutiquesTool() {
        inputs = new ArrayList<BoutiquesInput>();
        outputFiles = new ArrayList<BoutiquesOutputFile>();
        tags = new HashMap<String,String>();
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public String getDockerIndex() {
        return dockerIndex;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getChallengerEmail() {
        return challengerEmail;
    }

    public String getChallengerTeam() {
        return challengerTeam;
    }

    public List<BoutiquesInput> getInputs() {
        return inputs;
    }

    public List<BoutiquesOutputFile> getOutputFiles() {
        return outputFiles;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }
    
    public void setApplicationLFN(String applicationLFN) {
        this.applicationLFN = applicationLFN;
    }

    public String getWrapperLFN() {
        return this.applicationLFN + "/bin/" + getName() + ".sh";
    }

    public String getWrapperName() {
        return getName() + ".sh";
    }

    public String getGASWLFN() {
        return this.applicationLFN + "/gasw/" + getName() + ".xml";
    }

    public String getGwendiaLFN() {
        return this.applicationLFN + "/workflow/" + getName() + ".gwendia";
    }
 
    public String getJsonLFN() {
        return this.applicationLFN + "/json/" + getName() + ".json";
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage;
    }

    public void setDockerIndex(String dockerIndex) {
        this.dockerIndex = dockerIndex;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setChallengerEmail(String challengerEmail) {
        this.challengerEmail = challengerEmail;
    }

    public void setChallengerTeam(String challengerTeam) {
        this.challengerTeam = challengerTeam;
    }

    public String getApplicationLFN() {
        return applicationLFN;
    }

    public void addTag(String key, String value) {
        tags.put(key, value);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public boolean hasNextInput(BoutiquesInput input) {
        return inputs.lastIndexOf(input) < (inputs.size()-1) ? true : false;
    }

}
