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

import fr.insalyon.creatis.vip.datamanager.applet.upload.ProgressRunnable;
import fr.insalyon.creatis.vip.datamanager.applet.upload.UploadFilesBusiness;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import netscape.javascript.JSObject;

/**
 *
 * @author Rafael Ferreira da Silva, Sorina Camarasu
 */
public class Parser extends javax.swing.JPanel {

    private Main main;
    private File macFile;
    private MacroParser macroParser;
    private String localSimuFolder;
    private UploadFilesBusiness business;
    private String inputFile;
    private String[] inputs;

    /**
     * Creates new form Parser
     */
    public Parser(Main main) {

        this.main = main;

        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar = new javax.swing.JProgressBar();
        jLabel = new javax.swing.JLabel();
        fileTextField = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();
        jTextPane = new javax.swing.JTextPane();
        reportButton = new javax.swing.JButton();

        setBackground(Color.WHITE);

        jLabel.setBackground(Color.WHITE);
        jLabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel.setText("Parsing Mac File:");

        fileTextField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        fileTextField.setFocusable(false);

        jTextPane.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jScrollPane.setViewportView(jTextPane);

        reportButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        reportButton.setMnemonic('R');
        reportButton.setText("Report to Support Team");
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .add(jProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fileTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 388, Short.MAX_VALUE)
                        .add(reportButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel)
                    .add(fileTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reportButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        JSObject win = JSObject.getWindow(main);
        win.call("report", new String[]{jTextPane.getText()});
    }//GEN-LAST:event_reportButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fileTextField;
    private javax.swing.JLabel jLabel;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextPane jTextPane;
    private javax.swing.JButton reportButton;
    // End of variables declaration//GEN-END:variables

    public void parse(String fileName, String sessionId, String path,
            boolean unzip, boolean usePool, String codeBase) {

        reportButton.setVisible(false);
        fileTextField.setText(fileName);

        try {
            addMessage("Starting parse...");
            jProgressBar.setValue(0);
            macFile = new File(fileName);
            macroParser = new MacroParser(macFile);
            macroParser.parseMacroFiles();

            if (macroParser.hasAlias()) {
                error("Alias found in the GATE macro files. Please remove any alias used and start again.");
                return;

            } else if (macroParser.hasVisu()) {
                error("Vizualisation found in the GATE macro files. Please remove any \"/vis/\" commands and start again");
                return;

            } else if (!macroParser.hasAutoEngineSeed()) {
                error("SetEngineSeed is not auto. Please set the auto engine seed mode");
                return;

            } else if (macroParser.getNbParticles().equals("unknown") && (macroParser.getTimeStart().equals("unknown") || macroParser.getTimeStop().equals("unknown"))) {
                
                error("The number of particles to be simulated ('/run/beamOn' or '/gate/application/SetTotalNumberOfPrimaries' command) was not found. /gate/application/setTimeStart or /gate/application/setTimeStop was not found either");
                return;

            } else if (macroParser.hasSetMaxFileSize()) {
                error("setMaxFileSize found in the GATE macro files. Please remove any \"*/setMaxFileSize\" commands and start again.");
                return;
            }

            jProgressBar.setValue(30);
            if (macroParser.getUnidentifiedFiles() != null && !macroParser.getUnidentifiedFiles().isEmpty()) {

                String warning = "Warning: the following files could not be identified as macro, input or output:\n";
                for (String s : macroParser.getUnidentifiedFiles()) {
                    warning += "  - " + s + "\n";
                }
                warning += "\nThese files will not be available for your simulation.\n If they are required, then put them in one of 'mac', 'data' or 'output' directories.\n\n Do you want to ignore this warning and proceed with your simulation?";
                addMessage(warning);
                int reply = JOptionPane.showConfirmDialog(null, warning, "Warning: missing files", JOptionPane.YES_NO_OPTION);
                if (reply != JOptionPane.YES_OPTION) {
                    addMessage("Simulation cancelled by user.");
                    throw new GateException("Simulation cancelled by user.");
                }
            }
            
            if (macroParser.getPhaseSpaceFiles() != null && !macroParser.getPhaseSpaceFiles().isEmpty()) {
                String warning;
                if(macroParser.getPhaseSpaceFiles().size()>1){
                    warning = "Warning: more than one phase space file detected: \n";
                    for (String s : macroParser.getPhaseSpaceFiles()) {
                        warning += "  - " + s + "\n";
                    }
                    warning += "\nThese files will not be available for your simulation.\n The GateLab currently accepts maximum one phase space file that you have to upload before launching the simulation. Do you want to ignore this warning and proceed with your simulation?";
                    addMessage(warning);
                    int reply = JOptionPane.showConfirmDialog(null, warning, "Warning: missing files", JOptionPane.YES_NO_OPTION);
                    if (reply != JOptionPane.YES_OPTION) {
                        addMessage("Simulation cancelled by user.");
                        throw new GateException("Simulation cancelled by user.");
                    }
                }else{
                    warning = "Warning: Phase space file detected. Please upload it on your GateLab storage space and select it as simulation input before launching the simulation \n";
                    addMessage(warning);
                    int reply = JOptionPane.showConfirmDialog(null, warning, "Warning: missing files", JOptionPane.OK_CANCEL_OPTION);
                    if (reply != JOptionPane.OK_OPTION) {
                        addMessage("Simulation cancelled by user.");
                        throw new GateException("Simulation cancelled by user.");
                    }
                }
                
            }

            addMessage("Creating local and grid simulation folders...");
            DateFormat format = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
            localSimuFolder = macroParser.getBaseVRL().concat("simu_").concat(format.format(new Date()));
            macroParser.copyMacsAndInputs(localSimuFolder);
            addMessage("Simulation folders created.");
            jProgressBar.setValue(60);

            addMessage("Creating configuration file...");
            createConfigFile(localSimuFolder.concat(macroParser.getSeparator()));
            addMessage("Configuration file successfully created.");

            jProgressBar.setValue(80);
            addMessage("Parsing done.");

            addMessage("Uploading simulation data to the server...");

            // Code to perform actions on the form while uploading the files
            Runnable beforeRunnable = new Runnable() {
                @Override
                public void run() {
                    jProgressBar.setIndeterminate(true);
                }
            };

            Runnable afterRunnable = new Runnable() {
                @Override
                public void run() {
                    addMessage("Upload complete.");
                    jProgressBar.setValue(90);
                    jProgressBar.setIndeterminate(false);
                    complete();
                }
            };

            Runnable errorRunnable = new Runnable() {
                @Override
                public void run() {
                    error("Upload File Error: " + business.getResult());
                    jProgressBar.setIndeterminate(false);
                }
            };

            ProgressRunnable progressRunnable = new ProgressRunnable(jProgressBar);

            List<String> dataList = new ArrayList<String>();
            dataList.add(localSimuFolder.concat(macroParser.getSeparator()).concat("data"));
            dataList.add(localSimuFolder.concat(macroParser.getSeparator()).concat("mac"));
            dataList.add(localSimuFolder.concat(macroParser.getSeparator()).concat("wfl_config.txt"));
            String[] xmlFileNames = {localSimuFolder.concat(macroParser.getSeparator()).concat("materials.xml"),localSimuFolder.concat(macroParser.getSeparator()).concat("surfaces.xml")};
            for (String xmlFileName : xmlFileNames){
                if(new File(xmlFileName).exists())
                    dataList.add(xmlFileName);
            }
                
            business = new UploadFilesBusiness(sessionId, beforeRunnable,
                    afterRunnable, errorRunnable, progressRunnable, dataList, path,
                    unzip, usePool, codeBase, true);
            business.start();

        } catch (FileNotFoundException ex) {
            error("FileNotFoundException: " + ex.getMessage());
        } catch (IOException ex) {
            error("IOException: " + ex.getMessage());
        } catch (GateException ex) {
            error("GateLab Error: " + ex.getMessage());
        }
    }

    /**
     *
     * @param message
     */
    private void addMessage(String message) {

        appendToPane(message, Color.BLACK);
    }

    /**
     *
     * @param msg
     * @param c
     */
    private void appendToPane(String msg, Color c) {

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = jTextPane.getDocument().getLength();
        jTextPane.setCaretPosition(len);
        jTextPane.setCharacterAttributes(aset, false);
        jTextPane.replaceSelection(msg + "\n");
    }

    /**
     *
     * @param message
     * @param title
     */
    private void error(String message) {

        appendToPane("[ERROR] " + message, Color.RED);
        if (localSimuFolder != null) {
            FileUtilities.deleteDir(new File(localSimuFolder));
        }
        reportButton.setVisible(true);
        jProgressBar.setValue(0);
    }

    /**
     *
     * @param folder
     * @throws IOException
     */
    private void createConfigFile(String folder) throws IOException {

        FileWriter fstream = new FileWriter(folder.concat("wfl_config.txt"));
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(macFile.getName());
        out.close();
    }

    public void complete() {

        inputFile = business.getZipFileName();
        inputs = fillInInputs();
        try {
            addMessage("Deleting temporary files...");
            FileUtilities.deleteDir(new File(localSimuFolder));

        } finally {
            addMessage("Completed.");
            jProgressBar.setValue(100);
            JSObject win = JSObject.getWindow(main);
            win.call("uploadMacComplete", inputs);
        }
    }

    private String[] fillInInputs() {

        String type = this.macroParser.isStatic() ? "stat" : "dyn";
        String ps="";
        String parts ="";
        if(!macroParser.getNbParticles().equals("unknown"))
            parts = macroParser.getNbParticles();
        else{
//            double time = Double.parseDouble(macroParser.getTimeStop()) - Double.parseDouble(macroParser.getTimeStart());
//            parts=""+time;
                parts = "100";
        }
        
        if (macroParser.getPhaseSpaceFiles() != null && !macroParser.getPhaseSpaceFiles().isEmpty()){
            ps = macroParser.getPhaseSpaceFiles().get(0).toString();
        }else{
            ps = "dummy";
        }
        
        String inputsList[] = {
            "GateInput = ".concat(this.inputFile),
            "ParallelizationType = ".concat(type),
            "NumberOfParticles = ".concat(parts),
            "phaseSpace = ".concat(ps)};
        //System.out.println("************FIlled in PS input "+ps);
        return inputsList;
    }
}
