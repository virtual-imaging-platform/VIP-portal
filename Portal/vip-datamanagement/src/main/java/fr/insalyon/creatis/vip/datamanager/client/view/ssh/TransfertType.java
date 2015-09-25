/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.ssh;

import java.io.Serializable;

/**
 *
 * @author Nouha Boujelben
 */
public enum TransfertType implements Serializable{
    LFCToDevice,
    DeviceToLFC,
    Synchronization
    
}
