/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author ibrahim
 */

package fr.insalyon.creatis.vip.portal.server.dao.derby;
import fr.insalyon.creatis.vip.portal.server.dao.GatelabDAO;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GatelabData implements GatelabDAO
{
    
     private Connection connection;
     private String workflowID;

     public GatelabData(String workflowID)
     {
         this.workflowID= workflowID;
         connection = JobsConnection.getInstance().connect(
         ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
     }

    public long getNumberParticles (){

        System.out.println("------>> WorkflowID <<------>>"+ workflowID);

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT somme FROM somme ");
	    ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong("somme");

        } catch (SQLException ex) {
            System.out.println("Erreuurrr..."+ex.getMessage());
            ex.printStackTrace();
            return 0;
        }


    }

    public void StopWorkflowSimulation()
    {
        try
          {
            PreparedStatement ps = connection.prepareStatement("UPDATE somme SET simulation = 'true' ");
	    ps.execute();
          } catch (SQLException ex){ System.out.println("Erreur"+ex.getMessage()); }

    }

       

}
