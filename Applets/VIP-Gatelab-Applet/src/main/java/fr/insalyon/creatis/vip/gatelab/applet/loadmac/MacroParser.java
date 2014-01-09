/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.gatelab.applet.loadmac;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Sorina Camarasu, Rafael Ferreira da Silva
 */
public class MacroParser {

    private File mainMacroVRL;
    private String baseVRL;
    private List<String> macroFiles;
    private List<String> inputs;
    private List<String> outputs;
    private List<String> outputType;
    private List<String> unidentifiedFiles;
    private List<String> phaseSpaceFiles;
    private String nparticles;
    private String nseconds;
    private String nstart;
    private String nstop;
    // private SimuLogger sl;
    private Boolean statique;
    private Boolean alias;
    private Boolean visu;
    private Boolean setMaxFileSize;
    private Boolean autoEngineSeed;
    private String separator;

    // private String macFileContent;
    public MacroParser(File macroVRL) throws GateException {
        this.mainMacroVRL = macroVRL;

        this.separator = "/";
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            this.separator = "\\";
        }

        baseVRL = macroVRL.getParentFile().getParent().concat(this.separator);

        this.macroFiles = new ArrayList<String>();
        this.inputs = new ArrayList<String>();
        this.outputs = new ArrayList<String>();
        this.outputType = new ArrayList<String>();
        this.unidentifiedFiles = new ArrayList<String>();
        this.phaseSpaceFiles = new ArrayList<String>();

        this.nparticles = "unknown";
        this.nseconds = "unknown";
        this.nstart = "unknown";
        this.nstop = "unknown";
        // this.sl = logger;
        this.statique = false;
        this.alias = false;
        this.visu = false;
        this.setMaxFileSize = false;
        this.autoEngineSeed = false;

        String dirname = mainMacroVRL.getParentFile().getName().toString();

        System.out.println("MacroParser COnstructor 2, dirname is " + dirname);
        // TODO Sorina : print this as a message to the user and ask the user
        // for another entry (don't go further)
        if (dirname.compareTo("mac") != 0) {
            System.out.println("**************the macro file is not placed in the mac dir********************");
            throw new GateException(
                    "The macro file you've chosen is not placed in a 'mac' folder. Please choose another file. We remind you that all mac files must belong to a 'mac' folder, all inputs to a 'data' folder' and all outputs to an 'output' folder");
        }

        this.macroFiles.add(macroVRL.toString());

    }

    public void parseMacroFiles() throws GateException, FileNotFoundException,
            IOException {

        for (int i = 0; i < this.macroFiles.size(); i++) {
            // System.out.println("macroFiles.size is "+this.macroFiles.size());
            String macroVRL = this.macroFiles.get(i);
            System.out.println("**************Processing macro file " + macroVRL);
            processLineByLine(macroVRL);

        }

    }

    public boolean isParsingMacroFiles() throws GateException,
            FileNotFoundException, IOException {
        parseMacroFiles();
        return false;
    }

    public void copyMacsAndInputs(String simuVRL) throws IOException, GateException {
        // VFSClient vc = new VFSClient();
        File simuDir = new File(simuVRL);
        File dataDir = new File(simuVRL.concat(this.separator).concat("data"));
        File macroDir = new File(simuVRL.concat(this.separator).concat("mac"));

        if (!simuDir.exists()) {
            System.out.print("Creating folder " + simuDir);
            simuDir.mkdir();
        }
        if (!dataDir.exists()) {
            System.out.print("Creating folder " + dataDir);
            dataDir.mkdir();
        }
        if (!macroDir.exists()) {
            System.out.print("Creating folder " + macroDir);
            macroDir.mkdir();
        }

        System.out.print("Preparing new simu \n");

        for (int i = 0; i < this.macroFiles.size(); i++) {
            File mac_file = new File(macroFiles.get(i));
            if (!dangerous(mac_file)) {
                FileUtilities.copyFile(mac_file, new File(macroDir, mac_file.getName()));
            } else {
                throw new GateException(
                        "File \""
                        + mac_file.getName()
                        + "\" is considered harmful. Please remove it from your simulation.");
            }
        }

        for (int i = 0; i < this.inputs.size(); i++) {
            File input_file = new File(inputs.get(i));
            if (!dangerous(input_file)) {
                FileUtilities.copyFile(input_file, new File(dataDir, input_file.getName()));
            } else {
                throw new GateException(
                        "File \""
                        + input_file.getName()
                        + "\" is considered harmful. Please remove it from your simulation.");
            }
        }

    }

    public void handleMacFile(String macFile) throws GateException {
        String macVRL;
        // String[] result = macFile.split("\\/");
        // String[] result = getPathElements(macFile);
        String dirname = (new File(macFile)).getParentFile().getName().toString();
        if ((dirname != null) && dirname.equals("mac")) {
            macVRL = this.baseVRL.concat(macFile);
            if (!this.macroFiles.contains(macVRL)) {
                this.macroFiles.add(macVRL);
                /*
                 * if (this.sl != null) { sl.printMessage("Macro file *** " +
                 * macFile + " *** added"); }
                 */
                System.out.println("Macro file *** " + macFile + " *** added");
            }
        } else {
            System.out.println("**************the macro file " + macFile
                    + "is not placed in the mac dir********************");
            throw new GateException(
                    "The macro file "
                    + macFile
                    + "included from the main macro file is not placed in the mac folder. Please add this file to the mac folder, edit the macro file accordingly and start again");
            // TODO Sorina : print this as a message to the user and ask the
            // user for another entry (don't go further)

        }
    }

    public void handleInputs(String input) throws GateException {
        String inputVRL;
        String dirname = (new File(input)).getParentFile().getName().toString();
        // if ((result.length == 2) && result[0].equals("data")) {
        if ((dirname != null) && dirname.equals("data")) {
            inputVRL = this.baseVRL.concat(input);
            if (!this.inputs.contains(inputVRL)) {
                this.inputs.add(inputVRL);
                /*
                 * if (this.sl != null) { sl.printMessage("Input file *** " +
                 * inputVRL.toString() + " *** added"); }
                 */

                System.out.println("I've just added " + inputVRL
                        + " to the inputs");

                if (input.endsWith(".hdr") || input.endsWith(".h33") || input.endsWith(".mhd") || input.endsWith(".iff")) {
                    String extension = input.substring(input.length() - 3);
                    File srcNode = new File(inputVRL);
                    // String
                    // baseFileName=input.substring(0,srcNode.getName().toString().indexOf(".hdr"));
                    String baseFileName = srcNode.getName().toString();
                    File[] filesInNode = srcNode.getParentFile().listFiles();
                    for (int i = 0; i < filesInNode.length; i++) {
                        // System.out.println("Listing File "+i+" *** "
                        // +filesInNode[i].toString() +
                        // " ***, basename is "+baseFileName);
                        if (filesInNode[i].isFile()
                                && filesInNode[i].toString().contains(
                                baseFileName.substring(0, baseFileName.lastIndexOf(extension)))
                                && !filesInNode[i].toString().endsWith(extension)) {

                            String inputVRL1 = filesInNode[i].toString();
                            this.inputs.add(inputVRL1);
                            System.out.println("Input file *** " + inputVRL1
                                    + " *** added");

                        }
                    }
                    /*
                     * VRL inputVRL1 = this.baseVRL.append(newName);
                     * this.inputs.add(inputVRL1); if(this.sl!=null){
                     * sl.printMessage("Input file *** "
                     * +inputVRL1.getBasename() + " *** added"); }
                     */
                }
                // }
            }
        } else {
            System.out.println("**************the input file " + input
                    + "is not placed in the data dir********************");
            throw new GateException(
                    "The input file "
                    + input
                    + " given in the main macro file is not placed in the data folder. Please add this file to the data folder, edit the macro file accordingly and start again");
        }
    }

    public void handleOutputs(String output) throws GateException {
        // String[] result = output.split("\\/");
        // String[] result = getPathElements(output);
        // if ((result.length == 2) && result[0].equals("output")) {
        String dirname = (new File(output)).getParentFile().getName().toString();
        if ((dirname != null) && dirname.equals("output")) {
            if (!this.outputs.contains(output)) {
                this.outputs.add(output);
                /*
                 * if (this.sl != null) { sl.printMessage("Detected output *** "
                 * + output + " *** "); }
                 */
                System.out.println("Detected output *** " + output + " *** ");
                // System.out.println("I've just added "+output+" to the outputs");
                String[] result1 = output.split("\\.");

                if ((result1.length == 2)
                        && (!this.outputType.contains(result1[1]))) {
                    this.outputType.add(result1[1]);

                    // System.out.println("I've just added "+result1[1]+" to the outputType");
                }
            }
        } else {
            // TODO Sorina : print this as a message to the user and ask the
            // user for another entry (don't go further)
            System.out.println("**************the output file " + output
                    + "is not placed in the output dir********************");
            throw new GateException(
                    "The input file "
                    + output
                    + " given in the main macro file does not respect the imposed folder structure. It must be placed in an output folder. Example: output/outputFile. Please edit the macro file accordingly and start again");
        }
    }

    public void handleEngineSeed(String seed) {
        if (seed.equalsIgnoreCase("auto")) {
            this.autoEngineSeed = true;
            System.out.println("Found setEngineSeed auto");
        } else {
            System.out.println("Found setEngineSeed" + seed);
        }

    }

    public void handleParticles(int n, String part) {

        this.nparticles = part;
        if (n == 1) {
            System.out.println(" *** Found /run/beamOn " + part + " *** ");
        } else {
            if (n == 2) {
                System.out.println(" *** Found /gate/application/SetTotalNumberOfPrimaries "
                        + part + " *** ");
            } else {
                if (n == 3) {
                    System.out.println(" *** Found /gate/TNP_factice "
                            + part + " ***. Simulation must be static");
                    this.statique = true;
                }
            }
        }

    }

    public void handleSeconds(int n, String sec) throws GateException {

        try {
            if (n == 1) {
                this.nseconds = sec;
                System.out.println(" this.nseconds " + this.nseconds + " *** ");
            } else {
                if (n == 2) {
                    this.nstart = sec;
                    System.out.println(" this.nstart " + this.nstart + " *** ");
                } else {
                    if (n == 3) {
                        this.nstop = sec;
                        System.out.println(" this.nstop " + this.nstop + " *** ");
                    }
                }
            }
            // if we detect a time pattern we check if the simulation must be static
            if ((this.nseconds.compareTo("unknown") != 0) || (this.nstart.compareTo("unknown") != 0) || (this.nstop.compareTo("unknown") != 0)) {
                System.out.println("Time found: forcing parallelization type to static");
                this.statique = true;
            }
        } catch (Exception e) {
            throw new GateException("handleSeconds exception " + e.getMessage());
        }

    }

    public void processLineByLine(String vfsMacroPath) throws GateException,
            FileNotFoundException, IOException {
        // String macFileContent;
        Scanner scanner = null;
        Pattern delimiter = Pattern.compile("[\\s]+");
        // Pattern mac_pattern = Pattern.compile("control/execute");
        Pattern mac_pattern = Pattern.compile("mac/[A-Za-z0-9\\.\\-\\_]+");
        Pattern input_pattern = Pattern.compile("data/[A-Za-z0-9\\.\\-\\_]+");
        Pattern output_pattern = Pattern.compile("output/[A-Za-z0-9\\.\\-\\_]+");
        Pattern engineSeed_pattern = Pattern.compile("/gate/random/setEngineSeed");
        Pattern phaseSpace_pattern = Pattern.compile("/[A-Za-z0-9\\.\\-\\_/]+addPhaseSpaceFile");
        Pattern events_pattern1 = Pattern.compile("/run/beamOn");
        Pattern events_pattern2 = Pattern.compile(
                "/gate/application/setTotalNumberOfPrimaries");
        Pattern events_pattern3 = Pattern.compile("/gate/TNP_factice",
                Pattern.CASE_INSENSITIVE);

        Pattern time_pattern = Pattern.compile(
                "/gate/application/setTimeSlice", Pattern.CASE_INSENSITIVE);

        Pattern time_start = Pattern.compile(
                "/gate/application/setTimeStart", Pattern.CASE_INSENSITIVE);
        Pattern time_stop = Pattern.compile(
                "/gate/application/setTimeStop", Pattern.CASE_INSENSITIVE);

        Pattern file_pattern = Pattern.compile("[_a-zA-Z0-9\\-\\./]+\\.[_a-zA-Z][_a-zA-Z0-9\\-\\.]+", Pattern.CASE_INSENSITIVE);

        // scanner = new Scanner(macFileContent);
        //System.out.println("Begin parsing");
        //TODO: use a HashMap instead of al the if/elses below
        scanner = new Scanner(new FileInputStream(vfsMacroPath));
        String macFile = null;
        String input = null;
        String output = null;
        String engineSeed = null;
        String events = null;
        String seconds = null;
        String currentLine = null;
        String currentLineTrim = null;
        String ps = null;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentLineTrim = currentLine.trim();
            if (!currentLineTrim.startsWith("#")) {
                // macFile=processLine(mac_pattern, delimiter, currentLine, 1 );
                /*if (currentLine.contains("{")) {
                 alias = true;
                 break;
                 } else {
                 * 
                 */
                if (currentLineTrim.startsWith("/vis/") && !currentLine.contains("disable")) {
                    visu = true;
                    break;
                } else {
                    if (currentLineTrim.contains("setMaxFileSize")) {
                        this.setMaxFileSize = true;
                       // break;
                    } else {
                        macFile = processLine(mac_pattern, delimiter, currentLine, 0);
                    }
                    if (macFile != null) {
                        handleMacFile(macFile);
                    } else {
                        //phaseSpace files should not be copied as other input files
                        ps = processLine(phaseSpace_pattern, delimiter, currentLine, 1);
                        if (ps != null) {
                            this.phaseSpaceFiles.add(ps);
                        }else{
                            input = processLine(input_pattern, delimiter, currentLine,
                                    0);
                            if (input != null) {
                                handleInputs(input);
                            } else {
                                output = processLine(output_pattern, delimiter,
                                        currentLine, 0);
                                if (output != null) {
                                    handleOutputs(output);
                                } else {
                                    engineSeed = processLine(engineSeed_pattern, delimiter, currentLine, 1);
                                    if (engineSeed != null) {
                                        handleEngineSeed(engineSeed);
                                    } else {
                                        events = processLine(events_pattern1, delimiter,
                                                currentLine, 1);
                                        if (events != null) {
                                            handleParticles(1, events);
                                            // nparticles=events;
                                            // System.out.println("events number is "+nparticles);
                                        } else {
                                            events = processLine(events_pattern2,
                                                    delimiter, currentLine, 1);
                                            if (events != null) {
                                                handleParticles(2, events);
                                            } else {
                                                events = processLine(events_pattern3,
                                                        delimiter, currentLine, 1);
                                                if (events != null) {
                                                    handleParticles(3, events);
                                                } else {
                                                    seconds = processLine(time_pattern,
                                                            delimiter, currentLine, 1);
                                                    if (seconds != null) {
                                                        handleSeconds(1, seconds);
                                                    } else {
                                                        seconds = processLine(time_start,
                                                                delimiter, currentLine, 1);
                                                        if (seconds != null) {
                                                            handleSeconds(2, seconds);
                                                        } else {
                                                            seconds = processLine(time_stop,
                                                                    delimiter, currentLine, 1);
                                                            if (seconds != null) {
                                                                handleSeconds(3, seconds);
                                                            } else {

                                                                String fname = processLine(file_pattern, delimiter, currentLine, 0);
                                                                if (fname != null) {
                                                                    this.unidentifiedFiles.add(fname);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //}
            }
        }

        if (scanner != null) {
            scanner.close();
        }
        //System.out.println("End parsing");
        /*
         * finally { //ensure the underlying stream is always closed
         * if(scanner!=null) scanner.close(); }
         */

    }

    protected String processLine(Pattern toLookFor, Pattern delimiter,
            String aLine, int place) {
        // use a second Scanner to parse the content of each line
        Scanner scanner = new Scanner(aLine);
        String value = null;
        // scanner.next(pattern);
        scanner.useDelimiter(delimiter);
        String container = scanner.findInLine(toLookFor);
        if (container != null) {
            // System.out.println("!!!!!!!!Something was found while looking for "+
            // toLookFor+ "!!!!: " + container.trim());
            if (place != 0) {
                if (scanner.hasNext()) {
                    value = scanner.next();
                    // System.out.println("Name is : " + container.trim()+
                    // ", and Value is : " + value.trim() );
                }

            } else {
                return container;
            }
        }

        scanner.close();
        return value;

    }

    public String[] getPathElements(String path) {
        if (path == null) {
            return new String[0];
        }
        String separator = "/";
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            separator = "\\";
        }
        // String separator = System.getProperty("path.separator", "/");
        System.out.println("getpathelem : separator is " + separator);
        if (path.startsWith(separator)) {
            // we don't want to return empty string as a path element so remove
            // the leading slash
            path = path.substring(1);
        }

        return path.split(separator);
    }

    public String getBaseVRL() {
        return this.baseVRL;
    }

    public String getNbParticles() {
        //TODO: uncomment this line when support for time simulations
        /*
         if (this.nparticles.compareTo("unknown") == 0){
         return this.nseconds;
         }
         * 
         */
        return this.nparticles;
    }
    
    public String getTimeStart() {
        return this.nstart;
    }
     
     
    public String getTimeStop() {
        return this.nstop;
    }

    public String getSeparator() {
        return this.separator;
    }

    public Boolean isStatic() {
        return this.statique;
    }

    public Boolean hasAlias() {
        return this.alias;
    }

    public Boolean hasVisu() {
        return this.visu;
    }

    public Boolean hasSetMaxFileSize() {
        return this.setMaxFileSize;
    }

    public Boolean hasAutoEngineSeed() {
        return this.autoEngineSeed;
    }

    public List<String> getUnidentifiedFiles() {
        return unidentifiedFiles;
    }
    
    public List<String> getPhaseSpaceFiles() {
        return this.phaseSpaceFiles;
    }

    private boolean dangerous(File mac_file) {
        return (mac_file.getName().equals("Gate") || mac_file.getName().matches("[A-Za-z0-9\\.\\-\\_]*\\.so") || mac_file.getName().matches("[A-Za-z0-9\\.\\-\\_]*\\.so\\.[A-Za-z0-9\\.\\-\\_]*"));
    }

   
}
