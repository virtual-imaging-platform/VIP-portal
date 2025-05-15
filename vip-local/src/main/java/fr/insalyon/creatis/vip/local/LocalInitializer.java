package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Class that initializes necessary stuff for a local vip instance on startup.
 * If necessary it will create missing folder (admin home folder, support group
 * folder, application root folder).
 *
 * And, if there are some "local.data.xxx" properties in vip-local.conf, it will
 * initialize some data : an engine, a class, an application and install an
 * application version.
 *
 * The vip-local.conf provided in main/resources includes the necessary to
 * initialize a working grep application.
 *
 */

@Component
@Profile("local")
@Transactional
public class LocalInitializer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${local.data.doInit:false}")
    private Boolean initData;
    @Value("${local.data.engine.name:localEngine}")
    private String engineName;
    @Value("${local.data.class.name:localClass}")
    private String className;
    @Value("${local.data.application.name}")
    private String applicationName;
    private String applicationVersionProperty = "local.data.application.version";
    private String applicationVersionGwendiaProperty = "local.data.application.gwendia";
    private String applicationVersionScriptProperty = "local.data.application.script";
    private String applicationVersionInputProperty = "local.data.application.input";

    private Environment environment;
    private ResourceLoader resourceLoader;
    private ConfigurationBusiness configurationBusiness;
    private Server server;
    private LFCBusiness lfcBusiness;
    private EngineBusiness engineBusiness;
    private ApplicationBusiness applicationBusiness;
    private AppVersionBusiness appVersionBusiness;
    private TransferPoolBusiness transferPoolBusiness;

    @Autowired
    public LocalInitializer(
            Environment environment, ResourceLoader resourceLoader,
            ConfigurationBusiness configurationBusiness, Server server,
            LFCBusiness lfcBusiness, EngineBusiness engineBusiness,
            ApplicationBusiness applicationBusiness,
            TransferPoolBusiness transferPoolBusiness, AppVersionBusiness appVersionBusiness) {
        this.environment = environment;
        this.resourceLoader = resourceLoader;
        this.configurationBusiness = configurationBusiness;
        this.server = server;
        this.lfcBusiness = lfcBusiness;
        this.engineBusiness = engineBusiness;
        this.applicationBusiness = applicationBusiness;
        this.transferPoolBusiness = transferPoolBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }


    @EventListener(ContextRefreshedEvent.class)
    @Order(100) // low priority
    public void initLocalStuff() throws BusinessException {
        logger.info("Initialising VIP for local use");
        initFolders();
        if (initData) {
            initData();
        }
    }

    private String getApplicationImporterRootFolder() {
        // this is a stub for the old "boutiques.application.rootFolder" parameter,
        // introduced to allow removing this parameter from vip.conf, while keeping vip-local mostly unchanged.
        return "/vip/Support (group)/Applications";
    }

    private void initFolders() throws BusinessException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());

        createFolderIfNecessary(admin,
                server.getDataManagerUsersHome() + "/" + admin.getFolder(),
                "Admin home folder");

        // applications root folder parent must exist
        String appRootFolder = getApplicationImporterRootFolder();
        String appRootFolderParent = Paths.get(appRootFolder).getParent().toString();
        if ( ! lfcBusiness.exists(admin, appRootFolderParent)) {
            logger.error("Application importer parent dir [{}] must exist", appRootFolderParent);
            throw new IllegalStateException("Application importer parend dir must exist");
        }
        createFolderIfNecessary(admin,
                appRootFolder,
                "Application importer folder");
    }

    private void createFolderIfNecessary(User admin, String folderLFN, String folderDescription) throws BusinessException {
        boolean doFolderExist = lfcBusiness.exists(admin, folderLFN);
        if (doFolderExist) {
            logger.info("{} [{}] already exist", folderDescription, folderLFN);
        } else {
            logger.info("Creating {} [{}]", folderDescription, folderLFN);
            Path folderPath = Paths.get(folderLFN);
            lfcBusiness.createDir(admin,
                    folderPath.getParent().toString(),
                    folderPath.getFileName().toString());
        }
    }

    private void initData() throws BusinessException {
        initLocalEngine();
        initApplication();
        initAppVersion();
    }

    private void initLocalEngine() throws BusinessException {
        boolean engineExist = engineBusiness.get().stream().anyMatch(
                engine -> engineName.equals(engine.getName())
        );
        if (engineExist) {
            logger.info("local engine [{}] already exist", engineName);
            return;
        }
        logger.info("adding local engine [{}]", engineName);
        Engine newEngine = new Engine(engineName, "localEndpoint", "enabled");
        engineBusiness.add(newEngine);
    }

    private void initApplication() throws BusinessException {
        if (applicationBusiness.getApplication(applicationName) != null) {
            logger.info("local application [{}] already exist", applicationName);
            return;
        }
        logger.info("adding application [{}]", applicationName);
        Application application = new Application(applicationName,"");
        applicationBusiness.add(application);
    }

    private void initAppVersion() throws BusinessException {

        String applicationVersion = environment.getProperty(applicationVersionProperty);
        String gwendiaLocation = environment.getProperty(applicationVersionGwendiaProperty);
        String scriptLocation = environment.getProperty(applicationVersionScriptProperty);

        // this is optional, do it only if all the information are provided

        if (applicationVersion == null && gwendiaLocation == null && scriptLocation == null) {
            logger.info("no application version information provided, not installing any");
            return;
        }

        if (applicationVersion == null || gwendiaLocation == null || scriptLocation == null) {
            logger.error("Cannot install an application version : information is missing (version, gwendia or path)");
            throw new IllegalStateException("Cannot install an application version : information is missing");
        }

        boolean hasVersion = appVersionBusiness.getVersions(applicationName)
                .stream().anyMatch(version -> applicationVersion.equals(version.getVersion()));
        if (hasVersion) {
            logger.info("Application version [{}/{}] already exist", applicationName, applicationVersion);
            return;
        }
        logger.info("installing application version [{}/{}]", applicationName, applicationVersion);

        User admin = configurationBusiness.getUser(server.getAdminEmail());

        // create app folders
        String appFolder = getApplicationImporterRootFolder() + "/" + applicationName;
        createFolderIfNecessary(admin, appFolder, applicationName + " application folder");
        String versionFolder = appFolder + "/v" + applicationVersion;
        createFolderIfNecessary(admin, versionFolder, applicationName + " application version folder");

        // add gwendia and script files
        transferPoolBusiness.uploadFile(admin, getPathFromLocation(gwendiaLocation), versionFolder);
        transferPoolBusiness.uploadFile(admin, getPathFromLocation(scriptLocation), versionFolder);

        // create AppVersion
        AppVersion appVersion = new AppVersion(applicationName, applicationVersion, "{}", true);
        appVersionBusiness.add(appVersion);

        logger.info("Application version [{}/{}] installed", applicationName, applicationVersion);

        // add input file
        String inputFileLocation = environment.getProperty(applicationVersionInputProperty);
        if (inputFileLocation == null) {
            logger.info("No input file given for application version");
            return;
        }
        logger.info("adding application input file to admin home");
        transferPoolBusiness.uploadFile(admin, getPathFromLocation(inputFileLocation), "/vip/Home");
    }

    private String getPathFromLocation(String location) throws BusinessException {
        Resource resource = resourceLoader.getResource(location);
        try {
            if (resource.isFile()) {
                return resource.getFile().toString();
            } else {
                // it is probably in a jar, copy it locally before
                String tmpdir = System.getProperty("java.io.tmpdir");
                Path destination = Paths.get(tmpdir, resource.getFilename());
                Files.copy(resource.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                return destination.toString();
            }
        } catch (IOException e) {
            logger.error("cannot get file from [{}] location", location, e);
            throw new BusinessException(e);
        }
    }

}
