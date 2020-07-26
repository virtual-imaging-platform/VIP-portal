package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.CachedFile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("local")
public class GridaCacheClientLocal extends GRIDACacheClient {

    public GridaCacheClientLocal() {
        super(null, 0, null);
    }

    @Override
    public List<CachedFile> getCachedFiles() throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public void deleteCachedFile(String path) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }
}
