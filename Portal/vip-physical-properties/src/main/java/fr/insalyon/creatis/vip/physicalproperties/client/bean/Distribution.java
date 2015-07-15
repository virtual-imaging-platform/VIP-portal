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
package fr.insalyon.creatis.vip.physicalproperties.client.bean;

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
