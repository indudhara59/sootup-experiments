package com.indudhara.sootup.experiments;

import com.indudhara.sootup.util.ExperimentPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ClassListingExperiment {
    public List<String> run(Path classesDirectory) throws IOException {
        ExperimentPrinter.section("Classes in " + classesDirectory);

        if (!Files.isDirectory(classesDirectory)) {
            ExperimentPrinter.note("Directory does not exist. Run mvn compile first.");
            return List.of();
        }

        List<String> classNames;
        try (var paths = Files.walk(classesDirectory)) {
            classNames = paths
                    .filter(path -> path.toString().endsWith(".class"))
                    .map(classesDirectory::relativize)
                    .map(Path::toString)
                    .map(name -> name.replace('\\', '.').replace('/', '.'))
                    .map(name -> name.substring(0, name.length() - ".class".length()))
                    .sorted()
                    .toList();
        }

        if (classNames.isEmpty()) {
            ExperimentPrinter.note("No class files found.");
        } else {
            classNames.forEach(ExperimentPrinter::item);
        }

        return classNames;
    }
}
