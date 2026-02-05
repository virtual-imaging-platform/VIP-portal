package fr.insalyon.creatis.vip.core.server.business;

import java.text.Normalizer;

import fr.insalyon.creatis.vip.core.client.VipException;

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

    public static void assertOnlyLatin1Characters(String s) throws VipException {
        String nonLatin1Char = filterNonLatin1Characters(s);
        if ( ! nonLatin1Char.isEmpty()) {
            throw new VipException("Non-valid characters : [" + nonLatin1Char + "] (in string \"" + s + "\" )");
        }
    }

    public static String filterNonLatin1Characters(String s) {
        // remove latin1 characters to only keep non-latin1 ones
        return s.replaceAll("[\\p{InBasicLatin}\\p{InLatin-1Supplement}]", "");
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
