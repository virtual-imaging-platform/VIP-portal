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

/**
 *
 * @author Tristan Glatard
 */
public class BoutiquesInput implements IsSerializable {

    private String id;
    private String name;
    private String type;
    private String description;
    private String commandLineKey;
    private boolean list;
    private boolean optional;
    private String commandLineFlag;
    private String defaultValue;
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCommandLineKey() {
        return commandLineKey;
    }

    public boolean isList() {
        return list;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getCommandLineFlag() {
        return commandLineFlag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommandLineKey(String commandLineKey) {
        this.commandLineKey = commandLineKey;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setCommandLineFlag(String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    
    
}
