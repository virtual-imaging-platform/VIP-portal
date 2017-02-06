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

import fr.insalyon.creatis.vip.api.rest.model.Path;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data.Type;
import org.hamcrest.Matcher;

import java.util.*;
import java.util.function.Function;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_API_URI_PREFIX;
import static fr.insalyon.creatis.vip.core.client.view.util.CountryCode.re;

/**
 * Created by abonnet on 1/23/17.
 */
public class PathTestUtils {

    private static Map<String,Function> pathSuppliers;

    /*
       root /testFile1
            /testDir1   /testFile2
                        /testDir2   /testFile3
                                    /testFile4
                                    /testFile5


     */

    public static Data testFile1, testFile2, testFile3, testFile4, testFile5;
    public static Data root, testDir1, testDir2;

    public static Path testRootPath, testDir1Path, testDir2Path;
    public static Path testFile1Path, testFile2Path, testFile3Path, testFile4Path, testFile5Path;

    static {
        root = new Data("root", Type.folder, 2, null, null, null);
        testDir1 = new Data("testDir1", Type.folder, 2, null, null, null);
        testDir2 = new Data("testDir2", Type.folderSync, 3, null, null, null);
        testFile1 = new Data("testFile1.xml", Type.file, 42004, "Apr 04 2015", null, null);
        testFile2 = new Data("testFile2.json", Type.fileSync, 42005, "Dec 21 2016 ", null, null);
        testFile3 = new Data("testFile3", Type.file, 42006, "Jan 01 2001", null, null);
        testFile4 = new Data("testFile4.pdf", Type.file, 42007, "Jul 30 2014", null, null);
        testFile5 = new Data("testFile5.zip", Type.file, 42008, "Jun 15 1999", null, null);

        testRootPath = getPath(TEST_API_URI_PREFIX, root, true, null, null, "text/directory");
        testDir1Path = getPath(TEST_API_URI_PREFIX, testDir1, true, null, null, "text/directory");
        testDir2Path = getPath(TEST_API_URI_PREFIX, testDir2, true, null, null, "text/directory");
        testFile1Path = getPath(TEST_API_URI_PREFIX, testFile1, false, getTS(4,4,2015), null, "application/xml");
        testFile2Path = getPath(TEST_API_URI_PREFIX, testFile2, false, getTS(21,12,2016), null, "application/json");
        testFile3Path = getPath(TEST_API_URI_PREFIX, testFile3, false, getTS(1,1,2001), null, "application/octet-stream");
        testFile4Path = getPath(TEST_API_URI_PREFIX, testFile4, false, getTS(30,07,2014), null, "application/pdf");
        testFile5Path = getPath(TEST_API_URI_PREFIX, testFile5, false, getTS(15,06,1999), null, "application/zip");

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
        if (data == root) return getTS(13,2,2015);
        if (data == testDir1) return getTS(7,3,2016);
        if (data == testDir2) return getTS(23,5,2016);
        throw new RuntimeException("Getting modif date of invalid data");
    }

    public static String getDataModifDate(Data data) {
        if (data == root) return "Feb 13 2015";
        if (data == testDir1) return "Mar 07 2016";
        if (data == testDir2) return "May 23 2016";
        throw new RuntimeException("Getting modif date of invalid data");
    }

    public static String getAbsolutePath(Data data) {
        if (data == root) return "/root";
        if (data == testDir1) return "/root/testDir1";
        if (data == testDir2) return "/root/testDir1/testDir2";
        if (data == testFile1) return "/root/testFile1.xml";
        if (data == testFile2) return "/root/testDir1/testFile2.json";
        if (data == testFile3) return "/root/testDir1/testDir2/testFile3";
        if (data == testFile4) return "/root/testDir1/testDir2/testFile4.pdf";
        if (data == testFile5) return "/root/testDir1/testDir2/testFile5.zip";
        throw new RuntimeException("Wrong test data");
    }

    public static Path getPathWithTS(Path path) {
        Long modifDate = path.getLastModificationDate();
        if (path == testRootPath) modifDate = getDataModitTS(root);
        if (path == testDir1Path) modifDate = getDataModitTS(testDir1);
        if (path == testDir2Path) modifDate = getDataModitTS(testDir2);
        Path newPath = new Path();
        newPath.setPlatformURI(path.getPlatformURI());
        newPath.setIsDirectory(path.getIsDirectory());
        newPath.setLastModificationDate(modifDate);
        newPath.setExecutionId(path.getExecutionId());
        newPath.setExists(path.getExists());
        newPath.setMimeType(path.getMimeType());
        newPath.setSize(path.getSize());
        return newPath;
    }

    private static Path getPath(
            String prefix, Data data, boolean isDirectory,
            Long modificationDate, String executionId, String mimeType) {
        Path path = new Path();
        path.setPlatformURI(prefix + getAbsolutePath(data));
        path.setIsDirectory(isDirectory);
        path.setLastModificationDate(modificationDate);
        path.setExecutionId(executionId);
        path.setExists(true);
        path.setMimeType(mimeType);
        path.setSize(data.getLength());
        return path;
    }

    private static Map<String,Function> getPathSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("platformURI", "lastModificationDate", "isDirectory", "exists",
                        "size", "executionId", "mimeType"),
                Path::getPlatformURI,
                Path::getLastModificationDate,
                Path::getIsDirectory,
                Path::getExists,
                Path::getSize,
                Path::getExecutionId,
                Path::getMimeType);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToPath(Path path) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return JsonCustomObjectMatcher.jsonCorrespondsTo(path, pathSuppliers, suppliersRegistry);
    }

}
