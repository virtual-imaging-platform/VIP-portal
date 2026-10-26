package fr.insalyon.creatis.vip.core.server.business;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.HexFormat;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TwoFactorCodeDAO;
import fr.insalyon.creatis.vip.core.server.model.TwoFactorCode;

@Service
public class TwoFactorBusiness {

    private static final int MAX_ATTEMPTS = 3;
    private static final int CODE_EXPIRATION_MINUTES = 10;

    private final TwoFactorCodeDAO twoFactorCodeDAO;
    private final EmailBusiness emailBusiness;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Random random = new SecureRandom();
    @Autowired
    public TwoFactorBusiness(TwoFactorCodeDAO twoFactorCodeDAO, EmailBusiness emailBusiness) {
        this.twoFactorCodeDAO = twoFactorCodeDAO;
        this.emailBusiness = emailBusiness;
    }


    public void sendCode(String email) throws VipException {
        String code = generateCode();
        Timestamp expireAT = new Timestamp(System.currentTimeMillis() + CODE_EXPIRATION_MINUTES * 60 * 1000);

        try {
            twoFactorCodeDAO.save(email, hash(code), expireAT);
        } catch (DAOException ex) {
            throw new VipException("Error saving two-factor code: " + ex.getMessage(), ex);
        }
        String content = "<p>Your two-factor authentication code is: <strong>" + code + "</strong></p>"
                + "<p>This code will expire in " + CODE_EXPIRATION_MINUTES + " minutes.</p>";
        emailBusiness.sendEmail("VIP verification code", content,
                        new String[] { email }, true, email);
                logger.info("2FA code sent to '{}'", email);
            }


    private String generateCode() {
        int n = random.nextInt(1_000_000);
        return String.format("%06d", n);
    }
    
    public boolean verifyCode(String email, String submittedCode) throws VipException {
        try {
            TwoFactorCode stored = twoFactorCodeDAO.get(email);

            if (stored == null) {
                logger.warn("2FA failed for '{}': no pending code", email);
                return false;
            }

            if (stored.isExpired() || stored.getAttempts() >= MAX_ATTEMPTS) {
                twoFactorCodeDAO.delete(email);
                logger.warn("2FA failed for '{}': code expired or too many attempts", email);
                return false;
            }

            boolean ok = submittedCode != null && MessageDigest.isEqual(
                    hash(submittedCode.trim()).getBytes(StandardCharsets.UTF_8),
                    stored.getCodeHash().getBytes(StandardCharsets.UTF_8));

            if (ok) {
                twoFactorCodeDAO.delete(email);
                logger.info("2FA succeeded for '{}'", email);
                return true;
            }

            twoFactorCodeDAO.incrementAttempts(email);
            logger.warn("2FA failed for '{}': wrong code", email);
            return false;

        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }


    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing input", e);
        }
    }

   
}