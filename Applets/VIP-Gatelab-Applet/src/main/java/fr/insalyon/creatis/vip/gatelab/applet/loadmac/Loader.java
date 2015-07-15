/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.gatelab.applet.loadmac;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.vip.datamanager.applet.upload.CellRender;
import fr.insalyon.creatis.vip.datamanager.applet.upload.UploadFiles;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Loader extends javax.swing.JPanel {

    private Main main;
    private String[] colsName = new String[]{"", "Name", "Size", "Modification Date"};
    private int[] colsSize = new int[]{30, 250, 70, 175};
    private DefaultTableModel model;
    private List<RowSorter.SortKey> sortKeys;
    private TableRowSorter<TableModel> sorter;
    private String currentDir;
    private String SEPARATOR = "/";

    public Loader(Main main) {
        
        this.main = main;
        
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new DefaultTableModel(colsName, 0) {
                        @Override
                        public boolean isCellEditable(int rowIndex, int mColIndex) {
                            return false;
                        }
                    };
                    initComponents();
                    initialization();
                    loadData(System.getProperty("user.home"));
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initialization() {

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            SEPARATOR = "\\";
        }

        loadRoot();

        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getComponent().isEnabled()
                        && me.getButton() == MouseEvent.BUTTON1
                        && me.getClickCount() == 2) {

                    String name = jTable.getValueAt(jTable.rowAtPoint(me.getPoint()), 1).toString();
                    String baseDir = getBaseDir(name);
                    File f = new File(baseDir);
                    if (f.isDirectory()) {
                        loadData(baseDir);
                    } else {
                        parse(baseDir);
                    }
                }
            }
        });

        for (int i = 0; i < colsName.length; i++) {
            jTable.getColumn(colsName[i]).setPreferredWidth(colsSize[i]);
            jTable.getColumn(colsName[i]).setCellRenderer(new CellRender());
        }

        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));

        sorter = new TableRowSorter<TableModel>(model);
        sorter.setSortKeys(sortKeys);
        jTable.setRowSorter(sorter);
    }

    private void loadData(String baseDir) {

        File dir = new File(baseDir);
        if (!dir.exists()) {
            JOptionPane.showMessageDialog(null, "Path not found: " + baseDir, "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            jTable.setRowSorter(null);
            jTextField.setText(baseDir);
            this.currentDir = baseDir;
            model.getDataVector().removeAllElements();

            for (File f : dir.listFiles()) {
                if (!f.isHidden()) {
                    if (f.isDirectory()) {
                        ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-folder.png"));
                        model.addRow(new Object[]{icon, f.getName(), "", ""});

                    } else if (f.getName().endsWith(".mac")) {
                        ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-file.png"));
                        String size = FileUtils.parseFileSize(f.length());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
                        String date = sdf.format(new Date(f.lastModified()));
                        model.addRow(new Object[]{icon, f.getName(), size, date});
                    }
                }

            }
            model.fireTableDataChanged();
            sorter = new TableRowSorter<TableModel>(model);
            sorter.setSortKeys(sortKeys);
            jTable.setRowSorter(sorter);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton = new javax.swing.JButton();
        jToolBar = new javax.swing.JToolBar();
        jTextField = new javax.swing.JTextField();
        folderupButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        homeButton = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setBackground(Color.WHITE);
        setPreferredSize(new java.awt.Dimension(550, 280));

        jButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton.setMnemonic('L');
        jButton.setText("Load Mac File");
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        jToolBar.setBackground(Color.WHITE);
        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        jTextField.setColumns(40);
        jTextField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });
        jToolBar.add(jTextField);

        folderupButton.setIcon(new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-folderup.png")));
        folderupButton.setToolTipText("Folder Up");
        folderupButton.setFocusable(false);
        folderupButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        folderupButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        folderupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderupButtonActionPerformed(evt);
            }
        });
        jToolBar.add(folderupButton);

        refreshButton.setIcon(new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-refresh.png")));
        refreshButton.setToolTipText("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar.add(refreshButton);

        homeButton.setIcon(new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-home.png")));
        homeButton.setToolTipText("Home");
        homeButton.setFocusable(false);
        homeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        homeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
        jToolBar.add(homeButton);

        jScrollPane.setBackground(Color.WHITE);

        jTable.setBackground(Color.WHITE);
        jTable.setModel(model);
        jTable.setGridColor(Color.WHITE);
        jScrollPane.setViewportView(jTable);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel1.setText("File Type: GATE Mac File (*.mac)");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel1)
                .addContainerGap())
            .add(jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed

        if (jTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No mac file selected.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (jTable.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(null, "You must select just 1 mac.", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            String fileName = getBaseDir(jTable.getValueAt(jTable.getSelectedRow(), 1).toString());
            parse(fileName);
        }
    }//GEN-LAST:event_jButtonActionPerformed

    private void jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldActionPerformed
        loadData(jTextField.getText());
    }//GEN-LAST:event_jTextFieldActionPerformed

    private void folderupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderupButtonActionPerformed
        if (currentDir.equals(SEPARATOR)
                || (currentDir.contains(":" + SEPARATOR) && currentDir.length() == 3)) {
            loadRoot();

        } else {
            String baseDir = currentDir.substring(0, currentDir.lastIndexOf(SEPARATOR));
            if (!baseDir.contains(SEPARATOR)) {
                baseDir += SEPARATOR;
            }
            loadData(baseDir);
        }
    }//GEN-LAST:event_folderupButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        loadData(currentDir);
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        loadData(System.getProperty("user.home"));
    }//GEN-LAST:event_homeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton folderupButton;
    private javax.swing.JButton homeButton;
    private javax.swing.JButton jButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextField;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

    private String getBaseDir(String name) {
        if (currentDir != null) {
            if (currentDir.equals(SEPARATOR)
                    || (currentDir.contains(":" + SEPARATOR) && currentDir.length() == 3)) {
                return currentDir + name;
            } else {
                return currentDir + SEPARATOR + name;
            }
        }
        return name;
    }

    private void loadRoot() {

        jTable.setRowSorter(null);
        jTextField.setText("");
        model.getDataVector().removeAllElements();
        currentDir = null;

        for (File root : File.listRoots()) {
            ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-folder.png"));
            model.addRow(new Object[]{icon, root.toString(), "", ""});
        }
        model.fireTableDataChanged();
    }

    private void parse(String fileName) {
        
        main.parse(fileName);
    }

    private Container getContentPane() {
        return this;
    }
}
