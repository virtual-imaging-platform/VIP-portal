package fr.insalyon.creatis.vip.social.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.bean.User;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class Message implements IsSerializable {

    private long id;
    private User sender;
    private User[] receivers;
    private String title;
    private String message;
    private String posted;
    private Date postedDate;
    private boolean read;

    public Message() {
    }

    public Message(long id, User sender, User receiver, String title, String message,
            String posted, Date postedDate, boolean read) {

        this(id, sender, new User[]{receiver}, title, message, posted, postedDate, read);
    }

    public Message(long id, User sender, User[] receivers, String title, String message, String posted, Date postedDate, boolean read) {
        this.id = id;
        this.sender = sender;
        this.receivers = receivers;
        this.title = title;
        this.message = message;
        this.posted = posted;
        this.postedDate = postedDate;
        this.read = read;
    }

    public long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getPosted() {
        return posted;
    }

    public boolean isRead() {
        return read;
    }

    public String getTitle() {
        return title;
    }

    public User[] getReceivers() {
        return receivers;
    }

    public Date getPostedDate() {
        return postedDate;
    }
}
