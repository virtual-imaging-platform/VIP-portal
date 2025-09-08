package fr.insalyon.creatis.vip.application.server.business.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Silva
 */
public class ProxyUtil {

    private static final Logger logger = LoggerFactory.getLogger(ProxyUtil.class);
    public static final String CHARSET_UTF8 = "UTF-8";

    public static String readAsString(String proxyFileName) {

        try {
            File file = new File(proxyFileName);
            InputStream inps = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[32 * 1024];
            int len = 0;

            while ((len = inps.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }

            byte[] data = bos.toByteArray();
            return new String(data, CHARSET_UTF8);

        } catch (IOException ex) {
            logger.error("Error reading proxy file {}", proxyFileName, ex);
        }
        return null;
    }
}
