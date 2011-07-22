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
package fr.insalyon.creatis.vip.datamanager.applet.upload;

import fr.insalyon.creatis.devtools.FileUtils;
import java.awt.Color;
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
import netscape.javascript.JSObject;

/**
 *
 * @author Rafael Silva
 */
public class UploadFiles extends javax.swing.JApplet {

    private String[] colsName = new String[]{"", "Name", "Size", "Modification Date"};
    private int[] colsSize = new int[]{30, 250, 70, 175};
    private DefaultTableModel model;
    private List<RowSorter.SortKey> sortKeys;
    private TableRowSorter<TableModel> sorter;
    private String currentDir;
    private String SEPARATOR = "/";
    private String user;
    private String userdn;
    private String proxy;
    private String path;
    private boolean unzip;
    private boolean usePool;

    /** Initializes the applet Main */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UploadFiles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UploadFiles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UploadFiles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UploadFiles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    model = new DefaultTableModel(colsName, 0) {

                        @Override
                        public boolean isCellEditable(int rowIndex, int mColIndex) {
                            return false;
                        }
                    };
                    initComponents();
                    initialization();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialization() {

        this.user = getParameter("user");
        this.userdn = getParameter("userdn");
        this.proxy = getParameter("proxy");
        this.path = getParameter("path");
        this.unzip = Boolean.valueOf(getParameter("unzip"));
        this.usePool = Boolean.valueOf(getParameter("pool"));

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

        jTable.setRowSorter(null);
        this.currentDir = baseDir;
        jTextField.setText(baseDir);

        File dir = new File(baseDir);
        model.getDataVector().removeAllElements();

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-folder.png"));
                model.addRow(new Object[]{icon, f.getName(), "", ""});

            } else {
                ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-file.png"));
                String size = FileUtils.parseFileSize(f.length());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
                String date = sdf.format(new Date(f.lastModified()));
                model.addRow(new Object[]{icon, f.getName(), size, date});
            }

        }
        model.fireTableDataChanged();
        sorter = new TableRowSorter<TableModel>(model);
        sorter.setSortKeys(sortKeys);
        jTable.setRowSorter(sorter);
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jTextField = new javax.swing.JTextField();
        folderupButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        jButton = new javax.swing.JButton();

        setBackground(Color.WHITE);
        setPreferredSize(new java.awt.Dimension(550, 350));

        jScrollPane.setBackground(Color.WHITE);

        jTable.setBackground(Color.WHITE);
        jTable.setModel(model);
        jTable.setGridColor(Color.WHITE);
        jScrollPane.setViewportView(jTable);

        jToolBar1.setBackground(Color.WHITE);
        jToolBar1.setFloatable(false);
        jToolBar1.setForeground(Color.WHITE);
        jToolBar1.setRollover(true);

        jTextField.setColumns(40);
        jTextField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jTextField.setFocusable(false);
        jToolBar1.add(jTextField);

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
        jToolBar1.add(folderupButton);

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
        jToolBar1.add(refreshButton);

        jButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton.setText("Upload");
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(jButton)
                .addContainerGap(472, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

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

private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
    try {
        if (jTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No data selected.", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            jButton.setText("Uploading...");
            jButton.setEnabled(false);

            List<String> dataList = new ArrayList<String>();
            for (int i : jTable.getSelectedRows()) {
                dataList.add(getBaseDir(jTable.getValueAt(i, 1).toString()));
            }

            UploadFilesBusiness business = new UploadFilesBusiness();
            String fileName = business.uploadFiles(dataList, user, userdn, proxy,
                    path, unzip, usePool, getCodeBase().toString());

            JSObject win = JSObject.getWindow(this);
            win.call("uploadComplete", new String[]{fileName});
        }

    } catch (BusinessException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}//GEN-LAST:event_jButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton folderupButton;
    private javax.swing.JButton jButton;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextField;
    private javax.swing.JToolBar jToolBar1;
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

        jTextField.setText("");
        model.getDataVector().removeAllElements();
        currentDir = null;

        for (File root : File.listRoots()) {
            ImageIcon icon = new ImageIcon(UploadFiles.class.getClassLoader().getResource("images/icon-folder.png"));
            model.addRow(new Object[]{icon, root.toString(), "", ""});
        }
    }
}
