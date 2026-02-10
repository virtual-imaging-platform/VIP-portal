package fr.insalyon.creatis.vip.application.client.inter;

import java.util.List;

import fr.insalyon.creatis.vip.application.models.Tag;

public interface CustomApplicationModule {
    
    public boolean doOverride(List<Tag> tags);
    public void run(String appName, String appVersion, String tileName);
}
