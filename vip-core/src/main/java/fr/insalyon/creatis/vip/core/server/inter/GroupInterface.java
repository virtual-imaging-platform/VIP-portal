package fr.insalyon.creatis.vip.core.server.inter;

import java.util.List;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;

public interface GroupInterface {

    public List<String> getItems(String groupname) throws VipException;
    public void delete(String item, String groupname) throws VipException;
    public List<String> getMissingGroupsRessources(User user) throws VipException; 
}
