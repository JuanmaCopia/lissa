
# LISSA

This is the artifact of the paper "LISSA: Lazy Initialization with Specialized Solver Aid", accepted for ASE 2022. The artifact includes a modified version of the Symbolic (Java) Path Finder tool. The modifications implement the proposed approach to detect and prune spurious symbolic heap-allocated structures generated during lazy initialization. We developed a new solver, capable of deciding satisfiability of those symbolic structures, called SymSolve, whose source code is also included in this artifact. We use SymSolve to prune invalid lazy initialization steps generated during the symbolic execution of programs with heap-allocated inputs.

In summary, the artifact is composed by the developed solver SymSolve, a modified version of Java Symbolic Path Finder that make use of SymSolve, and the necessary scripts to reproduce the evaluation of the paper's case studies.

The artifact can be obtained by cloning the following GitHub repository: https://github.com/JuanmaCopia/lissa

For installation instructions refer to INSTALL.md

