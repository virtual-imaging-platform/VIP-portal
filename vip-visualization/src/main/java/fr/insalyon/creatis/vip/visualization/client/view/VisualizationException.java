package fr.insalyon.creatis.vip.visualization.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

public class VisualizationException
    extends VipException implements IsSerializable {

    public VisualizationException() {}

    public VisualizationException(String message) {
        super(message);
    }

    public VisualizationException(Throwable thrwbl) {
        super(thrwbl);
    }
}
