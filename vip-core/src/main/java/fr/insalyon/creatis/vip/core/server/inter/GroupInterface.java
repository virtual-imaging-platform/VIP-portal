package fr.insalyon.creatis.vip.core.server.inter;

import java.util.List;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public interface GroupInterface {

    public List<String> getItems(String groupname) throws BusinessException;
    public void delete(String item, String groupname) throws BusinessException;
}
