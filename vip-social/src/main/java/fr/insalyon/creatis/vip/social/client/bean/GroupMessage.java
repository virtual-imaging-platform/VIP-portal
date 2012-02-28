/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
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
