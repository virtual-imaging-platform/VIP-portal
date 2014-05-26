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
package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class AppVersion implements IsSerializable {

    private String applicationName;
    private String version;
    private String lfn;
    private String owner;
    private boolean visible;
    private String fullName;

    public AppVersion() {
    }

    public AppVersion(String lfn) {
        this.lfn = lfn;
    }

    public AppVersion(String applicationName, String version, String lfn, boolean visible) {

        this.applicationName = applicationName;
        this.version = version;
        this.lfn = lfn;
        this.visible = visible;


    }

    public AppVersion(String applicationName, String version, String lfn, String owner, boolean visible) {

        this.applicationName = applicationName;
        this.version = version;
        this.lfn = lfn;
        this.owner = owner;
        this.visible = visible;

    }

    public AppVersion(String applicationName, String version, String lfn, String owner, String fullName, boolean visible) {

        this.applicationName = applicationName;
        this.version = version;
        this.lfn = lfn;
        this.owner = owner;
        this.visible = visible;
        this.fullName = fullName;

    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getVersion() {
        return version;
    }

    public String getLfn() {
        return lfn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
