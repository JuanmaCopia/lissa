package symsolve.explorers;

import symsolve.config.SymSolveConfig;

public interface VectorStateSpaceExplorerFactory {

    VectorStateSpaceExplorer makeSymbolicVectorExplorer(SymSolveConfig config);

}
