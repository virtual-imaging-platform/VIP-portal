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
package fr.insalyon.creatis.vip.datamanager.applet.upload;

import fr.insalyon.creatis.devtools.zip.FolderZipper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Rafael Silva
 */
public class UploadFilesBusiness extends Thread {

    private String sessionId;
    private Runnable beforeRunnable;
    private Runnable afterRunnable;
    private Runnable errorRunnable;
    private ProgressRunnable progressRunnable;
    private List<String> dataList;
    private String path;
    private boolean unzip;
    private boolean usePool;
    private String codebase;
    private boolean deleteDataList;
    private String result;
    private String zipFileName;

    public UploadFilesBusiness(String sessionId, Runnable beforeRunnable,
            Runnable afterRunnable, Runnable errorRunnable, ProgressRunnable progressRunnable,
            List<String> dataList, String path, boolean unzip, boolean usePool,
            String codebase, boolean deleteDataList) {

        this.sessionId = sessionId;
        this.beforeRunnable = beforeRunnable;
        this.afterRunnable = afterRunnable;
        this.errorRunnable = errorRunnable;
        this.progressRunnable = progressRunnable;
        this.dataList = dataList;
        this.path = path;
        this.unzip = unzip;
        this.usePool = usePool;
        this.codebase = codebase;
        this.deleteDataList = deleteDataList;
    }

    @Override
    public void run() {
        try {

            SwingUtilities.invokeAndWait(beforeRunnable);

            File fileToUpload = null;
            boolean single = true;

            if (dataList.size() == 1 && new File(dataList.get(0)).isFile()) {
                fileToUpload = new File(dataList.get(0));

            } else {

                String fileName = System.getProperty("java.io.tmpdir")
                        + "/file-" + System.nanoTime() + ".zip";
                FolderZipper.zipListOfData(dataList, fileName);
                fileToUpload = new File(fileName);
                zipFileName = fileToUpload.getName();
                single = false;
            }

            // Call Servlet
            URL servletURL = new URL(codebase + "/fr.insalyon.creatis.vip.portal.Main/uploadfilesservice");
            HttpURLConnection servletConnection = (HttpURLConnection) servletURL.openConnection();
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            servletConnection.setUseCaches(false);
            servletConnection.setDefaultUseCaches(false);
            servletConnection.setChunkedStreamingMode(4096);

            servletConnection.setRequestProperty("vip-cookie-session", sessionId);
            servletConnection.setRequestProperty("Content-Type", "application/octet-stream");
            servletConnection.setRequestProperty("Content-Length", Long.toString(fileToUpload.length()));

            servletConnection.setRequestProperty("path", path);
            servletConnection.setRequestProperty("fileName", fileToUpload.getName());
            servletConnection.setRequestProperty("single", single + "");
            servletConnection.setRequestProperty("unzip", unzip + "");
            servletConnection.setRequestProperty("pool", usePool + "");

            long fileSize = fileToUpload.length();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToUpload));
            OutputStream os = servletConnection.getOutputStream();

            try {
                byte[] buffer = new byte[4096];
                long done = 0;
                while (true) {
                    int bytes = bis.read(buffer);
                    if (bytes < 0) {
                        break;
                    }
                    done += bytes;
                    os.write(buffer, 0, bytes);
                    progressRunnable.setValue((int) (done * 100 / fileSize));
                    SwingUtilities.invokeAndWait(progressRunnable);
                }
                os.flush();

            } finally {
                os.close();
                bis.close();
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(servletConnection.getInputStream()));
            String operationID = null;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("id=")) {
                        operationID = line.split("=")[1];
                    }
                    System.out.println(line);
                }
            } finally {
                reader.close();
            }

            if (!single) {
                fileToUpload.delete();
            }

            if (deleteDataList) {
                for (String data : dataList) {
                    FileUtils.deleteQuietly(new File(data));
                }
            }

            result = operationID;
            SwingUtilities.invokeAndWait(afterRunnable);

        } catch (Exception ex) {
            result = ex.getMessage();
            SwingUtilities.invokeLater(errorRunnable);
        }
    }

    public String getResult() {
        return result;
    }

    public String getZipFileName() {
        return zipFileName;
    }
}
