package fr.insalyon.creatis.vip.datamanager.client.bean;

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
