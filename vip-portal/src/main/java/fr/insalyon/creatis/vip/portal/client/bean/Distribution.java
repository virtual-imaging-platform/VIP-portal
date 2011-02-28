/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtext.client.widgets.MessageBox;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glatard
 */
public class Distribution implements IsSerializable {

    private String name;
    private String expression;
    private List<DistributionParameter> parameters;

    public Distribution() {
         this.parameters = new ArrayList<DistributionParameter>();
    }

    public Distribution(String name, String expression, List<DistributionParameter> parameters) {
        
        this.parameters = new ArrayList<DistributionParameter>();
        
        this.name = name;
        this.expression = expression;
        for (DistributionParameter p : parameters) {
            this.parameters.add(p);
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistributionParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<DistributionParameter> params) {

            MessageBox.alert("here");
        this.parameters.clear();
        for (DistributionParameter p : params) {
            this.parameters.add(p);
        }
        MessageBox.alert("there");
    }
  }
