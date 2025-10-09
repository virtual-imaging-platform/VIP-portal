package fr.insalyon.creatis.vip.visualization.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.visualization.client.bean.Image;
import fr.insalyon.creatis.vip.visualization.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.visualization.client.rpc.VisualizationService;
import fr.insalyon.creatis.vip.visualization.client.view.VisualizationException;
import fr.insalyon.creatis.vip.visualization.server.business.VisualizationBusiness;
import jakarta.servlet.ServletException;

public class VisualizationServiceImpl extends AbstractRemoteServiceServlet
    implements VisualizationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private VisualizationBusiness visualizationBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        visualizationBusiness = getBean(VisualizationBusiness.class);
    }

    @Override
    public Image getImageSlicesURL(String imageFileName, String direction)
        throws VisualizationException {

        try {
            trace(logger, "Slicing image: " + imageFileName);
            return visualizationBusiness.getImageSlicesURL(
                imageFileName, direction);
        } catch (VipException ex) {
            throw new VisualizationException(ex);
        }
    }

    @Override
    public VisualizationItem getVisualizationItemFromLFN(String lfn)
            throws VisualizationException {
        try {
            trace(logger, "Getting URL for file: " + lfn);
            User user = getSessionUser();
            return visualizationBusiness.getVisualizationItemFromLFN(
                lfn,
                user);
        } catch (VipException ex) {
            throw new VisualizationException(ex);
        }
    }
}
