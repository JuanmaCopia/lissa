package gov.nasa.jpf.symbc.heap.solving.techniques;

import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.vm.ThreadInfo;


public class IFREPOK extends LISSA {

    public IFREPOK(ConfigParser config) {
        super(config);
    }

    @Override
    public boolean checkHeapSatisfiability(ThreadInfo ti, SymbolicInputHeap symInputHeap) {
        return true;
    }
    
}
