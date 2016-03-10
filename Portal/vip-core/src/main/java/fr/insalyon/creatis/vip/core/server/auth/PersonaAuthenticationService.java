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
package fr.insalyon.creatis.vip.core.server.auth;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author glatard
 */
public class PersonaAuthenticationService extends AbstractAuthenticationService {

    private static final Logger logger = Logger.getLogger(PersonaAuthenticationService.class);
    private JSONObject jsonObj;

    @Override
    protected void checkValidRequest(HttpServletRequest request) throws BusinessException {
        try {
            logger.info("Mozilla Persona authentication request");

            String verifyURL = Server.getInstance().getMozillaPersonaValidationURL();
            String audience = request.getScheme() + "://" + request.getServerName();
            String assertion = request.getParameter("assertion");

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(verifyURL);

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("assertion", assertion));
            params.add(new BasicNameValuePair("audience", audience));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException ex) {
                throw new BusinessException(ex);
            }

            //Execute and get the response.
            CloseableHttpResponse response;
            try {
                response = httpclient.execute(httppost);
            } catch (IOException ex) {
                throw new BusinessException(ex);
            }
            HttpEntity entity = response.getEntity();

            BufferedReader br = null;
            String json = "";
            if (entity != null) {
                InputStream instream = null;
                try {
                    instream = entity.getContent();
                    try {
                        br = new BufferedReader(new InputStreamReader(instream));
                        while (br.ready()) {
                            json += br.readLine();
                        }
                    } finally {
                        instream.close();
                        br.close();
                        response.close();
                    }
                } catch (IOException | IllegalStateException ex) {
                    throw new BusinessException(ex);
                } finally {
                    try {
                        instream.close();
                    } catch (IOException ex) {
                        throw new BusinessException(ex);
                    }
                }
            }

            jsonObj = new JSONObject(json);
            String status = jsonObj.getString("status");

            logger.info("status: " + status);

            if (!status.equals("okay")) {
                throw new BusinessException("Persona assertion is not valid!");
            }

        } catch (JSONException ex) {
            throw new BusinessException(ex);
        }

    }

    @Override
    protected String getEmail() throws BusinessException {
        try {
            return jsonObj.getString("email");
        } catch (JSONException ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getDefaultAccountType() {
        return null;
    }

}
