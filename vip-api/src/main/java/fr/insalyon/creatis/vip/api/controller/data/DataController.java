package fr.insalyon.creatis.vip.api.controller.data;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import fr.insalyon.creatis.vip.api.business.DataApiBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.model.ExistsApiResponse;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.UploadData;
import fr.insalyon.creatis.vip.core.client.VipException;
import jakarta.servlet.http.HttpServletRequest;

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
    public PathProperties getPathProperties() throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "getPathProperties", currentUser().getEmail(), completePath);
        // business call
        return dataApiBusiness.getPathProperties(completePath);
    }

    @RequestMapping(path = "/**", params = "action=exists")
    public ExistsApiResponse doesPathExists() throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "doesPathExists", currentUser().getEmail(), completePath);
            // business call
        return new ExistsApiResponse(dataApiBusiness.doesFileExist(completePath));
    }

    @RequestMapping(path = "/**", params = "action=list")
    public List<PathProperties> listDirectory() throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "listDirectory", currentUser().getEmail(), completePath);
        // business call
        return dataApiBusiness.listDirectory(completePath);
    }

    @RequestMapping(path = "/**", params = "action=md5")
    public void getFileMD5() throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "getFileMD5", currentUser().getEmail(), completePath);
        // business call
        // TODO implement that
        logger.error("Unsupported getFileMD5 call");
        throw new VipException("Not implemented");
    }

    @RequestMapping(path = "/**", params = "action=content")
    public ResponseEntity<FileSystemResource> downloadRawFile() throws VipException {
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
    public void deletePath() throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "deletePath", currentUser().getEmail(), completePath);
        // business call
        dataApiBusiness.deletePath(completePath);
    }

    @RequestMapping(path = "/**", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadFile(InputStream requestInputStream) throws VipException {
        String completePath = extractWildcardPath(httpServletRequest);
        logMethodInvocation(logger, "uploadFile", currentUser().getEmail(), completePath);
        // business call
        dataApiBusiness.uploadRawFileFromInputStream(
                completePath, requestInputStream);
        // TODO : think about returning the PahtProperties of the created Path, to be informed of a filename change
    }

    @RequestMapping(path = "/**", method = RequestMethod.PUT, consumes = "application/carmin+json")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadCustomData(@RequestBody UploadData uploadData) throws VipException {
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

