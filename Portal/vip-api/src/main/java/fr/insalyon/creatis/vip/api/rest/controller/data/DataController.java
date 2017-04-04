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
package fr.insalyon.creatis.vip.api.rest.controller.data;

import fr.insalyon.creatis.vip.api.business.*;
import fr.insalyon.creatis.vip.api.rest.RestApiBusiness;
import fr.insalyon.creatis.vip.api.rest.controller.processing.ExecutionControler;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.bouncycastle.asn1.ua.DSTU4145NamedCurves.params;

/**
 * Created by abonnet on 1/13/17.
 */
@RestController
@RequestMapping("path")
public class DataController {

    public static final Logger logger = Logger.getLogger(DataController.class);

    @Autowired
    private RestApiBusiness restApiBusiness;

    @Autowired
    private DataApiBusiness dataApiBusiness;

    // although the controller is a singleton, these are proxies that always point on the current request
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(params = "uri")
    public Path getPath(@RequestParam String uri) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("getPath", uri);
        restApiBusiness.getApiContext(httpServletRequest, true);
        // business call
        return dataApiBusiness.getFileApiPath(uri);
    }

    @RequestMapping(path = "exists", params = "uri")
    public Boolean doesPathExists(@RequestParam String uri) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("doesPathExists", uri);
        restApiBusiness.getApiContext(httpServletRequest, true);
        // business call
        return dataApiBusiness.doesFileExist(uri);
    }

    @RequestMapping(method = RequestMethod.DELETE, params = "uri")
    public void deletePath(@RequestParam String uri) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("deletePath", uri);
        restApiBusiness.getApiContext(httpServletRequest, true);
        // business call
        dataApiBusiness.deletePath(uri);
    }

    @RequestMapping(path="listDirectory", params = "uri")
    public List<Path> listDirectory(@RequestParam String uri) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("listDirectory", uri);
        restApiBusiness.getApiContext(httpServletRequest, true);
        // business call
        return  dataApiBusiness.listDirectory(uri);
    }

    @RequestMapping(path="download", params="uri")
    public String downloadFile(@RequestParam String uri) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("downloadFile", uri);
        restApiBusiness.getApiContext(httpServletRequest, true);
        // business call
        return dataApiBusiness.getFileContent(uri);
    }

    @RequestMapping(path="upload", method = RequestMethod.PUT)
    public Path uploadPath(@RequestBody UploadData uploadData) throws ApiException {
        ApiUtils.methodInvocationLog("uploadPath", uploadData.getUri());
        restApiBusiness.getApiContext(httpServletRequest, true);
        return dataApiBusiness.uploadData(uploadData);
    }
}
