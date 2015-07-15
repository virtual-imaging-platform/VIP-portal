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
package fr.insalyon.creatis.vip.vipcoworkapplet;

import fr.cnrs.i3s.cowork.core.gui.DesignFrame;
import fr.cnrs.i3s.cowork.core.knowledgebase.KnowledgeBase;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Hello world!
 *
 */
public class Cowork extends javax.swing.JApplet {

    private String sessionId;
    private String email;
    private String endpoint;
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
            java.util.logging.Logger.getLogger(Cowork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cowork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cowork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cowork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
  

                public void run() {
                    try {
                        sessionId = getParameter("sessionId");
                        email = getParameter("email");
                        endpoint = getCodeBase().toString()+"/fr.insalyon.creatis.vip.portal.Main/coworkservice";
                        
                        DesignFrame frame = new DesignFrame(true);
                        frame.setAppletParams(endpoint,email,sessionId);
      
                        String home = System.getProperty("user.home");
                        File config = new File(home+File.separator+".cowork/config");
                        PropertiesConfiguration pc = new PropertiesConfiguration(config);
                        
                        String password = (String) pc.getProperty("password"),  login = (String) pc.getProperty("login");
                        PasswordDialog p = new PasswordDialog(null, "Please login to the knowledge base");
                        while (password == null || login == null) {
                            if (p.showDialog()) {
                                
                                login = p.getName();
                                password = p.getPass();
                            }
                            if(login != null && password != null) {
                                if (JOptionPane.showConfirmDialog(null, "Remember credentials (unencrypted)?", "Rememeber?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    pc.setProperty("password", password);
                                    pc.setProperty("login", login);
                                    pc.save();
                                }
                            }
                                
                        }
                         
                        KnowledgeBase kb = new KnowledgeBase("jdbc:mysql://"+getCodeBase().getHost()+"/cowork",  login, password, "http://cowork.i3s.cnrs.fr/");
                        frame.setKB(kb);
                        frame.setVisible(true);
                    } catch (ConfigurationException ex) {
                        ex.printStackTrace();
                        
                        JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(ex), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(ex), "Error", JOptionPane.ERROR_MESSAGE);
                    }


                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
