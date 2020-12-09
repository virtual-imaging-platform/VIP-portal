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
package fr.insalyon.creatis.vip.applicationimporter.server.business;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class TargzUtils {

    private static final Logger logger = LoggerFactory.getLogger(TargzUtils.class);

    public void createTargz(List<File> pathIn, String pathOut) throws BusinessException {
        try {

            FileOutputStream fos = new FileOutputStream(pathOut);
            TarArchiveOutputStream tos = new TarArchiveOutputStream(
                    new GZIPOutputStream(new BufferedOutputStream(fos)));
            for (File entry : pathIn) {
                addFileToTarGz(tos, entry, null);
            }
            tos.finish();
            tos.close();
        } catch (IOException ex) {
            logger.error("Error creating targz {} from {}", pathOut, pathIn, ex);
            throw new BusinessException(ex);
        }
    }

    private void addFileToTarGz(TarArchiveOutputStream tOut, File f, String dir) throws BusinessException {
        try {
            TarArchiveEntry tarEntry;
            if (dir == null) {
                tarEntry = new TarArchiveEntry(f, f.getName());
            } else {
                tarEntry = new TarArchiveEntry(f, dir + "/" + f.getName());
            }
            tOut.putArchiveEntry(tarEntry);
            if (!f.isDirectory()) {
                FileInputStream in = new FileInputStream(f);
                IOUtils.copy(in, tOut);
                tOut.closeArchiveEntry();
                in.close();
            } else {
                tOut.closeArchiveEntry();
                String name = f.getName();
                File[] children = f.listFiles();
                if (children != null) {
                    for (File child : children) {
                        
                        addFileToTarGz(tOut, new File(child.getParent() + "/" + child.getName()), name);
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Error adding file {} to targz", f, ex);
            throw new BusinessException(ex);
        }
    }
}
