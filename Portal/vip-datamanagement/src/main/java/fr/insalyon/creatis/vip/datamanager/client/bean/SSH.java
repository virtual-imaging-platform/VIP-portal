/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

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
    private TransfertType transfertType;
    private String directory;
    private String status;
    private long numberSynchronizationFailed;
    private boolean deleteFilesFromSource;

    public SSH() {
    }

    public SSH(String email, String name, String user, String host, int port, TransfertType transfertType, String directory, String status, long numberSynchronizationFailed, boolean deleteFilesFromSource) {
        this.email = email;
        this.name = name;
        this.user = user;
        this.host = host;
        this.port = port;
        this.transfertType = transfertType;
        this.directory = directory;
        this.status = status;
        this.numberSynchronizationFailed = numberSynchronizationFailed;
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

    public TransfertType getTransfertType() {
        return transfertType;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public long getNumberSynchronizationFailes() {
        return numberSynchronizationFailed;
    }

    public void setNumberSynchronizationFailes(long numberSynchronizationFailed) {
        this.numberSynchronizationFailed = numberSynchronizationFailed;
    }

    public boolean isDeleteFilesFromSource() {
        return deleteFilesFromSource;
    }

    public void setDeleteFilesFromSource(boolean deleteFilesFromSource) {
        this.deleteFilesFromSource = deleteFilesFromSource;
    }

}
