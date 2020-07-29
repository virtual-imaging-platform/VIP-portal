package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.grida.common.bean.ZombieFile;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("local")
@Primary
public class GridaZombieClientLocal extends GRIDAZombieClient {

    public GridaZombieClientLocal() {
        super(null, 0, null);
    }

    @Override
    public List<ZombieFile> getList() throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public void delete(String surl) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }


}
