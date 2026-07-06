package com.indudhara.sootup;

import com.indudhara.sootup.experiments.ClassListingExperiment;
import com.indudhara.sootup.experiments.MethodDependencyExperiment;
import com.indudhara.sootup.experiments.MethodListingExperiment;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        Path classesDirectory = args.length > 0 ? Path.of(args[0]) : Path.of("target", "classes");
        String className = args.length > 1 ? args[1] : "SampleTarget";

        new ClassListingExperiment().run(classesDirectory);
        new MethodListingExperiment().run(classesDirectory, className);
        new MethodDependencyExperiment().run(classesDirectory, className);
    }
}
