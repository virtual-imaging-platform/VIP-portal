package fr.insalyon.creatis.vip.core.server.business.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;

@Service
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    @Autowired private Server server;

    public String read(File file) {
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

    public Path getValidWorkflowPath(User user, String filepath) {
        // normalize and verify there is no risk of accessing a file outside the workflows directory

        Path workflowsPath = Paths.get(server.getWorkflowsPath()).normalize().toAbsolutePath();
        Path requestedPath = Paths.get(server.getWorkflowsPath(), filepath).normalize().toAbsolutePath(); // do not use resolve as filepath could be absolute

        System.err.println(workflowsPath);
        System.err.println(requestedPath);
        if ( ! requestedPath.startsWith(workflowsPath)) {
            logger.warn("(" + user.getEmail() + ") Attempt to access file outside workflows path: '" + filepath + "'.");
            return null;
        } else {
            return requestedPath;
        }
    }
}
