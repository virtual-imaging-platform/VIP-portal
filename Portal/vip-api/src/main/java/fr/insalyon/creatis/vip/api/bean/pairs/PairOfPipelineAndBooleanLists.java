/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean.pairs;

import fr.insalyon.creatis.vip.api.bean.Pipeline;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "PairOfPipelineAndBooleanLists")
public class PairOfPipelineAndBooleanLists {

    @XmlElement(name = "pipelines", required = true)
    public ArrayList<Pipeline> pipelines;
    @XmlElement(name = "canExecute", required = true)
    public ArrayList<Boolean> canExecute;
}
