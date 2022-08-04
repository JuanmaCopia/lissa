package gov.nasa.jpf.symbc.heap.solving.solver;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.solving.HeapSolvingInstructionFactory;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;

import korat.finitization.impl.CVElem;

import symsolve.SymSolve;
import symsolve.config.SolverConfig;
import symsolve.vector.SymSolveVector;


public class SymSolveHeapSolver {


    SymSolve solver;
    long solvingTime = 0;

    
    public SymSolveHeapSolver() {
        solver = createSymSolveInstance();
    }

    private SymSolve createSymSolveInstance() {
    	ConfigParser conf = HeapSolvingInstructionFactory.getConfigParser();
    	SolverConfig symSolveConfig = new SolverConfig(conf.symSolveClassName, conf.finitizationArgs, conf.symmetryBreakingStrategy, conf.predicateName);
        return new SymSolve(symSolveConfig);
    }

    public boolean isSatisfiable(SymSolveVector vector) {
    	long time = System.currentTimeMillis();
        boolean result = solver.isSatisfiable(vector);
        solvingTime += (System.currentTimeMillis() - time);
        return result;
    }

    public boolean assertProperty(SymSolveVector vector, String propertyMethodName, boolean useSpecialFinitization) {
    	long time = System.currentTimeMillis();
        boolean result = solver.assertProperty(vector, propertyMethodName, useSpecialFinitization);
        solvingTime += (System.currentTimeMillis() - time);
        return result;
    }

    public boolean isSatisfiableAutoHybridRepOK(SymSolveVector vector) {
        return solver.isSatAutoHybridRepOK(vector);
    }

    public HashMap<String, Integer> getDataScopes() {
        return solver.getScopes();
    }

    public CVElem[] getVectorFormat() {
        return solver.getVectorFormat();
    }
    
    public CVElem[] getPropertyCheckerVectorFormat() {
        return solver.getSpecialPropertyCheckerVectorFormat();
    }

    public long getSolvingTime() {
    	return solvingTime;
    }

}
