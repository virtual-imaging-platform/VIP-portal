/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.client.rpc.NewsService;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.NewsBusiness;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class NewsServiceImpl extends AbstractRemoteServiceServlet implements NewsService {

    private static Logger logger = Logger.getLogger(NewsServiceImpl.class);
    private NewsBusiness newsBusiness;

    public NewsServiceImpl() {
        newsBusiness = new NewsBusiness();
    }

    public List<News> getNews() throws CoreException {

        try {
            return newsBusiness.getNews();

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    public void add(News news) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Adding news '" + news.getTitle() + "'.");
            news.setAuthor(getSessionUser().getFullName());
            newsBusiness.add(news);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    public void update(News news) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Updating news '" + news.getTitle() + "'.");
            newsBusiness.update(news);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }

    public void remove(News news) throws CoreException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing news '" + news.getTitle() + "'.");
            newsBusiness.remove(news);

        } catch (BusinessException ex) {
            throw new CoreException(ex);
        }
    }
}
