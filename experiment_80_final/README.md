# Baseline Data: Traditional Automated Unit Test Generation

This repository contains the **baseline experimental data** for the study: *"LLM-Based vs. Traditional Automated Unit Test Generation"*. It records the performance of traditional tools (**Randoop** and **EvoSuite**) on 80 selected bugs from the **Defects4J** benchmark.

## Directory Structure

The data is organized by **Project** and **Bug ID**.

```text
experiment_80_final/
├── summary_report.csv          # [Main Result] Metrics for 80 bugs (Coverage & Detection)
├── Lang/                       # Apache Commons Lang (25 bugs)
│   ├── 1/
│   │   ├── code/               # Source code (Buggy version)
│   │   └── baseline/           # Generated test suites
│   │       ├── randoop/        # Randoop output (.tar.bz2)
│   │       └── evosuite/       # EvoSuite output (.tar.bz2)
├── Math/                       # Apache Commons Math (25 bugs)
├── Time/                       # Joda-Time (15 bugs)
└── Jsoup/                      # Jsoup HTML Parser (15 bugs)
```

## Experimental Methodology

### 1. Subject Selection
We selected **80 bugs** from the Defects4J (v3.0.1) benchmark. The selection criteria focused on **single-class fixes** and logical independence to ensure a fair comparison with LLMs in the subsequent phase.

### 2. Environment & Tools
*   **Framework**: Defects4J v3.0.1 (Dockerized).
*   **Java Environment**: **Java 11 (OpenJDK 11)**.
    *   *Note*: Due to the strict compilation rules regarding package visibility in Java 11, three legacy bugs (`Jsoup-4`, `Jsoup-6`, `Jsoup-9`) failed to compile the generated tests. These specific cases are marked as **`N/A`** in the dataset to reflect the compatibility limitations of traditional tools in modern environments.
*   **Test Generation Tools**:
    *   **Randoop**: Feedback-directed random test generation.
    *   **EvoSuite**: Search-based software testing (Genetic Algorithm).
*   **Configuration**:
    *   **Time Budget**: **180 seconds** per bug per tool.
    *   **Memory**: 8GB allocated to container.

### 3. Evaluation Metrics & Standards

#### A. Code Coverage Analysis
We utilized the built-in **`defects4j coverage`** command for analysis.
*   **Underlying Tool**: The analysis relies on **Cobertura** bytecode instrumentation. It instruments the compiled classes to track execution traces.
*   **Measurement Standard**:
    We define the coverage formulas as follows:

    ```
    Line Coverage = (Executed Lines / Total Instrumentable Lines)
    ```

    ```
    Branch Coverage = (Executed Branches / Total Branches)
    ```

*   **Data Source**: The metrics in `summary_report.csv` are extracted directly from the `line-rate` and `branch-rate` attributes in the generated `coverage.xml`.

#### B. Real-World Bug Detection
We define "Bug Detection" based on the differential behavior between the **Buggy** and **Fixed** versions:
1.  Tests are generated based on the **Buggy (`b`)** version.
2.  The same test suite is executed against the **Fixed (`f`)** version.
3.  **Criterion**:
    *   **DETECTED (YES)**: Tests pass on Buggy version **AND** Tests fail on Fixed version. (The test suite successfully captured the buggy behavior that was changed in the fix).
    *   **NOT DETECTED (NO)**: Tests pass on both versions.

