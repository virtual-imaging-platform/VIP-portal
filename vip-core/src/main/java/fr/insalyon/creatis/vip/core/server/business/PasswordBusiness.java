package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

@Service
public class PasswordBusiness extends CommonBusiness {

    private final UserDAO userDAO;
    private final Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Autowired
    public PasswordBusiness(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean isModernFormat(String storedHash) {
        return storedHash != null && storedHash.startsWith("$argon2id$");
    }

    public String hash(String plainPassword) {
        return encoder.encode(md5(plainPassword));
    }

    public boolean verify(String plainPassword, String storedHash) {
        return encoder.matches(md5(plainPassword), storedHash);
    }

    public String upgradeLegacyMd5Hash(String existingMd5Hash) {
        return encoder.encode(existingMd5Hash);
    }

    private String md5(String plainPassword) {
        try {
            return MD5.get(plainPassword);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error("Error computing MD5 step for password hashing", ex);
            throw new RuntimeException(ex);
        }
    }

    public void update(User user, String currentPassword, String newPassword) throws VipException {
        try {
            String storedHash = userDAO.getPasswordHash(user.getEmail());
            boolean currentPasswordCorrect = storedHash != null
                    && isModernFormat(storedHash)
                    && verify(currentPassword, storedHash);

            if (!currentPasswordCorrect) {
                logger.error("The current password mismatch for {}", user.getEmail());
                throw new VipException("The current password mismatch.");
            }

            userDAO.resetPassword(user.getEmail(), hash(newPassword));
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void setPassword(String email, String newPassword) throws VipException {
        try {
            userDAO.resetPassword(email, hash(newPassword));
        } catch (DAOException ex) {
            logger.error("Error setting password for {}", email, ex);
            throw new VipException(ex);
        }
    }

    public void reset(String email, String code, String password) throws VipException {
        try {
            User user = userDAO.get(email);

            if (code.equals(user.getCode())) {
                userDAO.resetPassword(email, hash(password));
            } else {
                logger.error("Wrong reset code for {} : {}", email, code);
                throw new VipException("Wrong reset code.");
            }
        } catch (DAOException ex) {
            logger.error("Error resetting password for {}", email, ex);
            throw new VipException(ex);
        }
    }
}