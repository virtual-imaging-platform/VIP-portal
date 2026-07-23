package fr.insalyon.creatis.vip.publication.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.publication.models.Publication;
import fr.insalyon.creatis.vip.publication.models.PublicationType;
import fr.insalyon.creatis.vip.publication.server.business.PublicationBusiness;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;

public class PublicationsIT extends BaseSpringIT {
    @Autowired
    private PublicationBusiness publicationBusiness;
    @Autowired
    private ApplicationBusiness applicationBusiness;

    private long idPublicationCreated;
    private final String publicationApplicationName = "testApp";

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        adminEmail = server.getAdminEmail();
        admin = userBusiness.getUserWithGroups(adminEmail);

        asAdminContext(() -> applicationBusiness.add(new Application(publicationApplicationName, "test citation")));

        // Create test publication
        Publication publication = new Publication("Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);
        publicationBusiness.setUserSupplier(() -> admin);
        publicationBusiness.addPublication(publication);
        idPublicationCreated = publicationBusiness.getPublications().get(0).getId();
    }

    @Test
    public void testInitialisation() throws VipException {
        // verify number publications
        Assertions.assertEquals(1, publicationBusiness.getPublications().size(), "Incorrect number of publications");

        // verify publications authors
        Assertions.assertEquals("author1, author2", publicationBusiness.getPublication(idPublicationCreated).getAuthors(), "Incorrect authors value");

        // verify publications vip application
        Assertions.assertEquals(publicationApplicationName, publicationBusiness.getPublication(idPublicationCreated).getVipApplication(), "Incorrect VIPApplication value");
    }


    /* ********************************************************************************************************************************************** */
    /* ********************************************************** create and add publication ******************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddPublication() throws VipException {
        publicationBusiness.setUserSupplier(() -> admin);
        // With id
        Publication publication = new Publication(idPublicationCreated, "Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);
        publicationBusiness.addPublication(publication);

        // Without id
        Publication publication2 = new Publication("Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);
        publicationBusiness.addPublication(publication2);

        // Without vipAuthor
        Publication publication3 = new Publication(idPublicationCreated, "Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);
        publicationBusiness.addPublication(publication3);

        Assertions.assertEquals(4, publicationBusiness.getPublications().size(), "Incorrect publications number");
    }

    @Test
    public void testAddExistingPublication() throws VipException {
        Publication publication = new Publication(idPublicationCreated, "Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);

        // No exception because the id is not taken into account for the object creation
        publicationBusiness.addPublication(publication);
    }

    @Test
    public void testCatchAddPublicationNonExistentVipAuthor() {
        Publication publication = new Publication(idPublicationCreated, "Publication title", "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);

        publicationBusiness.setUserSupplier(() -> nonExistentUser);
        Exception exception = assertThrows(
                VipException.class, () ->
                        publicationBusiness.addPublication(publication)
        );

        // INSERT + nonExistent foreign key vipAuthor => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "Referential integrity constraint violation"));
    }


    /* ********************************************************************************************************************************************** */
    /* ************************************************************** update publication ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdatePublication() throws VipException {
        Publication publication = new Publication(idPublicationCreated, "Publication title", "21/06/2023", "01010100", "author2, author3", PublicationType.Journal, "typeName", null, publicationApplicationName);

        publicationBusiness.setUserSupplier(() -> admin);
        publicationBusiness.updatePublication(publication);

        Assertions.assertEquals("author2, author3", publicationBusiness.getPublication(idPublicationCreated).getAuthors(), "Incorrect authors value");

    }

    @Test
    public void testSetAttributesUpdatePublication() throws VipException, DAOException, GRIDAClientException {
        Publication publication = publicationBusiness.getPublication(idPublicationCreated);

        publicationBusiness.setUserSupplier(() -> admin);
        asAdminContext(() -> {
            authenticationBusiness.getOrCreateUser("test1@test.fr", "institution");
        });
        publication.setAuthors("author2, author3");
        publication.setId(idPublicationCreated);
        publication.setDate("22/06/2023");
        publication.setDoi("010110");
        publication.setType(PublicationType.BookChapter);
        publication.setTitle("Publication title updated");
        publication.setTypeName("typeName updated");
        publication.setTypeName("typeName updated");

        publicationBusiness.updatePublication(publication);

        // verify updated properties of publication
        Assertions.assertEquals("author2, author3", publicationBusiness.getPublication(idPublicationCreated).getAuthors(), "Incorrect publication authors");
        Assertions.assertEquals(idPublicationCreated, publicationBusiness.getPublication(idPublicationCreated).getId(), "Incorrect publication id");
        Assertions.assertEquals("22/06/2023", publicationBusiness.getPublication(idPublicationCreated).getDate(), "Incorrect publication DOI");
        Assertions.assertEquals("010110", publicationBusiness.getPublication(idPublicationCreated).getDoi(), "Incorrect publication DOI");
        Assertions.assertEquals("Book Chapter", publicationBusiness.getPublication(idPublicationCreated).getType(), "Incorrect publication type");
        Assertions.assertEquals("Publication title updated", publicationBusiness.getPublication(idPublicationCreated).getTitle(), "Incorrect publication title");
        Assertions.assertEquals("typeName updated", publicationBusiness.getPublication(idPublicationCreated).getTypeName(), "Incorrect publication typeName");
        Assertions.assertEquals(adminEmail, publicationBusiness.getPublication(idPublicationCreated).getVipAuthor(), "Incorrect publication VIP author");
    }

    @Test
    public void testCatchUpdateNonExistentPublication() throws VipException {
        Publication publication = new Publication(100L, "Publication title", "21/06/2023", "01010100", "author2, author3", PublicationType.Journal, "typeName", null, publicationApplicationName);

        publicationBusiness.setUserSupplier(() -> admin);
        Exception exception = assertThrows(VipException.class, () -> publicationBusiness.updatePublication(publication));

        assertTrue(StringUtils.contains(exception.getMessage(), "not found"));

        // Check that not publication with the new id was created
        Assertions.assertEquals(1, publicationBusiness.getPublications().size(), "Incorrect number of publications");
    }

    @Test
    public void testCatchUpdatePublicationNonExistentVipAuthor() throws VipException {
        Publication publication = publicationBusiness.getPublication(idPublicationCreated);
        publication.setAuthors("author2, author3");
        publication.setVipAuthor("nonExistent_vip_author@test.fr");

        publicationBusiness.setUserSupplier(() -> admin);
        publicationBusiness.updatePublication(publication);

        // vipAuthor is protected by the business layer and must remain unchanged
        assertEquals("author2, author3", publicationBusiness.getPublication(idPublicationCreated).getAuthors(), "Incorrect publication authors");
        assertEquals(adminEmail, publicationBusiness.getPublication(idPublicationCreated).getVipAuthor(), "Incorrect vipAuthor publication updated");
    }

    /* ********************************************************************************************************************************************** */
    /* *************************************************************** get publication ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetPublication() throws VipException {
        Publication publication = publicationBusiness.getPublication(idPublicationCreated);

        // verify publication information
        Assertions.assertEquals("author1, author2", publication.getAuthors(), "Incorrect publication authors");
        Assertions.assertEquals(idPublicationCreated, publication.getId(), "Incorrect publication id");
        Assertions.assertEquals("21/06/2023", publication.getDate(), "Incorrect publication DOI");
        Assertions.assertEquals("01010100", publication.getDoi(), "Incorrect publication DOI");
        Assertions.assertEquals("Journal Article", publication.getType(), "Incorrect publication type");
        Assertions.assertEquals("Publication title", publication.getTitle(), "Incorrect publication title");
        Assertions.assertEquals("typeName", publication.getTypeName(), "Incorrect publication typeName");
        Assertions.assertEquals(adminEmail, publication.getVipAuthor(), "Incorrect publication VIP author");
    }

    @Test
    public void testCatchGetNonExistentPublication() throws VipException {
        // SELECT + nonExistent primary key publicationId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        assertNull(publicationBusiness.getPublication(100L));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** remove publication ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemovePublication() throws VipException {
        publicationBusiness.setUserSupplier(() -> admin);
        publicationBusiness.removePublication(idPublicationCreated);
        Assertions.assertEquals(0, publicationBusiness.getPublications().size(), "Incorrect number of publications");
    }

    @Test
    public void testCatchRemoveInexistantPublication() throws VipException {
        publicationBusiness.setUserSupplier(() -> admin);
        Exception exception = assertThrows(VipException.class, () -> publicationBusiness.removePublication(100L));

        assertTrue(StringUtils.contains(exception.getMessage(), "not found"));

        // Verify there is still 1 publication
        Assertions.assertEquals(1, publicationBusiness.getPublications().size(), "Incorrect number of publications");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** special characters test ******************************************************* */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testNonAsciiCharacter() {

        Publication publication3 = new Publication(idPublicationCreated, "Publication title with special character :" +
                " CT‑scan or \u2011 (non breaking hyphen / U+2011)"
            + " un coeur : I \u2764 Java!",
            "21/06/2023", "01010100", "author1, author2", PublicationType.Journal, "typeName", null, publicationApplicationName);

        publicationBusiness.setUserSupplier(() -> admin);
        VipException businessException = assertThrows(VipException.class,
                () -> publicationBusiness.addPublication(publication3));

        Assertions.assertTrue(businessException.getMessage().startsWith("Error : Non-valid characters : [‑‑❤]"));
    }

}
