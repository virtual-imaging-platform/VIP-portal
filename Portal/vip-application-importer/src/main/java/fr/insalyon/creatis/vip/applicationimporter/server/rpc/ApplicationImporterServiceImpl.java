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

import fr.insalyon.creatis.vip.applicationimporter.server.business.ApplicationImporterBusiness;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesTool;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

public class ApplicationImporterServiceImpl extends fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet
        implements ApplicationImporterService {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ApplicationImporterServiceImpl.class);

    @Override
    public String readFileAsString(String fileLFN) throws ApplicationImporterException {
        try {
            trace(logger, "Reading file "+fileLFN+" as string.");
            ApplicationImporterBusiness abi = new ApplicationImporterBusiness();
            return abi.readFileAsString(fileLFN, getSessionUser());
        } catch (CoreException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        } catch (BusinessException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        }
    }

    @Override
    public void createApplication(BoutiquesTool bt, String type, HashMap<String, BoutiquesTool> bts, boolean isRunOnGrid, boolean overwriteVersion, boolean challenge) throws ApplicationImporterException {
        try {
            trace(logger, "Creating application");
            ApplicationImporterBusiness abi = new ApplicationImporterBusiness();
            abi.createApplication(bt, type, bts, isRunOnGrid, overwriteVersion, getSessionUser(), challenge);
        } catch (CoreException | BusinessException ex) {
            logger.error(ex);
            throw new ApplicationImporterException(ex);
        } catch (JSONException ex) {
            Logger.getLogger(ApplicationImporterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
