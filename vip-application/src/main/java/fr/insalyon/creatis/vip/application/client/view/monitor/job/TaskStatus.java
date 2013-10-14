/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public enum TaskStatus {

    SUCCESSFULLY_SUBMITTED("#CC9933", "Submitted"),
    QUEUED("#DBA400", "Queued"),
    RUNNING("#8CC653", "Running"),
    COMPLETED("#287fd6", "Completed"),
    CANCELLED("#FF8575", "Cancelled"),
    CANCELLED_REPLICA("#287fd6", "Cancelled (Replica)"),
    ERROR("#d64949", "Failed"),
    STALLED("#1A767F", "Stalled"),
    REPLICATE("#8CC653", "Replicate"),
    KILL("#8CC653", "Kill"),
    KILL_REPLICA("#8CC653", "Kill"),
    RESCHEDULE("#8CC653", "Reschedule"),
    ERROR_HELD("#9CAB4C", "Held"),
    STALLED_HELD("#9CAB4C", "Held"),
    UNHOLD_ERROR("#9CAB4C", "Unhold"),
    UNHOLD_STALLED("#9CAB4C", "Unhold");
    //
    private String color;
    private String description;

    private TaskStatus(String color, String description) {

        this.color = color;
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if the status is a running state.
     *
     * @return
     */
    public boolean isRunningState() {

        if (this == SUCCESSFULLY_SUBMITTED
                || this == QUEUED
                || this == RUNNING
                || this == REPLICATE
                || this == KILL
                || this == KILL_REPLICA
                || this == RESCHEDULE) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the status is a completed state with outputs.
     *
     * @return
     */
    public boolean isCompletedStateWithOutputs() {

        if (this == COMPLETED
                || this == ERROR) {
            return true;
        }
        return false;
    }
}
