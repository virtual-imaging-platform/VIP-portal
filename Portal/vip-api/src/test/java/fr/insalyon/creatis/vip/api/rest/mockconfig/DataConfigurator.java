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

import fr.insalyon.creatis.vip.api.rest.config.BaseVIPSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import org.mockito.Mockito;
import static org.mockito.Matchers.anyObject;

import java.util.Arrays;

import static fr.insalyon.creatis.vip.api.data.PathTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.*;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.ROOT;
import static org.mockito.Matchers.eq;

/**
 * Created by abonnet on 2/23/17.
 */
public class DataConfigurator {

    public static void configureFS(BaseVIPSpringIT testSuite) throws BusinessException {
        // exists
        // getModifDate
        // listDir
        LFCBusiness mockLFCBusinnes = testSuite.getLfcBusiness();
        // /vip
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser1), eq(ROOT), eq(true), anyObject()))
            .thenReturn(Arrays.asList(user1Dir));
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser2), eq(ROOT), eq(true), anyObject()))
            .thenReturn(Arrays.asList(user2Dir));
        // /vip/Home
        Mockito.when(mockLFCBusinnes.exists(
                         eq(baseUser1), eq("/vip/Home"), anyObject()))
            .thenReturn(true);
        Mockito.when(mockLFCBusinnes.exists(
                         eq(baseUser2), eq("/vip/Home"), anyObject()))
            .thenReturn(true);
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser1), eq("/vip/Home"), eq(true), anyObject()))
            .thenReturn(Arrays.asList(testFile1));
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser2), eq("/vip/Home"), eq(true), anyObject()))
            .thenReturn(Arrays.asList(testFile2, testDir1));
        // (user1) /vip/Home/testFile1
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser1), eq("/vip/Home/testFile1.xml"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser1), eq("/vip/Home/testFile1.xml"), eq(true), anyObject()))
            .thenReturn(Arrays.asList(getAbsoluteData(testFile1)));
        // (user2) /vip/Home/testFile2
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testFile2.json"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2), eq("/vip/Home/testFile2.json"), eq(true), anyObject()))
            .thenReturn(Arrays.asList(getAbsoluteData(testFile2)));
        // (user2) /vip/Home/testDir1
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1"),
                eq(true),
                anyObject()))
            .thenReturn(Arrays.asList(testFile3, testFile4, testFile5));
        Mockito.when(
            mockLFCBusinnes.getModificationDate(
                eq(baseUser2), eq("/vip/Home/testDir1"), anyObject()))
            .thenReturn(getDataModitTS(testDir1)*1000);
        // (user2) /vip/Home/testFile[345]
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile3"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile4.pdf"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile5.zip"), anyObject()))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile3"), eq(true), anyObject()))
            .thenReturn(Arrays.asList(getAbsoluteData(testFile3)));
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1/testFile4.pdf"),
                eq(true),
                anyObject()))
            .thenReturn(Arrays.asList(getAbsoluteData(testFile4)));
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1/testFile5.zip"),
                eq(true),
                anyObject()))
            .thenReturn(Arrays.asList(getAbsoluteData(testFile5)));
    }

}
