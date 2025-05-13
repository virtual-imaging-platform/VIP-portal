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
package fr.insalyon.creatis.vip.application.client.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class AppVersion implements IsSerializable {

    private String applicationName;
    private String version;
    private String descriptor;
    private String doi;
    private boolean visible;
    private List<String> resources;
    private List<String> tags;
    private Map<String, String> settings;

    public AppVersion() {}

    public AppVersion(String applicationName, String version, String descriptor,
                      Map<String,String> settings, boolean visible) {
        this.applicationName = applicationName;
        this.version = version;
        this.descriptor = descriptor;
        this.visible = visible;
        this.resources = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.settings = settings;
    }

    public AppVersion(String applicationName, String version, String descriptor, boolean visible) {
        this(applicationName, version, descriptor, new HashMap<>(), visible);
    }

    public AppVersion(String applicationName, String version, String descriptor,
                      String doi, Map<String,String> settings, boolean visible) {
        this(applicationName, version, descriptor, settings, visible);
        this.doi = doi;
    }

    public AppVersion(String applicationName, String version, String descriptor,
                      String doi, boolean visible, List<String> resources, List<String> tags) {
        this(applicationName, version, descriptor, visible);
        this.doi = doi;
        this.resources = resources;
        this.tags = tags;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getVersion() {
        return version;
    }

    public String getDescriptor() { return descriptor; }

    public String getDoi() {
        return doi;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public String getSettingsAsString() {
        return settings.entrySet().stream()
            .map((e) -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(", "));
    }

    public boolean isVisible() {
        return visible;
    }

    public List<String> getResources() {
        return resources;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
