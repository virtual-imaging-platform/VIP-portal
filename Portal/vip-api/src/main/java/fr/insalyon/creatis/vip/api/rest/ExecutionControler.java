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
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.business.ApiContext;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.business.ExecutionBusiness;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by abonnet on 7/13/16.
 */
@RestController
@RequestMapping("/execution")
public class ExecutionControler {

    public static final Logger logger = Logger.getLogger(ExecutionControler.class);

    // although the controler is a singleton, these are proxies that always point on the current request
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping("/{executionId}")
    @ResponseBody
    public void getExecution(@PathVariable String executionId) throws ApiException {
        ApiUtils.methodInvocationLog("getExecution", executionId);
        ApiUtils.throwIfNull(executionId, "Execution id");
        ApiContext apiContext = new RestApiBusiness().getApiContext(httpServletRequest, true);
        ExecutionBusiness eb = new ExecutionBusiness(apiContext);
        eb.checkIfUserCanAccessExecution(executionId);
        Execution e = eb.getExecution(executionId,false);
    }

}
