package fr.insalyon.creatis.vip.visualization.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.visualization.models.Image;
import fr.insalyon.creatis.vip.visualization.models.VisualizationItem;

public interface VisualizationServiceAsync {
    public void getImageSlicesURL(
        String imageLocalPath,
        String direction,
        AsyncCallback<Image> asyncCallback);

    public void getVisualizationItemFromLFN(
        String lfn, AsyncCallback<VisualizationItem> asyncCallback);
}
