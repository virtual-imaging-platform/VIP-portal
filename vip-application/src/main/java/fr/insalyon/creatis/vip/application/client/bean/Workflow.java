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
package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Silva
 */
public class Workflow implements IsSerializable {

    private String application;
    private String workflowID;
    private String userName;
    private String date;
    private String majorStatus;
    private String minorStatus;

    public Workflow() {
    }

    public Workflow(String application, String workflowID, String userName, String date) {
        this.application = application;
        this.workflowID = workflowID;
        this.userName = userName;
        this.date = date;
    }

    public Workflow(String application, String workflowID, String userName, String date, String majorStatus, String minorStatus) {
        this.application = application;
        this.workflowID = workflowID;
        this.userName = userName;
        this.date = date;
        this.majorStatus = majorStatus;
        this.minorStatus = minorStatus;
    }

    public String getApplication() {
        return application;
    }

    public String getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }

    public String getWorkflowID() {
        return workflowID;
    }

    public String getMajorStatus() {
        return majorStatus;
    }

    public void setMajorStatus(String majorStatus) {
        this.majorStatus = majorStatus;
    }

    public String getMinorStatus() {
        return minorStatus;
    }

    @Override
    public String toString() {
        return application + "\n" + workflowID + "\n" + userName + "\n" + date;
    }
}
