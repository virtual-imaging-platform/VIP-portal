package fr.insalyon.creatis.vip.n4u.server.velocity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.h2.util.IOUtils;

public class ArchiveTools{

    /**
     * décompresse le fichier zip dans le répertoire donné
     * @param folder le répertoire où les fichiers seront extraits
     * @param zipfile le fichier zip à décompresser
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void unzip(File zipfile, File folder) throws FileNotFoundException, IOException{

        // création de la ZipInputStream qui va servir à lire les données du fichier zip
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(zipfile.getCanonicalFile())));

        // extractions des entrées du fichiers zip (i.e. le contenu du zip)
        ZipEntry ze = null;
        try {
            while((ze = zis.getNextEntry()) != null){

                // Pour chaque entrée, on crée un fichier
                // dans le répertoire de sortie "folder"
                File f = new File(folder.getCanonicalPath(), ze.getName());
           
                // Si l'entrée est un répertoire,
                // on le crée dans le répertoire de sortie
                // et on passe à l'entrée suivante (continue)
                if (ze.isDirectory()) {
                    f.mkdirs();
                    continue;
                }
               
                // L'entrée est un fichier, on crée une OutputStream
                // pour écrire le contenu du nouveau fichier
                f.getParentFile().mkdirs();
                OutputStream fos = new BufferedOutputStream(
                        new FileOutputStream(f));
           
                // On écrit le contenu du nouveau fichier
                // qu'on lit à partir de la ZipInputStream
                // au moyen d'un buffer (byte[])
                try {
                    try {
                        final byte[] buf = new byte[8192];
                        int bytesRead;
                        while (-1 != (bytesRead = zis.read(buf)))
                            fos.write(buf, 0, bytesRead);
                    }
                    finally {
                        fos.close();
                    }
                }
                catch (final IOException ioe) {
                    // en cas d'erreur on efface le fichier
                    f.delete();
                    throw ioe;
                }
            }
        }
        finally {
            // fermeture de la ZipInputStream
            zis.close();
        }
    }
    public static void compress(List<File> pathIn, String pathOut) {
        try {

            FileOutputStream fos = new FileOutputStream(pathOut);
            TarArchiveOutputStream zos = new TarArchiveOutputStream(
                    new GZIPOutputStream(new BufferedOutputStream(fos)));
            for (File entry : pathIn) {
                addFileToTarGz(zos, entry,null);
            }
            zos.finish();
            zos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void addFileToTarGz(TarArchiveOutputStream tOut, File f,String dir) throws IOException {
       TarArchiveEntry tarEntry;
        if (dir == null) {
             tarEntry = new TarArchiveEntry(f,f.getName());
        } else {
             tarEntry = new TarArchiveEntry(f, dir + "/" + f.getName());
        }
        tOut.putArchiveEntry(tarEntry);
        if (!f.isDirectory()) {
            FileInputStream in = new FileInputStream(f);
            IOUtils.copy(in, tOut);
            tOut.closeArchiveEntry();
            in.close();
        } else {
            tOut.closeArchiveEntry();
            String name=f.getName();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    
                    addFileToTarGz(tOut, new File(child.getParent()+"/"+child.getName()),name);
                }
            }
        }
    }

}
