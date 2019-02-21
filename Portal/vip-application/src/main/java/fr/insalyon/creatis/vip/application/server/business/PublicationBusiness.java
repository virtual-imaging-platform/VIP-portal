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

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by abonnet on 2/21/19.
 */
public class PublicationBusiness {

    private static final Logger logger = Logger.getLogger(PublicationBusiness.class);

    public String publishVersion(String applicationName, String version) throws BusinessException {
        // fetch json file

        // TODO : verify it has an author

        // call publish command

        // get the doi and return it
        String doi = "";
        return doi;
    }

    private List<String> runCommand(String command) throws BusinessException {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
        builder.redirectErrorStream(true);
        Process process = null;
        List<String> cout = new ArrayList<>();

        try {
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
            e.printStackTrace();
        } finally {
            closeProcess(process);
        }

        if (process.exitValue() != 0) {
            logger.error(
                    "Command failed : " + String.join("\n", cout));
            throw new BusinessException("Command " + command + "failed : " + String.join("\n", cout));
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
                // ignored
            }
        }
    }
}
