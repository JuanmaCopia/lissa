package symsolve.solver;


import korat.finitization.impl.Finitization;
import korat.finitization.impl.IntSet;
import korat.finitization.impl.StateSpace;
import korat.testing.impl.CannotFindFinitizationException;
import korat.testing.impl.CannotFindPredicateException;
import korat.testing.impl.CannotInvokeFinitizationException;
import korat.testing.impl.CannotInvokePredicateException;
import korat.utils.IIntList;
import korat.utils.IntListAI;
import symsolve.candidates.CandidateBuilder;
import symsolve.candidates.PredicateChecker;
import symsolve.config.SymSolveConfig;
import symsolve.explorers.VectorStateSpaceExplorer;
import symsolve.explorers.VectorStateSpaceExplorerFactory;
import symsolve.explorers.impl.SymbolicVectorExplorerFactory;
import symsolve.utils.CodeGenerator;
import symsolve.utils.Helper;
import symsolve.vector.SymSolveVector;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Solver {

    StateSpace stateSpace;
    VectorStateSpaceExplorer symbolicVectorSpaceExplorer;
    CandidateBuilder candidateBuilder;
    CodeGenerator codeGenerator;
    IIntList accessedIndices;
    Class<?> rootClass;
    Finitization finitization;
    PredicateChecker predicateChecker;


    public Solver(SymSolveConfig params) throws ClassNotFoundException, CannotFindFinitizationException,
            CannotInvokeFinitizationException, CannotFindPredicateException {

        rootClass = Finitization.getClassLoader().loadClass(params.getFullyQualifiedClassName());

        String[] finArgs = params.getFinitizationArgs();
        Method finMethod = Helper.getFinMethod(rootClass, params.getFinitizationName(), finArgs);
        finitization = Helper.invokeFinMethod(rootClass, finMethod, finArgs);
        predicateChecker = new PredicateChecker();
        finitization.initialize(predicateChecker);
        stateSpace = finitization.getStateSpace();
        int vectorSize = stateSpace.getTotalNumberOfFields();
        accessedIndices = new IntListAI(vectorSize);
        IIntList changedFields = new IntListAI(vectorSize);

        predicateChecker.initialize(rootClass, params.getPredicateName(), accessedIndices);

        candidateBuilder = new CandidateBuilder(stateSpace, changedFields);
        codeGenerator = new CodeGenerator(stateSpace, rootClass);

        VectorStateSpaceExplorerFactory heapExplorerFactory = new SymbolicVectorExplorerFactory(stateSpace, accessedIndices, changedFields);
        symbolicVectorSpaceExplorer = heapExplorerFactory.makeSymbolicVectorExplorer(params);
    }

    public boolean runAutoHybridRepok(SymSolveVector vector) throws CannotInvokePredicateException {
        symbolicVectorSpaceExplorer.initialize(vector);
        Object candidate = candidateBuilder.buildCandidate(vector.getConcreteVector());
        if (predicateChecker.checkPredicate(candidate))
            return true;
        return areSymbolicFieldsAccessed(vector);
    }

    private boolean areSymbolicFieldsAccessed(SymSolveVector vector) {
        for (int i = 0; i < accessedIndices.numberOfElements(); i++) {
            int index = accessedIndices.get(i);
            if (vector.isSymbolicIndex(index))
                return true;
        }
        return false;
    }

    public boolean startSearch(SymSolveVector initialVector) throws CannotInvokePredicateException {
        if (symbolicVectorSpaceExplorer.canBeDeterminedUnsat(initialVector))
            return false;
        symbolicVectorSpaceExplorer.initialize(initialVector);
        int[] vector = symbolicVectorSpaceExplorer.getCandidateVector();
        while (vector != null) {
            Object candidate = candidateBuilder.buildCandidate(vector);
            if (predicateChecker.checkPredicate(candidate))
                return true;
            vector = symbolicVectorSpaceExplorer.getNextCandidate();
        }
        return false;
    }

    public HashMap<String, Integer> getScopes() {
        return finitization.getScopes();
    }

    public Map<String, IntSet> getIntegerFieldsMinMaxMap() {
        return finitization.getIntegerFieldsMinMaxMap();
    }

    public int[] getCandidateVector() {
        return this.symbolicVectorSpaceExplorer.getCandidateVector().clone();
    }

    public StateSpace getStateSpace() {
        return stateSpace;
    }

    /*    public boolean runRepOK(SymSolveVector vector) throws CannotInvokePredicateException {
        symbolicVectorSpaceExplorer.initialize(vector);
        Object candidate = candidateBuilder.buildCandidate(vector.getConcreteVector());
        return predicateChecker.checkPredicate(candidate);
    }*/

}
