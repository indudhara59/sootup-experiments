package com.indudhara.sootup.experiments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassListingExperimentTest {
    @Test
    void listsClassNamesFromDirectoryTree(@TempDir Path tempDir) throws Exception {
        Files.createDirectories(tempDir.resolve("demo"));
        Files.createFile(tempDir.resolve("SampleTarget.class"));
        Files.createFile(tempDir.resolve("demo").resolve("OrderService.class"));

        var classNames = new ClassListingExperiment().run(tempDir);

        assertEquals(
                java.util.List.of("SampleTarget", "demo.OrderService"),
                classNames);
    }

    @Test
    void returnsEmptyListForMissingDirectory(@TempDir Path tempDir) throws Exception {
        var classNames = new ClassListingExperiment().run(tempDir.resolve("missing"));

        assertEquals(java.util.List.of(), classNames);
    }
}
