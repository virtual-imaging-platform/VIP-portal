package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.application.server.business.ReproVipBusiness;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import org.apache.bsf.debug.util.RemoteServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.RemoteException;

public class ReproVipServiceImpl extends AbstractRemoteServiceServlet implements ReproVipService {
    private ReproVipBusiness reproVipBusiness;
    @Override
    public void init() throws ServletException {
        super.init();
        reproVipBusiness = getBean(ReproVipBusiness.class);
    }

    public void executionAdminEmail(String user) {
        try {
            reproVipBusiness.executionAdminEmail(user);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }
}


