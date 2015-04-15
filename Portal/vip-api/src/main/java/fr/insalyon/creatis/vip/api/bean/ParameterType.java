/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author Tristan Glatard
 */
@XmlType(name = "ParameterType")
public enum ParameterType {
    File, String, Boolean, Int64, Double, List
}
