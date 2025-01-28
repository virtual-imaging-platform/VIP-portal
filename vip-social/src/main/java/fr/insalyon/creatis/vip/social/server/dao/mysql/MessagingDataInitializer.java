package fr.insalyon.creatis.vip.social.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.dao.mysql.TableInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
@Transactional
public class MessagingDataInitializer extends JdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TableInitializer tableInitializer;

    @Autowired
    public MessagingDataInitializer(
            DataSource dataSource, TableInitializer tableInitializer) {
        setDataSource(dataSource);
        this.tableInitializer = tableInitializer;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Order(20) // Social tables references vip-core tables and must be created after
    public void onStartup() {
        logger.info("Configuring VIP Social database.");

        tableInitializer.createTable("VIPSocialMessage",
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "sender VARCHAR(255), "
                        + "title VARCHAR(255), "
                        + "message TEXT, "
                        + "posted TIMESTAMP, "
                        + "FOREIGN KEY (sender) REFERENCES VIPUsers(email) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE");

        tableInitializer.createTable("VIPSocialMessageSenderReceiver",
                "message_id BIGINT, "
                        + "receiver VARCHAR(255), "
                        + "user_read BOOLEAN, "
                        + "PRIMARY KEY (message_id, receiver), "
                        + "FOREIGN KEY (receiver) REFERENCES VIPUsers(email) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY (message_id) REFERENCES VIPSocialMessage(id) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT");

        tableInitializer.createTable("VIPSocialGroupMessage",
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "sender VARCHAR(255), "
                        + "name VARCHAR(255), "
                        + "title VARCHAR(255), "
                        + "message TEXT, "
                        + "posted TIMESTAMP, "
                        + "FOREIGN KEY (sender) REFERENCES VIPUsers(email) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY(name) REFERENCES VIPGroups(name) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT");
    }
}
