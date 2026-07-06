package com.indudhara.sootup.experiments;

import com.indudhara.sootup.util.ExperimentPrinter;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.signatures.MethodSignature;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

import java.nio.file.Path;
import java.util.List;

public class MethodListingExperiment {
    public List<MethodSignature> run(Path classesDirectory, String className) {
        ExperimentPrinter.section("Methods in " + className);

        JavaView view = createView(classesDirectory);
        var classType = JavaIdentifierFactory.getInstance().getClassType(className);
        var sootClass = view.getClass(classType);

        if (sootClass.isEmpty()) {
            ExperimentPrinter.note("SootUp could not resolve the class.");
            return List.of();
        }

        List<MethodSignature> methods = sootClass.get().getMethods().stream()
                .map(method -> method.getSignature())
                .sorted((left, right) -> left.toString().compareTo(right.toString()))
                .toList();

        methods.forEach(method -> ExperimentPrinter.item(method.toString()));
        return methods;
    }

    static JavaView createView(Path classesDirectory) {
        AnalysisInputLocation inputLocation =
                new JavaClassPathAnalysisInputLocation(classesDirectory.toString());
        return new JavaView(inputLocation);
    }
}
