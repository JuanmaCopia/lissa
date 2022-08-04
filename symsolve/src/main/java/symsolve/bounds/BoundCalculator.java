package symsolve.bounds;

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
import symsolve.config.BoundCalculatorConfig;
import symsolve.explorers.VectorStateSpaceExplorer;
import symsolve.explorers.VectorStateSpaceExplorerFactory;
import symsolve.explorers.impl.SymbolicVectorExplorerFactory;
import symsolve.utils.Helper;
import symsolve.vector.SymSolveVector;

import java.lang.reflect.Method;

public class BoundCalculator {

    StateSpace stateSpace;
    VectorStateSpaceExplorer symbolicVectorSpaceExplorer;
    CandidateBuilder candidateBuilder;
    IIntList accessedIndices;
    Class<?> rootClass;
    Finitization finitization;
    PredicateChecker predicateChecker;


    public BoundCalculator(BoundCalculatorConfig params) throws ClassNotFoundException, CannotFindFinitizationException,
            CannotInvokeFinitizationException, CannotFindPredicateException {

        rootClass = Finitization.getClassLoader().loadClass(params.getFullyQualifiedClassName());

        String[] finArgs = params.getFinitizationArgs();
        Method finMethod = symsolve.utils.Helper.getFinMethod(rootClass, params.getFinitizationName(), finArgs);
        finitization = Helper.invokeFinMethod(rootClass, finMethod, finArgs);
        predicateChecker = new PredicateChecker();
        finitization.initialize(predicateChecker);
        stateSpace = finitization.getStateSpace();
        int vectorSize = stateSpace.getTotalNumberOfFields();
        accessedIndices = new IntListAI(vectorSize);
        IIntList changedFields = new IntListAI(vectorSize);
        
        predicateChecker.initialize(rootClass, params.getPredicateName(), accessedIndices);

        candidateBuilder = new CandidateBuilder(stateSpace, changedFields);

        VectorStateSpaceExplorerFactory heapExplorerFactory = new SymbolicVectorExplorerFactory(stateSpace, accessedIndices, changedFields);
        symbolicVectorSpaceExplorer = heapExplorerFactory.makeSymbolicVectorExplorer(params);
    }

    public Bounds calculateBounds() throws CannotInvokePredicateException {
        BoundRecorder boundsRecorder = new BoundRecorder(finitization);
        SymSolveVector initialVector = new SymSolveVector(stateSpace.getTotalNumberOfFields());
        symbolicVectorSpaceExplorer.initialize(initialVector);
        int[] vector = symbolicVectorSpaceExplorer.getCandidateVector();
        while (vector != null) {
            Object candidate = candidateBuilder.buildCandidate(vector);
            if (predicateChecker.checkPredicate(candidate))
                boundsRecorder.recordBounds(vector);
            vector = symbolicVectorSpaceExplorer.getNextCandidate();
        }
        return boundsRecorder.getBounds();
    }
}
