package integrationtest;

import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicationsIT extends BaseSpringIT {

    @Autowired
    private PublicationBusiness publicationBusiness;

    @Test
    public void shouldAddAndGetPublications() throws BusinessException {

        Assertions.assertTrue(publicationBusiness.getPublications().isEmpty());

        Publication publication = new Publication(
                "test pub", "test pub date", "test doi", "test pub authors",
                "test pub type", "test pub type name", "test pub author",
                "test pub app");
        publicationBusiness.addPublication(publication);

        Assertions.assertEquals(1, publicationBusiness.getPublications().size());
    }

}
