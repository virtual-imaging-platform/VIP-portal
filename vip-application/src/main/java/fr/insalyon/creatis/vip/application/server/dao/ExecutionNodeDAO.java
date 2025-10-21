package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.models.Node;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

public interface ExecutionNodeDAO {

    public Node getNode(String siteID, String nodeName) throws DAOException;
}
