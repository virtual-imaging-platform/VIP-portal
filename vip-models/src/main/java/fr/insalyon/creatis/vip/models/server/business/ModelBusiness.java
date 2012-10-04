/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.models.server.business;

import com.hp.hpl.jena.rdf.model.RDFWriter;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelQueryer;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectSearcher;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.*;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayer.Resolution;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.ObjectLayerPart.Format;


import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;



import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.rpc.DataManagerServiceImpl;
import java.io.*;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard, Rafael Silva
 */
public class ModelBusiness {

    private static final Logger logger = Logger.getLogger(ModelBusiness.class);

    /**
     *
     * @param modelZipFile
     * @return
     * @throws BusinessException
     */
    public List<String> getFiles(String modelZipFile, User user, String zipFullPath, boolean bUpload) throws BusinessException, DataManagerException {

        System.out.println("Calling getFiles " + modelZipFile);
        
        if (modelZipFile.lastIndexOf(".zip") == -1) {
            throw new BusinessException("This is not a zip file");
        }

        try {
            String rootDirectory =  getZipPath(user, zipFullPath,bUpload);
            System.out.println("Calling getFiles " + rootDirectory);
            ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(rootDirectory + modelZipFile));
            ZipEntry zipentry = zipinputstream.getNextEntry();

            ArrayList<String> files = new ArrayList<String>();
            byte[] buf = new byte[1024];

            while (zipentry != null) {

                if (!zipentry.isDirectory()) {
                    //for each entry to be extracted
                    String entryName = zipentry.getName();
                    if (entryName.endsWith(".rdf")) { //extract only the annotations.
                        FileOutputStream fileoutputstream;
                        File newFile = new File(entryName);
                        String directory = newFile.getParent();

                        if (directory == null) {
                            if (newFile.isDirectory()) {
                                break;
                            }
                        }
                        int n;
                        fileoutputstream = new FileOutputStream(
                                rootDirectory + entryName);
                        System.out.println("rdf file : " + entryName);
                        while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                            fileoutputstream.write(buf, 0, n);
                        }
                        fileoutputstream.close();
                        zipinputstream.closeEntry();
                        //checkEmptyRDF(rootDirectory + entryName);
                        //checkRDFEncoding(rootDirectory + entryName);
                    }
                    files.add(rootDirectory + entryName);
                }
                zipentry = zipinputstream.getNextEntry();
            }
            zipinputstream.close();

            return files;

        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
    }
   private boolean checkRDFEncoding(String file) throws FileNotFoundException, IOException {
        File f = new File(file);
        boolean btest = false;
        InputStream ips = new FileInputStream(file);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String temp = br.readLine();
        String tp = "";
        if (temp.contains("windows"))
        {
            System.out.println("windows encoding");
            temp = temp.replace("windows-1252", "UTF-8");
            btest = true;
            while ((tp  = br.readLine()) != null)
            {
                temp += tp;
            }
            br.close();
            ips.close();
            
            OutputStream out = new FileOutputStream(f); 
            out.write(temp.getBytes());
            out.close();    
        }
        else
        {
             br.close();
            ips.close();
        }
        return btest;
    }

        private void copyFile(String srFile, String dtFile) throws FileNotFoundException, IOException {

        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        InputStream in = new FileInputStream(f1);

        OutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        System.out.println("File copied.");
    }
    

      private boolean checkEmptyRDF(String file) throws FileNotFoundException, IOException {
        boolean bres = true;
        System.out.println("Empty file : " + file);
        File f1 = new File(file);
        InputStream ips = new FileInputStream(file);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        if (br.read() == -1)
        {
            System.out.println("Empty file : " + file);
            bres = false;
        }
        br.close();
        return bres;
    }
    
    public void checkRDF(String zipName,User user, String zipFullPath, boolean bUpload) throws DataManagerException, FileNotFoundException, IOException
    {
        List<File> files = new ArrayList<File>();
        String rootDirectory = getZipPath(user,zipFullPath, bUpload);
        File zipFile = new File(rootDirectory + zipName);
        File zipdir = new File(rootDirectory + "/zip/");
        if (!zipdir.exists()) {
            zipdir.mkdirs();
        }
        copyFile(rootDirectory + zipName, zipdir + zipName);

        byte[] buf = new byte[1024];
        String modelname = "";
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipdir + zipName));

        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            if (name.contains(".rdf")) {
                modelname = name;
            }

            FileOutputStream fileoutputstream;
            int n;
            fileoutputstream = new FileOutputStream(zipdir + "/" + name);
            while ((n = zin.read(buf, 0, 1024)) > -1) {
                fileoutputstream.write(buf, 0, n);
            }
            fileoutputstream.close();
            zin.closeEntry();

            files.add(new File(zipdir + "/" + name));
            entry = zin.getNextEntry();
        }
        // Close the streams        
        zin.close();

       if (checkRDFEncoding(zipdir + "/"+ modelname))
       {
           File fz = new File(rootDirectory + "zip//" + zipName);
             if (fz.exists())
            fz.delete();
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(rootDirectory + "zip//" + zipName));
       
        // Compress the files
        for (File i : files) {
            System.out.println("files zipped :" + i);
            InputStream in = new FileInputStream(i);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(i.getName()));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
        }
        // Complete the ZIP file
        out.close();
        copyFile(rootDirectory + "zip//" + zipName, rootDirectory + zipName);
       }
       else
       {
           //nothing           
       }
    
    }
    
    
    public SimulationObjectModel recordAddedFiles(String zipName, List<String> addfiles, SimulationObjectModel model, String lfn, User user, String nwName,String zipFullPath, boolean bUpload) throws IOException, DataManagerException {

         String modelname = "";
        if (zipName == null) // to avoid crash for archive without rdf file
        {
            zipName = model.getModelName() + ".rdf";
            modelname = zipName;
        }
        List<File> files = new ArrayList<File>();
        String rootDirectory = getZipPath(user, zipFullPath, bUpload);
        File zipFile = new File(rootDirectory + zipName);
        if (zipFile.exists())
                    System.out.println("zipname :" + zipFile);
        else
            zipFile.createNewFile();
        File zipdir = new File(rootDirectory + "/zip/");
        if (!zipdir.exists()) {
            zipdir.mkdirs();
        }
        System.out.println("zipname :" + zipFile);
        System.out.println("zipdir :" + zipdir);
        copyFile(rootDirectory + zipName, zipdir + zipName);

        byte[] buf = new byte[1024];
       
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipdir + zipName));


        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            if (name.contains(".rdf")) {
                modelname = name;
            }

            FileOutputStream fileoutputstream;
            int n;
            System.out.println("Start to write");
            fileoutputstream = new FileOutputStream(zipdir + "/" + name);
            System.out.println("Writing " + zipdir + "/" + name);
            while ((n = zin.read(buf, 0, 1024)) > -1) {
                fileoutputstream.write(buf, 0, n);
            }
            fileoutputstream.close();
            zin.closeEntry();

            files.add(new File(zipdir + "/" + name));
            System.out.println("files present :" + rootDirectory + name);
            entry = zin.getNextEntry();
        }
        // Close the streams        
        zin.close();
        System.out.println("time to add " + addfiles.size());
        //copy additional file
        if (addfiles.size() > 0) {
            for (String file : addfiles) {
                copyFile(rootDirectory + file, zipdir + file);
                files.add(new File(zipdir + file));
                System.out.println("filex added :" + zipdir + file);
            }
        }
        //copy rdf.
        // Create a new model
       // SimulationObjectModel nwmodel = SimulationObjectModelFactory.createModel(model.getModelName());
        if(!nwName.isEmpty())
        {
            //model.setModelName(nwName);
            SimulationObjectModelFactory.setName(model, nwName);
            System.out.println("new name:" + nwName);
        }
        model.setModelOwner(user.getLastName());
        //model.setModelDescription(model.getModelName());
        //modelCopy(model, nwmodel);
        System.out.println("timepoint " + model.getTimepoints().size());

        if(modelname.isEmpty())
        {
            modelname = model.getModelName() + ".rdf";
            files.add(new File(zipdir  + "/"+ modelname ));
        }
        else
        {
            File f = new File(zipdir  + "/"+ modelname);
            if (f.exists())
                f.delete();
        }
        model.setStorageURL(lfn);
        SimulationObjectModelFactory.setStorageURL(model, lfn);
        SimulationObjectModelFactory.inferModelSemanticAxes(model);
       
        SimulationObjectModelFactory.dumpInFileModel(model,zipdir + "//"+ modelname);
        SimulationObjectModelFactory.completeModel(model);
        System.out.println("URI: " + model.getURI());

        File fz = new File(rootDirectory + "zip//" + zipName);
        if (fz.exists())
            fz.delete();
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(rootDirectory + "zip//" + zipName));
       
        // Compress the files
        for (File i : files) {
            System.out.println("files zipped :" + i);
            InputStream in = new FileInputStream(i);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(i.getName()));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
        }
        // Complete the ZIP file
        out.close();
        copyFile(rootDirectory + "zip//" + zipName, rootDirectory + zipName);
        return model;
    }

    public SimulationObjectModel createModel(String modelName, String user) {

        SimulationObjectModel model = SimulationObjectModelFactory.createModel(modelName);
        String storageURL = "";
        model.setStorageURL(storageURL);
        SimulationObjectModelFactory.setStorageURL(model, storageURL);

        System.out.println("model owner :" + user);
        model.setModelOwner(user);
        SimulationObjectModelFactory.setModelOwner(model, user);

        String description = "Empty model";
        model.setModelDescription(description);
        SimulationObjectModelFactory.setModelDescription(model, description);

        Date now = new Date();
        model.setLastModificationDate(now);
        SimulationObjectModelFactory.setLastModificationDate(model, now);
        //addTimePoint(model, now);
        //addInstant(model,0);
        return model;
    }

    public SimulationObjectModel.ObjectType getObjectType(String objectName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
     public SimulationObjectModel setDescription(SimulationObjectModel model, String description )
     {
         model.setModelDescription(description);
         return model;
     }

    public List<SimulationObjectModelLight> listAllModels() throws BusinessException {

        try {
            return SimulationObjectModelQueryer.getAllModels();

        } catch (Exception ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public SimulationObjectModel getADAM() {
        try {
            return SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile("/tmp/adam.rdf", false);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

        public void completeModel(SimulationObjectModel som) {
        SimulationObjectModelFactory.inferModelSemanticAxes(som);
        SimulationObjectModelFactory.completeModel(som);
    }

     public String getURLFromURI(String uri)
     {
            return SimulationObjectModelFactory.getURLFromURI(uri);
     }
     
    public SimulationObjectModel rebuildObjectModelFromTripleStore(String uri) {
        return SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri);
    }

    public SimulationObjectModel rebuildObjectModelFromAnnotationFile(String fileName, String user) {
        try {
            System.out.println(fileName);
            if(checkEmptyRDF(fileName))
            {
                String nwname = fileName.substring(0,fileName.indexOf(".rdf"))+ "_copy.rdf";
                SimulationObjectModelFactory.deepCopyModelFromAnnotationFile(fileName, nwname);
                System.out.println(nwname);
                return SimulationObjectModelFactory.rebuildObjectModelFromAnnotationFile(nwname, true);
            }
            else
            {
               return createModel("Empty", user);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public SimulationObjectModel setStorageUrl(SimulationObjectModel som, String url) {
        System.out.println("url" + url);
     //   System.out.println("URI " + som.getURI());
        som.setStorageURL(url);
        SimulationObjectModelFactory.setStorageURL(som, url);
        return som;
    }

    public void deleteAllModelsInTheTripleStore() {
        SimulationObjectModelFactory.deleteAllModelsInPersistentStore();
    }

    public void deleteModel(String uri) throws BusinessException {
        try {
            System.out.println("Deleting model with uri " + uri);
            SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri);
            SimulationObjectModelFactory.deleteModelInPersistentStore(som);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    public List<SimulationObjectModelLight> searchModels(String query,
            String[] types, String[] time) throws BusinessException {

        boolean anat = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("anatomical")) {
                anat = true;
            }
        }

        boolean path = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("pathological")) {
                path = true;
            }
        }

        boolean foreign = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("foreign object")) {
                foreign = true;
            }
        }

        boolean external = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("external agent")) {
                external = true;
            }
        }

        boolean longi = false;
        for (int i = 0; i < time.length; i++) {
            if (time[i].toLowerCase().equals("longitudinal follow-up")) {
                longi = true;
            }
        }

        boolean move = false;
        for (int i = 0; i < time.length; i++) {
            if (time[i].toLowerCase().equals("movement")) {
                move = true;
            }
        }

        boolean geom = false;
        for (int i = 0; i < types.length; i++) {
            if (types[i].toLowerCase().equals("geometrical")) {
                geom = true;
            }
        }
        ArrayList<SimulationObjectModelLight> result = new ArrayList<SimulationObjectModelLight>();

        List<SimulationObjectModelLight> somls = listAllModels();
        for (SimulationObjectModelLight soml : somls) {
            SimulationObjectModel som = SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(soml.getURI());

//            System.out.println("looking at SOML " + soml.getModelName());
//
//            if (soml.isLongitudinal()) {
//                System.out.println("SOML is longitudinal");
//            }
//            if (soml.isMoving()) {
//                System.out.println("SOML is moving");
//            }
//
//            if (soml.getSemanticAxes()[0]) {
//                System.out.println("SOML is anatomical");
//            }
//
//            if (soml.getSemanticAxes()[1]) {
//                System.out.println("SOML is pathological");
//            }
//            if (soml.getSemanticAxes()[2]) {
//                System.out.println("SOML is geometricaal");
//            }
//            if (soml.getSemanticAxes()[3]) {
//                System.out.println("SOML is foreign");
//            }
//
//            if (soml.getSemanticAxes()[4]) {
//                System.out.println("SOML is external");
//            }

            if ((soml.isLongitudinal() || !longi)) {

                if (soml.isMoving() || !move) {
                    if (soml.getSemanticAxes()[0] || !anat) {
                        if (soml.getSemanticAxes()[1] || !path) {
                            if (soml.getSemanticAxes()[3] || !foreign) {
                                if (soml.getSemanticAxes()[2] || !geom) {
                                    if (soml.getSemanticAxes()[4] || !external) {

                                        for (Timepoint t : som.getTimepoints()) {
                                            for (Instant it : t.getInstants()) {
                                                for (ObjectLayer ol : it.getObjectLayers()) {
                                                    for (ObjectLayerPart olp : ol.getLayerParts()) {
                                                        if (matches(olp.getReferredObject().getObjectName().replace("_", " ") + " (" + olp.getFormat() + ": ", query)) {
                                                            if (!result.contains(soml)) {
                                                                result.add(soml);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        // System.out.println("Don't include " + soml.getModelName() + " because it has no exernal agent");
                                    }
                                } else {
                                    // System.out.println("Don't include " + soml.getModelName() + " because it has no geometrical object");
                                }
                            } else {
                                // System.out.println("Don't include " + soml.getModelName() + " because it has no foreign body");
                            }
                        } else {
                            // System.out.println("Don't include " + soml.getModelName() + " because it's has not pathology");
                        }
                    } else {
                        // System.out.println("Don't include " + soml.getModelName() + " because it has no anatomy");
                    }
                } else {
                    //System.out.println("Don't include " + soml.getModelName() + " because it's not moving");
                }

            } else {
                // System.out.println("Don't include " + soml.getModelName() + " because it's not longitudinal");
            }
        }
        return result;
    }

    private boolean matches(String target, String query) {
        return ((target.toLowerCase().indexOf(query.toLowerCase()) != -1) || (query.toLowerCase().indexOf(target.toLowerCase()) != -1));
    }

    public String getStorageURL(String uri) throws BusinessException {
        try {
            return SimulationObjectModelFactory.rebuildObjectModelFromTripleStore(uri).getStorageURL();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public SimulationObjectModel addTimePoint(SimulationObjectModel som, Date d) {
        System.out.println("date:" + d.toString());
        System.out.println("model :" + som.getTimepoints().size());
        Timepoint tp = SimulationObjectModelFactory.createAndAddTimepoint(som, d);
        som.addTimepoint(tp);
        return som;
    }

    public SimulationObjectModel addInstant(SimulationObjectModel som, int tp) {
        System.out.println("instant to create tp :" + tp);
        Instant in = SimulationObjectModelFactory.createAndAddInstant(som.getTimepoint(tp), "PT0.001S");
        System.out.println("instant created");
        som.getTimepoint(tp).addInstant(in);
        System.out.println("instant added");

        return som;
    }

    public SimulationObjectModel renameTimepoint(SimulationObjectModel model, int tp, Date starting) {
        System.out.println("starting changed");
        ArrayList<Timepoint> tps = model.getTimepoints();
        tps.get(tp).setStartingDate(starting);
        model.setTimepoints(tps);
        return model;
    }

    public SimulationObjectModel renameInstant(SimulationObjectModel model, int tp, int ins, String duration) {
        System.out.println("duration changed");
        ArrayList<Instant> in = model.getTimepoint(tp).getInstants();
        in.get(ins).setDuration(duration);
        model.getTimepoint(tp).setInstants(in);
        return model;
    }

    public SimulationObjectModel duplicateTimePoint(SimulationObjectModel objectModel, int tp) {
        ArrayList<Timepoint> tps = objectModel.getTimepoints();
        Timepoint copy = SimulationObjectModelFactory.createAndAddTimepoint(objectModel, tps.get(tp).getStartingDate());
        timepointCopy(tps.get(tp), copy, objectModel, tp);
        tps.add(tp + 1, copy);
        objectModel.setTimepoints(tps);
        System.out.println("timepoint copied");
        return objectModel;
    }

    public SimulationObjectModel setInstantDuration(SimulationObjectModel objectModel, int tp, int ins, String duration) {
        ArrayList<Instant> in = objectModel.getTimepoint(tp).getInstants();
        in.get(ins).setDuration(duration);
        objectModel.getTimepoint(tp).setInstants(in);
        System.out.println("duration changed :" + duration);
        return objectModel;
    }

    public SimulationObjectModel duplicateInstant(SimulationObjectModel objectModel, int tp, int ins) {
        ArrayList<Instant> in = objectModel.getTimepoint(tp).getInstants();
        Instant copy = new Instant();
        instantCopy(in.get(ins), copy, objectModel, tp, ins);
        in.add(ins + 1, copy);
        objectModel.getTimepoint(tp).setInstants(in);
        System.out.println("instant copied");
        return objectModel;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Model copy
    ////////////////////////////////////////////////////////////////////////////
    private void modelCopy(SimulationObjectModel src, SimulationObjectModel dest) {
        int index = 0;
        for (Timepoint tp : src.getTimepoints()) {
            Timepoint tpdest;
            if (index == 0) {
                tpdest = dest.getTimepoint(0);
            } else {
                tpdest = SimulationObjectModelFactory.createAndAddTimepoint(dest, tp.getStartingDate());
            }
            timepointCopy(tp, tpdest, dest, index);
            if (index > 0) {
                dest.addTimepoint(tpdest);
            }
            index++;
        }
        
        index = 0;
        for (Timepoint tp : src.getTimepoints()) {
            timepointDeepCopy(tp, dest.getTimepoint(index), dest, index);
            index++;
        }
        
        
    }

    private void timepointCopy(Timepoint src, Timepoint dest, SimulationObjectModel model, int indtp) {
        int index = 0;
        for (Instant ins : src.getInstants()) {
            if (index > 0)
            {
               dest.addInstant(SimulationObjectModelFactory.createAndAddInstant(dest, ins.getDuration()));
            }
            index++;
        }
    }

    private void timepointDeepCopy(Timepoint src, Timepoint dest, SimulationObjectModel model, int indtp) {
        int index = 0; 
        for (Instant ins : src.getInstants()) {
            instantCopy(ins, dest.getInstant(index), model, indtp, index);
            index++;
        }
    }
    
    private void instantCopy(Instant src, Instant dest, SimulationObjectModel model, int indtp, int indins) {

        for (ObjectLayer obj : src.getObjectLayers()) {
            ObjectLayer objdest = SimulationObjectModelFactory.createObjectLayer(model,
                    indtp, indins, obj.getType(), Resolution.none);
            objectLayerCopy(obj, objdest);
            dest.addObjectLayer(objdest);
        }

        for (PhysicalParametersLayer ppl : src.getPhysicalParametersLayers()) {
            PhysicalParametersLayer ppldest = new PhysicalParametersLayer();
            PhysicalParametersLayerCopy(ppl, ppldest);
            dest.addPhysicalParametersLayer(ppl);
        }
        dest.setDuration(src.getDuration());
    }

    private void objectLayerCopy(ObjectLayer src, ObjectLayer dest) {


        for (ObjectLayerPart vx : src.getLayerParts(Format.voxel)) {
            addLayerPartToExistingLayer(dest, vx.getReferredObject().getObjectName(),
                    vx.getFileNames(), vx.getLabel(), Format.voxel, -1);
//            ObjectLayerPart vxdest = new ObjectLayerPart();
//            objectLayerPartCopy(vx,vxdest);
//            vxdest.setLabel(vx.getLabel());
//            vxdest.setParent(dest);
//            dest.addObjectLayerPart(vxdest);
        }

        for (ObjectLayerPart ms : src.getLayerParts(Format.mesh)) {
            addLayerPartToExistingLayer(dest, ms.getReferredObject().getObjectName(),
                    ms.getFileNames(), ms.getLabel(), Format.mesh, ms.getPriority());
//            ObjectLayerPart msdest = new ObjectLayerPart();
//            objectLayerPartCopy(ms,msdest);
//            msdest.setParent(dest);
//            msdest.setPriority(ms.getPriority());
//            dest.addObjectLayerPart(msdest);
        }

        for (PhysicalParameter pp : src.getPhysicalParameters()) {
            PhysicalParameter ppdest = SimulationObjectModelFactory.createPhysicalParameter(
                    pp.getType(), pp.getFileNames(), pp.getB0());
            dest.addPhysicalParameters(ppdest);
        }
        for (PhysicalParametersLayer ppl : src.getPhysicalParametersLayers()) {
            PhysicalParametersLayer ppldest = SimulationObjectModelFactory.createPhysicalParametersLayer(
                    ppl.getType(), ppl.getFileName(), ppl.getB0(),ppl.getExternalAgentName(), ppl.getUnitOfMeasure());
            SimulationObjectModelFactory.addPhysicalParametersLayerToObjectLayer(ppldest, dest);
            dest.addPhysicalParametersLayer(ppldest);
        }
    }

    private void objectLayerPartCopy(ObjectLayerPart src, ObjectLayerPart dest) {
        dest.setFileNames(src.getFileNames());
        dest.setFormat(src.getFormat());
        dest.setReferredObject(src.getReferredObject());
        dest.setType(src.getType());
    }

    private void PhysicalParametersLayerCopy(PhysicalParametersLayer src, PhysicalParametersLayer dest) {
        dest.setFileName(src.getFileName());
        dest.setB0(src.getB0());
        dest.setType(src.getType());
        //dest.setURI(src.setURI());
    }

    private void PhysicalParameterCopy(PhysicalParameter src, PhysicalParameter dest) {
        dest.setB0(src.getB0());
        dest.setFileNames(src.getFileNames());
        dest.setType(src.getType());
        //dest.setURI(dest.getURI());
    }

    public List<String[]> searchWithScope(String query, boolean[] scope) {
        List<String[]> action = new ArrayList<String[]>();

        //  String[][] resultats = new String[]();
        int i = 0;
        System.out.println(query);
        SimulationObjectMatch[] res = SimulationObjectSearcher.searchWithScope(query, scope);
        System.out.println(query);
        for (SimulationObjectMatch ii : res) {
            String[] resultatSs = new String[3];
            resultatSs[0] = ii.getObjectName();
            resultatSs[1] = ii.getType().toString();
            resultatSs[2] = String.valueOf(ii.getScore());
            action.add(resultatSs);
            i++;
        }
        System.out.println(query);
        return action;
    }

    public SimulationObjectModel addObject(SimulationObjectModel model, String ontoName, List<String> objName, int tp, int ins, int type, int label) {
        System.out.println("object to add");
        ArrayList<String> objects = new ArrayList<String>(objName);

        if (type == 0)//mesh type
        {

            System.out.println("mesh");
            System.out.println("ontologie" + ontoName + "object : " + objName.get(0));
            addObjectNoResolutionHandling(model, tp, ins, ontoName, objects, label, Format.mesh, -1);
        } else if (type == 1) {

            System.out.println("ontologie" + ontoName + "object : " + objects);
            addObjectNoResolutionHandling(model, tp, ins, ontoName, objects, label, Format.voxel, -1);
        } else {
        }
        return model;
    }
    
    public SimulationObjectModel addMap(SimulationObjectModel model, String name,
            int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int b0, String externalAgent, String unitOfMeasure) {
        
        ArrayList<String> filenames = new ArrayList<String>();
        filenames.add(name);

        PhysicalParametersLayer ppl = SimulationObjectModelFactory.createPhysicalParametersLayer(pptype, filenames, b0, externalAgent, unitOfMeasure);
        SimulationObjectModelFactory.addPhysicalParametersLayerToInstant(ppl, model.getInstant(tp, ins));
        model.getInstant(tp, ins).addPhysicalParametersLayer(ppl);
         
        return model;
    }

    public SimulationObjectModel addLUT(SimulationObjectModel model, SimulationObjectModel.ObjectType layer, String name, int tp, int ins, PhysicalParametersLayer.PhysicalParameterType pptype, int type) {
        ArrayList<ObjectLayer> aLayers = model.getInstant(tp, ins).getObjectLayers();
        int index = -1;
        for (ObjectLayer lay : aLayers) {
            index++;
            if (lay.getType() == layer) {
                break;
            }
        }
        ArrayList<String> objects = new ArrayList<String>();
        objects.add(name);
        if (type == 2) {
            addPhysicalParametersLUT(model, pptype, objects, -1, model.getInstant(tp, ins).getObjectLayers(index), tp, ins);
        } else if (type == 3) {
            addPhysicalParametersLayer(model, pptype, objects, -1, model.getInstant(tp, ins).getObjectLayers(index), tp, ins,"","");
        } else {
            //nothing
        }
        return model;
    }

    private static void addObjectNoResolutionHandling(SimulationObjectModel objectModel, int timePointIndex, int instantIndex, String objectName, ArrayList<String> fileName, int objectLabel, ObjectLayerPart.Format fileFormat, int objectPriority) {

        // found during fuzzy search (type field of SimulationObjectMatching
        //	SimulationObjectModelFactory.ObjectType objectType = SimulationObjectModelFactory.getObjectType(objectName); 

        // check if there is a layer of the type of the layer part we want to add
        // in this example we do not check the resolution type, we add it to the layer with resolution "none"
        ObjectLayer objectLayer = SimulationObjectModelUtil.getObjectLayerOfTypeOfResolution(objectModel, timePointIndex, instantIndex, SimulationObjectModelFactory.getObjectType(objectName), Resolution.none);

        // if the object layer of the needed type doesn't exist we create it and add the part
        if (objectLayer == null) {
            // create a new layer
            createLayerAndAddLayerPart(objectModel, timePointIndex, instantIndex, Resolution.none, objectName, fileName, objectLabel, fileFormat, objectPriority);
        } else {
            addLayerPartToExistingLayer(objectLayer, objectName, fileName, objectLabel, fileFormat, objectPriority);
        }
    }

    private static void addLayerPartToExistingLayer(ObjectLayer objectLayer, String objectName, ArrayList<String> fileName, int objectLabel, Format fileFormat, int objectPriority) {
        // call a servlet with the same prototype than the current method

        // the servlet will do :
        ObjectLayerPart objectLayerPart = SimulationObjectModelFactory.createObjectLayerPart(objectName, fileName, objectLabel, fileFormat, objectPriority);
        SimulationObjectModelFactory.addObjectLayerPart(objectLayer, objectLayerPart);

        // the servlet will return an object layer part we need to add the part to the layer
        objectLayer.addObjectLayerPart(objectLayerPart);
    }

    public static void createLayerAndAddLayerPart(SimulationObjectModel objectModel, int timePointIndex, int instantIndex, Resolution layerResolution, String objectName, ArrayList<String> fileName, int objectLabel, Format fileFormat, int objectPriority) {
        // call a servlet with the same prototype than the current method

        // the servlet will do :
        ObjectLayerPart objectLayerPart = SimulationObjectModelFactory.createObjectLayerPart(objectName, fileName, objectLabel, fileFormat, objectPriority);
        ObjectLayer objectLayer = SimulationObjectModelFactory.createObjectLayerAndAddLayerPart(objectModel, timePointIndex, instantIndex, layerResolution, objectLayerPart);
        objectLayer.addObjectLayerPart(objectLayerPart);

        // the servlet will return an object layer, we just need to add to the current timepoint/index
        objectModel.getInstant(timePointIndex, instantIndex).addObjectLayer(objectLayer);
    }

    /**
     * Remove a timepoint
     *
     * @param objectModel model to modify
     * @param tp timepoint to remove
     * @return the updated model
     */
    public SimulationObjectModel removeTimePoint(SimulationObjectModel objectModel, int tp) {
        System.out.println("timepoint to remove" + tp);
        ArrayList<Timepoint> timep = objectModel.getTimepoints();
        System.out.println("timepoint to remove selected size " + timep.size());
        SimulationObjectModelFactory.removeTimepoint(timep.get(tp));
        timep.remove(tp);
        objectModel.setTimepoints(timep);
        System.out.println("timepoint removed");
        return objectModel;
    }

    /**
     * Remove an instant from a specific timepoint
     *
     * @param objectModel model to modify
     * @param tp timepoint selected
     * @param ins instant to remove
     * @return the updated model
     */
    public SimulationObjectModel removeInstant(SimulationObjectModel objectModel, int tp, int ins) {
        ArrayList<Instant> in = objectModel.getTimepoint(tp).getInstants();
        SimulationObjectModelFactory.removeInstant(in.get(ins));
        in.remove(ins);
        objectModel.getTimepoint(tp).setInstants(in);
        System.out.println("instant removed");
        return objectModel;
    }

    /**
     * Remove an layer from a specific instant
     *
     * @param objectModel model to modify
     * @param tp timepoint selected
     * @param ins instant selected
     * @param layer layer to remove
     * @return the updated model
     */
    public SimulationObjectModel removeLayer(SimulationObjectModel objectModel, int tp, int ins, String layer) {

        layer = layer.replace(" ", "_");
        layer = layer.toLowerCase();
        ArrayList<ObjectLayer> layers = new ArrayList<ObjectLayer>();
        System.out.println("remove this type " + layer);
        layers = objectModel.getTimepoint(tp).getInstant(ins).getObjectLayers();
        int ind = 0;
        System.out.println("layers size " + layer);
        for (ObjectLayer lay : layers) {
            System.out.println("layer type : " + lay.getType());
            if (lay.getType().toString().equals(layer)) {
                layers.remove(lay);
                break;
            }
            ind++;
        }
        objectModel.getTimepoint(tp).getInstant(ins).removeObjectLayer(ind);
        System.out.println("layer removed");
        return objectModel;
    }
    
     /**
     * Remove all objects from a specific layer
     *
     * @param model model to modify
     * @param tp timepoint selected
     * @param ins instant selected
     * @param layer layer selected
     * @return the updated model
     */
     public SimulationObjectModel removeObjects(SimulationObjectModel model, int tp, int ins, String layer) {
        ArrayList<ObjectLayer> layers = model.getTimepoint(tp).getInstant(ins).getObjectLayers();
        ObjectLayer ind = null;// = new ObjectLayer();
        int j = 0;
        for (ObjectLayer lay : layers) {
            if (lay.getType().toString().equals(layer)) {
                ind = lay;
                break;
            }
            j++;
        }

        ArrayList<ObjectLayerPart> parts = ind.getLayerParts();
        parts.clear();
        ind.setLayerParts(parts);
        layers.set(j, ind);
        model.getTimepoint(tp).getInstant(ins).setObjectLayers(layers);

        System.out.println("all objects removed");
        return model;
    }

      /**
     * Remove all physical paramters from a specific layer
     *
     * @param model model to modify
     * @param tp timepoint selected
     * @param ins instant selected
     * @param layer layer selected
     * @return the updated model
     */
     public SimulationObjectModel removePhysicals(SimulationObjectModel model, int tp, int ins, String layer) {
        ArrayList<ObjectLayer> layers = model.getTimepoint(tp).getInstant(ins).getObjectLayers();
        ObjectLayer ind = null;// = new ObjectLayer();
        int j = 0;
        for (ObjectLayer lay : layers) {
            if (lay.getType().toString().equals(layer)) {
                ind = lay;
                break;
            }
            j++;
        }

        ArrayList<PhysicalParametersLayer> parts = ind.getPhysicalParametersLayers();
        parts.clear();
        ind.setPhysicalParametersLayers(parts);
        layers.set(j, ind);
        model.getTimepoint(tp).getInstant(ins).setObjectLayers(layers);

        System.out.println("all physical parameters removed");
        return model;
    }

     
     
    /**
     * Remove an object from a specific layer
     *
     * @param objectModel model to modify
     * @param tp timepoint selected
     * @param ins instant selected
     * @param layer layer selected
     * @param name object name to remove
     * @return the updated model
     */
    public SimulationObjectModel removeObject(SimulationObjectModel objectModel, int tp, int ins, String layer, String name) {
      
        ArrayList<ObjectLayer> layers = objectModel.getTimepoint(tp).getInstant(ins).getObjectLayers();
        ObjectLayer ind = null;// = new ObjectLayer();
        int j = 0;
        for (ObjectLayer lay : layers) {
            if (lay.getType().toString().equals(layer)) {
                ind = lay;
                break;
            }
            j++;
        }
        ArrayList<ObjectLayerPart> parts = ind.getLayerParts();
        int i = 0;
        for (ObjectLayerPart part : parts) {
            ArrayList<String> files = part.getFileNames();
            boolean bremove = false;
            for (String file : files) {
                if (file.contains(name)) {
                    bremove = true;
                    files.remove(file);
                    break;
                }
            }
            if (bremove) {
                part.setFileNames(files);
                parts.set(i, part);
                ind.setLayerParts(parts);
                layers.set(j, ind);
                objectModel.getTimepoint(tp).getInstant(ins).setObjectLayers(layers);
            }
            i++;
        }
        System.out.println("object removed");
        return objectModel;
    }
    
    
     /**
     * Remove an object from a specific layer
     *
     * @param objectModel model to modify
     * @param tp timepoint selected
     * @param ins instant selected
     * @param layer layer selected
     * @param name object name to remove
     * @return the updated model
     */
    public SimulationObjectModel removePhysical(SimulationObjectModel objectModel, int tp, int ins, String layer, PhysicalParametersLayer.PhysicalParameterType type) {
      
        ArrayList<ObjectLayer> layers = objectModel.getTimepoint(tp).getInstant(ins).getObjectLayers();
        ObjectLayer ind = null;// = new ObjectLayer();
        int j = 0;
        for (ObjectLayer lay : layers) {
            if (lay.getType().toString().equals(layer)) {
                ind = lay;
                break;
            }
            j++;
        }
        ArrayList<PhysicalParametersLayer> parts = ind.getPhysicalParametersLayers();
        int i = 0;
        PhysicalParametersLayer ppl = null;
        for (PhysicalParametersLayer part : parts) {
            PhysicalParametersLayer.PhysicalParameterType pptype = part.getType();
            boolean bremove = false;
            if (pptype.equals(type)) {
                    ppl = part;
                    break;
                }
            i++;
        }
        parts.remove(i);
        ind.setPhysicalParametersLayers(parts);
        layers.set(j, ind);
        objectModel.getTimepoint(tp).getInstant(ins).setObjectLayers(layers);
        System.out.println("physical parameter removed");
        return objectModel;
    }

	
    public static void addPhysicalParametersLayer(SimulationObjectModel objectModel, PhysicalParametersLayer.PhysicalParameterType physicalParametersType, ArrayList<String> fileName, double b0, ObjectLayer objectLayer, int timePointIndex, int instantIndex, String externalAgentName, String unitOfMeasure) {

        // list the object layers of the current instant
        // ask to the user if we add the physical layer to an object layer or the instant (i.e. the model)

        // call the servlet to create the physical parameters layer
        // parameters : physical paramter type, filename
        PhysicalParametersLayer physicalParametersLayer = SimulationObjectModelFactory.createPhysicalParametersLayer(physicalParametersType, fileName, b0, externalAgentName, unitOfMeasure);
		
        // if the user want to add it to the instant (not linked to an object layer)
        // in this example 0,0
        if (objectLayer == null) {
            SimulationObjectModelFactory.addPhysicalParametersLayerToInstant(physicalParametersLayer, objectModel.getInstant(timePointIndex, instantIndex));
            objectModel.getInstant(timePointIndex, instantIndex).addPhysicalParametersLayer(physicalParametersLayer);
        } else {
            // or to an object layer
            // in this example the first layer of instant 0,0 (linked to an object layer)
            SimulationObjectModelFactory.addPhysicalParametersLayerToObjectLayer(physicalParametersLayer, objectLayer);
            objectLayer.addPhysicalParametersLayer(physicalParametersLayer);
            //objectModel.getInstant(timePointIndex, instantIndex).getObjectLayers(0).addPhysicalParametersLayer(physicalParametersLayer);
        }
    }

    public static void addPhysicalParametersLUT(SimulationObjectModel objectModel, PhysicalParametersLayer.PhysicalParameterType physicalParametersType, ArrayList<String> fileName, double b0, ObjectLayer objectLayer, int timePointIndex, int instantIndex) {
        PhysicalParameter physicalParameter = SimulationObjectModelFactory.createPhysicalParameter(physicalParametersType, fileName, b0);
        objectLayer.addPhysicalParameters(physicalParameter);
        SimulationObjectModelFactory.addPhysicalParametersToObjectLayer(objectLayer, physicalParameter);
    }

    private String getZipPath(User user,String zipFullPath, boolean bUpload) throws DataManagerException
    {
        String rootDirectory = "";
            if(bUpload)
            {
                rootDirectory = Server.getInstance().getDataManagerPath() + "/uploads/";
            }
            else
            {
                String remotePath = DataManagerUtil.parseBaseDir(user, zipFullPath);
                rootDirectory = Server.getInstance().getDataManagerPath()
                    + "/downloads" + FilenameUtils.getFullPath(remotePath);
                System.out.println("model path " + zipFullPath);
                System.out.println("remote path " + remotePath);
                System.out.println("full path " + rootDirectory);
            }
        return rootDirectory;
    }
    public String extractRaw(String name, String zipname, User user, String zipFullPath, boolean bUpload) throws DataManagerException {
        
        String rootDirectory = getZipPath(user, zipFullPath,bUpload);
         
        File dir = new File(rootDirectory);
        boolean bfound = false;
        System.out.println("zip :" + zipname);
        System.out.println("name" + name);
        String raw = "";
        for (String fi : dir.list()) {
            if (fi.equals(name)) {
                bfound = true;
                break;
            }

        }
        if (bfound) {
            raw = extractRawfromFile(rootDirectory + name);
        } else {
            raw = extractRawfromZipFile(name, rootDirectory + zipname);
        }
        return raw;
    }

    public String extractRawfromFile(String name) {
        String result = "";
        FileInputStream fstream = null;
        try {

            fstream = new FileInputStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("ElementDataFile")) {
                    result = line.substring(line.indexOf("=")).replace("=", "").replace(" ", "");
                    break;
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public String extractRawfromZipFile(String name, String zipname) {
        ZipInputStream zipinputstream = null;
        String result = "";
        System.out.println(zipname);
        try {

            zipinputstream = new ZipInputStream(new FileInputStream(zipname));
            ZipEntry zipentry = zipinputstream.getNextEntry();
            ArrayList<String> files = new ArrayList<String>();
            byte[] buf = new byte[1024];
            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                if (entryName.equals(name)) { //extract only the annotations.
                    FileOutputStream fileoutputstream;
                    fileoutputstream = new FileOutputStream(
                            zipname + "_temp");
                    int n = 0;
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                        fileoutputstream.write(buf, 0, n);
                    }
                    fileoutputstream.close();
                    result = extractRawfromFile(zipname + "_temp");
                    System.out.println("result : " + result);
                    zipinputstream.closeEntry();
                    File f = new File(zipname + "_temp");
                    f.delete();
                    break;
                }
                zipentry = zipinputstream.getNextEntry();
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zipinputstream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ModelBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public String readLicense(String file) throws FileNotFoundException, IOException {
        String license = "";

        File f = new File(file);
        InputStream ips = new FileInputStream(file);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String tp;
        while ((tp = br.readLine()) != null) {
            license += tp;
        }
        br.close();
        ips.close();

        return license;
    }
}
