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

    public static Path testFile1Path;
    public static Path testDir1Path;
    public static Path testFile2Path;
    public static Path testDir2Path;

    static {
        root = new Data("root", Type.folder, 2, null, null, null);
        testDir1 = new Data("testDir1", Type.folder, 2, null, null, null);
        testDir2 = new Data("testDir2", Type.folderSync, 3, null, null, null);
        testFile1 = new Data("testFile1", Type.file, 42004, null, null, null);
        testFile2 = new Data("testFile2", Type.fileSync, 42005, null, null, null);
        testFile3 = new Data("testFile3", Type.file, 42006, null, null, null);
        testFile4 = new Data("testFile4", Type.file, 42007, null, null, null);
        testFile5 = new Data("testFile5", Type.file, 42008, null, null, null);

        testFile1Path = getPath(TEST_API_URI_PREFIX, testFile1, false, null, null, null);
        testDir1Path = getPath(TEST_API_URI_PREFIX, testDir1, true, null, null, null);
        testFile2Path = getPath(TEST_API_URI_PREFIX, testFile2, false, null, null, null);
        testDir2Path = getPath(TEST_API_URI_PREFIX, testDir2, true, null, null, null);

        pathSuppliers = getPathSuppliers();
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

    public static String getAbsolutePath(Data data) {
        if (data == root) return "/root";
        if (data == testDir1) return "/root/testDir1";
        if (data == testDir2) return "/root/testDir1/testDir2";
        if (data == testFile1) return "/root/testFile1";
        if (data == testFile2) return "/root/testDir1/testFile2";
        if (data == testFile3) return "/root/testDir1/testDir2/testFile3";
        if (data == testFile4) return "/root/testDir1/testDir2/testFile4";
        if (data == testFile5) return "/root/testDir1/testDir2/testFile5";
        throw new RuntimeException("Wrong test data");
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
