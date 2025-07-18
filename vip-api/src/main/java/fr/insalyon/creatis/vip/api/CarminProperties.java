/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api;

/**
 * Created by abonnet on 7/21/16.
 */
public interface CarminProperties {

    String CORS_AUTHORIZED_DOMAINS = "cors.autorized-domains";

    // CARMIN COMMON
    String PLATFORM_NAME = "carmin.platform.name";
    String PLATFORM_DESCRIPTION = "carmin.platform.description";
    String PLATFORM_EMAIL = "carmin.platform.email";
    String SUPPORTED_TRANSFER_PROTOCOLS = "carmin.platform.supported_transfer_protocols";
    String SUPPORTED_MODULES = "carmin.platform.supported_modules";
    String DEFAULT_LIMIT_LIST_EXECUTION = "carmin.platform.default_limit_list_execution";
    String UNSUPPORTED_METHODS = "carmin.platform.unsupported_methods";
    String SUPPORTED_API_VERSION = "carmin.platform.supported_API_Version";

    // CARMIN auth

    String APIKEY_HEADER_NAME = "carmin.authentication.apikey.header.name";
    String APIKEY_GENERATE_NEW_EACH_TIME = "carmin.authentication.apikey.generate_new_key_on_each_authentication";

    // CARMIN DATA

    String API_DIRECTORY_MIME_TYPE = "carmin.data.mime_type.directory";
    String API_DEFAULT_MIME_TYPE = "carmin.data.mime_type.default";
    String API_DOWNLOAD_RETRY_IN_SECONDS = "carmin.data.download.retry";
    String API_DOWNLOAD_TIMEOUT_IN_SECONDS = "carmin.data.download.timeout";
    String API_DATA_TRANSFERT_MAX_SIZE = "carmin.data.max_size";

    // CARMIN PROCESSING

    String API_PIPELINE_WHITE_LIST = "carmin.processing.pipelines.whitelist";
    String ADDITIONNAL_INPUT_VALID_CHARS = "\\[\\]?&="; // will be use in regex, so brackets must be escaped

    // NON-CARMIN : STATISTICS
    String STATS_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    // NON-CARMIN : Auth

    String SHANOIR_HOST_IP = "shanoir.host.ip";
    String KEYCLOAK_ACTIVATED = "keycloak.active";

    // EGI OIDC login parameters
    String EGI_CLIENT_ID = "oidc.egi.client_id";
    String EGI_CLIENT_SECRET = "oidc.egi.client_secret";
    String EGI_REDIRECT_URI = "oidc.egi.redirect_uri";
    String EGI_AUTHORIZATION_URI = "oidc.egi.authorization_uri";
    String EGI_TOKEN_URI = "oidc.egi.token_uri";
    String EGI_USER_INFO_URI = "oidc.egi.user_info_uri";
    String EGI_JWK_SET_URI = "oidc.egi.jwk_set_uri";

    // LSLOGIN OIDC login parameters
    String LSLOGIN_CLIENT_ID = "oidc.lslogin.client_id";
    String LSLOGIN_CLIENT_SECRET = "oidc.lslogin.client_secret";
    String LSLOGIN_REDIRECT_URI = "oidc.lslogin.redirect_uri";
    String LSLOGIN_AUTHORIZATION_URI = "oidc.lslogin.authorization_uri";
    String LSLOGIN_TOKEN_URI = "oidc.lslogin.token_uri";
    String LSLOGIN_USER_INFO_URI = "oidc.lslogin.user_info_uri";
    String LSLOGIN_JWK_SET_URI = "oidc.lslogin.jwk_set_uri";
}
