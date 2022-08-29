# LISSA: Lazy Initialization with Specialized Solver Aid

`LISSA` is an efficient symbolic execution approach for programs that manipulate complex heap-allocated data structures with rich structural constraints. This repository includes the `LISSA` implementation and the artifact for the paper "LISSA: Lazy Initialization with Specialized Solver Aid", accepted for publication in ASE 2022.

`LISSA`'s implementation is based on the symbolic execution engine of the `Symbolic (Java) PathFinder` tool [0]. `LISSA` employs a novel solver, called `SymSolve`, to detect spurious partially symbolic structures generated during lazy initialization, and prune their corresponding spurious paths. `SymSolve`'s implementation is based on the `Korat` bounded exhaustive test generation tool [1]. 

To favor reproducibility of the experiments and comparison with existing and future approaches, we release all the implementations as open source. The artifact and its source code can be obtained by cloning the following GitHub repository: https://github.com/JuanmaCopia/lissa.

For instructions on installing and executing `LISSA`, and for reproducing the experiments in the paper, please refer to `INSTALL.md`.

# References

[0] https://github.com/SymbolicPathFinder/jpf-symbc

[1] http://korat.sourceforge.net
