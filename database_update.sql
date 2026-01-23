-- v1.24
alter table VIPAppVersions ADD json_lfn VARCHAR(255);
alter table VIPAppVersions ADD doi VARCHAR(255);

-- v1.25
ALTER TABLE `VIPSocialGroupMessage` DROP FOREIGN KEY `VIPSocialGroupMessage_ibfk_1`;
ALTER TABLE `VIPSocialGroupMessage` ADD FOREIGN KEY (`sender`) REFERENCES `VIPUsers` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `VIPSocialMessageSenderReceiver` DROP FOREIGN KEY `VIPSocialMessageSenderReceiver_ibfk_1`;
ALTER TABLE `VIPSocialMessageSenderReceiver` ADD FOREIGN KEY (`receiver`) REFERENCES `VIPUsers` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `VIPSocialMessage` DROP FOREIGN KEY `VIPSocialMessage_ibfk_1`;
ALTER TABLE `VIPSocialMessage` ADD FOREIGN KEY (`sender`) REFERENCES `VIPUsers` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `VIPApplications` DROP FOREIGN KEY `fk_VIPApplications_1`;
ALTER TABLE `VIPApplications` ADD FOREIGN KEY (`owner`) REFERENCES `VIPUsers` (`email`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `VIPAppInputs` DROP FOREIGN KEY `VIPAppInputs_ibfk_1`;
ALTER TABLE `VIPAppInputs` ADD FOREIGN KEY (`email`) REFERENCES `VIPUsers` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `VIPUsers` ADD next_email VARCHAR(255);

-- v1.27
alter table VIPApiKeys
  add foreign key (identifier) references VIPExternalPlatforms (identifier)
  on delete cascade on update cascade;

-- v2.1
DROP TABLE VIPDropboxAccounts;

-- v2.2
alter table VIPAppVersions ADD useBoutiquesForm BOOLEAN;

-- v2.3.2
ALTER TABLE VIPUsers DROP COLUMN phone;

-- v2.4
DROP TABLE VIPAccountsGroups;
DROP TABLE VIPAccounts;
ALTER TABLE VIPExternalPlatforms ADD upload_url VARCHAR(255);
ALTER TABLE VIPExternalPlatforms ADD keycloak_client_id VARCHAR(255);
ALTER TABLE VIPExternalPlatforms ADD refresh_token_url VARCHAR(255);

-- v2.7
USE workflowsdb;
ALTER TABLE Workflows ADD tags VARCHAR(255);

-- v4.6
ALTER TABLE VIPApplications ADD note VARCHAR(255);
ALTER TABLE VIPAppVersions ADD note VARCHAR(255);