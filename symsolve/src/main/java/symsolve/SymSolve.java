package symsolve;

import korat.finitization.impl.CVElem;
import korat.testing.impl.CannotFindPredicateException;
import korat.testing.impl.CannotInvokePredicateException;
import symsolve.config.SolverConfig;
import symsolve.solver.PropertyChecker;
import symsolve.solver.Solver;
import symsolve.vector.SymSolveVector;

import java.util.HashMap;


public class SymSolve {


    SolverConfig config;
    private Solver solver;
    private PropertyChecker defaultPropertyChecker;
    private PropertyChecker specialPropertyChecker;


    /**
     * Creates a SymSolve instance according to the provides configuration parameters.
     *
     * @param config The configuration parameters.
     */
    public SymSolve(SolverConfig config) {
        this.config = config;
        try {
            solver = new Solver(config);
            defaultPropertyChecker = new PropertyChecker(config, config.getFinitizationName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            specialPropertyChecker = new PropertyChecker(config, config.getPropertyCheckFinitizationName());
        } catch (Exception ignored) {
            specialPropertyChecker = null;
        }
    }

    private static boolean assertProperty(PropertyChecker propertyChecker, SymSolveVector vector, String propertyMethodName) {
        boolean result = false;
        try {
            result = propertyChecker.checkPropertyForAllValidInstances(vector, propertyMethodName);
        } catch (CannotInvokePredicateException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        } catch (CannotFindPredicateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
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
     * Checks if a property holds for all possible concretizations (within the bounds)
     * of the symbolic instance represented by a vector represented as a string.
     *
     * @param vector              The vector representing a partially symbolic instance.
     * @param propertyMethodName  The name of the boolean routine that checks the property.
     * @param specialFinitization if true the special property check finitization will be used,
     *                            otherwise the default solving finitization will be used.
     * @return true if the property holds for all possible concretizations of the symbolic
     * instance, false otherwise.
     */
    public boolean assertProperty(String vector, String propertyMethodName, boolean specialFinitization) {
        return assertProperty(new SymSolveVector(vector), propertyMethodName, specialFinitization);
    }

    /**
     * Checks if a property holds for all possible concretizations (within the bounds)
     * of the symbolic instance represented by a vector represented as a string.
     *
     * @param vector             The vector representing a partially symbolic instance.
     * @param propertyMethodName The name of the boolean routine that checks the property.
     * @return true if the property holds for all possible concretizations of the symbolic
     * instance, false otherwise.
     */
    public boolean assertProperty(String vector, String propertyMethodName) {
        return assertProperty(new SymSolveVector(vector), propertyMethodName, false);
    }

    /**
     * Checks if a property holds for all possible concretizations (within the bounds)
     * of the symbolic instance represented by a vector.
     *
     * @param vector              The vector representing a partially symbolic instance.
     * @param propertyMethodName  The name of the boolean routine that checks the property.
     * @param specialFinitization if true the special property check finitization will be used,
     *                            otherwise the default solving finitization will be used.
     * @return true if the property holds for all possible concretizations of the symbolic
     * instance, false otherwise.
     */
    public boolean assertProperty(SymSolveVector vector, String propertyMethodName, boolean specialFinitization) {
        if (specialFinitization) {
            if (specialPropertyChecker == null)
                throw new RuntimeException(String.format("Finitization Method: %s not found", config.getPropertyCheckFinitizationName()));
            return assertProperty(specialPropertyChecker, vector, propertyMethodName);
        }
        return assertProperty(defaultPropertyChecker, vector, propertyMethodName);
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
     * Returns the format of the representation vector used to assert properties about the structures.
     *
     * @return A vector describing the types and fields that represent the
     * structure.
     */
    public CVElem[] getSpecialPropertyCheckerVectorFormat() {
        if (specialPropertyChecker != null)
            return specialPropertyChecker.getStateSpace().getStructureList().clone();
        else
            return null;
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
