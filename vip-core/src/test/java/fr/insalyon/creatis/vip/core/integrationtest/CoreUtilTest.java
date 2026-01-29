package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoreUtilTest {

    @Test
    public void testLatin1Characters() {
        // CoreUtil.filterNonLatin1Characters REMOVES latin1 characters and keeps the non-latin1 ones
        Assertions.assertEquals(CoreUtil.filterNonLatin1Characters("coucou c'est Axel"), "");
        // these accents should be accepted : éÉÈàðñòóö÷øùúûüýþÿ
        Assertions.assertEquals(CoreUtil.filterNonLatin1Characters("éÉÈàðñòóö÷øùúûüýþÿ"), "");
        // once a bug on VIP : non-breaking hyphen can cause problems
        Assertions.assertEquals(CoreUtil.filterNonLatin1Characters("CT‑scan with non-breaking hyphen \u2011"), "‑‑");
        // emoji
        Assertions.assertEquals(CoreUtil.filterNonLatin1Characters("un emoji coeur : I \u2764 Java!"), "❤");
    }
}
