/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.applet.upload;

import fr.insalyon.creatis.devtools.zip.FolderZipper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Rafael Silva
 */
public class UploadFilesBusiness {

    public String uploadFiles(List<String> dataList, String user, String userdn,
            String proxy, String path, boolean unzip, boolean usePool,
            String codebase, boolean deleteDataList) throws BusinessException {

        try {
            File fileToUpload = null;
            boolean single = true;

            if (dataList.size() == 1 && new File(dataList.get(0)).isFile()) {
                fileToUpload = new File(dataList.get(0));

            } else {

                String fileName = System.getProperty("java.io.tmpdir")
                        + "/file-" + System.nanoTime() + ".zip";
                FolderZipper.zipListOfData(dataList, fileName);
                fileToUpload = new File(fileName);
                single = false;
            }

            // Call Servlet
            URL servletURL = new URL(codebase + "/fr.insalyon.creatis.vip.portal.Main/uploadfilesservice");
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            servletConnection.setUseCaches(false);
            servletConnection.setDefaultUseCaches(false);

            servletConnection.setRequestProperty("Content-Type", "application/octet-stream");
            servletConnection.setRequestProperty("Content-Length", Long.toString(fileToUpload.length()));

            servletConnection.setRequestProperty("user", user);
            servletConnection.setRequestProperty("userdn", userdn);
            servletConnection.setRequestProperty("proxy", proxy);
            servletConnection.setRequestProperty("path", path);
            servletConnection.setRequestProperty("fileName", fileToUpload.getName());
            servletConnection.setRequestProperty("single", single + "");
            servletConnection.setRequestProperty("unzip", unzip + "");
            servletConnection.setRequestProperty("pool", usePool + "");

            FileInputStream fis = new FileInputStream(fileToUpload);
            OutputStream os = servletConnection.getOutputStream();

            try {
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytes = fis.read(buffer);
                    if (bytes < 0) {
                        break;
                    }
                    os.write(buffer, 0, bytes);
                }
                os.flush();

            } finally {
                os.close();
                fis.close();
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(servletConnection.getInputStream()));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
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

            return fileToUpload.getName();

        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
    }
}
