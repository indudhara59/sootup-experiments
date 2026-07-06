package com.indudhara.sootup.experiments;

import com.indudhara.sootup.analysis.SootUpAnalysisContext;
import com.indudhara.sootup.util.ExperimentPrinter;
import sootup.java.core.JavaSootMethod;

public class JimpleBodyExperiment {
    private static final int MAX_STATEMENTS_PER_METHOD = 12;

    public void run(SootUpAnalysisContext context, String className) {
        ExperimentPrinter.section("Jimple body preview for " + className);

        var sootClass = context.resolveClass(className);
        if (sootClass.isEmpty()) {
            ExperimentPrinter.note("SootUp could not resolve the class.");
            return;
        }

        sootClass.get().getMethods().stream()
                .filter(JavaSootMethod::hasBody)
                .sorted((left, right) -> left.getSignature().toString().compareTo(right.getSignature().toString()))
                .forEach(method -> {
                    var body = method.getBody();
                    ExperimentPrinter.item(method.getSignature().toString());
                    body.getLocals().stream()
                            .map(local -> local.getName() + ":" + local.getType())
                            .sorted()
                            .forEach(local -> ExperimentPrinter.note("local " + local));

                    body.getStmts().stream()
                            .limit(MAX_STATEMENTS_PER_METHOD)
                            .forEach(statement -> ExperimentPrinter.note("stmt " + statement));

                    if (body.getStmts().size() > MAX_STATEMENTS_PER_METHOD) {
                        ExperimentPrinter.note("... "
                                + (body.getStmts().size() - MAX_STATEMENTS_PER_METHOD)
                                + " more statements");
                    }
                });
    }
}
