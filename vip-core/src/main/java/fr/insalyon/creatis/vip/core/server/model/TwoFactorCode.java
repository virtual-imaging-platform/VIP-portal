package fr.insalyon.creatis.vip.core.server.model;

import java.sql.Timestamp;

public class TwoFactorCode {

    private final String codeHash;
    private final String email;
    private final Timestamp expireAT;
    private final int attempts;

    public TwoFactorCode(String email, String codeHash, Timestamp expireAT, int attempts) {
        this.email = email;
        this.codeHash = codeHash;
        this.expireAT = expireAT;
        this.attempts = attempts;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getExpireAT() {
        return expireAT;
    }

    public int getAttempts() {
        return attempts;
    }
    public boolean isExpired() {
        return expireAT == null || expireAT.before(new Timestamp(System.currentTimeMillis()));
    }
}