package fr.insalyon.creatis.vip.core.server.business;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

class PasswordBusinessTest {

    private static final String EMAIL = "test@example.com";

    private UserDAO userDAO;
    private PasswordBusiness passwordBusiness;
    private final AtomicReference<String> storedHash = new AtomicReference<>();

    @BeforeEach
    void setUp() throws Exception {
        userDAO = mock(UserDAO.class);
        passwordBusiness = new PasswordBusiness(userDAO);
        storedHash.set(null);

        when(userDAO.authenticate(eq(EMAIL), anyString(), any(PasswordEncoder.class)))
                .thenAnswer(invocation -> {
                    String password = invocation.getArgument(1);
                    PasswordEncoder encoder = invocation.getArgument(2);
                    return storedHash.get() != null && encoder.matches(password, storedHash.get());
                });
        doAnswer(invocation -> {
            storedHash.set(invocation.getArgument(1));
            return null;
        }).when(userDAO).resetPassword(eq(EMAIL), anyString());
    }

    private boolean canAuthenticate(String plainPassword) throws Exception {
        return passwordBusiness.authenticate(EMAIL, plainPassword);
    }
    private User user() {
        return new User("firstName", "lastName", EMAIL, "institution", CountryCode.fr);
    }

    @Test
    void hashThenAuthenticateWithCorrectPasswordShouldSucceed() throws Exception {
        storedHash.set(passwordBusiness.hash("mdp123*"));
        assertTrue(storedHash.get().startsWith("$argon2id$"));
        assertTrue(canAuthenticate("mdp123*"));
    }

    @Test
    void hashThenAuthenticateWithWrongPasswordShouldFail() throws Exception {
        storedHash.set(passwordBusiness.hash("mdp123*"));
        assertFalse(canAuthenticate("SomethingElse"));
    }
    
    @Test
    void hashcalledTwiceWithSamePasswordshouldProduceDifferentHashes() throws Exception {
        String hash1 = passwordBusiness.hash("mdp123*");
        String hash2 = passwordBusiness.hash("mdp123*");
        assertNotEquals(hash1, hash2);
        storedHash.set(hash1);
        assertTrue(canAuthenticate("mdp123*"));
        storedHash.set(hash2);
        assertTrue(canAuthenticate("mdp123*"));
    }

    @Test
    void hashshouldUseOwaspParameters() throws VipException {
        String hash = passwordBusiness.hash("mdp123*");
        assertTrue(hash.startsWith("$argon2id$v=19$m=19456,t=2,p=1$"));
    }

    @Test
    void authenticatewithLegacyRawMd5HashshouldFail() throws Exception {
        storedHash.set(MD5.get("mdp123*"));
        assertFalse(canAuthenticate("mdp123*"));
    }

    @Test
    void upgradeLegacyMd5HashthenAuthenticatshouldSucceedWithOriginalPassword() throws Exception {
        storedHash.set(passwordBusiness.upgradeLegacyMd5Hash(MD5.get("mdp123*")));
        assertTrue(storedHash.get().startsWith("$argon2id$"));
        assertTrue(canAuthenticate("mdp123*"));
        assertFalse(canAuthenticate("WrongPassword"));
    }

    @Test
    void updatewithCorrectCurrentPasswordshouldChangePassword() throws Exception {
        storedHash.set(passwordBusiness.hash("currentPassword"));
        passwordBusiness.update(user(), "currentPassword", "newPassword");
        assertTrue(canAuthenticate("newPassword"));
        assertFalse(canAuthenticate("currentPassword"));
    }

    @Test
    void updatewithWrongCurrentPasswordshouldThrowAndKeepPassword() throws Exception {
        storedHash.set(passwordBusiness.hash("currentPassword"));
        VipException exception = assertThrows(VipException.class,
                () -> passwordBusiness.update(user(), "wrongPassword", "newPassword"));
        assertTrue(exception.getMessage().contains("The current password mismatch"));
        verify(userDAO, never()).resetPassword(anyString(), anyString());
        assertTrue(canAuthenticate("currentPassword"));
    }

    @Test
    void resetwithCorrectCode_shouldChangePassword() throws Exception {
        User user = user();
        user.setCode("the-code");
        when(userDAO.get(EMAIL)).thenReturn(user);

        passwordBusiness.reset(EMAIL, "the-code", "newPassword");

        assertTrue(canAuthenticate("newPassword"));
    }

    @Test
    void resetwithWrongCode_shouldThrowAndNotChangePassword() throws Exception {
        User user = user();
        user.setCode("the-code");
        when(userDAO.get(EMAIL)).thenReturn(user);

        VipException exception = assertThrows(VipException.class,
                () -> passwordBusiness.reset(EMAIL, "wrong-code", "newPassword"));

        assertTrue(exception.getMessage().contains("Wrong reset code"));
        verify(userDAO, never()).resetPassword(anyString(), anyString());
    }
}