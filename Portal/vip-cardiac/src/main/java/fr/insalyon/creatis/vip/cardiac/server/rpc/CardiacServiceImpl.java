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
