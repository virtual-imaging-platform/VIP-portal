package fr.insalyon.creatis.vip.visualization.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.visualization.client.view.VisualizationException;
import fr.insalyon.creatis.vip.visualization.models.Image;
import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

public interface VisualizationService extends RemoteService {

    public static final String SERVICE_URI = "/visualizationservice";

    public static class Util {
        public static VisualizationServiceAsync getInstance() {
            VisualizationServiceAsync instance =
                (VisualizationServiceAsync)
                GWT.create(VisualizationService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public Image getImageSlicesURL(String localPath, String direction)
        throws VisualizationException;

    public VisualizationItem getVisualizationItemFromLFN(String lfn)
        throws VisualizationException;
}
