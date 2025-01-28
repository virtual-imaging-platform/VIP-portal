package fr.insalyon.creatis.vip.application.server.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.inter.GroupInterface;

@Service
public class GroupInterfaceImpl implements GroupInterface {

    ResourceBusiness resourceBusiness;
    GroupBusiness groupBusiness;
    ApplicationBusiness applicationBusiness;

    @Autowired
    public GroupInterfaceImpl(ResourceBusiness resourceBusiness, GroupBusiness groupBusiness, ApplicationBusiness applicationBusiness) {
        this.resourceBusiness = resourceBusiness;
        this.groupBusiness = groupBusiness;
        this.applicationBusiness = applicationBusiness;
    }

    @Override
    public List<String> getItems(String groupname) throws BusinessException {
        List<String> items = new ArrayList<>();
        Group group = groupBusiness.get(groupname);

        switch (group.getType()) {
            case APPLICATION:
                items = applicationBusiness.getApplications(group)
                    .stream()
                    .map((a) -> a.getName())
                    .collect(Collectors.toList());
                break;

            case RESOURCE:
                items = resourceBusiness.getByGroup(group)
                    .stream()
                    .map((r) -> r.getName())
                    .collect(Collectors.toList());
                break;
        }
        return items;
    }

    @Override
    public void delete(String item, String groupname) throws BusinessException {
        Group group = groupBusiness.get(groupname);

        switch (group.getType()) {
            case APPLICATION:
                applicationBusiness.dissociate(new Application(item, ""), group);
                break;

            case RESOURCE:
                resourceBusiness.dissociate(new Resource(item), group);
                break;
        }
    }
}
