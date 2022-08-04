package gov.nasa.jpf.symbc.heap.solving.techniques;

import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import symsolve.vector.SymSolveVector;

public class LIHYBRID extends LISSA {


	HashMap<Integer, Integer> fieldGetCount = new HashMap<Integer, Integer>();
	

	public LIHYBRID(ConfigParser config) {
		super(config);
	}
	
    @Override
    public boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap) {
        resetGetFieldCount();
        SymSolveVector vector = canonicalizer.createVector(symInputHeap);
        return heapSolver.isSatisfiableAutoHybridRepOK(vector);
    }
    
    @Override
    public void pathFinished(VM vm, ThreadInfo terminatedThread) {
        countPath();
        checkPathValidity(vm, terminatedThread);
    }
    
    private void checkPathValidity(VM vm, ThreadInfo terminatedThread) {
        HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
        SymbolicInputHeap symInputHeap = heapCG.getCurrentSymInputHeap();
        SymSolveVector vector = canonicalizer.createVector(symInputHeap);
        if (symInputHeap != null && !heapSolver.isSatisfiable(vector))
            invalidPaths++;
    }

    @Override
    public boolean reachedGETFIELDLimit(int objRef) {
        if (!fieldGetCount.containsKey(objRef))
            fieldGetCount.put(objRef, 0);
        Integer count = fieldGetCount.get(objRef);

        if (count >= config.getFieldLimit) {
            resetGetFieldCount();
            return true;
        }
        fieldGetCount.put(objRef, count + 1);
        return false;
    }

    private void resetGetFieldCount() {
        fieldGetCount.clear();
    }

}
