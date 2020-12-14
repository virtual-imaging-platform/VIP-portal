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

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
@Service
@Transactional
public class ClassBusiness {

    private ClassDAO classDAO;

    @Autowired
    public ClassBusiness(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    public List<AppClass> getClasses() throws BusinessException {
        try {
            return classDAO.getClasses();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getClassesName() throws BusinessException {
        List<String> classesName = new ArrayList<>();
        for (AppClass appClass : getClasses()) {
            classesName.add(appClass.getName());
        }

        return classesName;
    }

    public AppClass getClass(String className) throws BusinessException {
        try {
            return classDAO.getClass(className);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addClass(AppClass c) throws BusinessException {
        try {
            classDAO.add(c);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateClass(AppClass c) throws BusinessException {
        try {
            classDAO.update(c);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeClass(String name) throws BusinessException {
        try {
            classDAO.remove(name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getUserClassesName(String email, boolean validAdmin)
            throws BusinessException {

        List<String> classesName = new ArrayList<String>();
        for (AppClass appClass : getUserClasses(email, validAdmin)) {
            classesName.add(appClass.getName());
        }

        return classesName;
    }

    public List<AppClass> getUserClasses(String email, boolean validAdmin)
            throws BusinessException {
        try {
            return classDAO.getUserClasses(email, validAdmin);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
