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
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class LFCBusiness {

    private static final Logger logger = Logger.getLogger(LFCBusiness.class);

    /**
     * 
     * @param user
     * @param baseDir
     * @param refresh
     * @return
     * @throws BusinessException 
     */
    public List<Data> listDir(String user, String baseDir, boolean refresh) 
            throws BusinessException {

        try {
            List<GridData> list = CoreUtil.getGRIDAClient().getFolderData(
                    DataManagerUtil.parseBaseDir(user, baseDir), refresh);

            List<Data> dataList = new ArrayList<Data>();
            for (GridData data : list) {
                if (data.getType() == GridData.Type.Folder) {
                    dataList.add(new Data(data.getName(),
                            data.getType().name(), data.getPermissions()));

                } else {
                    dataList.add(new Data(data.getName(), data.getType().name(),
                            data.getLength(), data.getModificationDate(),
                            data.getReplicas(), data.getPermissions()));
                }
            }
            return dataList;

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param path
     * @throws BusinessException 
     */
    public void delete(String user, String path) throws BusinessException {

        try {
            CoreUtil.getGRIDAClient().delete(
                    DataManagerUtil.parseBaseDir(user, path));

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param paths
     * @throws BusinessException 
     */
    public void delete(String user, List<String> paths) throws BusinessException {

        try {
            List<String> parsedPaths = new ArrayList<String>();
            for (String path : paths) {
                try {
                    parsedPaths.add(DataManagerUtil.parseBaseDir(user, path));
                } catch (DataManagerException ex) {
                    logger.error(ex);
                }
            }
            CoreUtil.getGRIDAClient().delete(parsedPaths);

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param baseDir
     * @param name
     * @throws BusinessException 
     */
    public void createDir(String user, String baseDir, String name) throws BusinessException {

        try {
            CoreUtil.getGRIDAClient().createFolder(
                    DataManagerUtil.parseBaseDir(user, baseDir), name);

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param oldPath
     * @param newPath
     * @param extendPath
     * @throws BusinessException 
     */
    public void rename(String user, String oldPath, String newPath,
            boolean extendPath) throws BusinessException {

        try {
            CoreUtil.getGRIDAClient().rename(
                    DataManagerUtil.parseBaseDir(user, oldPath),
                    DataManagerUtil.parseBaseDir(user, newPath));

        } catch (GRIDAClientException ex) {
            if (ex.getMessage().contains("Can not rename/move") && extendPath) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat("-yyyy.MM.dd-HH.mm.ss");
                String newExtPath = newPath + sdf.format(new Date());
                rename(user, oldPath, newExtPath, false);

            } else {
                logger.error(ex);
                throw new BusinessException(ex);
            }
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param baseDir
     * @param paths
     * @param newBaseDir
     * @param extendPath
     * @throws BusinessException 
     */
    public void rename(String user, String baseDir, List<String> paths,
            String newBaseDir, boolean extendPath) throws BusinessException {

        for (String name : paths) {
            rename(user, baseDir + "/" + name, newBaseDir + "/" + name, extendPath);
        }
    }

    /**
     * 
     * @param user
     * @param path
     * @return
     * @throws BusinessException 
     */
    public boolean exists(String user, String path) throws BusinessException {

        try {
            return CoreUtil.getGRIDAClient().exist(DataManagerUtil.parseBaseDir(user, path));

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param path
     * @return
     * @throws BusinessException 
     */
    public long getModificationDate(String user, String path) throws BusinessException {

        try {
            return CoreUtil.getGRIDAClient().
                    getModificationDate(DataManagerUtil.parseBaseDir(user, path));

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param paths
     * @return
     * @throws BusinessException 
     */
    public List<Long> getModificationDate(String user, List<String> paths)
            throws BusinessException {
        try {
            List<String> parsedPaths = new ArrayList<String>();
            for (String path : paths) {
                parsedPaths.add(DataManagerUtil.parseBaseDir(user, path));
            }

            return CoreUtil.getGRIDAClient().getModificationDate(parsedPaths);

        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
}
