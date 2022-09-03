package gov.nasa.jpf.symbc.heap.solving.techniques;

import java.util.Arrays;
import java.util.HashMap;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.vm.ThreadInfo;

import symsolve.vector.SymSolveVector;


public class LISSAM extends LISSA {
	
	
	HashMap<String, Boolean> isSatCache = new HashMap<>();

    
	public LISSAM(ConfigParser config) {
        super(config);
    }

    @Override
    public boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap) {
        SymSolveVector vector = canonicalizer.createVector(symInputHeap);
        String query = createIsSatQueryString(vector);

        Boolean isSAT = isSatCache.get(query);
        if (isSAT != null) {
        	cacheHits++;
            return isSAT;
        }

        isSAT = heapSolver.isSatisfiable(vector);
        isSatCache.put(query, isSAT);
        return isSAT;
    }
    
    private String createIsSatQueryString(SymSolveVector vector) {
    	return Arrays.toString(vector.getPartialVector());
    }
    
}
