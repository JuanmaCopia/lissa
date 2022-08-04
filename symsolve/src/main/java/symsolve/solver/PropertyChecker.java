package symsolve.solver;


import korat.finitization.impl.Finitization;
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
import symsolve.utils.Helper;
import symsolve.vector.SymSolveVector;

import java.lang.reflect.Method;

public class PropertyChecker {

    StateSpace stateSpace;
    VectorStateSpaceExplorer symbolicVectorSpaceExplorer;
    CandidateBuilder candidateBuilder;
    IIntList accessedIndices;
    Class<?> rootClass;
    Finitization finitization;
    PredicateChecker predicateChecker;
    Method predicate;
    Method property;


    public PropertyChecker(SymSolveConfig params, String finitizationName) throws ClassNotFoundException, CannotFindFinitizationException,
            CannotInvokeFinitizationException, CannotFindPredicateException {

        rootClass = Finitization.getClassLoader().loadClass(params.getFullyQualifiedClassName());

        finitization = Helper.getFinitization(rootClass, finitizationName, params.getFinitizationArgs());
        predicateChecker = new PredicateChecker();
        finitization.initialize(predicateChecker);

        stateSpace = finitization.getStateSpace();
        int vectorSize = stateSpace.getTotalNumberOfFields();

        accessedIndices = new IntListAI(vectorSize);
        IIntList changedFields = new IntListAI(vectorSize);

        predicate = Helper.getPredicateMethod(rootClass, params.getPredicateName());
        predicateChecker.initialize(rootClass, params.getPredicateName(), accessedIndices);
        candidateBuilder = new CandidateBuilder(stateSpace, changedFields);

        VectorStateSpaceExplorerFactory heapExplorerFactory = new SymbolicVectorExplorerFactory(stateSpace, accessedIndices, changedFields);
        symbolicVectorSpaceExplorer = heapExplorerFactory.makeSymbolicVectorExplorer(params);
    }

    public boolean checkPropertyForAllValidInstances(SymSolveVector initialVector, String propertyMethodName) throws CannotInvokePredicateException, CannotFindPredicateException {
        if (!checkPropertyForCandidate(initialVector, propertyMethodName))
            return false;
        if (!property.getName().equals(predicate.getName())) {
            predicateChecker.setPredicate(predicate);
            symbolicVectorSpaceExplorer.initialize(initialVector);
            int[] vector = symbolicVectorSpaceExplorer.getCandidateVector();
            while (vector != null) {
                Object candidate = candidateBuilder.buildCandidate(vector);
                if (predicateChecker.checkPredicate(candidate)) {
                    if (!invokeProperty(candidate))
                        return false;
                }
                vector = symbolicVectorSpaceExplorer.getNextCandidate();
            }
        }
        return true;
    }

    public boolean checkPropertyForCandidate(SymSolveVector initialVector, String propertyMethodName) throws CannotInvokePredicateException, CannotFindPredicateException {
        if (property == null)
            property = Helper.getPredicateMethod(rootClass, propertyMethodName);
        predicateChecker.setPredicate(property);
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

    private boolean invokeProperty(Object candidate) throws CannotInvokePredicateException {
        assert (candidate != null);
        try {
            return (Boolean) property.invoke(candidate, (Object[]) null);
        } catch (Exception e) {
            throw new CannotInvokePredicateException(rootClass, property.getName(), e.getMessage(), e);
        }
    }

    public StateSpace getStateSpace() {
        return stateSpace;
    }

}
