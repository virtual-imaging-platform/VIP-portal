/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard tristan.glatard@creatis.insa-lyon.fr
 */
@XmlType(name = "Module")
public enum Module {
    Processing, Data, Management, Commercial
}
