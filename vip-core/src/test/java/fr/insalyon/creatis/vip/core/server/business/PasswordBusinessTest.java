package fr.insalyon.creatis.vip.core.server.business;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.insalyon.creatis.devtools.MD5;

class PasswordBusinessTest {
    private final PasswordBusiness passwordBusiness = new PasswordBusiness(null);

    private boolean matches(String plainPassword, String storedHash) throws Exception {
        return passwordBusiness.getEncoder().matches(MD5.get(plainPassword), storedHash);
    }

    @Test
    void hashThenMatch_withCorrectPassword_shouldSucceed() throws Exception {
        String plain = "MySecret123!";

        String stored = passwordBusiness.hash(plain);

        assertTrue(matches(plain, stored));
    }

    @Test
    void hashThenMatch_withWrongPassword_shouldFail() throws Exception {
        String stored = passwordBusiness.hash("MySecret123!");

        assertFalse(matches("SomethingElse", stored));
    }

    @Test
    void hash_calledTwiceWithSamePassword_shouldProduceDifferentHashes() throws Exception {
        // Argon2 uses a random salt each time, so the two hash strings must
        // be different, even though both are valid for the same password
        String plain = "MySecret123!";

        String stored1 = passwordBusiness.hash(plain);
        String stored2 = passwordBusiness.hash(plain);

        assertNotEquals(stored1, stored2);
        assertTrue(matches(plain, stored1));
        assertTrue(matches(plain, stored2));
    }

    @Test
    void hash_shouldUseOwaspParameters() {
        // OWASP: Argon2id, 19 MiB (19456 KiB) of memory, 2 iterations, parallelism 1
        String stored = passwordBusiness.hash("MySecret123!");

        assertTrue(stored.startsWith("$argon2id$v=19$m=19456,t=2,p=1$"));
    }

    @Test
    void matches_withLegacyRawMd5Hash_shouldFail() throws Exception {
        // A raw MD5 hash (not migrated) must never be accepted
        String plain = "MySecret123!";
        String legacyMd5Hash = MD5.get(plain);

        assertFalse(matches(plain, legacyMd5Hash));
    }

    @Test
    void upgradeLegacyMd5Hash_thenMatch_shouldSucceedWithOriginalPassword() throws Exception {
        // Simulates exactly what the standalone migration script does:
        // take an existing MD5 hash (no plaintext needed) and re-encode it
        String plain = "MySecret123!";
        String existingMd5Hash = MD5.get(plain);

        String upgraded = passwordBusiness.upgradeLegacyMd5Hash(existingMd5Hash);

        assertTrue(upgraded.startsWith("$argon2id$"));
        assertTrue(matches(plain, upgraded));
        assertFalse(matches("WrongPassword", upgraded));
    }
}