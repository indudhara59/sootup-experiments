package com.indudhara.sootup;

import com.indudhara.sootup.analysis.SootUpAnalysisContext;
import com.indudhara.sootup.experiments.ClassListingExperiment;
import com.indudhara.sootup.experiments.JimpleBodyExperiment;
import com.indudhara.sootup.experiments.MethodDependencyExperiment;
import com.indudhara.sootup.experiments.MethodListingExperiment;
import com.indudhara.sootup.experiments.ProjectSummaryExperiment;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Path classesDirectory = args.length > 0 ? Path.of(args[0]) : Path.of("target", "classes");
        List<String> classNames = args.length > 1
                ? Arrays.asList(Arrays.copyOfRange(args, 1, args.length))
                : List.of("SampleTarget", "demo.OrderService");

        var context = new SootUpAnalysisContext(classesDirectory);
        var discoveredClasses = new ClassListingExperiment().run(classesDirectory);

        new ProjectSummaryExperiment().run(context, discoveredClasses);

        for (String className : classNames) {
            new ProjectSummaryExperiment().runForClass(context, className);
            new MethodListingExperiment().run(context, className);
            new MethodDependencyExperiment().run(context, className);
            new JimpleBodyExperiment().run(context, className);
        }
    }
}
