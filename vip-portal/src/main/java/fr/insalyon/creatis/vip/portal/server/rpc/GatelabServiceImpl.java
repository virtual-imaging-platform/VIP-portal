
/**
 *
 * @author Ibrahim Kallel
 */

package fr.insalyon.creatis.vip.portal.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.portal.server.business.gatelab.GatelabInputs;
import fr.insalyon.creatis.vip.portal.client.rpc.GatelabService;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import java.util.Map;
import java.util.HashMap;


public class GatelabServiceImpl extends RemoteServiceServlet implements GatelabService {

    public Map<String, String>  getGatelabWorkflowInputs(String workflowID){
        GatelabInputs gateinputs = new GatelabInputs (workflowID);
        long nb = DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();
        Map<String, String> inputMap = new  HashMap<String, String>();
        inputMap = gateinputs.getWorkflowInputs();
        inputMap.put("runnedparticles",""+nb );
        return inputMap;
    }

    public long getNumberParticles (String workflowID){
        return DAOFactory.getDAOFactory().getGatelabDAO(workflowID).getNumberParticles();

    }

    public void StopWorkflowSimulation (String workflowID){
        DAOFactory.getDAOFactory().getGatelabDAO(workflowID).StopWorkflowSimulation();
    }
    



}
