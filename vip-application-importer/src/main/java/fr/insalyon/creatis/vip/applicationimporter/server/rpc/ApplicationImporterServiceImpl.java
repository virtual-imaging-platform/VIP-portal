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
package fr.insalyon.creatis.vip.applicationimporter.server.rpc;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.server.model.boutiques.BoutiquesDescriptor;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.server.business.ApplicationImporterBusiness;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationImporterServiceImpl extends fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet
        implements ApplicationImporterService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationImporterBusiness applicationImporterBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationImporterBusiness = getBean(ApplicationImporterBusiness.class);
    }

    @Override
    public String readAndValidateBoutiquesFile(String fileLFN) throws ApplicationImporterException {
        try {
            trace(logger, "Reading file "+fileLFN+" as string.");
            return applicationImporterBusiness.readAndValidationBoutiquesFile(
                    fileLFN, getSessionUser());
        } catch (CoreException | BusinessException ex) {
            throw new ApplicationImporterException(ex);
        }
    }

    @Override
    public void createApplication(BoutiquesApplication bt, boolean overwriteVersion,
            List<String> tags, List<String> resources)
            throws ApplicationImporterException {
        try {
            trace(logger, "Creating application");
            applicationImporterBusiness.createApplication(
                    bt, overwriteVersion, tags, resources, getSessionUser());
        } catch (CoreException | BusinessException ex) {
            throw new ApplicationImporterException(ex);
        }
    }

    @Override
    public String getApplicationImporterRootFolder() throws ApplicationImporterException {
        return server.getApplicationImporterRootFolder();
    }

    @Override
    public List<String> getApplicationImporterRequirements() throws ApplicationImporterException {
        return server.getApplicationImporterRequirements();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getBoutiquesTags(String boutiquesJsonFile) throws ApplicationImporterException {
        try {
            Map<String, String> map = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            BoutiquesDescriptor descriptor = objectMapper.readValue(boutiquesJsonFile, BoutiquesDescriptor.class);

            if (descriptor.getTags() != null) {
                descriptor.getTags().getAdditionalProperties().forEach((k, v) -> {
                    String valueString = (v instanceof List) 
                            ? String.join(",", (List<String>) v) 
                            : String.valueOf(v);
                    map.put(k, valueString);
                });
            }
            return map;
        } catch (JsonProcessingException e) {
            throw new ApplicationImporterException(e);
        }
    }
}
