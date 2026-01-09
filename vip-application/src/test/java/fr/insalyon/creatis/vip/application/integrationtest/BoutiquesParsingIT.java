package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.boutiques.model.ContainerImage;
import fr.insalyon.creatis.boutiques.model.Input;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class BoutiquesParsingIT extends BaseApplicationSpringIT {

    @Value("classpath:FreeSurfer-Recon-all_v731.json")
    Resource resourceFile;

    @Test
    public void testFreesurferParsing() throws BusinessException, IOException {
        BoutiquesDescriptor desc = boutiquesBusiness.parseBoutiquesFile(resourceFile.getFile());

        // general stuff
        Assertions.assertEquals("FreeSurfer-Recon-all", desc.getName());
        Assertions.assertEquals("v7.3.1", desc.getToolVersion());
        Assertions.assertTrue(desc.getCommandLine().contains("recon-all -subjid [SUBJID] [NIFTI] [DIRECTIVES]"));
        Assertions.assertEquals(1, desc.getOutputFiles().size());
        Assertions.assertEquals(2, desc.getCustom().getAdditionalProperties().size());
        Assertions.assertEquals("Natacha Beck <natacha.beck at mcgill.ca>", desc.getCustom().getAdditionalProperties().get("cbrain:author"));
        Assertions.assertEquals(true, desc.getCustom().getAdditionalProperties().get("cbrain:readonly-input-files"));


        // container
        Assertions.assertEquals(ContainerImage.Type.DOCKER, desc.getContainerImage().getType());
        Assertions.assertEquals(1, desc.getContainerImage().getContainerOpts().size());
        Assertions.assertEquals("-v /tmp:/tmp", desc.getContainerImage().getContainerOpts().get(0));

        // inputs
        Assertions.assertEquals(13, desc.getInputs().size());
        Optional<Input> licenseInput = desc.getInputs().stream().filter(i -> "license".equals(i.getId())).findAny();
        Assertions.assertTrue(licenseInput.isPresent());
        Assertions.assertFalse(licenseInput.get().getOptional());
        Assertions.assertEquals(Input.Type.FILE, licenseInput.get().getType());
        Assertions.assertEquals("[LICENSE_FILE]", licenseInput.get().getValueKey());

        Optional<Input> directivesInput = desc.getInputs().stream().filter(i -> "directives".equals(i.getId())).findAny();
        Assertions.assertTrue(directivesInput.isPresent());
        Assertions.assertEquals(Input.Type.STRING, directivesInput.get().getType());
        Assertions.assertTrue(directivesInput.get().getValueChoices().containsAll(Arrays.asList("-all", "-autorecon2", "-autorecon2-perhemi")));

        // outputs
        Assertions.assertEquals(1, desc.getOutputFiles().size());
        Assertions.assertEquals("Output", desc.getOutputFiles().iterator().next().getName());
        Assertions.assertEquals("[SUBJID].tgz", desc.getOutputFiles().iterator().next().getPathTemplate());
        Assertions.assertEquals("[RESULTS]", desc.getOutputFiles().iterator().next().getValueKey());

    }


}
