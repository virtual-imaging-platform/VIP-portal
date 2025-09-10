package fr.insalyon.creatis.vip.social.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.bean.User;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class GroupMessage implements IsSerializable {

    private long id;
    private User sender;
    private String groupName;
    private String title;
    private String message;
    private String posted;
    private Date postedDate;

    public GroupMessage() {
    }

    public GroupMessage(long id, User sender, String groupName, String title, 
            String message, String posted, Date postedDate) {

        this.id = id;
        this.sender = sender;
        this.groupName = groupName;
        this.title = title;
        this.message = message;
        this.posted = posted;
        this.postedDate = postedDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getPosted() {
        return posted;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public User getSender() {
        return sender;
    }

    public String getTitle() {
        return title;
    }
}
