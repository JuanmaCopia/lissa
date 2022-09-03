package gov.nasa.jpf.symbc.heap.solving.techniques;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.canonicalizer.Canonicalizer;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.symbc.heap.solving.solver.SymSolveHeapSolver;
import gov.nasa.jpf.vm.ThreadInfo;

import symsolve.vector.SymSolveVector;

public class LISSA extends SolvingStrategy {

	
    protected SymSolveHeapSolver heapSolver;
    protected Canonicalizer canonicalizer;


    public LISSA(ConfigParser config) {
    	this.config = config;
        heapSolver = new SymSolveHeapSolver();
        canonicalizer = new Canonicalizer(heapSolver.getVectorFormat());
    }

    @Override
    public boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap) {
        SymSolveVector vector = canonicalizer.createVector(symInputHeap);
        return heapSolver.isSatisfiable(vector);
    }

    @Override
    public Integer getBoundForClass(String simpleClassName) {
    	HashMap<String, Integer> scopes = heapSolver.getDataScopes();
        return scopes.get(simpleClassName);
    }

	@Override
	public long getSolvingTime() {
		return heapSolver.getSolvingTime();
	}

}
