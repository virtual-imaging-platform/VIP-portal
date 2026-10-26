package fr.insalyon.creatis.vip.core.server.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import fr.insalyon.creatis.vip.core.server.dao.TwoFactorCodeDAO;
import fr.insalyon.creatis.vip.core.server.model.TwoFactorCode;

class TwoFactorBusinessTest {

    private static final String EMAIL = "user@test.com";
    private TwoFactorCodeDAO twoFactorCodeDAO;
    private EmailBusiness emailBusiness;
    private TwoFactorBusiness twoFactorBusiness;
    private record SentCode(String code, String hash, Timestamp expiresAt) {}

    @BeforeEach
    void setUp() {
        twoFactorCodeDAO = mock(TwoFactorCodeDAO.class);
        emailBusiness = mock(EmailBusiness.class);
        twoFactorBusiness = new TwoFactorBusiness(twoFactorCodeDAO, emailBusiness);
    }

    /** Calls sendCode and captures what was stored in the database and sent by email. */
    private SentCode sendAndCapture() throws Exception {
        twoFactorBusiness.sendCode(EMAIL);
        ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Timestamp> expiresCaptor = ArgumentCaptor.forClass(Timestamp.class);
        verify(twoFactorCodeDAO).save(eq(EMAIL), hashCaptor.capture(), expiresCaptor.capture());
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailBusiness).sendEmail(anyString(), contentCaptor.capture(),
                any(String[].class), anyBoolean(), anyString());
        String content = contentCaptor.getValue();
        // Find a group of exactly 6 digits in the email, whatever the formatting around it
        Matcher m = Pattern.compile("(?<!\\d)(\\d{6})(?!\\d)").matcher(content);
        assertTrue(m.find(), "the email must contain a 6-digit code, received content: " + content);
        return new SentCode(m.group(1), hashCaptor.getValue(), expiresCaptor.getValue());
    }
    /** Returns a code guaranteed to be different from the given one. */
    private String anotherCode(String code) {
        return code.equals("000000") ? "111111" : "000000";
    }
    @Test
    void sendCode_storesHashNotPlainCode_andExpiresInAbout10Minutes() throws Exception {
        long before = System.currentTimeMillis();
        SentCode sent = sendAndCapture();
        assertEquals(6, sent.code().length());
        assertEquals(64, sent.hash().length(), "hex-encoded SHA-256 must be 64 characters long");
        assertNotEquals(sent.code(), sent.hash(), "the code must never be stored in plain text");

        long validity = sent.expiresAt().getTime() - before;
        assertTrue(validity > 9 * 60 * 1000 && validity <= 11 * 60 * 1000,
                "the code must expire in about 10 minutes");
    }

    @Test
    void verifyCode_correctCode_returnsTrueAndDeletesCode() throws Exception {
        SentCode sent = sendAndCapture();
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), sent.expiresAt(), 0));
        assertTrue(twoFactorBusiness.verifyCode(EMAIL, sent.code()));
        // One time code must be deleted after a successful check
        verify(twoFactorCodeDAO).delete(EMAIL);
    }

    @Test
    void verifyCode_wrongCode_returnsFalseAndCountsAttempt() throws Exception {
        SentCode sent = sendAndCapture();
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), sent.expiresAt(), 0));

        assertFalse(twoFactorBusiness.verifyCode(EMAIL, anotherCode(sent.code())));
        verify(twoFactorCodeDAO).incrementAttempts(EMAIL);
        verify(twoFactorCodeDAO, never()).delete(EMAIL);
    }

    @Test
    void verifyCode_expiredCode_returnsFalseAndDeletesCode() throws Exception {
        SentCode sent = sendAndCapture();
        Timestamp past = new Timestamp(System.currentTimeMillis() - 60_000);
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), past, 0));
        //Even the right code must be refused once expired
        assertFalse(twoFactorBusiness.verifyCode(EMAIL, sent.code()));
        verify(twoFactorCodeDAO).delete(EMAIL);
    }

    @Test
    void verifyCode_tooManyAttempts_refusesEvenCorrectCode() throws Exception {
        SentCode sent = sendAndCapture();
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), sent.expiresAt(), 5));
        //After the maximum number of attempts, even the right code must be refused
        assertFalse(twoFactorBusiness.verifyCode(EMAIL, sent.code()));
        verify(twoFactorCodeDAO).delete(EMAIL);
    }

    @Test
    void verifyCode_noPendingCode_returnsFalse() throws Exception {
        when(twoFactorCodeDAO.get(EMAIL)).thenReturn(null);
        assertFalse(twoFactorBusiness.verifyCode(EMAIL, "123456"));
    }

    @Test
    void verifyCode_nullCode_returnsFalse() throws Exception {
        SentCode sent = sendAndCapture();
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), sent.expiresAt(), 0));
        assertFalse(twoFactorBusiness.verifyCode(EMAIL, null));
    }

    @Test
    void verifyCode_codeWithSpaces_isAccepted() throws Exception {
        SentCode sent = sendAndCapture();
        when(twoFactorCodeDAO.get(EMAIL))
                .thenReturn(new TwoFactorCode(EMAIL, sent.hash(), sent.expiresAt(), 0));
        //Users often copy-paste the code with extra spaces
        assertTrue(twoFactorBusiness.verifyCode(EMAIL, " " + sent.code() + " "));
    }
}