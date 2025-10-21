package fr.insalyon.creatis.vip.application.server.business;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.boutiques.model.Custom;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;

@Service
@Transactional
public class BoutiquesBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private AppVersionBusiness appVersionBusiness;
    private ObjectMapper objectMapper;

    @Autowired
    public BoutiquesBusiness(Server server, ObjectMapper objectMapper, AppVersionBusiness appVersionBusiness) {
        this.server = server;
        this.objectMapper = objectMapper;
        this.appVersionBusiness = appVersionBusiness;
    }

    public String publishVersion(User user, String applicationName, String version)
            throws VipException {
        AppVersion appVersion = appVersionBusiness.getVersion(applicationName, version);

        // verify that the descriptor has an author
        BoutiquesDescriptor descriptor = parseBoutiquesString(appVersion.getDescriptor());
        String author = descriptor.getAuthor();
        if (author == null || author.isEmpty()) {
            logger.error("Can't publish an descriptor with no author");
            throw new VipException("Can't publish an descriptor with no author");
        }

        // The basename of the file used in bosh publish will be visible on zenodo,
        // so we want a "clean" name, and a unique location on the filesystem to avoid race conditions.
        Path tempDir, tempFile;
        try {
            // create a dedicated unique temporary directory
            tempDir = Files.createTempDirectory("VipPublish-");
            // create the descriptor filename inside this dir
            tempFile = tempDir.resolve(appVersion.getDescriptorFilename());
            // write the descriptor content to that file
            Files.write(tempFile, appVersion.getDescriptor().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("Failed creating temporary file for publish", e);
            throw new VipException("Failed creating temporary file for publish", e);
        }

        // do the publishing, and cleanup tempFile+tempDir afterward
        String doi;
        try {
            // call publish command
            String command = "FILE=" + tempFile + "; " + server.getPublicationCommandLine();
            List<String> output = runCommandAndFailOnError(command);

            // get the doi
            // There should be only one line with the DOI
            doi = getDoiFromPublishOutput(output);

            // save the doi in database
            saveDoiForVersion(doi, applicationName, version);
        } finally {
            // failure to clean up temp files is not fatal
            try {
                Files.delete(tempFile);
                Files.delete(tempDir);
            } catch (IOException e) {
                logger.warn("Failed deleting temporary file after publish:" + tempFile, e);
            }
        }
        return doi;
    }

    public void validateBoutiqueFile(String localPath) throws VipException {
        // check file size, 100 kiB max
        try {
            if (Files.size(Paths.get(localPath)) >= 100 * 1024) {
                throw new VipException("Boutiques file too large");
            }
        } catch (IOException e) {
            throw new VipException("Can't get boutiques file size", e);
        }
        // call validate command
        String command = "bosh validate " + localPath;
        try {
            // if no exception : the command was  successful
            runCommand(command);
        } catch (CommandErrorException e) {
            // if there's an error, only keep the first line because the output can be very long
            // and the first line contains the json validation error message
            String firstLine = e.getCout().isEmpty() ? "< No Information> " : e.getCout().get(0);
            throw new VipException("Boutiques file not valid : " + firstLine);
        }
    }

    public String getApplicationDescriptorString(String applicationName, String applicationVersion)
            throws VipException {
        AppVersion appVersion = appVersionBusiness.getVersion(applicationName, applicationVersion);
        return appVersion.getDescriptor();
    }

    public BoutiquesDescriptor parseBoutiquesFile(File boutiquesFile) throws VipException {
        try {
            return objectMapper.readValue(boutiquesFile, BoutiquesDescriptor.class);
        } catch (IOException e) {
            logger.error("Error reading {} file for boutiques parsing", boutiquesFile, e);
            throw new VipException("Error reading boutiques file", e);
        }
    }

    public BoutiquesDescriptor parseBoutiquesString(String descriptor) throws VipException {
        try {
            return objectMapper.readValue(descriptor, BoutiquesDescriptor.class);
        } catch (IOException e) {
            logger.error("Error parsing descriptor", e);
            throw new VipException("Error parsing descriptor", e);
        }
    }

    private void saveDoiForVersion(String doi, String applicationName, String applicationVersion) throws VipException {
        appVersionBusiness.updateDoiForVersion(doi, applicationName, applicationVersion);
    }

    private String getDoiFromPublishOutput(List<String> publishOutput) throws VipException {
        if (publishOutput.size() != 1) {
            logger.error("Wrong publication output, there should be only one line : {}",
                    String.join("\n", publishOutput));
            throw new VipException("Wrong publication output.");
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


    private List<String> runCommandAndFailOnError(String command) throws VipException {
        try {
            return runCommand(command);
        } catch (CommandErrorException e) {
            throw new VipException("Command {" + command + "} failed : " + String.join("\n", e.getCout()));
        }
    }

    private List<String> runCommand(String command) throws CommandErrorException, VipException {
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
            throw new VipException("Unexpected error in a boutiques command", e);
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

    @SuppressWarnings("unchecked")
    public Map<String, String> getOverriddenInputs(BoutiquesDescriptor descriptor) {
        final String customKeyName = "vip:overriddenInputs";
        Custom custom = descriptor.getCustom();
        if (custom == null) {
            return null;
        }
        Map<String, Object> customProperties = custom.getAdditionalProperties();
        if (!customProperties.containsKey(customKeyName)) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        Object overriddenInputs = customProperties.get(customKeyName);
        if (overriddenInputs instanceof Map) {
            Map<String, String> oi = (Map<String,String>)overriddenInputs;
            for (String key: oi.keySet()) {
                String value = oi.get(key);
                result.put(key, value);
            }
        }
        return result;
    }
}
