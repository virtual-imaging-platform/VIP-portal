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

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EngineBusiness {

    /**
     *
     * @param engine
     * @throws BusinessException
     */
    public void add(Engine engine) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getEngineDAO().add(engine);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param engine
     * @throws BusinessException
     */
    public void update(Engine engine) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getEngineDAO().update(engine);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    /**
     * 
     * @param name
     * @throws BusinessException 
     */
    public void remove(String name) throws BusinessException {
        
        try {
            ApplicationDAOFactory.getDAOFactory().getEngineDAO().remove(name);
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<Engine> get() throws BusinessException {
        
        try {
            return ApplicationDAOFactory.getDAOFactory().getEngineDAO().get();
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
