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
package fr.insalyon.creatis.vip.api.rest.mockconfig;

import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.rest.config.BaseVIPSpringIT;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static fr.insalyon.creatis.vip.api.data.AppVersionTestUtils.getVersion;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.getDescriptor;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

/**
 * Created by abonnet on 10/14/16.
 */
public class ApplicationsConfigurator {

    /**
     * Should take only Application and AppVersion classes in the sequence :
     * Application AppVersion+
     * (an application followed by one or more version)
     */
    public static void configureApplications(
            BaseVIPSpringIT test,
            User user,
            List<AppClass> classes,
            Object... args) throws BusinessException {
        // parse the args to map the application to the versions
        Application currentApplication = null;
        Map<Application, List<AppVersion>> applicationVersions = new HashMap<>();
        for (Object currentArg : args) {
            if (currentArg instanceof Application) {
                currentApplication = (Application) currentArg;
                applicationVersions.put(currentApplication, new ArrayList<>());
            } else {
                applicationVersions.get(currentApplication).
                        add(getVersion((AppVersion) currentArg, currentApplication));
            }
        }
        List<String> classNames =
                classes.stream().map(AppClass::getName).collect(Collectors.toList());
        // verify the classes given are good
        Predicate<Application> isAppNotOK =
                app -> Collections.disjoint(app.getApplicationClasses(), classNames);
        if (applicationVersions.keySet().stream().anyMatch(isAppNotOK)) {
            throw new RuntimeException("misconfiguration of test class>app config");
        }
        // configure mocks
        ClassBusiness classBusiness = test.getClassBusiness();
        ApplicationBusiness applicationBusiness = test.getApplicationBusiness();
        // 1 return user classes
        when(classBusiness.getUserClasses(user.getEmail(), false)).thenReturn(classes);
        when(classBusiness.getUserClassesName(user.getEmail(), false)).thenReturn(classNames);
        // 2 return apps for the classes
        when(applicationBusiness.getApplications(anyListOf(String.class))).
                thenReturn(new ArrayList<>(applicationVersions.keySet()));
        // 3 return versions for each app
        for (Application app : applicationVersions.keySet()) {
            when(applicationBusiness.getVersions(app.getName())).
                    thenReturn(applicationVersions.get(app));
            when(applicationBusiness.getApplication(app.getName()))
                    .thenReturn(app);
        }
    }

    public static String configureAnApplication(
            BaseVIPSpringIT test,
            User user, Application app,
            AppVersion version,
            Integer... appParamsIndexes) throws BusinessException {
        WorkflowBusiness workflowBusiness = test.getWorkflowBusiness();
        when(workflowBusiness.getApplicationDescriptor(user, app.getName(), version.getVersion()))
                .thenReturn(getDescriptor("desc test", appParamsIndexes));
        return ApiUtils.getPipelineIdentifier(app.getName(), version.getVersion());
    }
}
