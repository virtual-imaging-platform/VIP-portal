package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

/**
 *
 * @author Rafael Silva
 */
public interface ExecutionNodeDAO {

    public Node getNode(String siteID, String nodeName) throws DAOException;
}
