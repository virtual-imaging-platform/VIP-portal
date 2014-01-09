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
