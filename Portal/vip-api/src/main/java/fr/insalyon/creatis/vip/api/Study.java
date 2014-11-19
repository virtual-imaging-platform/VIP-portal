/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tristan Glatard
 */
@Path("/studies")
/**
 * A study in the PAF API is a group in VIP.
 */
public class Study extends APIClass {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response listAllAccessibleGroups(@Context HttpServletRequest req) {
        try {
            this.getConfigurationServiceImpl().setSession(req.getSession());
            User user = getUser(req);
            StringBuilder output = new StringBuilder("");
            List<Group> publicGroups = getConfigurationServiceImpl().getPublicGroups();
            Map<Group, GROUP_ROLE> userGroups = null;

            if (user != null) { // i.e. user is authenticated
                userGroups = getConfigurationServiceImpl().getUserGroups(null);
                for (Group group : userGroups.keySet()) {
                    output.append(formatGroupString(group, userGroups.get(group)));
                }
            }

            // user may not be authenticated. 
            for (Group group : publicGroups) {
                if (user == null || !isInMap(userGroups,group)) {
                    output.append(formatGroupString(group, GROUP_ROLE.None));
                }
            }

            return Response.status(Response.Status.OK).entity(output.toString()).build();
        } catch (CoreException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response listGroup(@PathParam("name") String groupName, @Context HttpServletRequest req) {
        try {
            this.getConfigurationServiceImpl().setSession(req.getSession());
            User user = getUser(req);
            StringBuilder output = new StringBuilder("");
            Map<Group, GROUP_ROLE> userGroups = null;
            List<Group> publicGroups = getConfigurationServiceImpl().getPublicGroups();
            if (user != null) { // i.e. user is authenticated
                userGroups = getConfigurationServiceImpl().getUserGroups(null);
                for (Group group : userGroups.keySet()) {
                    if (group.getName().equals(groupName)) {
                        output.append(formatDetailedGroupString(group, userGroups.get(group)));
                    }
                }
            }

            //user may not be authenticated
            for (Group group : publicGroups) {
                if ((user == null || !isInMap(userGroups,group)) && group.getName().equals(groupName)) {
                    output.append(formatDetailedGroupString(group, GROUP_ROLE.None));
                }
            }

            return Response.status(Response.Status.OK).entity(output.toString()).build();
        } catch (CoreException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private String formatGroupString(Group group, GROUP_ROLE role) {
        String p = group.isPublicGroup() ? "public" : "private";
        return group.getName() + " (" + p + ") - " + role.toString() + "\n";
    }

    private String formatDetailedGroupString(Group group, GROUP_ROLE role) {
        String p = group.isPublicGroup() ? "public" : "private";
        String files = group.isGridFile() ? "grid files," : "";
        String jobs = group.isGridJobs() ? "grid jobs" : "";
        StringBuilder output = new StringBuilder(group.getName() + " (" + p);
        if (!files.equals("") || !jobs.equals("")) {
            output.append(",");
        }
        output.append(files).append(jobs);
        output.append(")\n");
        if (role.equals(GROUP_ROLE.Admin)) {
            try {
                for (User u : getConfigurationServiceImpl().getUsersFromGroup(group.getName())) {
                    output.append(u.getFullName()).append(",").append(u.getEmail()).append("\n");
                }
            } catch (CoreException ex) {
                return ""; // group does not exist or cannot get users from group
            }
        }
        return output.toString();
    }

    private User getUser(HttpServletRequest req) throws CoreException {
        return (User) req.getSession().getAttribute(CoreConstants.SESSION_USER);
    }

    private boolean isInMap(Map<Group, GROUP_ROLE> userGroups, Group group) {
        for(Group mapGroup : userGroups.keySet())
            if(mapGroup.getName().equals(group.getName()))
                return true;
        return false;
    }
}
