# LISSA: Lazy Initialization with Specialized Solver Aid

`LISSA` is an efficient symbolic execution approach for programs that manipulate complex heap-allocated data structures with rich structural constraints. This repository includes the `LISSA` implementation and the artifact for the paper "LISSA: Lazy Initialization with Specialized Solver Aid", accepted for publication in ASE 2022.

`LISSA`'s implementation is based on the symbolic execution engine of the `Symbolic (Java) PathFinder` tool [0]. `LISSA` employs a novel solver, called `SymSolve`, to detect spurious partially symbolic structures generated during lazy initialization, and prune their corresponding spurious paths. `SymSolve`'s implementation is based on the `Korat` bounded exhaustive test generation tool [1]. 

To favor reproducibility of the experiments and comparison with existing and future approaches, we release all the implementations as open source. The artifact and its source code can be obtained by cloning the following GitHub repository: https://github.com/JuanmaCopia/lissa.

For instructions on installing and executing `LISSA`, and for reproducing the experiments in the paper, please refer to `INSTALL.md`.

## Folder structure

`jpf-symbc` folder contains a modified version of Symbolic PathFinder in which we implement our approach.

The `jpf-core` folder includes the version of Java PathFinder Model Checker [2] in which Symbolic PathFinder is based upon.

The folder `symsolve` contains the source files of `SymSolve`, our solver for partially symbolic structures.

## Folder structure of a case study

The source files of the case studies can be found in: `jpf-symbc/src/examples/heapsolving`. Each case study have the following files and folders:

```
├── <ClassName>.java             --> The java class that contains the methods under test
├── <ClassName>.jpf              --> The configuration file
├── <ClassName>Harness.java      --> A Harness necessary to run techniques that require perform previous actions before SUT's execution (IFREPOK and DRIVER).
├── <method 1>                   --> A folder for each of the SUTs, containing the main entry for the SUT
│   └── <ClassName>Main.java     --> A java class that contains the main entry that calls SUT
├── <method 2>
│   └── <ClassName>Main.java
├── <method 3>
│   └── <ClassName>Main.java
└── symsolve
    └── <ClassName>.java        --> A java class containing the finitization method 
```

# References

[0] https://github.com/SymbolicPathFinder/jpf-symbc

[1] http://korat.sourceforge.net

[2] https://github.com/corinus/jpf-core
