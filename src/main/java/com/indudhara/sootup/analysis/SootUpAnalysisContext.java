package com.indudhara.sootup.analysis;

import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaSootClass;
import sootup.java.core.views.JavaView;

import java.nio.file.Path;
import java.util.Optional;

public class SootUpAnalysisContext {
    private final Path classesDirectory;
    private final JavaIdentifierFactory identifierFactory;
    private final JavaView view;

    public SootUpAnalysisContext(Path classesDirectory) {
        this.classesDirectory = classesDirectory;
        this.identifierFactory = JavaIdentifierFactory.getInstance();
        AnalysisInputLocation inputLocation =
                new JavaClassPathAnalysisInputLocation(classesDirectory.toString());
        this.view = new JavaView(inputLocation);
    }

    public Path classesDirectory() {
        return classesDirectory;
    }

    public JavaIdentifierFactory identifierFactory() {
        return identifierFactory;
    }

    public JavaView view() {
        return view;
    }

    public Optional<JavaSootClass> resolveClass(String className) {
        return view.getClass(identifierFactory.getClassType(className));
    }
}
