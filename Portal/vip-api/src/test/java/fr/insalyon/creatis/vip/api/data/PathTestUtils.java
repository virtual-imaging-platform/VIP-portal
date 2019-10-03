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

import fr.insalyon.creatis.vip.api.rest.model.PathProperties;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data.Type;
import org.hamcrest.Matcher;

import java.util.*;
import java.util.function.Function;

/**
 * Created by abonnet on 1/23/17.
 */
public class PathTestUtils {

    private static Map<String,Function> pathSuppliers;

    /*
       vip  /user1      /testFile1
            /user2      /testFile2
                        /testDir1   /testFile3
                                    /testFile4
                                    /testFile5
            /grouptest  /testFile6


     */

    public static Data vipRoot, user1Dir, user2Dir, groupTestDir, testDir1;
    public static Data testFile1, testFile2, testFile3, testFile4, testFile5, testFile6;

    public static PathProperties testVipRootPathProperties, testUser1DirPathProperties, testUser2DirPathProperties,
            testGroupTestDiPathProperties, testDir1PathProperties;
    public static PathProperties testFile1PathProperties, testFile2PathProperties, testFile3PathProperties,
            testFile4PathProperties, testFile5PathProperties, testFile6PathProperties;

    static {
        vipRoot = new Data("vip", Type.folder, 3, null, null, null);
        user1Dir = new Data("Home", Type.folder, 1, null, null, null);
        user2Dir = new Data("Home", Type.folder, 2, null, null, null);
        groupTestDir = new Data("groupTest (group)", Type.folder, 1, null, null, null);
        testDir1 = new Data("testDir1", Type.folder, 3, null, null, null);

        testFile1 = new Data("testFile1.xml", Type.file, 42004, "Apr 04 2015", null, null);
        testFile2 = new Data("testFile2.json", Type.fileSync, 42005, "Dec 21 2016 ", null, null);
        testFile3 = new Data("testFile3", Type.file, 42006, "Jan 01 2001", null, null);
        testFile4 = new Data("testFile4.pdf", Type.file, 42007, "Jul 30 2014", null, null);
        testFile5 = new Data("testFile5.zip", Type.file, 42008, "Jun 15 1999", null, null);
        testFile6 = new Data("testFile6.unknown", Type.file, 42009, "Aug 28 2014", null, null);

        testVipRootPathProperties = getPath(vipRoot, true, null, null, "text/directory");
        testUser1DirPathProperties = getPath(user1Dir, true, null, null, "text/directory");
        testUser2DirPathProperties = getPath(user2Dir, true, null, null, "text/directory");
        testGroupTestDiPathProperties = getPath(groupTestDir, true, null, null, "text/directory");
        testDir1PathProperties = getPath(testDir1, true, null, null, "text/directory");

        testFile1PathProperties = getPath(testFile1, false, getTS(4,4,2015), null, "text/xml");
        testFile2PathProperties = getPath(testFile2, false, getTS(21,12,2016), null, "application/json");
        testFile3PathProperties = getPath(testFile3, false, getTS(1,1,2001), null, "application/octet-stream");
        testFile4PathProperties = getPath(testFile4, false, getTS(30,7,2014), null, "application/pdf");
        testFile5PathProperties = getPath(testFile5, false, getTS(15,6,1999), null, "application/zip");
        testFile6PathProperties = getPath(testFile6, false, getTS(28,8,2014), null, "application/octet-stream");

        pathSuppliers = getPathSuppliers();
    }

    private static Long getTS(int day, int month, int year) {
        // return timestamp in seconds
        return new GregorianCalendar(year, month-1, day).getTimeInMillis() / 1000;
    }

    public static Data getAbsoluteData(Data data) {
        return new Data(
                getAbsolutePath(data),
                data.getType(),
                data.getLength(),
                data.getModificationDate(),
                data.getReplicas(),
                data.getPermissions()
        );
    }

    public static Long getDataModitTS(Data data) {
        if (data == vipRoot) return getTS(13,2,2015);
        if (data == user1Dir) return getTS(21,9,2011);
        if (data == user2Dir) return getTS(1,10,2012);
        if (data == groupTestDir) return getTS(3,11,2010);
        if (data == testDir1) return getTS(7,3,2016);
        throw new RuntimeException("Getting modif date of invalid data");
    }

    public static String getDataModifDate(Data data) {
        if (data == vipRoot) return "Feb 13 2015";
        if (data == user1Dir) return "Sep 21 2011";
        if (data == user2Dir) return "Oct 1 2012";
        if (data == groupTestDir) return "Nov 3 2010";
        if (data == testDir1) return "Mar 07 2016";
        throw new RuntimeException("Getting modif date of invalid data");
    }

    public static String getAbsolutePath(Data data) {
        if (data == vipRoot) return "/vip";
        if (data == user1Dir) return "/vip/Home";
        if (data == user2Dir) return "/vip/Home";
        if (data == groupTestDir) return "/vip/groupTest (group)";
        if (data == testDir1) return "/vip/Home/testDir1";

        if (data == testFile1) return "/vip/Home/testFile1.xml";
        if (data == testFile2) return "/vip/Home/testFile2.json";
        if (data == testFile3) return "/vip/Home/testDir1/testFile3";
        if (data == testFile4) return "/vip/Home/testDir1/testFile4.pdf";
        if (data == testFile5) return "/vip/Home/testDir1/testFile5.zip";
        if (data == testFile6) return "/vip/groupTest (group)/testFile6.zip";
        throw new RuntimeException("Wrong test data");
    }

    public static PathProperties getPathWithTS(PathProperties pathProperties) {
        Long modifDate = pathProperties.getLastModificationDate();
        if (pathProperties == testVipRootPathProperties) modifDate = getDataModitTS(vipRoot);
        if (pathProperties == testUser1DirPathProperties) modifDate = getDataModitTS(user1Dir);
        if (pathProperties == testUser2DirPathProperties) modifDate = getDataModitTS(user2Dir);
        if (pathProperties == testGroupTestDiPathProperties) modifDate = getDataModitTS(groupTestDir);
        if (pathProperties == testDir1PathProperties) modifDate = getDataModitTS(testDir1);
        PathProperties newPathProperties = new PathProperties();
        newPathProperties.setPath(pathProperties.getPath());
        newPathProperties.setIsDirectory(pathProperties.getIsDirectory());
        newPathProperties.setLastModificationDate(modifDate);
        newPathProperties.setExecutionId(pathProperties.getExecutionId());
        newPathProperties.setExists(pathProperties.getExists());
        newPathProperties.setMimeType(pathProperties.getMimeType());
        newPathProperties.setSize(pathProperties.getSize());
        return newPathProperties;
    }

    public static PathProperties getPath(Data data, boolean isDirectory,
            Long modificationDate, String executionId, String mimeType) {
        PathProperties pathProperties = new PathProperties();
        pathProperties.setPath(getAbsolutePath(data));
        pathProperties.setIsDirectory(isDirectory);
        pathProperties.setLastModificationDate(modificationDate);
        pathProperties.setExecutionId(executionId);
        pathProperties.setExists(true);
        pathProperties.setMimeType(mimeType);
        pathProperties.setSize(data.getLength());
        return pathProperties;
    }

    private static Map<String,Function> getPathSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("path", "lastModificationDate", "isDirectory", "exists",
                        "size", "executionId", "mimeType"),
                PathProperties::getPath,
                PathProperties::getLastModificationDate,
                PathProperties::getIsDirectory,
                PathProperties::getExists,
                PathProperties::getSize,
                PathProperties::getExecutionId,
                PathProperties::getMimeType);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToPath(PathProperties pathProperties) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(pathProperties, pathSuppliers, suppliersRegistry);
    }

}
