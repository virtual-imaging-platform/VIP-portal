package fr.insalyon.creatis.vip.applicationimporter.integrationtest;

import fr.insalyon.creatis.vip.applicationimporter.server.business.TargzUtils;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class TargzIT {


    @Test
    @EnabledOnOs(value = OS.LINUX)
    public void verifyTargz() throws IOException, BusinessException, InterruptedException {
        TargzUtils targzUtils = new TargzUtils();

        final String FILENAME1 = "file1ToCompress.txt";
        final String FILENAME2 = "file2ToCompress.txt";
        final String OUTARCHIVENAME = "testArchive.tgz";

        File path1 = new ClassPathResource(FILENAME1).getFile();
        File path2 = new ClassPathResource(FILENAME2).getFile();
        File outArchive = new File(
                new ClassPathResource("").getFile(),
                OUTARCHIVENAME);

        // call SUT

        targzUtils.createTargz(
                Arrays.asList(path1, path2),
                outArchive.getAbsolutePath());

        // verify result archive
        Path untarPath = callUntarCommand(outArchive.toPath());
        File[] untarFiles = untarPath.toFile().listFiles();
        Assertions.assertEquals(2, untarFiles.length);
        Assertions.assertTrue(Stream.of(untarFiles).anyMatch(f -> FILENAME1.equals(f.getName())));
        Assertions.assertTrue(Stream.of(untarFiles).anyMatch(f -> FILENAME2.equals(f.getName())));

    }

    private Path callUntarCommand(Path tarFile) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        Path tmpUntarDir = tarFile.getParent().resolve("tmpUntarDir");
        tmpUntarDir.toFile().mkdir();
        Path outputFile = tarFile.getParent().resolve("untarOutput.out");

        builder.command("tar", "-zxf", tarFile.toString());
        builder.directory(tmpUntarDir.toFile());
        builder.redirectErrorStream(true);
        builder.redirectOutput(Redirect.appendTo(outputFile.toFile()));
        Process process = builder.start();
        process.waitFor();
        return tmpUntarDir;
    }
}
