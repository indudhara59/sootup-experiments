# sootup-experiments

Educational experiments for learning [SootUp](https://github.com/soot-oss/SootUp) and Java static analysis workflows.

## Project Status

Work in progress. This repository is intentionally small and practical: each experiment should compile, run, and show one concrete SootUp concept without pretending to be a production analyzer.

## Technologies

- Java 17
- Maven
- SootUp 2.0.0

## What SootUp Is Used For

SootUp is used here to load compiled Java bytecode, resolve classes, inspect methods, and read method bodies in SootUp's intermediate representation. The current code focuses on basic program structure exploration before moving into deeper analyses.

## Current Experiments

- `ClassListingExperiment`: lists class files found under a compiled target directory.
- `MethodListingExperiment`: resolves one target class with SootUp and prints its declared methods.
- `MethodDependencyExperiment`: resolves method bodies with SootUp and prints method invocation statements found in those bodies.

The dependency experiment is deliberately modest. It reports direct invocation statements visible in the loaded method bodies. It is not a complete call graph.

## Planned Experiments

- Build a real call graph using SootUp's call graph module.
- Explore entry point configuration.
- Compare source-code and bytecode frontends.
- Inspect Jimple statements in more detail.
- Add small examples for control-flow and data-flow analysis.

## How To Build

```bash
mvn compile
```

The Maven build treats `examples/` as an additional source directory, so `examples/SampleTarget.java` is compiled into `target/classes` along with the experiment runner.

Generated files under `target/` are intentionally not committed. Reviewers can reproduce them with `mvn compile`.

## How To Run

```bash
mvn exec:java
```

By default, `Main` analyzes the compiled `SampleTarget` class from `target/classes`.

You can also pass a different compiled-classes directory and class name:

```bash
mvn exec:java -Dexec.args="target/classes SampleTarget"
```

## Limitations

- The current class listing is filesystem-based, while method and body inspection use SootUp.
- The dependency-style experiment prints direct method invocation statements only.
- Full call graph generation is planned but not implemented yet.
- The sample target is intentionally small so the output is easy to read.

## Reviewer Notes

This repository is designed to be evaluated from source:

1. Read the experiment classes under `src/main/java/com/indudhara/sootup/experiments`.
2. Run `mvn compile` to verify the project builds.
3. Run `mvn exec:java` to see the current SootUp experiments against `SampleTarget`.

The GitHub Actions workflow also runs `mvn compile` on pushes and pull requests.
