package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;

public class AppVersionTestUtils {

    static final public AppVersion version01;
    static final public AppVersion version42;
    static final public AppVersion version43;

    static {
        version01 = new AppVersion("application (TOCHANGE)", "0.1", "{}", true);
        version42 = new AppVersion("application (TOCHANGE)", "4.2", "{}", true);
        version43 = new AppVersion("application (TOCHANGE)", "4.2", "{}", false);
    }

    static public AppVersion getVersion(AppVersion base, Application app) {
        return new AppVersion(
                app.getName(),
                base.getVersion(),
                base.getDescriptor(),
                base.isVisible());
    }
}
