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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ClassBusiness {

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<AppClass> getClasses() throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getClassDAO().getClasses();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws BusinessException 
     */
    public List<String> getClassesName() throws BusinessException {

        List<String> classesName = new ArrayList<String>();
        for (AppClass appClass : getClasses()) {
            classesName.add(appClass.getName());
        }

        return classesName;
    }
    
    /**
     * 
     * @param c
     * @throws BusinessException 
     */
    public void addClass(AppClass c) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getClassDAO().add(c);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param c
     * @throws BusinessException 
     */
    public void updateClass(AppClass c) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getClassDAO().update(c);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param name
     * @throws BusinessException 
     */
    public void removeClass(String name) throws BusinessException {

        try {
            ApplicationDAOFactory.getDAOFactory().getClassDAO().remove(name);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param validAdmin
     * @return
     * @throws BusinessException 
     */
    public List<String> getUserClassesName(String email, boolean validAdmin)
            throws BusinessException {

        List<String> classesName = new ArrayList<String>();
        for (AppClass appClass : getUserClasses(email, validAdmin)) {
            classesName.add(appClass.getName());
        }

        return classesName;
    }

    /**
     * 
     * @param email
     * @param validAdmin
     * @return
     * @throws BusinessException 
     */
    public List<AppClass> getUserClasses(String email, boolean validAdmin)
            throws BusinessException {

        try {
            return ApplicationDAOFactory.getDAOFactory().getClassDAO().getUserClasses(email, validAdmin);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
