package fr.insalyon.creatis.vip.datamanager.server;

import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import java.io.File;
import java.util.*;

public class DataManagerUtil {

    public static String extractName(String lfcDir) {
        return lfcDir.substring(lfcDir.lastIndexOf("/") + 1);
    }

     public static List<String> getPaths(List<String> groups){
        ArrayList<String> paths = new ArrayList<>();
        for(String s : groups)
            paths.add(s.replaceAll(" ", "_"));
        return paths;
    }

    /*
        remove spaces, accents and non-ascii characters
     */
    public static String getCleanFilename(String fileName) {
        fileName = new File(fileName).getName().trim().replaceAll(" ", "_");
        return CoreUtil.getCleanStringAscii(fileName, "");
    }
}
