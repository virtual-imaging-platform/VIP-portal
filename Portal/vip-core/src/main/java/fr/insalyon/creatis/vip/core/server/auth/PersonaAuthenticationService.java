/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.server.auth;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

      @Override
    protected String getEmailIfValidRequest(HttpServletRequest request) throws BusinessException {
       logger.info("Mozilla Persona authentication request");

        String verifyURL = Server.getInstance().getMozillaPersonaValidationURL();
        String audience = request.getScheme()+ "://"+ request.getServerName();
        String assertion = request.getParameter("assertion");
        
        if(assertion == null){
            throw new BusinessException("Assertion string is null");
        }
        
        String email;
        try {
            email = getEmailFromAssertionIfValid(assertion, audience, verifyURL);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        } 
        return email;
        
    }

    private String getEmailFromAssertionIfValid(String assertion, String audience, String verifyURL) throws IOException, JSONException, BusinessException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(verifyURL);

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("assertion", assertion));
        params.add(new BasicNameValuePair("audience", audience));
        httppost.setEntity(new UrlEncodedFormEntity(params));

//Execute and get the response.
        CloseableHttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        BufferedReader br = null;
        String json = "";
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                // do something useful
                br = new BufferedReader(new InputStreamReader(instream));
                while (br.ready()) {
                    json += br.readLine();
                }
            } finally {
                instream.close();
                br.close();
                response.close();
            }
        }

        JSONObject jsonObj = new JSONObject(json);
        String status = jsonObj.getString("status");

        logger.info("status: " + status);

        if (status.equals("okay")) {
            String email = jsonObj.getString("email");
            return email;
        } else {
            logger.info(jsonObj.toString());
            throw new BusinessException("Invalid assertion (status was " + status + ")");
        }
    }

    @Override
    public String getDefaultAccountType() {
        return null;
    }

  
}
