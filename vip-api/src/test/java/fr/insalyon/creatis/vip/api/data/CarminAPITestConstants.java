package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;

import static fr.insalyon.creatis.vip.core.server.model.Module.COMMERCIAL;
import static fr.insalyon.creatis.vip.core.server.model.Module.DATA;
import static fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol.HTTP;
import static fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol.WEBDAV;

public interface CarminAPITestConstants {
    String TEST_PLATFORM_NAME = "TestPlatform";
    String TEST_PLATFORM_DESCRIPTION = "Test Platform description";
    String TEST_PLATFORM_EMAIL = "test@email.tst";
    String TEST_DEFAULT_LIST_LIMIT = "42";
    String TEST_SUPPORTED_API_VERSION = "Version 4.2";

    /* WARNING : keep following properties consistant */
    SupportedTransferProtocol[] TEST_SUPPORTED_PROTOCOLS = {HTTP, WEBDAV};
    String TEST_SUPPORTED_TRANSFER_PROTOCOLS_STRING = "HTTP,WEBDAV";
    Module[] TEST_SUPPORTED_MODULES = {DATA, COMMERCIAL};
    String TEST_SUPPORTED_MODULES_STRING = "COMMERCIAL,DATA";
    String[] TEST_UNSUPPORTED_METHOD = {"method1", "method2"};
    String TEST_UNSUPPORTED_METHODS_STRING = "method1,method2";
    /* End of consistant properties */

    String TEST_DOWNLOAD_PATH = "path/test/download";
    String TEST_DEFAULT_MIMETYPE = "application/octet-stream";
    String TEST_DIR_MIMETYPE = "text/directory";
    String TEST_DATA_DOWNLOAD_TIMEOUT = "11";
    String Test_DATA_DOWNLOAD_RETRY = "2";
    String TEST_DATA_MAX_SIZE = "42300";

    String TEST_APIKEY_HEADER = "testapikey";
    String TEST_GENERATE_NEW_APIKEY_EACH_TIME = "false";

    String TEST_PIPELINE_WHITELIST = "application 3/4.2";
}
