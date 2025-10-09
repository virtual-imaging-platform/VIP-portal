package fr.insalyon.creatis.vip.api.rest.mockconfig;

import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static fr.insalyon.creatis.vip.api.data.PathTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.ROOT;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Arrays;
import java.util.Collections;

import org.mockito.Mockito;

import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;

public class DataConfigurator {

    public static void configureFS(BaseRestApiSpringIT testSuite) throws VipException {
        // exists
        // getModifDate
        // listDir
        LFCBusiness mockLFCBusinnes = testSuite.getLfcBusiness();
        // /vip
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser1), eq(ROOT), eq(true)))
            .thenReturn(Arrays.asList(user1Dir));
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser2), eq(ROOT), eq(true)))
            .thenReturn(Arrays.asList(user2Dir));
        // /vip/Home
        Mockito.when(mockLFCBusinnes.exists(
                         eq(baseUser1), eq("/vip/Home")))
            .thenReturn(true);
        Mockito.when(mockLFCBusinnes.exists(
                         eq(baseUser2), eq("/vip/Home")))
            .thenReturn(true);
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser1), eq("/vip/Home"), eq(true)))
            .thenReturn(Arrays.asList(testFile1));
        Mockito.when(mockLFCBusinnes.listDir(
                         eq(baseUser2), eq("/vip/Home"), eq(true)))
            .thenReturn(Arrays.asList(testFile2, testDir1));
        // (user1) /vip/Home/testFile1
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser1), eq("/vip/Home/testFile1.xml")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser1), eq("/vip/Home/testFile1.xml"), eq(true)))
            .thenReturn(Arrays.asList(testFile1));
        // (user2) /vip/Home/testFile2
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testFile2.json")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2), eq("/vip/Home/testFile2.json"), eq(true)))
            .thenReturn(Arrays.asList(testFile2));
        // (user2) /vip/Home/testDir1
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1"),
                eq(true)))
            .thenReturn(Arrays.asList(testFile3, testFile4, testFile5));
        Mockito.when(
            mockLFCBusinnes.getModificationDate(
                eq(baseUser2), eq("/vip/Home/testDir1")))
            .thenReturn(getDataModitTS(testDir1)*1000);
        // (user2) /vip/Home/testFile[345]
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile3")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile4.pdf")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.exists(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile5.zip")))
            .thenReturn(true);
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2), eq("/vip/Home/testDir1/testFile3"), eq(true)))
            .thenReturn(Collections.singletonList(testFile3));
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1/testFile4.pdf"),
                eq(true)))
            .thenReturn(Collections.singletonList(testFile4));
        Mockito.when(
            mockLFCBusinnes.listDir(
                eq(baseUser2),
                eq("/vip/Home/testDir1/testFile5.zip"),
                eq(true)))
            .thenReturn(Collections.singletonList(testFile5));
    }

}
