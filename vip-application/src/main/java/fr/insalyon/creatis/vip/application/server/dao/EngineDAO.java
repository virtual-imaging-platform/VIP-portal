package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface EngineDAO {

    public void add(Engine engine) throws DAOException;

    public void update(Engine engine) throws DAOException;

    public void remove(String name) throws DAOException;
    
    public List<Engine> get() throws DAOException;
    
    public List<Engine> getByResource(Resource resource) throws DAOException;
}
