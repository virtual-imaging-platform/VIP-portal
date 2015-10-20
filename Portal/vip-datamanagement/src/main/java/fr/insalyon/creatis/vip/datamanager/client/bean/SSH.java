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
package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author glatard, Nouha Boujelben
 */
public class SSH implements IsSerializable {

    private String email;
    private String name;
    private String user;
    private String host;
    private int port;
    private TransferType transferType;
    private String directory;
    private String status;
    private Date theEarliestNextSynchronistation;
    private long numberSynchronizationFailed;
    private boolean deleteFilesFromSource;

    public SSH() {
    }

    /**
     * Adds an SSH connection.
     *
     * @param email the user mail
     * @param name the connection name
     * @param user the ssh user name
     * @param host the ssh host name
     * @param port the ssh port
     * @param transferType specifying a type to transfer data
     * @param directory the full path of the synchronization folder
     * @param status the connection status
     * @param theEarliestNextSynchronistation
     * @param numberSynchronizationFailed the number of times the
     * synchronization failed
     * @param deleteFilesFromSource enbale delete files from source location
     */

    public SSH(String email, String name, String user, String host, int port, TransferType transferType, String directory, String status, Date theEarliestNextSynchronistation, long numberSynchronizationFailed, boolean deleteFilesFromSource) {
        this.email = email;
        this.name = name;
        this.user = user;
        this.host = host;
        this.port = port;
        this.transferType = transferType;
        this.directory = directory;
        this.status = status;
        this.theEarliestNextSynchronistation = theEarliestNextSynchronistation;
        this.numberSynchronizationFailed = numberSynchronizationFailed;
        this.deleteFilesFromSource = deleteFilesFromSource;
    }

    public SSH(String email, String name, String user, String host, int port, TransferType transferType, String directory, String status, boolean deleteFilesFromSource) {
        this.email = email;
        this.name = name;
        this.user = user;
        this.host = host;
        this.port = port;
        this.transferType = transferType;
        this.directory = directory;
        this.status = status;
        this.deleteFilesFromSource = deleteFilesFromSource;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDirectory() {
        return directory;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public String getStatus() {
        return status;
    }

    public Date getTheEarliestNextSynchronistation() {
        return theEarliestNextSynchronistation;
    }

    public void setTheEarliestNextSynchronistation(Date theEarliestNextSynchronistation) {
        this.theEarliestNextSynchronistation = theEarliestNextSynchronistation;
    }

    public String getEmail() {
        return email;
    }

    public long getNumberSynchronizationFailed() {
        return numberSynchronizationFailed;
    }

    public void setNumberSynchronizationFailed(long numberSynchronizationFailed) {
        this.numberSynchronizationFailed = numberSynchronizationFailed;
    }

    public boolean isDeleteFilesFromSource() {
        return deleteFilesFromSource;
    }

    public void setDeleteFilesFromSource(boolean deleteFilesFromSource) {
        this.deleteFilesFromSource = deleteFilesFromSource;
    }

}
