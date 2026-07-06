package com.indudhara.sootup.experiments;

import com.indudhara.sootup.analysis.SootUpAnalysisContext;
import com.indudhara.sootup.model.InvocationRecord;
import com.indudhara.sootup.util.ExperimentPrinter;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.java.core.JavaSootMethod;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class MethodDependencyExperiment {
    public List<InvocationRecord> run(Path classesDirectory, String className) {
        return run(new SootUpAnalysisContext(classesDirectory), className);
    }

    public List<InvocationRecord> run(SootUpAnalysisContext context, String className) {
        ExperimentPrinter.section("Direct method invocation statements in " + className);

        var sootClass = context.resolveClass(className);

        if (sootClass.isEmpty()) {
            ExperimentPrinter.note("SootUp could not resolve the class.");
            return List.of();
        }

        List<InvocationRecord> invocations = sootClass.get().getMethods().stream()
                .sorted((left, right) -> left.getSignature().toString().compareTo(right.getSignature().toString()))
                .flatMap(method -> collectInvocations(method).stream())
                .toList();

        if (invocations.isEmpty()) {
            ExperimentPrinter.note("No direct invocation statements found.");
        }

        invocations.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        InvocationRecord::sourceMethod,
                        java.util.TreeMap::new,
                        java.util.stream.Collectors.toList()))
                .forEach((sourceMethod, calls) -> {
                    ExperimentPrinter.item(sourceMethod);
                    calls.forEach(call -> ExperimentPrinter.note("calls " + call.targetMethod()));
                });

        ExperimentPrinter.note("This is direct body inspection, not a full call graph.");
        return invocations;
    }

    public static List<InvocationRecord> collectInvocations(JavaSootMethod method) {
        if (!method.hasBody()) {
            return List.of();
        }

        return method.getBody().getStmts().stream()
                .filter(statement -> statement.isInvokableStmt()
                        && statement.asInvokableStmt().containsInvokeExpr())
                .map(statement -> {
                    Optional<AbstractInvokeExpr> invokeExpr = statement.asInvokableStmt().getInvokeExpr();
                    return invokeExpr.map(expr -> new InvocationRecord(
                            method.getSignature().toString(),
                            expr.getMethodSignature().toString(),
                            statement.toString()));
                })
                .flatMap(Optional::stream)
                .toList();
    }
}
