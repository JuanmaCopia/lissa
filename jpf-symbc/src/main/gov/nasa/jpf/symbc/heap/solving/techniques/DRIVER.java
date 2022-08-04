package gov.nasa.jpf.symbc.heap.solving.techniques;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.vm.ThreadInfo;


public class DRIVER extends SolvingStrategy {
	

    public DRIVER(ConfigParser config) {
        this.config = config;
    }

    @Override
    public boolean isLazyInitializationBased() {
        return false;
    }

    @Override
    public boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap) {
        return true;
    }

}
