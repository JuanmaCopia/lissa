package symsolve.explorers.impl;

import korat.finitization.impl.StateSpace;
import korat.utils.IIntList;
import symsolve.config.SolverConfig;
import symsolve.config.SymSolveConfig;
import symsolve.explorers.VectorStateSpaceExplorer;
import symsolve.explorers.VectorStateSpaceExplorerFactory;

public class SymbolicVectorExplorerFactory implements VectorStateSpaceExplorerFactory {

    StateSpace stateSpace;
    IIntList accessedIndices;
    IIntList changedFields;


    public SymbolicVectorExplorerFactory(StateSpace stateSpace, IIntList accessedIndices, IIntList changedFields) {
        this.stateSpace = stateSpace;
        this.accessedIndices = accessedIndices;
        this.changedFields = changedFields;
    }

    public VectorStateSpaceExplorer makeSymbolicVectorExplorer(SymSolveConfig config) {
        SymmetryBreakStrategy strategy = config.getSymmetryBreakStretegy();
        switch (strategy) {
            case SYMMETRY_BREAK:
                return new SymmetryBreakingExplorer(stateSpace, accessedIndices, changedFields);
            case SYMMETRY_BREAK_REVERSE:
                return new ReverseSymmetryBreakingExplorer(stateSpace, accessedIndices, changedFields);
            case NO_SYMMETRY_BREAK:
                return new NoSymmetryBreakingExplorer(stateSpace, accessedIndices, changedFields);
            case SYMMETRY_BREAK_BOUNDED:
                SolverConfig solverConf = (SolverConfig) config;
                return new SymmetryBreakingExplorerBounded(stateSpace, accessedIndices, changedFields, solverConf.getBounds());
            default:
                throw new IllegalArgumentException(strategy.name() + " is not a valid Exploration Strategy ");
        }
    }

}
