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
package fr.insalyon.creatis.vip.api.controller.data;

import fr.insalyon.creatis.vip.api.business.DataApiBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.ExistsApiResponse;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.UploadData;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by abonnet on 1/13/17.
 */
@RestController
@RequestMapping("path")
public class DataController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataApiBusiness dataApiBusiness;

    // although the controller is a singleton, these are proxies that always point on the current request
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public DataController(DataApiBusiness dataApiBusiness,
                          HttpServletRequest httpServletRequest) {
        this.dataApiBusiness = dataApiBusiness;
        this.httpServletRequest = httpServletRequest;
    }

    @RequestMapping(path = "/**", params = "action=properties")
    public PathProperties getPathProperties() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "getPathProperties", currentUser().getEmail(), completePath);
        // business call
        return dataApiBusiness.getPathProperties(completePath);
    }

    @RequestMapping(path = "/**", params = "action=exists")
    public ExistsApiResponse doesPathExists() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "doesPathExists", currentUser().getEmail(), completePath);
            // business call
        return new ExistsApiResponse(dataApiBusiness.doesFileExist(completePath));
    }

    @RequestMapping(path = "/**", params = "action=list")
    public List<PathProperties> listDirectory() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "listDirectory", currentUser().getEmail(), completePath);
        // business call
        return dataApiBusiness.listDirectory(completePath);
    }

    @RequestMapping(path = "/**", params = "action=md5")
    public void getFileMD5() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "getFileMD5", currentUser().getEmail(), completePath);
        // business call
        // TODO implement that
        logger.error("Unsupported getFileMD5 call");
        throw new ApiException("Not implemented");
    }

    @RequestMapping(path = "/**", params = "action=content")
    public ResponseEntity<FileSystemResource> downloadRawFile() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "downloadFile", currentUser().getEmail(), completePath);
        // business call
        File file = dataApiBusiness.getFile(completePath);
        FileSystemResource res = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        // TODO improve mime-type
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }

    @RequestMapping(path = "/**", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePath() throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "deletePath", currentUser().getEmail(), completePath);
        // business call
        dataApiBusiness.deletePath(completePath);
    }

    @RequestMapping(path = "/**", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadFile(InputStream requestInputStream) throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "uploadFile", currentUser().getEmail(), completePath);
        // business call
        dataApiBusiness.uploadRawFileFromInputStream(
                completePath, requestInputStream);
        // TODO : think about returning the PahtProperties of the created Path, to be informed of a filename change
    }

    @RequestMapping(path = "/**", method = RequestMethod.PUT, consumes = "application/carmin+json")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadCustomData(@RequestBody UploadData uploadData) throws ApiException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "uploadCustomData", currentUser().getEmail(), completePath);
        // business call
        dataApiBusiness.uploadCustomData(completePath, uploadData);
    }

    private String extractWildcardPath(HttpServletRequest request) {
        String prefixToSearch = "/rest/path/"; // TODO : parametize that
        String decodedUri = UriUtils.decode(request.getRequestURI(), "UTF-8");
        int index = decodedUri.indexOf(prefixToSearch);
        // "-1" at the end to keep the beginning slash
        return decodedUri.substring(index + prefixToSearch.length() - 1);
    }
}

