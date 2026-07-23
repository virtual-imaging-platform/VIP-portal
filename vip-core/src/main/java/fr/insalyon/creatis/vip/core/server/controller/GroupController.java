package fr.insalyon.creatis.vip.core.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Permissions:
 * - Users can see the public groups (even the one the do not belong to) + the private groups they belong to
 * - Admins can see all the groups
 * - Only admins can create/edit/delete groups
 */
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupBusiness groupBusiness;

    @Autowired
    public GroupController(GroupBusiness groupBusiness) {
        this.groupBusiness = groupBusiness;
    }

    @GetMapping
    public PrecisePage<Group> list(@RequestParam(defaultValue = "false") boolean onlyApplications,
            @RequestParam(defaultValue = "false") boolean onlyResources,
            @RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity)
            throws VipException {
        return groupBusiness.get(onlyApplications, onlyResources, offset, quantity);
    }

    @GetMapping(params = "q")
    public List<Group> search(@RequestParam String q,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int limit) throws VipException {
        return groupBusiness.searchGroups(q, limit);
    }

    @GetMapping(value = "{id}")
    public Group get(@PathVariable String id) throws VipException {
        Group group = groupBusiness.get(id);

        if (group == null) {
        throw new VipException(DefaultError.NOT_FOUND, Group.class.getSimpleName(), id);
        } else {
            return group;
        }
    }

    @DeleteMapping(value = "{id}")
    public void deleteVersion(@PathVariable String id) throws VipException {
        Group group = groupBusiness.get(id);

        if (group == null) {
            throw new VipException(DefaultError.NOT_FOUND, Group.class.getSimpleName(), id);
        } else {
            groupBusiness.remove(id);
        }
    }

    @PutMapping(value = "{id}")
    public Group createOrUpdate(@PathVariable String id, @RequestBody @Valid Group group)
            throws VipException {
        if ( ! id.equals(group.getName())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, id, "Group name do not match!");
        } else {
            Group existingGroup = groupBusiness.get(id);

            if (existingGroup == null) {
                groupBusiness.add(group);
            } else {
                groupBusiness.update(existingGroup.getName(), group);
            }
            return groupBusiness.get(id);
        }
    }

    @PostMapping()
    public Group create(@RequestBody @Valid Group group) throws VipException {
        return createOrUpdate(group.getName(), group);
    }
}
