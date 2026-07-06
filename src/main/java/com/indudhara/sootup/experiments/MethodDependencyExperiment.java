package com.indudhara.sootup.experiments;

import com.indudhara.sootup.util.ExperimentPrinter;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.java.core.JavaIdentifierFactory;

import java.nio.file.Path;

public class MethodDependencyExperiment {
    public void run(Path classesDirectory, String className) {
        ExperimentPrinter.section("Direct method invocation statements in " + className);

        var view = MethodListingExperiment.createView(classesDirectory);
        var classType = JavaIdentifierFactory.getInstance().getClassType(className);
        var sootClass = view.getClass(classType);

        if (sootClass.isEmpty()) {
            ExperimentPrinter.note("SootUp could not resolve the class.");
            return;
        }

        sootClass.get().getMethods().stream()
                .sorted((left, right) -> left.getSignature().toString().compareTo(right.getSignature().toString()))
                .forEach(method -> {
                    ExperimentPrinter.item(method.getSignature().toString());
                    if (!method.hasBody()) {
                        ExperimentPrinter.note("No body available.");
                        return;
                    }

                    method.getBody().getStmts().stream()
                            .filter(statement -> statement.isInvokableStmt()
                                    && statement.asInvokableStmt().containsInvokeExpr())
                            .map(statement -> statement.asInvokableStmt().getInvokeExpr())
                            .flatMap(java.util.Optional::stream)
                            .map(AbstractInvokeExpr::getMethodSignature)
                            .forEach(signature -> ExperimentPrinter.note("calls " + signature));
                });

        ExperimentPrinter.note("This is direct body inspection, not a full call graph.");
    }
}
