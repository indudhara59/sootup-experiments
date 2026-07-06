package com.indudhara.sootup.experiments;

import com.indudhara.sootup.analysis.SootUpAnalysisContext;
import com.indudhara.sootup.model.MethodSummary;
import com.indudhara.sootup.util.ExperimentPrinter;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootMethod;

import java.util.List;

public class ProjectSummaryExperiment {
    public void run(SootUpAnalysisContext context, List<String> classNames) {
        ExperimentPrinter.section("Project summary");

        int resolvedClasses = 0;
        int methods = 0;
        int methodsWithBodies = 0;
        int statements = 0;
        int invocations = 0;

        for (String className : classNames) {
            var sootClass = context.resolveClass(className);
            if (sootClass.isEmpty()) {
                continue;
            }

            resolvedClasses++;
            for (JavaSootMethod method : sootClass.get().getMethods()) {
                MethodSummary summary = summarize(method);
                methods++;
                if (summary.hasBody()) {
                    methodsWithBodies++;
                    statements += summary.statementCount();
                    invocations += summary.invocationCount();
                }
            }
        }

        ExperimentPrinter.item("classes resolved by SootUp: " + resolvedClasses + " / " + classNames.size());
        ExperimentPrinter.item("methods discovered: " + methods);
        ExperimentPrinter.item("methods with bodies: " + methodsWithBodies);
        ExperimentPrinter.item("Jimple statements inspected: " + statements);
        ExperimentPrinter.item("direct invocation statements: " + invocations);
    }

    public void runForClass(SootUpAnalysisContext context, String className) {
        ExperimentPrinter.section("Class summary for " + className);

        var sootClass = context.resolveClass(className);
        if (sootClass.isEmpty()) {
            ExperimentPrinter.note("SootUp could not resolve the class.");
            return;
        }

        JavaSootClass resolvedClass = sootClass.get();
        ExperimentPrinter.item("fields: " + resolvedClass.getFields().size());
        ExperimentPrinter.item("methods: " + resolvedClass.getMethods().size());
        resolvedClass.getSuperclass()
                .ifPresent(superClass -> ExperimentPrinter.item("superclass: " + superClass));

        resolvedClass.getMethods().stream()
                .map(ProjectSummaryExperiment::summarize)
                .sorted((left, right) -> left.signature().compareTo(right.signature()))
                .forEach(summary -> ExperimentPrinter.note(summary.signature()
                        + " | params=" + summary.parameterCount()
                        + ", locals=" + summary.localCount()
                        + ", stmts=" + summary.statementCount()
                        + ", branches=" + summary.branchCount()
                        + ", invokes=" + summary.invocationCount()));
    }

    public static MethodSummary summarize(JavaSootMethod method) {
        if (!method.hasBody()) {
            return new MethodSummary(
                    method.getSignature().toString(),
                    method.getReturnType().toString(),
                    method.getParameterCount(),
                    0,
                    0,
                    0,
                    0,
                    false);
        }

        var body = method.getBody();
        int branchCount = (int) body.getStmts().stream()
                .filter(statement -> statement.branches())
                .count();
        int invocationCount = (int) body.getStmts().stream()
                .filter(statement -> statement.isInvokableStmt()
                        && statement.asInvokableStmt().containsInvokeExpr())
                .count();

        return new MethodSummary(
                method.getSignature().toString(),
                method.getReturnType().toString(),
                method.getParameterCount(),
                body.getLocalCount(),
                body.getStmts().size(),
                branchCount,
                invocationCount,
                true);
    }
}
