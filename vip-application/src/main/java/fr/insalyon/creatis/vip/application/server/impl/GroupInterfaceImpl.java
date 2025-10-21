package fr.insalyon.creatis.vip.application.server.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.inter.GroupInterface;

@Service
public class GroupInterfaceImpl implements GroupInterface {

    private ResourceBusiness resourceBusiness;
    private GroupBusiness groupBusiness;
    private ApplicationBusiness applicationBusiness;
    private AppVersionBusiness appVersionBusiness;

    @Autowired
    public GroupInterfaceImpl(ResourceBusiness resourceBusiness, GroupBusiness groupBusiness, ApplicationBusiness applicationBusiness, AppVersionBusiness appVersionBusiness) {
        this.resourceBusiness = resourceBusiness;
        this.groupBusiness = groupBusiness;
        this.applicationBusiness = applicationBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    @Override
    public List<String> getItems(String groupname) throws VipException {
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
    public void delete(String item, String groupname) throws VipException {
        Group group = groupBusiness.get(groupname);

        switch (group.getType()) {
            case APPLICATION:
                applicationBusiness.dissociate(new Application(item, ""), groupname);
                break;

            case RESOURCE:
                resourceBusiness.dissociate(new Resource(item), group);
                break;
        }
    }

    @Override
    public List<String> getMissingGroupsRessources(User user) throws VipException {
        // show which resources are missing depending on user application groups
        Set<String> result = new HashSet<>();
        List<Application> userApps = applicationBusiness.getApplications(user);
        List<Resource> userResources = resourceBusiness.getAvailableForUser(user);
        Set<Resource> missingResources = new HashSet<>();

        for (Application app : userApps) {
            for (AppVersion appVersion : appVersionBusiness.getVersions(app.getName())) {
                List<Resource> versionResources = resourceBusiness.getByAppVersion(appVersion);

                if ( ! versionResources.stream().anyMatch((r) -> userResources.contains(r))) {
                    missingResources.addAll(versionResources);
                }
            }
        }
        for (Resource resource : missingResources) {
            result.addAll(resource.getGroupsNames());
        }

        return new ArrayList<>(result);
    }
}
