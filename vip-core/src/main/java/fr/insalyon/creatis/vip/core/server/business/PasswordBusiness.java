package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int MEMORY_KIB = 19 * 1024; //19 MiB
    private static final int ITERATIONS = 2;
    private final PasswordEncoder encoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY_KIB, ITERATIONS);  

    @Autowired
    public PasswordBusiness(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String hash(String plainPassword) throws VipException {
        return encoder.encode(md5(plainPassword));
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
                if (!authenticate(user.getEmail(), currentPassword)) {
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
      public boolean authenticate(String email, String plainPassword) throws DAOException, VipException {
      return userDAO.authenticate(email, md5(plainPassword), encoder);
  }
}