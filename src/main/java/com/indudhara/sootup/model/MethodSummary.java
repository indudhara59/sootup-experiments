package com.indudhara.sootup.model;

public record MethodSummary(
        String signature,
        String returnType,
        int parameterCount,
        int localCount,
        int statementCount,
        int branchCount,
        int invocationCount,
        boolean hasBody
) {
}
