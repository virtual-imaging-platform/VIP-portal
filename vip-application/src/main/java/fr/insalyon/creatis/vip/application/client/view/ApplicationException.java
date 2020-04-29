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
package fr.insalyon.creatis.vip.application.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

import javax.validation.constraints.Max;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationException extends VipException implements IsSerializable {

    /* Reserved codes : 2xxx : vip-application */
    public enum ApplicationError implements VipError {
        PLATFORM_MAX_EXECS(2000),
        USER_MAX_EXECS(2001),
        WRONG_APPLICATION_DESCRIPTOR(2002);


        private Integer code;
        ApplicationError(Integer code) { this.code = code; }
        @Override
        public Integer getCode() { return code; }
    }

    static {
        addMessage(ApplicationError.PLATFORM_MAX_EXECS, "Max number of running executions reached on the platform.", 0);
        addMessage(ApplicationError.USER_MAX_EXECS, "Max number of running executions reached.<br />You already have {} running executions.", 1);
        addMessage(ApplicationError.WRONG_APPLICATION_DESCRIPTOR, "Error getting application descriptor for {}.", 1);
    }

    public ApplicationException() {
    }

    public ApplicationException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(ApplicationError applicationError, Object... params) {
        super(applicationError, params);
    }

    public ApplicationException(ApplicationError applicationError, Throwable cause, Object... params) {
        super(applicationError, cause, params);
    }
}
