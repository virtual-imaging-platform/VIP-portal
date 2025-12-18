package fr.insalyon.creatis.vip.core.server.business;

import java.text.Normalizer;

public class CoreUtil {

    /*
        remove accents and non-ascii characters
    */
    public static String getCleanStringAscii(String s, String replacement) {
        return getCleanString(s, replacement, true, false);
    }

    public static String getCleanStringAlnum(String s, String replacement) {
        return getCleanString(s, replacement, false, true);
    }

    private static String getCleanString(String s, String replacement, boolean onlyKeepAscii, boolean onlyKeepAlnum) {
        // Normalizer.normalize with NFKD form decompose accentuated
        // letters into separate "accent mark + base letter" characters
        s = Normalizer.normalize(s, Normalizer.Form.NFKD);

        if (onlyKeepAscii) {
            // the [^\\p{ASCII}] regex remove all non-ascii characters
            // so also the separated accents char if removeAccents is true
            s = s.replaceAll("[^\\p{ASCII}]", replacement);
        } else if (onlyKeepAlnum) {
            s = s.replaceAll("[^a-zA-Z0-9]", replacement);
        }
        return s;
    }
}
