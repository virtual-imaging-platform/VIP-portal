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
package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.server.dao.DAOFactory;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class NewsBusiness {

    private final static Logger logger = Logger.getLogger(NewsBusiness.class);

    public List<News> getNews() throws BusinessException {
        try {
            return DAOFactory.getDAOFactory().getNewsDAO().getNews();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String add(News news) throws BusinessException {
        try {
            String result = DAOFactory.getDAOFactory().getNewsDAO().add(news);
            sendEmail(news.getTitle(), news.getMessage());
            return result;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String update(News news) throws BusinessException {
        try {
            return DAOFactory.getDAOFactory().getNewsDAO().update(news);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String remove(News news) throws BusinessException {
        try {
            return DAOFactory.getDAOFactory().getNewsDAO().remove(news);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendEmail(String subject, String content) throws BusinessException {

        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.creatis.insa-lyon.fr");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setContent(content, "text/html");
            mimeMessage.addHeader("Content-Type", "text/html");

            InternetAddress from = new InternetAddress(
                    ServerConfiguration.getInstance().getMailFrom(),
                    "VIP News");
            mimeMessage.setReplyTo(new InternetAddress[]{from});
            mimeMessage.setFrom(from);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject("[VIP-News] " + subject);

            Transport transport = session.getTransport();
            transport.connect();

            InternetAddress[] addressTo = null;
            String[] recipients = ServerConfiguration.getInstance().getNewsRecipients();
            
            if (recipients != null && recipients.length > 0) {
                addressTo = new InternetAddress[recipients.length];
                for (int i = 0; i < recipients.length; i++) {
                    addressTo[i] = new InternetAddress(recipients[i]);
                }
                mimeMessage.setRecipients(Message.RecipientType.TO, addressTo);

                transport.sendMessage(mimeMessage, addressTo);
                transport.close();

            } else {
                logger.warn("There's no recipients to send the news.");
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (MessagingException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
}
