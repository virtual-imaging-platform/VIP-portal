package fr.insalyon.creatis.vip.application.server.business.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Silva
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String read(File file) {

        StringBuilder content = new StringBuilder();
        try {

            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                content.append(line).append("\n");
            }

            buffer.close();
        } catch (java.io.IOException ex) {
            logger.error("Error reading file {}", file, ex);
        }

        return (content.length() == 0) ? null : content.toString();
    }
}
