/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.server.rpc;

import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.cardiac.client.rpc.CardiacService;
import fr.insalyon.creatis.vip.cardiac.client.view.CardiacException;
import fr.insalyon.creatis.vip.cardiac.dao.CardiacDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class CardiacServiceImpl extends AbstractRemoteServiceServlet implements CardiacService {

    private static Logger logger = Logger.getLogger(CardiacServiceImpl.class);

    public CardiacServiceImpl() {
    }

    @Override
    public void addSimulation(Simulation s) throws CardiacException {
        try {
            trace(logger, "Adding cardiac simulation '" + s.getName() + "'.");
            CardiacDAOFactory.getDAOFactory().getCardiacDAO().addSimulation(s);
        } catch (CoreException ex) {
            throw new CardiacException(ex);
        } catch (DAOException ex) {
            throw new CardiacException(ex);
        }
    }

    @Override
    public void deleteSimulation(Simulation s) throws CardiacException {
        try {
            trace(logger, "Deleting cardiac simulation '" + s.getName() + "'.");
            CardiacDAOFactory.getDAOFactory().getCardiacDAO().deleteSimulation(s);
        } catch (CoreException ex) {
            throw new CardiacException(ex);
        } catch (DAOException ex) {
            throw new CardiacException(ex);
        }
    }

    @Override
    public void updateSimulation(Simulation s) throws CardiacException {
        try {
            trace(logger, "Updating description of cardiac simulation '" + s.getName() + "'.");
            CardiacDAOFactory.getDAOFactory().getCardiacDAO().updateSimulation(s);
        } catch (CoreException ex) {
            throw new CardiacException(ex);
        } catch (DAOException ex) {
            throw new CardiacException(ex);
        }
    }

//    @Override
//    public void addFileToSimulation(File f, Simulation s) throws CardiacException {
//        try {
//            trace(logger, "Adding file "+f.getName()+" to cardiac simulation '" + s.getName() + "'.");
//            CardiacDAOFactory.getDAOFactory().getCardiacDAO().addFileToSimulation(f,s);
//        } catch (CoreException ex) {
//            throw new CardiacException(ex);
//        } catch (DAOException ex) {
//            throw new CardiacException(ex);
//        }
//    }

//    @Override
//    public void deleteFileFromSimulation(File f, Simulation s) throws CardiacException {
//         try {
//            trace(logger, "Deleting file "+f.getName()+" from cardiac simulation '" + s.getName() + "'.");
//            CardiacDAOFactory.getDAOFactory().getCardiacDAO().deleteFileFromSimulation(f,s);
//        } catch (CoreException ex) {
//            throw new CardiacException(ex);
//        } catch (DAOException ex) {
//            throw new CardiacException(ex);
//        }
//    }

    @Override
    public List<Simulation> getSimulations() throws CardiacException {
        try {
            trace(logger, "Getting cardiac simulations.");
            
//        Simulation s = new Simulation("Super simulation","This is a super simulation done by me");
//        File f = new File("lfn:///grid/biomed/creatis/tristan/hello.jdl","file 1","My super file");
//        ArrayList<Simulation> simus = new ArrayList<Simulation>();
//        s.getFiles().add(f);
//        simus.add(s);
//        
//        Simulation s1 = new Simulation("Super simulation 1","This is a super simulation done by me 1");
//        File f1 = new File("lfn:///grid/biomed/creatis/tristan/hello.jdl","file 1","My super file");
//      
//        s1.getFiles().add(f1);
//        simus.add(s1);
//        return simus;
//        
           return CardiacDAOFactory.getDAOFactory().getCardiacDAO().getSimulations();
        
        } catch (CoreException ex) {
            throw new CardiacException(ex);
        
        } catch (DAOException ex) {
            throw new CardiacException(ex);
        }
    }
    

  
}
