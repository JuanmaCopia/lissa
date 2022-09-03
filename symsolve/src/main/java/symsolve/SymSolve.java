package symsolve;

import korat.finitization.impl.CVElem;
import korat.testing.impl.CannotInvokePredicateException;
import symsolve.config.SolverConfig;
import symsolve.solver.Solver;
import symsolve.vector.SymSolveVector;

import java.util.HashMap;


public class SymSolve {


    SolverConfig config;
    private Solver solver;


    /**
     * Creates a SymSolve instance according to the provides configuration parameters.
     *
     * @param config The configuration parameters.
     */
    public SymSolve(SolverConfig config) {
        this.config = config;
        try {
            solver = new Solver(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decides whether a partially symbolic instance represented by a string vector
     * is SAT.
     *
     * @param vector The vector representing a partially symbolic instance.
     * @return True if the symbolic structure is SAT, false if it is UNSAT.
     */
    public boolean isSatisfiable(String vector) {
        return isSatisfiable(new SymSolveVector(vector));
    }

    /**
     * Decides whether a partially symbolic instance represented by a vector is SAT.
     *
     * @param vector The vector representing a partially symbolic instance.
     * @return True if the symbolic structure is SAT, false if it is UNSAT.
     */
    public boolean isSatisfiable(SymSolveVector vector) {
        boolean result = false;

        try {
            result = solver.startSearch(vector);
        } catch (CannotInvokePredicateException e) {
            e.printStackTrace(System.err);
        }
        return result;
    }

    /**
     * Decides whether the symbolic instance represented by a vector is SAT. If it
     * is, returns the vector solution.
     *
     * @param vector The vector representing a partially symbolic instance.
     * @return The solution vector if the symbolic structure is SAT, null if it is
     * UNSAT.
     */
    public int[] solve(SymSolveVector vector) {
        if (isSatisfiable(vector))
            return solver.getCandidateVector();
        return null;
    }

    /**
     * Decides whether a partially symbolic instance represented by a string vector
     * is SAT according to the hybrid repOK strategy.
     *
     * @param vector The vector representing a partially symbolic instance.
     * @return True if the symbolic structure is SAT, false if it is UNSAT.
     */
    public boolean isSatAutoHybridRepOK(SymSolveVector vector) {
        boolean result = false;
        try {
            result = solver.runAutoHybridRepok(vector);
        } catch (CannotInvokePredicateException e) {
            e.printStackTrace(System.err);
        }
        return result;
    }

    /**
     * Returns the format of the representation vector.
     *
     * @return A vector describing the types and fields that represent the
     * structure.
     */
    public CVElem[] getVectorFormat() {
        return solver.getStateSpace().getStructureList().clone();
    }

    /**
     * Returns a map of class names to the maximum number of allowed objects
     * (bounds) of that class to construct possible concretizations.
     *
     * @return A map of simple class names to maximum number of objects.
     */
    public HashMap<String, Integer> getScopes() {
        return solver.getScopes();
    }

}
