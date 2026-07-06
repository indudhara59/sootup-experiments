package com.indudhara.sootup.util;

public final class ExperimentPrinter {
    private ExperimentPrinter() {
    }

    public static void section(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }

    public static void item(String value) {
        System.out.println(" - " + value);
    }

    public static void note(String value) {
        System.out.println("   " + value);
    }
}
