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
package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.server.model.boutiques.BoutiquesDescriptor;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.WRONG_APPLICATION_DESCRIPTOR;

/**
 * Created by abonnet on 2/21/19.
 */
@Service
@Transactional
public class BoutiquesBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private DataManagerBusiness dataManagerBusiness;
    private ApplicationBusiness applicationBusiness;
    private ObjectMapper objectMapper;

    @Autowired
    public BoutiquesBusiness(Server server, DataManagerBusiness dataManagerBusiness,
                             ApplicationBusiness applicationBusiness, ObjectMapper objectMapper) {
        this.server = server;
        this.dataManagerBusiness = dataManagerBusiness;
        this.applicationBusiness = applicationBusiness;
        this.objectMapper = objectMapper;
    }

    public String publishVersion(User user, String applicationName, String version)
            throws BusinessException {

        // fetch json file
        String jsonLfn = getJsonLfn(applicationName, version);
        String localFile = dataManagerBusiness.getRemoteFile(user, jsonLfn);

        // TODO : verify it has an author (refactor boutique parser from application-importer

        // call publish command
        String command = "FILE=" + localFile + "; " + server.getPublicationCommandLine();
        List<String> output = runCommandAndFailOnError(command);

        // get the doi
        // There should be only one line with the DOI
        String doi = getDoiFromPublishOutput(output);

        // save the doi in database
        saveDoiForVersion(doi, applicationName, version);

        return doi;
    }

    public void validateBoutiqueFile(String localPath) throws BusinessException {
        // call validate command
        String command = "bosh validate " + localPath;
        try {
            // if no exception : the command was  successful
            runCommand(command);
        } catch (CommandErrorException e) {
            // if there's an error, only keep the first line because the output can be very long
            // and the first line contains the json validation error message
            String firstLine = e.getCout().isEmpty() ? "< No Information> " : e.getCout().get(0);
            throw new BusinessException("Boutiques file not valid : " + firstLine);
        }
    }

    private String getJsonLfn(String applicationName, String applicationVersion)
            throws BusinessException {
        AppVersion appVersion = applicationBusiness.getVersion(
            applicationName, applicationVersion);
        if (appVersion.getJsonLfn() == null) {
            logger.error("No json lfn for this application : {} / {}", applicationName, applicationVersion);
            throw new BusinessException("There is no json lfn for this application version.");
        }
        return appVersion.getJsonLfn();
    }

    public String getApplicationDescriptorString(
            User user, String applicationName, String applicationVersion)
            throws BusinessException {
        String descriptorLfn = getJsonLfn(applicationName, applicationVersion);
        try {
            String localFilePath =
                    dataManagerBusiness.getRemoteFile(user, descriptorLfn);
            return new Scanner(new File(localFilePath)).useDelimiter("\\Z").next();
        } catch (IOException ex) {
            logger.error("Error reading boutiques file {}", descriptorLfn, ex);
            throw new BusinessException(ex);
        }
    }

    public BoutiquesDescriptor parseBoutiquesFile(File boutiquesFile) throws BusinessException {
        try {
            return objectMapper.readValue(boutiquesFile, BoutiquesDescriptor.class);
        } catch (IOException e) {
            logger.error("Error reading {} file for boutiques parsing", boutiquesFile, e);
            throw new BusinessException("Error reading boutiques file", e);
        }
    }

    private void saveDoiForVersion(
            String doi, String applicationName, String applicationVersion)
            throws BusinessException {

        applicationBusiness.updateDoiForVersion(
            doi, applicationName, applicationVersion);
    }

    private String getDoiFromPublishOutput(List<String> publishOutput) throws BusinessException {
        if (publishOutput.size() != 1) {
            logger.error("Wrong publication output, there should be only one line : {}",
                    String.join("\n", publishOutput));
            throw new BusinessException("Wrong publication output.");
        }
        return publishOutput.get(0);
    }

    private class CommandErrorException extends Exception {

        private List<String> cout;

        public CommandErrorException(List<String> cout) {
            this.cout = cout;
        }

        public List<String> getCout() {
            return cout;
        }
    }


    private List<String> runCommandAndFailOnError(String command) throws BusinessException {
        try {
            return runCommand(command);
        } catch (CommandErrorException e) {
            throw new BusinessException("Command {" + command + "} failed : " + String.join("\n", e.getCout()));
        }
    }

    private List<String> runCommand(String command) throws CommandErrorException, BusinessException {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
        builder.redirectErrorStream(true);
        Process process = null;
        List<String> cout = new ArrayList<>();

        try {
            logger.info("Executing command : " + command);
            process = builder.start();
            BufferedReader r = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = r.readLine()) != null) {
                cout.add(s);
            }
            process.waitFor();
            closeProcess(process);
        } catch (IOException | InterruptedException e) {
            logger.error("Unexpected error in a boutiques command : {}",
                    String.join("\n", cout), e);
            throw new BusinessException("Unexpected error in a boutiques command", e);
        } finally {
            closeProcess(process);
        }

        if (process.exitValue() != 0) {
            logger.error("Command failed : {}",
                    String.join("\n", cout));
            throw new CommandErrorException(cout);
        }
        process = null;
        return cout;
    }

    private void closeProcess(Process process) {
        if (process == null) return;
        close(process.getOutputStream());
        close(process.getInputStream());
        close(process.getErrorStream());
        process.destroy();
    }

    private void close(Closeable c) {

        if (c != null) {
            try {
                c.close();
            } catch (IOException ex) {
                logger.error("Error closing {}", c);
            }
        }
    }
}
