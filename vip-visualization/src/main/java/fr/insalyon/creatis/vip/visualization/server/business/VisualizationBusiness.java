package fr.insalyon.creatis.vip.visualization.server.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import fr.insalyon.creatis.vip.visualization.models.Image;
import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

@Service
@Transactional
public class VisualizationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAClient gridaClient;
    private DataManagerBusiness dataManagerBusiness;
    private LfcPathsBusiness lfcPathsBusiness;

    @Autowired
    public VisualizationBusiness(
            GRIDAClient gridaClient, LfcPathsBusiness lfcPathsBusiness,
            DataManagerBusiness dataManagerBusiness) {
        this.gridaClient = gridaClient;
        this.dataManagerBusiness = dataManagerBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public Image getImageSlicesURL(String imageFileName, String dir)
        throws VipException {

        File imageFile = new File(imageFileName);
        String imageDirName = imageFile.getParent() + "/"
            + imageFile.getName() + "-" + dir + "-slices";
        File imageDir = new File(imageDirName);

        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        String sliceZeroFileName = imageDirName + "/slice0.png";
        File sliceZero = new File(sliceZeroFileName);
        if (!sliceZero.exists()) {
            //split slices
            ProcessBuilder builder = new ProcessBuilder(
                "slice.sh", imageFileName, imageDirName, dir);
            builder.redirectErrorStream(true);
            try {
                builder.start();
                try {
                    // wait for the first slice to be produced but not for all
                    // slices ;)
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException ex) {
                    logger.error("slice.sh waiting interrupted", ex);
                    new File(imageDirName).delete();
                    throw new VipException(ex);
                }
            } catch (IOException ex) {
                logger.error("Error running slice.sh command for {}",
                        imageFileName, ex);
                new File(imageDirName).delete();
                throw new VipException(ex);
            }
        }
        //get z value
        ProcessBuilder builderZ =
            new ProcessBuilder("getz.sh", imageFileName, dir);
        builderZ.redirectErrorStream(true);
        String number = "";
        try {
            try {
                Process process = builderZ.start();
                process.waitFor();

                InputStream stdout = process.getInputStream();
                BufferedReader reader =
                    new BufferedReader(new InputStreamReader(stdout));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                    number += line;
                }
            } catch (InterruptedException ex) {
                logger.error("getz.sh waiting interrupted", ex);
                new File(imageDirName).delete();
                throw new VipException(ex);
            }

        } catch (IOException ex) {
            logger.error("Error running getz.sh command for {}",
                    imageFileName, ex);
            new File(imageDirName).delete();
            throw new VipException(ex);
        }

        logger.info("IMAGE DIR NAME IS " + imageDirName);
        return new Image(
            imageDirName,
            Integer.parseInt(number.trim()),
            imageDirName.substring(
                imageDirName.indexOf("/files/viewer")) + "/");
    }

    public VisualizationItem getVisualizationItemFromLFN(
            String lfn, User user) throws VipException {

        dataManagerBusiness.getRemoteFile(user, lfn);

        // Hack: if it is a .mhd file, download also the raw file with the same
        // name, testing multiple possible extensions.
        String rawFileExtension = "";
        if (lfn.endsWith(".mhd")) {
            rawFileExtension =
                    rawFileForMhdFile(lfn, user).orElse("");
        }

        return new VisualizationItem(lfn, rawFileExtension);
    }

    private Optional<String> rawFileForMhdFile(
            String lfn, User user) {

        String[] extensions = {".raw", ".zraw", ".raw.gz"};

        return java.util.Arrays.stream(extensions)
            .map(extension -> buildLfnName(user, lfn, extension))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(fe -> checkIfExists(fe.remotePath))
            .findFirst()
            .filter(fe -> downloadFile(user, fe.remotePath))
            .map(fe -> fe.extension);
    }

    private Optional<FilenameAndExtension> buildLfnName(
            User user, String lfn, String extension) {
        try {
            return Optional.of(new FilenameAndExtension(
                    lfcPathsBusiness.parseBaseDir(
                            user, lfn.replaceAll("\\.mhd$", extension)),
                    extension));
        } catch (DataManagerException dme) {
            logger.warn("Error while building lfn name with new extension: {}. Ignoring", lfn);
            return Optional.empty();
        }
    }

    private boolean checkIfExists(String filename) {
        try {
            return gridaClient.exist(filename);
        } catch (GRIDAClientException gce) {
            logger.warn("Error while grida checked existance of: {}", filename, gce);
            return false;
        }
    }

    private boolean downloadFile(User user, String remotePath) {
        try {
            dataManagerBusiness.getRemoteFile(user, remotePath);
            return true;
        } catch (VipException e) {
            logger.warn("Error while downloading file: {}", remotePath, e);
            return false;
        }
    }

    private static class FilenameAndExtension {
        public final String remotePath;
        public final String extension;
        public FilenameAndExtension(String remotePath, String extension) {
            this.remotePath = remotePath;
            this.extension = extension;
        }
    }
}
