package symsolve.explorers;

import symsolve.vector.SymSolveVector;

public interface VectorStateSpaceExplorer {

    boolean canBeDeterminedUnsat(SymSolveVector vector);

    int[] getCandidateVector();

    void initialize(SymSolveVector vector);

    int[] getNextCandidate();

}
