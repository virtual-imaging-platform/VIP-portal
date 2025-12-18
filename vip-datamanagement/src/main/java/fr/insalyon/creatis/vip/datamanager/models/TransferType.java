package fr.insalyon.creatis.vip.datamanager.models;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha Boujelben
 */
public enum TransferType implements IsSerializable {

    LFCToDevice,
    DeviceToLFC,
    Synchronization

}
