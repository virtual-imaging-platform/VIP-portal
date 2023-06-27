package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.ExecutionPublicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExecutionsBusiness {
    private ExecutionPublicDAO executionPublicDAO;

    @Autowired
    public ExecutionsBusiness(ExecutionPublicDAO executionPublicDAO) {
        this.executionPublicDAO = executionPublicDAO;
    }

    public List<Execution> getExecutions() throws BusinessException {
        try {
            return executionPublicDAO.getExecutions();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}

