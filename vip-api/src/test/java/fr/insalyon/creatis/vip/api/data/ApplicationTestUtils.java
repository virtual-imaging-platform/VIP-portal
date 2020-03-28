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
package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.application.client.bean.*;

import java.util.*;

/**
 * Created by abonnet on 7/26/16.
 */
public class ApplicationTestUtils {

    static public Application app1;
    static public Application app2;
    static public Application app3;

    static public Map<AppClass, List<Application>> applicationsPerClass = new HashMap<>();

    static {
        // init class->application map
        applicationsPerClass.put(ClassesTestUtils.class1, new ArrayList<>());
        applicationsPerClass.put(ClassesTestUtils.class2, new ArrayList<>());

        app1 = new Application("application 1", Arrays.asList(ClassesTestUtils.class1.getName()),
                UserTestUtils.baseUser1.getEmail(), UserTestUtils.baseUser1.getFullName(), "citation application 1");
        app2 = new Application("application 2", Arrays.asList(ClassesTestUtils.class1.getName()),
                UserTestUtils.baseUser1.getEmail(), UserTestUtils.baseUser1.getFullName(), "citation application 2");
        applicationsPerClass.get(ClassesTestUtils.class1).add(app2);
        app3 = new Application("application 3", Arrays.asList(ClassesTestUtils.class1.getName(), ClassesTestUtils.class2.getName()),
                UserTestUtils.baseUser2.getEmail(), UserTestUtils.baseUser2.getFullName(), "citation application 3");
        applicationsPerClass.get(ClassesTestUtils.class1).add(app3);
        applicationsPerClass.get(ClassesTestUtils.class2).add(app3);
    }
}
