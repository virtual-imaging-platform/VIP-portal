/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import org.jsoup.Jsoup;

/**
 *
 * @author nouha
 */
public class FileTools {

    /**
     * parse a html string to text
     *
     * @param html
     * @return
     */
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    /**
     *
     * @param source
     * @param dest
     */
    public static void copyFile(String source, String dest) {
        FileChannel in = null;
        FileChannel out = null;

        try {
            // Init
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

    }

    /**
     *
     * @param f
     * @param writer
     */
    public static void createFile(File f, StringWriter writer) throws VelocityException {
        FileWriter writerr;
        try {
            f.createNewFile();
            writerr = new FileWriter(f);
            try {
                writerr.write(writer.toString());
            } finally {
                writerr.close();
            }
        } catch (IOException e) {
            throw new VelocityException(e);
        }

    }

    public static void setDirRights(String dir, String user) throws VelocityException {

        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("chown -R " + user + ":" + user + " " + dir);
        } catch (IOException ex) {
            throw new VelocityException(ex);

        }
    }

    public static void delete(File file)
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            //if file, then delete it
            file.delete();
        }
    }

}
