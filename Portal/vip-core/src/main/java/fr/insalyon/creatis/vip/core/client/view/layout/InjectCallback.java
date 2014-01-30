/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.core.client.view.layout;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public abstract class InjectCallback implements IsSerializable{
    public void InjectCallback(){};
    public abstract void afterInject();
}
